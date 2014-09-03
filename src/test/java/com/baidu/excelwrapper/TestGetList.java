package com.baidu.excelwrapper;

import com.baidu.excelwrapper.util.Excel2BeanUtil;
import com.baidu.model.Person;
import com.baidu.model.Student;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * Created by zhangheng07 on 2014/9/1.
 */

public class TestGetList {
    @Test
    public void getPersonList() {
        File excelFile = new File(getClass().getResource("/person.xlsx").getPath());
        try {
            List<Person> modelList = Excel2BeanUtil.getBeanList(excelFile, Person.class);
            for(int i = 0;i<modelList.size();i++){
                Person obj = modelList.get(i);
                System.out.println(ToStringBuilder.reflectionToString(obj));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void getStudentList() {
        File excelFile = new File(getClass().getResource("/student.xlsx").getPath());
        try {
            List<Student> modelList = Excel2BeanUtil.getBeanList(excelFile, Student.class);
            for(int i = 0;i<modelList.size();i++){
                Student obj = modelList.get(i);
                System.out.println(ToStringBuilder.reflectionToString(obj));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
