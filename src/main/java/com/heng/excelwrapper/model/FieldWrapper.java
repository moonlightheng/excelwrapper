package com.heng.excelwrapper.model;

import com.heng.excelwrapper.annotation.ExcelWrap;

import java.lang.reflect.Field;

/**
 * Created by zhangheng07 on 2014/9/2.
 */
public class FieldWrapper implements Comparable<FieldWrapper>{

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

    @Override
    public int compareTo(FieldWrapper o) {
        return this.getE().order()-o.getE().order();
    }
}
