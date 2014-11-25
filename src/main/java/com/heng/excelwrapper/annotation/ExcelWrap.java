package com.heng.excelwrapper.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.heng.excelwrapper.constant.TransferType;

/**
 * Created by zhangheng07 on 2014/8/22.
 */

@Documented   
@Retention(RetentionPolicy.RUNTIME) 
@Target(ElementType.FIELD)
public @interface ExcelWrap {

    public String title() ;

    public TransferType dataType() default TransferType.STRING;

    public int maxLength() default 2000;

    public String defaultValue() default "";

    public String[]  multiValue() default  {};

    public String  separator() default  ",";

    public boolean nullable() default true;

    public String trueWord() default   "是";

    public String falseWord() default   "否";

    public String dateFormat() default  "yyyy-MM-dd HH:mm:ss";

    public String ListClass() default  "java.lang.String";

    public int order() default 0;

}
