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

-----------
<h2 id='product'> 3. /product/ </h2>
 
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
 
