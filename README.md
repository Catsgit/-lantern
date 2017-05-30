# Lantern
-------
## [目录]
* [/user/](#user)
    * [ ] [get_basic_info.do](#get_basic_info)
    * [ ] [update_basic.info.do](#update_basic_info)
    * [ ] [login.do](#login)
    * [ ] [register_get_verify.do](#register_get_verify)
    * [ ] [check_verify.do](#check_verify)
    * [ ] [register.do](#register)
    * [ ] [forget_get_verify.do](#forget_get_verify)
    * [ ] [forget_reset_password.do](#forget_reset_password)
    * [ ] [reset_password.do](#reset_password)
    * [ ] [logout.do](#logout)

* [/category/](#category)
    * [ ] [get_category.do](#get_category)

* [/product/](#product)
    * [ ] [get_product_list.do](#get_product_list)
    * [ ] [get_product_detail.do](#get_product_detail)

----

<h2 id='user'> /user/ </h2>

<h3 id='get_basic_info'> 1. get_basic_info.do </h3>


    ```
    request {
        (session)
    }
    response {
        UserBasicVo user
    }
    UserBasicVo {
        String username,
        String nickname,
        String phone,
        String email
    }
    ```
 
<h3 id='update_basic_info'> 2. update_basic_info.do </h3>   

    ```json
    request {
        (session)
        UserBasicVo user
    }
    response {
        status
    }
    ```

    
<h3 id='login'> 3. login.do </h3>

    ```json
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

    ```json
    request {
        String username
    }
    response {
        status
    }
    ```

<h3 id='check_verify'> 5. check_verify.do </h3>       

    ```json
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

    ```json
    request {
        User user (String username,String password)
        String token
    }
    response {
        status
    }
    ```

<h3 id='forget_get_verify'> 7. forget_get_verify.do </h3>

    ```json
    request {
        String username
    }
    response {
        status
    }
    ```

<h3 id='forget_reset_password'> 8. forget_reset_password.do </h3>

    ```json
    request {
        String username,
        String password,
        String token
    }
    response {
        status
    }
    ```

<h3 id='reset_password'> 9. reset_password.do </h3>

    ```json
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

    ```json
    request {
        (Session)
    }
    response {
        status
    }
    ```
-----
<h2 id='category'> 2. /category/ </h2>

<h3 id='get_category'> 1. get_category.do </h3>

    ```json
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

-----------
<h2 id='product'> 3. /product/ </h2>

<h3 id='get_product_list'> 1. get_product_list.do </h3>

    ```json
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
    
<h3 id='get_product_detail'> 2. get_product_detail.do </h3>

    ```json
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
    
