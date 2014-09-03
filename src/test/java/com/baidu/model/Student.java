package com.baidu.model;

import com.baidu.excelwrapper.annotation.ExcelWrap;
import com.baidu.excelwrapper.constant.TransferType;
import com.baidu.excelwrapper.model.ExcelBean;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangheng07 on 2014/9/3.
 */
public class Student extends ExcelBean {
    @ExcelWrap(title="姓名",maxLength = 100)
    private String name;
    @ExcelWrap(title="常用地址",dataType = TransferType.LIST)
    private List<String> adrresss;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getAdrresss() {
        return adrresss;
    }

    public void setAdrresss(List<String> adrresss) {
        this.adrresss = adrresss;
    }

}
