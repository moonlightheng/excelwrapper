package com.heng.excelwrapper.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Created by zhangheng07 on 2014/8/22.
 */

@Target(ElementType.TYPE)
public @interface ExcelOrder {

    public String[] titles() default { };
}
