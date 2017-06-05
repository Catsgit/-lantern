package com.lantern.service.impl;

import com.alipay.api.AlipayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.utils.ZxingUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lantern.common.Const;
import com.lantern.common.ServerResponse;
import com.lantern.dao.*;
import com.lantern.pojo.*;
import com.lantern.service.IOrderService;
import com.lantern.util.BigDecimalUtil;
import com.lantern.util.DateTimeUtil;
import com.lantern.util.FTPUtil;
import com.lantern.util.PropertiesUtil;
import com.lantern.vo.OrderItemVO;
import com.lantern.vo.OrderProductVO;
import com.lantern.vo.OrderVO;
import com.lantern.vo.ShippingVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by cat on 17-6-1.
 */
@Service("iOrderService")
public class OrderServiceImpl implements IOrderService {

    private static AlipayTradeService tradeService;

    static {

        /** 一定要在创建AlipayTradeService之前调用Configs.init()设置默认参数
         *  Configs会读取classpath下的zfbinfo.properties文件配置信息，如果找不到该文件则确认该文件是否在classpath目录
         */
        Configs.init("zfbinfo.properties");

        /** 使用Configs提供的默认参数
         *  AlipayTradeService可以使用单例或者为静态成员对象，不需要反复new
         */
        tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
    }

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderMasterMapper orderMasterMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private PayInfoMapper payInfoMapper;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ShippingMapper shippingMapper;

    @Override
    public ServerResponse createOrder(String userId, Integer shippingId) {
        //从购物车中获取数据
        List<Cart> cartList = cartMapper.selectCheckedCartByUserId(userId);

        //计算这个订单的总价
        ServerResponse serverResponse = this.getCartOrderItem(userId, cartList);
        if(!serverResponse.isSuccess()) {
            return serverResponse;
        }
        List<OrderItem> orderItemList = (List<OrderItem>) serverResponse.getData();
        BigDecimal payment = this.getOrderTotalPrice(orderItemList);

        //生成订单
        OrderMaster orderMaster = assembleOrder(userId, shippingId, payment);
        if(orderMaster == null) {
            return ServerResponse.createByErrorMessage("订单生成错误");
        }
        if(CollectionUtils.isEmpty(orderItemList)) {
            return ServerResponse.createByErrorMessage("购物车为空");
        }
        for(OrderItem orderItem : orderItemList) {
            orderItem.setOrderNo(orderMaster.getOrderNo());
        }
        //mybatis 批量插入
        orderItemMapper.batchInsert(orderItemList);
        //生成成功, 清减库存, 清空购物车
        this.reduceProductStock(orderItemList);
        this.cleanCart(cartList);

        //返回订单明细
        OrderVO orderVO = assembleOrderVO(orderMaster, orderItemList);
        return ServerResponse.createBySuccess(orderVO);
    }

    private OrderVO assembleOrderVO(OrderMaster orderMaster, List<OrderItem> orderItemList) {
        OrderVO orderVO = new OrderVO();
        orderVO.setOrderNo(orderMaster.getOrderNo());
        orderVO.setPayment(orderMaster.getPayment());
        orderVO.setPaymentType(orderMaster.getPaymentType());
        orderVO.setPaymentTypeDesc(Const.PaymentTypeEnum.codeOf(orderMaster.getPaymentType()).getValue());

        orderVO.setPostage(orderMaster.getPostage());
        orderVO.setStatus(orderMaster.getStatus());
        orderVO.setStatusDesc(Const.OrderStatusEnum.codeOf(orderMaster.getStatus()).getValue());

        orderVO.setShippingId(orderMaster.getShippingId());
        Shipping shipping = shippingMapper.selectByPrimaryKey(orderMaster.getShippingId());
        if(shipping != null) {
            orderVO.setReceiverName(shipping.getReceiverName());
            orderVO.setShippingVO(this.assembleShippingVO(shipping));
        }

        orderVO.setPaymentTime(DateTimeUtil.dateToStr(orderMaster.getPaymentTime()));
        orderVO.setSendTime(DateTimeUtil.dateToStr(orderMaster.getSendTime()));
        orderVO.setEndTime(DateTimeUtil.dateToStr(orderMaster.getEndTime()));
        orderVO.setCloseTime(DateTimeUtil.dateToStr(orderMaster.getCloseTime()));
        orderVO.setCreateTime(DateTimeUtil.dateToStr(orderMaster.getCreateTime()));

        orderVO.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));

        List<OrderItemVO> orderItemVOList = Lists.newArrayList();

        for(OrderItem orderItem : orderItemList) {
            OrderItemVO orderItemVO = this.assembleOrderItemVO(orderItem);
            orderItemVOList.add(orderItemVO);
        }

        orderVO.setOrderItemVOList(orderItemVOList);
        return orderVO;
    }

    private OrderItemVO assembleOrderItemVO(OrderItem orderItem) {
        OrderItemVO orderItemVO = new OrderItemVO();

        orderItemVO.setOrderNo(orderItem.getOrderNo());
        orderItemVO.setProductId(orderItem.getProductId());
        orderItemVO.setProductName(orderItem.getProductName());
        orderItemVO.setProductImage(orderItem.getProductImage());
        orderItemVO.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
        orderItemVO.setQuantity(orderItem.getQuantity());
        orderItemVO.setTotalPrice(orderItem.getTotalPrice());
        orderItemVO.setCreateTime(DateTimeUtil.dateToStr(orderItem.getCreateTime()));

        return orderItemVO;
    }

    private ShippingVO assembleShippingVO(Shipping shipping) {
        ShippingVO shippingVO = new ShippingVO();

        shippingVO.setReceiverName(shipping.getReceiverName());
        shippingVO.setReceiverPhone(shipping.getReceiverPhone());
        shippingVO.setReceiverProvince(shipping.getReceiverProvince());
        shippingVO.setReceiverCity(shipping.getReceiverCity());
        shippingVO.setReceiverDistrict(shipping.getReceiverDistrict());
        shippingVO.setReceiverAddress(shipping.getReceiverAddress());
        shippingVO.setReceiverZip(shipping.getReceiverZip());
        return shippingVO;
    }

    private void cleanCart(List<Cart> cartList) {
        for(Cart cart : cartList) {
            cartMapper.deleteByPrimaryKey(cart.getId());
        }
    }

    private void reduceProductStock(List<OrderItem> orderItemList) {
        for(OrderItem orderItem : orderItemList) {
            Product product = productMapper.selectByPrimaryKey(orderItem.getProductId());
            product.setStock(product.getStock() - orderItem.getQuantity());
            productMapper.updateByPrimaryKeySelective(product);
        }
    }

    private OrderMaster assembleOrder(String userId, Integer shippingId, BigDecimal payment) {
        OrderMaster orderMaster = new OrderMaster();
        long orderNo = this.generateOrderNo();
        orderMaster.setOrderNo(orderNo);
        orderMaster.setStatus(Const.OrderStatusEnum.NO_PAY.getCode());
        orderMaster.setPostage(0);
        orderMaster.setPaymentType(Const.PaymentTypeEnum.ONLINE_PAY.getCode());
        orderMaster.setPayment(payment);

        orderMaster.setUserId(userId);
        orderMaster.setShippingId(shippingId);
        //发货时间 付款时间在发货和付款时再赋值
        int rowCount = orderMasterMapper.insert(orderMaster);
        if(rowCount > 0) {
            return orderMaster;
        }
        return null;
    }

    private long generateOrderNo() {
        long currentTime = System.currentTimeMillis();
        return currentTime + new Random().nextInt(100);
    }

    private BigDecimal getOrderTotalPrice(List<OrderItem> orderItemList) {
        BigDecimal payment = new BigDecimal("0");
        for(OrderItem orderItem : orderItemList) {
            payment = BigDecimalUtil.add(payment.doubleValue(), orderItem.getTotalPrice().doubleValue());
        }
        return payment;
    }

    private ServerResponse getCartOrderItem(String userId, List<Cart> cartList) {
        List<OrderItem> orderItemList = Lists.newArrayList();
        if(CollectionUtils.isEmpty(cartList)) {
            return ServerResponse.createByErrorMessage("购物车为空");
        }
        //校验购物车的数据 包括产品的数量和状态
        for(Cart cartItem : cartList) {
            OrderItem orderItem = new OrderItem();
            Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
            if(Const.ProductStatusEnum.ON_SALE.getCode() != product.getStatus()) {
                return ServerResponse.createByErrorMessage("产品:" + product.getName() + "已下架");
            }
            if(cartItem.getQuantity() > product.getStock()) {
                return ServerResponse.createByErrorMessage("产品:" + product.getName() + "库存不足");
            }

            orderItem.setUserId(userId);
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setProductImage(product.getMainImage());
            orderItem.setCurrentUnitPrice(product.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(), cartItem.getQuantity()));
            orderItemList.add(orderItem);
        }
        return ServerResponse.createBySuccess(orderItemList);
    }

    @Override
    public ServerResponse cancelOrder(String userId, Long orderNo) {
        OrderMaster orderMaster = orderMasterMapper.selectByUserIdAndOrderNo(userId, orderNo);
        if(orderMaster == null) {
            return ServerResponse.createByErrorMessage("用户没有此订单");
        }
        if(orderMaster.getStatus() != Const.OrderStatusEnum.NO_PAY.getCode()) {
            return ServerResponse.createByErrorMessage("该订单以付款, 不可取消");
        }
        OrderMaster updateOrder = new OrderMaster();
        updateOrder.setId(orderMaster.getId());
        updateOrder.setStatus(Const.OrderStatusEnum.CANCELED.getCode());
        int rowCount = orderMasterMapper.updateByPrimaryKeySelective(updateOrder);
        if(rowCount > 0) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }

    @Override
    public ServerResponse getOrderCartProduct(String userId) {
        OrderProductVO orderProductVO = new OrderProductVO();

        //从购物车中获取数据
        List<Cart> cartList = cartMapper.selectCheckedCartByUserId(userId);
        ServerResponse serverResponse = this.getCartOrderItem(userId, cartList);
        if(!serverResponse.isSuccess()) {
            return serverResponse;
        }
        List<OrderItem> orderItemList = (List<OrderItem>) serverResponse.getData();

        List<OrderItemVO> orderItemVOList = Lists.newArrayList();

        BigDecimal payment = new BigDecimal("0");
        for(OrderItem orderItem : orderItemList) {
            payment = BigDecimalUtil.add(payment.doubleValue(), orderItem.getTotalPrice().doubleValue());
            orderItemVOList.add(assembleOrderItemVO(orderItem));
        }
        orderProductVO.setOrderTotalPrice(payment);
        orderProductVO.setOrderItemVOList(orderItemVOList);
        orderProductVO.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        return ServerResponse.createBySuccess(orderProductVO);
    }

    @Override
    public ServerResponse getOrderDetail(String userId, Long orderNo) {
        OrderMaster orderMaster = orderMasterMapper.selectByUserIdAndOrderNo(userId, orderNo);
        if(orderMaster != null) {
            List<OrderItem> orderItemList = orderItemMapper.getByOrderNoAndUserId(orderNo, userId);
            OrderVO orderVO = this.assembleOrderVO(orderMaster, orderItemList);
            return ServerResponse.createBySuccess(orderVO);
        }
        return ServerResponse.createByErrorMessage("用户没有此订单");
    }

    @Override
    public ServerResponse<PageInfo> getOrderList(String userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        System.out.println("pageNum:" + pageNum + " " + "pageSize:" + pageSize);
        List<OrderMaster> orderMasterList = orderMasterMapper.selectByUserId(userId);
        List<OrderVO> orderVOList = this.assembleOrderVOList(userId, orderMasterList);
        PageInfo pageResult = new PageInfo(orderMasterList);
        pageResult.setList(orderVOList);
        return ServerResponse.createBySuccess(pageResult);
    }

    private List<OrderVO> assembleOrderVOList(String userId, List<OrderMaster> orderMasterList) {
        List<OrderVO> orderVOList = Lists.newArrayList();
        for(OrderMaster orderMasterItem : orderMasterList) {
            List<OrderItem> orderItemList = Lists.newArrayList();
            if(userId == null) {
                orderItemList = orderItemMapper.getByOrderNo(orderMasterItem.getOrderNo());
            } else {
                orderItemList = orderItemMapper.getByOrderNoAndUserId(orderMasterItem.getOrderNo(), userId);
            }
            OrderVO orderVO = assembleOrderVO(orderMasterItem, orderItemList);
            orderVOList.add(orderVO);
        }
        return orderVOList;
    }

    @Override
    public ServerResponse pay(String userId, Long orderNo, String path) {
        Map<String, String> resultMap = Maps.newHashMap();
        OrderMaster orderMaster = orderMasterMapper.selectByUserIdAndOrderNo(userId, orderNo);
        if(orderMaster == null) {
            return ServerResponse.createByErrorMessage("用户没有此订单");
        }
        resultMap.put("orderNo", String.valueOf(orderMaster.getOrderNo()));

        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        String outTradeNo = orderMaster.getOrderNo().toString();

        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = new StringBuilder().append("Lantern小灯笼扫码支付, 订单号: ").append(outTradeNo).toString();

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = orderMaster.getPayment().toString();

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = new StringBuilder().append("订单: ").append(outTradeNo).append("购买商品共: ").append(totalAmount).append("元").toString();

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "operator_id";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "store_id";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");

        // 支付超时，定义为120分钟
        String timeoutExpress = "120m";

        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();

        List<OrderItem> orderItemList = orderItemMapper.getByOrderNoAndUserId(orderNo, userId);
        for(OrderItem orderItem : orderItemList) {
            // 创建一个商品信息，参数含义分别为商品id（使用国标）、名称、单价（单位为分）、数量，如果需要添加商品类别，详见GoodsDetail
            GoodsDetail goods = GoodsDetail.newInstance(orderItem.getProductId().toString(), orderItem.getProductName(),
                    BigDecimalUtil.mul(orderItem.getCurrentUnitPrice().doubleValue(), new Double(100).doubleValue()).longValue(),
                    orderItem.getQuantity());
            // 创建好一个商品后添加至商品明细列表
            goodsDetailList.add(goods);
        }

        // 创建扫码支付请求builder，设置请求参数
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
            .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
            .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
            .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
            .setTimeoutExpress(timeoutExpress)
            .setNotifyUrl(PropertiesUtil.getProperty("alipay.callback.url"))//支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
            .setGoodsDetailList(goodsDetailList);

        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                logger.info("支付宝预下单成功: )");

                AlipayTradePrecreateResponse response = result.getResponse();
                dumpResponse(response);

                File folder = new File(path);
                if(!folder.exists()) {
                    folder.setWritable(true);
                    folder.mkdirs();
                }

                // 需要修改为运行机器上的路径
                String qrPath = String.format(path+"/"+"qr-%s.png", response.getOutTradeNo());
                String qrFileName = String.format("qr-%s.png", response.getOutTradeNo());
                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, qrPath);

                File targetFile = new File(path, qrFileName);
                try {
                    FTPUtil.uploadFile(Lists.newArrayList(targetFile));
                } catch (IOException e) {
                    logger.error("上传二维码异常");
                }
                logger.info("qrPath:" + qrPath);

                String qrUrl = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFile.getName();
                resultMap.put("qrUrl", qrUrl);
                return ServerResponse.createBySuccess(resultMap);

            case FAILED:
                logger.error("支付宝预下单失败!!!");
                return ServerResponse.createByErrorMessage("支付宝预下单失败!!!");

            case UNKNOWN:
                logger.error("系统异常，预下单状态未知!!!");
                return ServerResponse.createByErrorMessage("系统异常，预下单状态未知!!!");

            default:
                logger.error("不支持的交易状态，交易返回异常!!!");
                return ServerResponse.createByErrorMessage("不支持的交易状态，交易返回异常!!!");
        }

    }

    // 简单打印应答
    private void dumpResponse(AlipayResponse response) {
        if (response != null) {
            logger.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (StringUtils.isNotEmpty(response.getSubCode())) {
                logger.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                        response.getSubMsg()));
            }
            logger.info("body:" + response.getBody());
        }
    }

    @Override
    public ServerResponse aliCallback(Map<String, String> params) {
        Long orderNo = Long.parseLong(params.get("out_trade_no"));
        String tradeNo = params.get("trade_no");
        String tradeStatus = params.get("trade_status");
        OrderMaster orderMaster = orderMasterMapper.selectByOrderNo(orderNo);
        if(orderMaster == null) {
            return ServerResponse.createByErrorMessage("非lantern商城的订单, 回调忽略");
        }
        if(orderMaster.getStatus() >= Const.OrderStatusEnum.PAID.getCode()) {
            return ServerResponse.createBySuccess("支付宝重复调用");
        }
        if(Const.AlipayCallback.TRADE_STATUS_TRADE_SUCCESS.equals(tradeStatus)) {
            orderMaster.setPaymentTime(DateTimeUtil.strToDate(params.get("gmt_payment")));
            orderMaster.setStatus(Const.OrderStatusEnum.PAID.getCode());
            orderMasterMapper.updateByPrimaryKeySelective(orderMaster);
        } else if(Const.AlipayCallback.TRADE_CLOSED.equals(tradeStatus)) {
            cancelOrder(orderMaster.getUserId(), orderMaster.getOrderNo());
            return ServerResponse.createByErrorMessage("未付款交易支付超时, 取消订单");
        }

        PayInfo payInfo = new PayInfo();
        payInfo.setUserId(orderMaster.getUserId());
        payInfo.setOrderNo(orderMaster.getOrderNo());
        payInfo.setPayPlatform(Const.PayPlatformEnum.ALIPAY.getCode());
        payInfo.setPlatformNumber(tradeNo);
        payInfo.setPlatformStatus(tradeStatus);

        payInfoMapper.insert(payInfo);

        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse queryOrderPayStatus(String userId, Long orderNo) {
        OrderMaster orderMaster = orderMasterMapper.selectByUserIdAndOrderNo(userId, orderNo);
        if(orderMaster == null) {
            return ServerResponse.createByErrorMessage("用户没有此订单");
        }

        if(orderMaster.getStatus() >= Const.OrderStatusEnum.PAID.getCode()) {
            return ServerResponse.createBySuccess(true);    //已付款
        }
        return ServerResponse.createBySuccess(false);      //未付款
    }

    //backend

    @Override
    public ServerResponse<PageInfo> getManageOrderList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        System.out.println("pageNum:" + pageNum + " " + "pageSize:" + pageSize);
        List<OrderMaster> orderMasterList = orderMasterMapper.selectAllOrder();
        List<OrderVO> orderVOList = this.assembleOrderVOList(null, orderMasterList);
        PageInfo pageResult = new PageInfo(orderMasterList);
        pageResult.setList(orderVOList);

        return ServerResponse.createBySuccess(pageResult);
    }

    @Override
    public ServerResponse<OrderVO> getManageOrderDetail(Long orderNo) {
        OrderMaster orderMaster = orderMasterMapper.selectByOrderNo(orderNo);
        if(orderMaster != null) {
            List<OrderItem> orderItemList = orderItemMapper.getByOrderNo(orderNo);
            OrderVO orderVO = this.assembleOrderVO(orderMaster, orderItemList);
            return ServerResponse.createBySuccess(orderVO);
        }
        return ServerResponse.createByErrorMessage("订单不存在");
    }

    @Override
    public ServerResponse<PageInfo> searchOrder(Long orderNo, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        System.out.println("pageNum:" + pageNum + " " + "pageSize:" + pageSize);
        OrderMaster orderMaster = orderMasterMapper.selectByOrderNo(orderNo);
        if(orderMaster != null) {
            List<OrderItem> orderItemList = orderItemMapper.getByOrderNo(orderNo);
            OrderVO orderVO = this.assembleOrderVO(orderMaster, orderItemList);
            PageInfo pageResult = new PageInfo(Lists.newArrayList(orderMaster));
            pageResult.setList(Lists.newArrayList(orderVO));
            return ServerResponse.createBySuccess(pageResult);
        }
        return ServerResponse.createByErrorMessage("订单不存在");
    }

    @Override
    public ServerResponse<String> sendGoods(Long orderNo) {
        OrderMaster orderMaster = orderMasterMapper.selectByOrderNo(orderNo);
        if(orderMaster != null) {
            if(orderMaster.getStatus() == Const.OrderStatusEnum.PAID.getCode()) {
                orderMaster.setStatus(Const.OrderStatusEnum.SHIPPED.getCode());
                orderMaster.setSendTime(new Date());
                orderMasterMapper.updateByPrimaryKeySelective(orderMaster);
                return ServerResponse.createBySuccess("发货成功");
            } else if(orderMaster.getStatus() == Const.OrderStatusEnum.NO_PAY.getCode()) {
                return ServerResponse.createByErrorMessage("该订单尚未付款");
            } else if(orderMaster.getStatus() == Const.OrderStatusEnum.CANCELED.getCode()) {
                return ServerResponse.createByErrorMessage("该订单已取消");
            } else {
                return ServerResponse.createByErrorMessage("请勿重复发货");
            }
        }
        return ServerResponse.createByErrorMessage("订单不存在");
    }
}
