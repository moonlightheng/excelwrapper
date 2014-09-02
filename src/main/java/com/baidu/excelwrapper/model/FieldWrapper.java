package com.baidu.excelwrapper.model;

import com.baidu.excelwrapper.annotation.ExcelWrap;

import java.lang.reflect.Field;

/**
 * Created by zhangheng07 on 2014/9/2.
 */
public class FieldWrapper {

    private Field f;

    private ExcelWrap e;

    public FieldWrapper(Field f, ExcelWrap e) {
        this.f = f;
        this.e = e;
    }

    public Field getF() {
        return f;
    }


    public ExcelWrap getE() {
        return e;
    }

}
