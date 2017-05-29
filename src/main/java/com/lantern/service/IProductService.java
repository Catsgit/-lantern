package com.lantern.service;

import com.github.pagehelper.PageInfo;
import com.lantern.common.ServerResponse;
import com.lantern.pojo.Product;
import com.lantern.vo.ProductDetailVO;

/**
 * Created by cat on 17-5-29.
 */
public interface IProductService {

    ServerResponse saveOrUpdateProduct(Product product);

    ServerResponse setProductStatus(Integer productId, Integer productStatus);

    ServerResponse<ProductDetailVO> manageProductDetail(Integer productId);

    ServerResponse<PageInfo> getProductList(int pageNum, int pageSize);
}
