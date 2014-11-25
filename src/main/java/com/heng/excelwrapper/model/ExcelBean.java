package com.heng.excelwrapper.model;

/**
 * Created by zhangheng07 on 2014/8/22.
 */
public  class ExcelBean {

    protected  String errorMessage;

    protected  Integer excelIndex;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Integer getExcelIndex() {
        return excelIndex;
    }

    public void setExcelIndex(Integer excelIndex) {
        this.excelIndex = excelIndex;
    }
}
