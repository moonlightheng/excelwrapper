package com.heng.excelwrapper.constant;

/**
 * Created by zhangheng07 on 2014/8/22.
 */
public enum TransferType {
    BOOLEAN, STRING, INTEGER, FLOAT, DATE, BIGDECIMAL, DOUBLE ,LONG,MULTI_SELECT,SINGLE_SELECT,LIST;

    public static TransferType getEnumByString(String type){
        for (TransferType enumtype : TransferType.values()) {
            if(enumtype.toString().equalsIgnoreCase(type)){
                return enumtype;
            }
        }
        return STRING;
    }
}
