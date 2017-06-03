# Lantern
-------
## [目录]
* [/user/](#user)
    * [x] [get_basic_info.do](#get_basic_info)
    * [x] [update_basic.info.do](#update_basic_info)
    * [x] [login.do](#login)
    * [x] [register_get_verify.do](#register_get_verify)
    * [x] [check_verify.do](#check_verify)
    * [x] [register.do](#register)
    * [x] [forget_get_verify.do](#forget_get_verify)
    * [x] [forget_reset_password.do](#forget_reset_password)
    * [ ] [reset_password.do](#reset_password)
    * [x] [logout.do](#logout)

* [/category/](#category)
    * [x] [get.do](#get_category)

* [/product/](#product)
    * [ ] [detail.do](#get_product_detail)
    * [ ] [list.do (page)](#get_product_list_page)

* [/cart/](#cart)
    * [ ] [add.do](#add_or_update_cart)
    * [ ] [update.do](#update_cart_product)
    * [ ] [delete.do](#delete_product)
    * [ ] [list.do](#get_cart_list)
    * [ ] [select_all.do](#all_product_checked)
    * [ ] [un_select_all.do](#all_product_un_checked)
    * [ ] [select.do](#one_product_checked)
    * [ ] [un_select.do](#one_product_unchecked)
    * [ ] [get_product_count.do](#get_product_count)

* [/shipping/](#shipping)
    * [ ] [add.do](#add_shipping)
    * [ ] [delete.do](#delete_shipping)
    * [ ] [update.do](#update_shipping)
    * [ ] [select.do](#select_one_shipping)
    * [ ] [list.do](#list_all_shipping)
        
* [/order/](#order)
    * [ ] [create.do](#create_order)
    * [ ] [cancel.do](#cancel_order)
    * [ ] [get_order_cart_product.do](#getcartproduct)
    * [ ] [detail.do](#get_order_detail)
    * [ ] [list.do](#get_order_list)
    * [ ] [pay.do](#pay_order)
    * [ ] [alipay_callback.do](#for alipay callback)
    * [ ] [query_order_pay_status.do](#check_pay_status)
    
* [/manage/category/](#manage_category)
    * [ ] [add.do](#add_category)
    - [ ] [set.do](#set_category_name)
    - [ ] [get.do](#get_category_manage)

- [/manage/product/](#manage_product)
    - [ ] [save.do](#save_product)
    - [ ] [set_status.do](#set_product_status)
    - [ ] [detail.do](#get_product_detail_manage)
    - [ ] [list.do](#get_product_list_manage)
    - [ ] [search.do](#search_product_manage)
    - [ ] [upload.do](#upload_img)
    - [ ] [richtext_img_upload.do](#richtext_img_upload)

- [/manage/user/](#manage_user)
    - [ ] [login.do](#login_manage)

* [/manage/order/](#manage_order)
    * [ ] [list.do](#get_all_order_list)
    * [ ] [detail.do](#get_order_detail)
    * [ ] [search.do](#seach_order_by_orderNo)
    * [ ] [send_goods.do](#send_goods)
----

<h2 id='user'> /user/ </h2>

<h3 id='get_basic_info'> 1. get_basic_info.do </h3>


 ```java
    request {
        HttpSession session
    }
    response {
        UserVO user
    }
    UserBasicVO {
        String username,
        String nickname,
        String phone,
        String email
    }
 ```
 
<h3 id='update_basic_info'> 2. update_basic_info.do </h3>   

 ```java
    request {
        (session)
        UserBasicVo user
    }
    response {
        status
    }
 ```

    
<h3 id='login'> 3. login.do </h3>

 ```java
    request {
        String username,
        String password,
        (session)
    }
    response {
        status
    }
 ```

<h3 id='register_get_verify'> 4. register_get_verify.do </h3>    

 ```java
    request {
        String username
    }
    response {
        status
    }
 ```

<h3 id='check_verify'> 5. check_verify.do </h3>       

 ```java
    request {
        String username,
        String verify
    }
    response {
        status
        token
    }
 ```
    
<h3 id='register'> 6. register.do </h3>

 ```java
    request {
        User user (String username,String password)
        String registerToken
    }
    response {
        status
    }
 ```

<h3 id='forget_get_verify'> 7. forget_get_verify.do </h3>

 ```java
    request {
        String username
    }
    response {
        status
    }
 ```

<h3 id='forget_reset_password'> 8. forget_reset_password.do </h3>

 ```java
    request {
        String username,
        String newPassword,
        String forgetToken
    }
    response {
        status
    }
 ```

<h3 id='reset_password'> 9. reset_password.do </h3>

 ```java
    request {
        (Session)
        String oldPassword,
        String newPassword
    }
    response {
        status
    }
 ```
    
<h3 id='logout'> 10. logout.do </h3>

 ```java
    request {
        (Session)
    }
    response {
        status
    }
 ```
-----
<h2 id='category'> 2. /category/ </h2>

<h3 id='get_category'> 1. get.do </h3>

 ```java
    request {
    }
    response {
        <List<CategoryBriefVO>> categoryList
        status
    }
    CategoryBrief {
        id: String,
        name: String
    }
 ```

<<<<<<< HEAD
 -----------
 <h2 id='product'> 3. /product/ </h2>

 <h3 id='get_product_list'> 1. list.do </h3>

 ```java
    request {
        String categoryId
    }
    response {
        <List<ProductBriefVO>> productList
        status
    }
    ProductBriefVO {
        id: String,
        name: String,
        mainImage: String,
        price: ...
    }
 ```
=======
-----------
<h2 id='product'> 3. /product/ </h2>
>>>>>>> e6848c71c93fd79da2bb591777e39a2d925efd41
 
 <h3 id='get_product_list_page'> 1. list.do (page) </h3>

 ```java
    request {
        String keyword,
        Integer categoryId,
        int pageNum,
        int pageSize,
        String orderBy(price_asc, price_desc)
    }
    response {
        PageInfo pageInfo,
        status
    }
    ProductBriefVO {
        id: String,
        name: String,
        mainImage: String,
        price: ...
    }
 ```

 <h3 id='get_product_detail'> 2. detail.do </h3>

 ```java
    request {
        String productId
    }
    response {
        ProductDetailVO productDetail
        status
    }
    ProductDetailVO {
        id: String,
        name: String,
        subtitle: String,
        subImages: String,
        price: ...,
        detail: ...
    }
 ```
 -------
 <h2 id='product'> 4. /cart/ </h2>

 <h3 id='add'> 1. add.do </h3>

 ```java
    request {
        Integer count,
        Integer productId
    }
    response {
        CartVO -> {
            List<CartProductVO> cartProductVOList -> {
                Integer id,
                String userId,
                Integer productId,
                Integer quantity,
                String productName,
                String productSubtitle,
                String productMainImage,
                BigDecimal productPrice,
                Integer productStatus,
                BigDecimal productTotalPrice,
                Integer productStock,
                Boolean productChecked,
                String limitQuantity
            },
            BigDecimal cartTotalPrice,
            Boolean allChecked,
            String imageHost
        }
        status
    }
 ```
 <h3 id='update'> 2. update.do </h3>

 ```java
    request {
        Integer count,
        Integer productId
    }
    response {
        CartVO,
        status
    }
 ```
 <h3 id='delete'> 3. delete.do </h3>

 ```java
    request {
        String productIds [example: "1,2,3"](可批量删除)
    }
    response {
        CartVO,
        status
    }
 ```
 <h3 id='get_cart_list'> 4. list.do </h3>

 ```java
    request {
    }
    response {
        CartVO,
        status
    }
 ```
 <h3 id='select_all_product'> 5. select_all.do </h3>

 ```java
    request {
    }
    response {
        CartVO,
        status
    }
 ```
 <h3 id='un_select_all_product'> 6. un_select_all.do </h3>

 ```java
    request {
    }
    response {
        CartVO,
        status
    }
 ```
 <h3 id='select_one_product'> 7. select.do </h3>

 ```java
    request {
        Integer productId
    }
    response {
        CartVO,
        status
    }
 ```
 <h3 id='un_select_one_product'> 8. un_select.do </h3>

 ```java
    request {
        Integer productId
    }
    response {
        CartVO,
        status
    }
 ```
 <h3 id='get_cart_product_count'> 9. get_product_count.do </h3>

 ```java
    request {
    }
    response {
        Integer,
        status
    }
 ```
 ------------
 <h2 id='shipping'> 3. /shipping/ </h2>
 
 <h3 id='add_shipping'> 1. add.do </h3>
 
 ```java
    request {
        Shipping shipping -> {
            String receiverName,
            String receiverPhone,
            String receiverProvince,
            String receiverCity,
            String receiverDistrict,
            String receiverAddress,
            String receiverZip,
        }
    }
    response {
        Map -> {
            Integer shippingId
        }
    }
 ```
 <h3 id='delete_shipping'> 2. delete.do </h3>
 
 ```java
    request {
        Integer shippingId
    }
    response {
        status,
        message
    }
    
 ```
 <h3 id='update_shipping'> 3. update.do </h3>
 
 ```java
    request {
        Shipping shipping
    }
    response {
        status,
        message
    }
```
 <h3 id='select_one_shipping'> 4. select.do </h3>
 
 ```java
    request {
        Integer shippingId
    }
    response {
        Shipping
        status
        message
    }
```
 <h3 id='list_all_shipping'> 5. list.do </h3>
 
 ```java
    request {
        int pageNum,
        int pageSize
    }
    response {
        PageInfo -> List<Shipping>
    }
```

 ------------
 <h2 id='order'> /order/ </h2>
 
 <h3 id='create_order'> 1. create.do </h3>
 
 ```java
    request {
        Integer shippingId
    }
    response {
        status,
        OrderVO -> {
            Long orderNo,
            BigDecimal payment,
            Integer paymentType,
            String paymentTypeDesc,
            Integer postage,
            Integer status,
            String statusDesc,
            String paymentTime,
            String sendTime,
            String endTime,
            String closeTime,
            String createTime;
            List<OrderItemVO> orderItemVOList -> {
                Long orderNo,
                Integer productId,
                String productName,
                String productImage,
                BigDecimal currentUnitPrice,
                Integer quantity,
                BigDecimal totalPrice,
                String createTime;
            },
            String imageHost,
            Integer shippingId,
            String receiverName,
            ShippingVO shippingVO -> {
                String receiverName,
                String receiverPhone,
                String receiverProvince,
                String receiverCity,
                String receiverDistrict,
                String receiverAddress,
                String receiverZip;
            };
        };
    }
```
 <h3 id='cancel_order'> 2. cancel.do </h3>
 
 ```java
    request {
        Long orderNo;
    }
    response {
        status;
    }
```
 <h3 id='get_order_cart_product'> 3. get_order_cart_product.do </h3>
 
 ```java
    request {
    
    }
    response {
        status,
        OrderProductVO -> {
            List<OrderItemVO> orderItemVOList,
            BigDecimal orderTotalPrice,
            String imageHost;
        }
    }
```
 <h3 id='get_order_detail'> 4. detail.do </h3>
 
 ```java
    request {
        Long orderNo;
    }
    response {
        status,
        OrderVO;
    }
```
 <h3 id='get_order_list'> 4. list.do </h3>
 
 ```java
    request {
        int pageNum;
        int pageSize;
    }
    response {
        status,
        PageInfo -> {
            List<OrderVO> orderVOList;
        }
    }
```
 <h3 id='pay_order'> 5. pay.do </h3>
 
 ```java
    request {
        Long orderNo;
    }
    response {
        status,
        Map<String, String> -> {
            orderNo,
            qrUrl; [二维码图片url];
        };
    }
```
 <h3 id='alipay_callback'> 6. alipay_callback.do </h3>
 
 ```java
    此函数由支付宝调用
    request {
    }
    response {
        Const
    }
```
 <h3 id='check_pay_status'> 7. query_order_pay_status.do </h3>
 
 ```java
    request {
        Long orderNo;
    }
    response {
        status;
    }
```
 
 ------------
 <h2 id='manage_category'> /manage/category/ </h2>
 
 <h3 id='add_category'> 1. add.do </h3>
 
 ```java
    request {
        String categoryName
    }
    response {
        status
    }
 ```

 <h3 id='set_category_name'> 2. set.do </h3>
 
 ```java
    request {
        String categoryId,
        String categoryName
    }
    response {
        status 
    }
 ```

 <h3 id='get_category_manage'> 2. get.do </h3>
 
 ```java
    request {
    }
    response {
        status
        List<CategoryVO> category
    }
 ``` 
 
 ------------
 <h2 id='manage_product'> /manage/product/ </h2>
 
 <h3 id='save_product'>  save.do </h3>
 
 ```java
    request {
        Product product
    }
    response {
        status
    }
 ```

 <h3 id='set_product_status'> set_status.do </h3>
 
  ```java
    request {
        Integer productId, 
        Integer productStatus
    }
    response {
        status
    }
 ```

 <h3 id='get_product_detail_manage'> detail.do </h3>
  
  ```java
    request {
        Integer productId
    }
    response {
        ProductDetailVO
        status
    }
 ```

 <h3 id='get_product_list_manage'> list.do </h3>
   
  ```java
    request {
        int pageNum, 
        int pageSize
    }
    response {
        PageInfo
        status
    }
 ```

 <h3 id='search_product_manage'> search.do </h3>
    
  ```java
    request {
        String productName, 
        Integer productId, 
        int pageNum, 
        int pageSize
    }
    response {
        PageInfo
        status
    }
 ```

 <h3 id='upload_img'> upload.do </h3>
     
  ```java
    request {
        Multipartfile file
    }
    response {
        Map(uri -> “文件名” url -> “文件路径”)
        status
    }
 ```

 <h3 id='richtext_img_upload'> richtext_img_upload.do </h3>
     
  ```java
    request {
        Multipartfile file
    }
    response {
        Map -> {
            success: false/true
            msg:xxxx
            file_path: url(上传成功才有)
        }
    }
 ```

-------
 <h2 id='manage_user'> /manage/user/ </h2>
 
 <h3 id='login_manage'>  login.do </h3>
 
 ```java
    request {
        String username,
        String password,
        (session)
    }
    response {
        status
    }
 ```
 
 -------
  <h2 id='manage_order'> /manage/order/ </h2>
  
  <h3 id='get_all_order_list'> 1. list.do </h3>
  
  ```java
    request {
        int pageNum,
        int pageSize;
    }
    response {
        status,
        PageInfo -> {
            List<OrderVO> orderVOList;
        };
    }
```
  <h3 id='get_order_detail'> 2. detail.do </h3>
  
  ```java
    request {
        Long orderNo;
    }
    response {
        status,
        OrderVO;
    }
```
  <h3 id='search_order_by_orderNo'> 3. search.do </h3>
  
  ```java
    request {
        Long orderNo,
        int pageNum,
        int pageSize;
    }
    response {
        status,
        PageInfo -> {
            List<OrderVO> orderVOList
        }
    }
```
  <h3 id='send_goods'> 4. send_goods.do </h3>
  
  ```java
    request {
        Long orderNo;
    }
    response {
        status,
        string;
    }
```
 
