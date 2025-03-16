package com.zqq.product.mapper;


import com.zqq.product.model.ProductInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ProductMapper {

    @Select("select * from product_detail where id=#{productId}")
    ProductInfo selectProductById(Integer productId);

}
