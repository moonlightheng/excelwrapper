package com.heng.excelwrapper.util;


import com.heng.excelwrapper.annotation.ExcelWrap;
import com.heng.excelwrapper.model.ExcelBean;
import com.heng.excelwrapper.model.FieldWrapper;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangheng07 on 2014/9/1.
 */
public class Excel2BeanUtil {

    public static <T> List<T> getBeanList(File excelFile, Class<? extends ExcelBean> clazz) throws Exception {
        List<T> beanList = new ArrayList<T>();
        Workbook book = WorkbookFactory.create(new FileInputStream(excelFile.getPath()));
        Sheet sheet = book.getSheetAt(0);
        Field[] fields = clazz.getDeclaredFields();
        Map<String, FieldWrapper> titleConfig = new HashMap<String, FieldWrapper>();
        checkTitle(sheet, fields, titleConfig);
        assembleBean(clazz, beanList, sheet, titleConfig);
        return beanList;
    }

    private static <T> void assembleBean(Class<? extends ExcelBean> clazz, List<T> beanList, Sheet sheet,
                                         Map<String, FieldWrapper> titleConfig) throws Exception {
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            if (sheet.getRow(i) == null) {
                continue;
            }
            Object obj = clazz.newInstance();
            if (filterBlank(sheet, i)) {
                continue;
            }

            BeanUtils.setProperty(obj, "excelIndex", i);  // set the value of excelIndex from ExcelBean
            for (int j = 0; j < sheet.getRow(0).getPhysicalNumberOfCells(); j++) {
                String title = sheet.getRow(0).getCell(j).toString();
                Cell cell = sheet.getRow(i).getCell(j);
                FieldWrapper fieldWrapper = titleConfig.get(title);
                StringBuffer errors = new StringBuffer();
                if (fieldWrapper == null) {
                    continue;
                }
                if (cell == null || StringUtils.isBlank(cell.toString())) {
                    if (!fieldWrapper.getE().nullable()) {
                        errors.append(fieldWrapper.getE().title()).append("不能为空;");
                        BeanUtils.setProperty(obj, "errorMessage", errors);
                    }
                    continue;
                }
                String fieldName = fieldWrapper.getF().getName();
                switch (fieldWrapper.getE().dataType()) {
                    case BOOLEAN:
                        readBoolean(obj, title, cell, fieldWrapper, errors, fieldName);
                        break;
                    case STRING:
                        readString(obj, title, cell, errors, fieldName);
                        break;
                    case INTEGER:
                        readInteger(obj, title, cell, errors, fieldName);
                        break;
                    case FLOAT:
                        readFloat(obj, title, cell, errors, fieldName);
                        break;
                    case DATE:
                        readDate(obj, title, cell, fieldWrapper, errors, fieldName);
                        break;
                    case BIGDECIMAL:
                        readBigDecimal(obj, title, cell, errors, fieldName);
                        break;
                    case DOUBLE:
                        readDouble(obj, title, cell, errors, fieldName);
                        break;
                    case LONG:
                        readLong(obj, title, cell, errors, fieldName);
                        break;
                    case MULTI_SELECT:
                        readMultiSelect(obj, title, cell, fieldWrapper, errors, fieldName);
                        break;
                    case SINGLE_SELECT:
                        readSingleSelect(obj, title, cell, fieldWrapper, errors, fieldName);
                        break;
                    case LIST:
                        readList(obj, title, cell, fieldWrapper, errors, fieldName);
                        break;
                }
                BeanUtils.setProperty(obj, "errorMessage", errors);
            }
            beanList.add((T) obj);
        }
    }

    private static void readList(Object obj, String title, Cell cell, FieldWrapper fieldWrapper, StringBuffer errors, String fieldName) {
        try {
            String value = cell.getStringCellValue();
            String[] values = value.split(fieldWrapper.getE().separator());
            List<String> items = new ArrayList<String>();
            for (String valueExcel : values) {
                items.add(valueExcel);
            }
            BeanUtils.setProperty(obj, fieldName, items);
        } catch (Exception e) {
            errors.append(title).append("转换成列表出错").append(cell.toString()).append(";");
        }
        return;
    }

    private static void readMultiSelect(Object obj, String title, Cell cell, FieldWrapper fieldWrapper, StringBuffer errors, String fieldName) throws Exception {
        try {
            String value = cell.getStringCellValue();
            String separator = fieldWrapper.getE().separator();
            String[] values = value.split(separator);
            String[] range = fieldWrapper.getE().multiValue();

            for (String valueExcel : values) {
                for (int k = 0; k < range.length; k++) {
                    if (range[k].trim().equals(valueExcel.trim())) {
                        break;
                    }
                    if (k == range.length - 1) {
                        errors.append(title ).append("未找到可选值 (").append(valueExcel).append(");");
                    }
                }
            }
            if (StringUtils.isNotBlank(errors.toString())) {
                return;
            }
            BeanUtils.setProperty(obj, fieldName, value);
        } catch (Exception e) {
            errors.append(title).append("转换多选值出错").append(cell.toString()).append(";");
        }
    }

    private static void readSingleSelect(Object obj, String title, Cell cell, FieldWrapper fieldWrapper, StringBuffer errors, String fieldName) throws Exception {
        try {
            String value = cell.getStringCellValue();
            String[] singleRange = fieldWrapper.getE().multiValue();
            for (int k = 0; k < singleRange.length; k++) {
                if (singleRange[k].trim().equals(value.trim())) {
                    break;
                }
                if (k == singleRange.length - 1) {
                    errors.append(title).append("未找到可选值 (").append(value).append(");");
                }
            }
            if (StringUtils.isNotBlank(errors.toString())) {
                return;
            }
            BeanUtils.setProperty(obj, fieldName, value);
        } catch (Exception e) {
            errors.append(title).append("转换单选值出错").append(cell.toString()).append(";");
        }
    }


    private static void readString(Object obj, String title, Cell cell,  StringBuffer errors, String fieldName) {
        try {
            String value = cell.getStringCellValue();
            BeanUtils.setProperty(obj, fieldName, value);
        } catch (Exception e) {
            errors.append(title).append("转换成文本类型出错").append(cell.toString()).append(";");
        }
    }

    /**
     * filter the line which has no available data
     *
     * @param sheet
     * @param index
     * @return
     */
    private static boolean filterBlank(Sheet sheet, int index) {
        boolean isAllBlank = true;
        for (int j = 0; j < sheet.getRow(0).getPhysicalNumberOfCells(); j++) {
            Cell cell = sheet.getRow(index).getCell(j);
            if (cell != null) {
                if (StringUtils.isNotBlank(cell.toString())) {
                    isAllBlank = false;
                    break;
                }
            }
        }
        return isAllBlank;
    }

    private static void checkTitle(Sheet sheet, Field[] fields, Map<String, FieldWrapper> titleConfig) throws Exception {
        for (Field f : fields) {
            ExcelWrap wrap = f.getAnnotation(ExcelWrap.class);
            FieldWrapper fieldWrapper = new FieldWrapper(f, wrap);

            if (wrap != null) {
                for (int j = 0; j < sheet.getRow(0).getPhysicalNumberOfCells(); j++) {
                    String excelTitleName = sheet.getRow(0).getCell(j).getStringCellValue().trim();
                    if (wrap.title().equals(excelTitleName)) {
                        titleConfig.put(wrap.title(), fieldWrapper);
                        break;
                    }
                    if (j == sheet.getRow(0).getPhysicalNumberOfCells() - 1) {
                        throw new Exception("未找到标题 " + wrap.title());
                    }

                }
            }
        }
    }


    private static void readBoolean(Object obj, String title, Cell cell, FieldWrapper fieldWrapper, StringBuffer errors, String fieldName) {
        String value = cell.getStringCellValue();
        try {
            if (fieldWrapper.getE().trueWord().equals(value)) {
                BeanUtils.setProperty(obj, fieldName, true);
            } else if (fieldWrapper.getE().falseWord().equals(value)) {
                BeanUtils.setProperty(obj, fieldName, false);
            } else {
                errors.append(title).append("未能映射到布尔值").append(cell.getStringCellValue()).append(";");
            }
        } catch (Exception e) {
            errors.append(title).append("转换成布尔类型出错").append(cell.getStringCellValue()).append(";");
        }
    }

    private static void readDate(Object obj, String title, Cell cell, FieldWrapper fieldWrapper, StringBuffer errors, String fieldName) {
        try {
            if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                BeanUtils.setProperty(obj, fieldName, cell.getDateCellValue());
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat(fieldWrapper.getE().dateFormat());
                BeanUtils.setProperty(obj, fieldName, sdf.parse(cell.getStringCellValue()));
            }
        } catch (Exception e) {
            errors.append(title).append("转换成日期类型出错"
            ).append(cell.toString()).append(";");
        }
    }

    private static void readBigDecimal(Object obj, String title, Cell cell, StringBuffer errors, String fieldName) {
        try {
            if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                BeanUtils.setProperty(obj, fieldName, new BigDecimal(cell.getNumericCellValue()));
            } else {
                BeanUtils.setProperty(obj, fieldName, new BigDecimal(cell.getStringCellValue()));
            }
        } catch (Exception e) {
            errors.append(title).append("转换成精确数值类型出错").append(cell.toString()).append(";");
        }
    }

    private static void readDouble(Object obj, String title, Cell cell, StringBuffer errors, String fieldName) {
        try {
            if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                BeanUtils.setProperty(obj, fieldName, cell.getNumericCellValue());
            } else {
                BeanUtils.setProperty(obj, fieldName, Double.parseDouble(cell.getStringCellValue()));
            }
        } catch (Exception e) {
            errors.append(title).append("转换成浮点类型出错").append(cell.toString()).append(";");
        }
    }

    private static void readFloat(Object obj, String title, Cell cell, StringBuffer errors, String fieldName) {
        try {
            if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                BeanUtils.setProperty(obj, fieldName, cell.getNumericCellValue());
            } else {
                BeanUtils.setProperty(obj, fieldName, Float.parseFloat(cell.getStringCellValue()));
            }
        } catch (Exception e) {
            errors.append(title).append("转换成浮点类型出错").append(cell.toString()).append(";");
        }
    }

    private static void readInteger(Object obj, String title, Cell cell, StringBuffer errors, String fieldName) {
        try {
            if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                BeanUtils.setProperty(obj, fieldName, cell.getNumericCellValue());
            } else {
                BeanUtils.setProperty(obj, fieldName, Integer.parseInt(cell.getStringCellValue()));
            }
        } catch (Exception e) {
            errors.append(title).append("转换成整数类型出错").append(cell.toString()).append(";");
        }
    }

    private static void readLong(Object obj, String title, Cell cell, StringBuffer errors, String fieldName) {
        try {
            if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                BeanUtils.setProperty(obj, fieldName, cell.getNumericCellValue());
            } else {
                BeanUtils.setProperty(obj, fieldName, Long.parseLong(cell.getStringCellValue()));
            }
        } catch (Exception e) {
            errors.append(title).append("转换成整数类型出错").append(cell.toString()).append(";");
        }
    }
}