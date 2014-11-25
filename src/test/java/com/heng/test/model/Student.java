package com.heng.test.model;

import com.heng.excelwrapper.annotation.ExcelWrap;
import com.heng.excelwrapper.constant.TransferType;
import com.heng.excelwrapper.model.ExcelBean;

import java.util.List;

/**
 * Created by zhangheng07 on 2014/9/3.
 */
public class Student extends ExcelBean {
    @ExcelWrap(title="姓名",maxLength = 100)
    private String name;
    @ExcelWrap(title="常用地址",dataType = TransferType.LIST)
    private List<String> addresses;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<String> addresses) {
        this.addresses = addresses;
    }
}
