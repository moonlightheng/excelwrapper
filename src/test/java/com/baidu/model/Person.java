package com.baidu.model;

import com.baidu.excelwrapper.annotation.ExcelWrap;
import com.baidu.excelwrapper.constant.TransferType;
import com.baidu.excelwrapper.model.ExcelBean;

/**
 * Created by zhangheng07 on 2014/9/1.
 */
public class Person extends ExcelBean {

    @ExcelWrap(title="姓名",maxLength = 100)
    private String name;
    @ExcelWrap(title="年龄",dataType= TransferType.INTEGER ,defaultValue="18")
    private Integer age;
    @ExcelWrap(title="性别", dataType= TransferType.SINGLE_SELECT, multiValue = {"男","女"},separator ="|")
    private String gender;
    @ExcelWrap(title="手机",dataType= TransferType.MULTI_SELECT,multiValue = {"IPhone","HTC","MX"},separator ="~")
    private String phones;
    @ExcelWrap(title="婚否",nullable = false,dataType= TransferType.BOOLEAN, trueWord = "已婚",falseWord = "未婚")
    private Boolean isMarried;


    public Boolean getIsMarried() {
        return isMarried;
    }

    public void setIsMarried(Boolean isMarried) {
        this.isMarried = isMarried;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhones() {
        return phones;
    }

    public void setPhones(String phones) {
        this.phones = phones;
    }
}
