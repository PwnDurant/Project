package com.why.library.common.enums;


import lombok.Getter;

import java.util.Arrays;

@Getter
public enum BookStatusEnum {

    DELETED(0,"删除"),
    NORMAL(1,"可借阅"),
    FORBIDDEN(2,"不可借阅");

    private Integer code;
    private String desc;

    BookStatusEnum(Integer code,String desc){
        this.code=code;
        this.desc=desc;
    }


//    通过状态码得到状态
    public static BookStatusEnum getStatusByCode(Integer code){
//        第一种:if else
//        if(code==0){
//            return BookStatusEnum.DELETED;
//        } else if (code==1) {
//            return BookStatusEnum.NORMAL;
//        }else{
//            return BookStatusEnum.FORBIDDEN;
//        }

//        第二种
//        switch (code){
//            case 0: return BookStatusEnum.DELETED;
//            case 1: return BookStatusEnum.NORMAL;
//            case 2: return BookStatusEnum.FORBIDDEN;
//        }


//        lambda
//        BookStatusEnums.values() 是java枚举类提供的一个方法，返回该枚举类的所有枚举常量的数组
//        Arrays.stream() 方法将枚举数组转换为Java8 Stream流，以便进行流式操作（如筛选，映射。。。）
//        .filter(status->status,getCode()==code) 用于筛选符合条件的元素，这里的lambda表达式作用：
//        1，取出流中的每个枚举status；2，调用status.getCode()获取其code值
//        3，如果code值等于外部传入的code则保留这个枚举对象；4，findFirst()用于从流中取出第一个枚举元素的对象，有时候筛选出来的可能不止一个元素
//        5，get()方法用于获取Option<T>的值，没有找到的话就会抛出NoSuchElementException
        return Arrays.stream(BookStatusEnum.values()).filter(status->status.getCode()==code).findFirst().get();
    }
}
