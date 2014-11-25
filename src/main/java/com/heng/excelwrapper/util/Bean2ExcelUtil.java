package com.heng.excelwrapper.util;

import com.heng.excelwrapper.annotation.ExcelWrap;
import com.heng.excelwrapper.model.ExcelBean;
import com.heng.excelwrapper.model.FieldWrapper;
import com.heng.excelwrapper.constant.TransferType;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by zhangheng07 on 2014/9/2.
 */
public class Bean2ExcelUtil {
    /**
     * the values of excelIndexs of beans must not be null
     *
     * @param excelFile
     * @param beans
     * @return excelFile
     * @throws Exception
     */
    public static File appendErrors2ExcelWithIndex(File excelFile, List<? extends ExcelBean> beans)
            throws Exception {

        Workbook book = WorkbookFactory.create(new FileInputStream(excelFile.getPath()));
        Sheet sheet = book.getSheetAt(0);
        Cell errorTitle = sheet.getRow(0).createCell(sheet.getRow(0).getPhysicalNumberOfCells());
        errorTitle.setCellValue("error_message");

        for (ExcelBean bean : beans) {
            int column = sheet.getRow(0).getPhysicalNumberOfCells() - 1;
            Cell cell = sheet.getRow(bean.getExcelIndex()).getCell(column);
            if (cell == null) {
                cell = sheet.getRow(bean.getExcelIndex()).createCell(column);
            }
            cell.setCellValue(bean.getErrorMessage());
        }

        FileOutputStream os = new FileOutputStream(excelFile.getPath());
        book.write(os);
        os.close();

        return excelFile;
    }

    /**
     * @param excelFile
     * @param beans
     * @return excelFile
     * @throws Exception
     */
    public static File appendErrors2Excel(File excelFile, List<? extends ExcelBean> beans)
            throws Exception {
        Workbook book = WorkbookFactory.create(new FileInputStream(excelFile.getPath()));
        Sheet sheet = book.getSheetAt(0);
        Cell errorTitle = sheet.getRow(0).createCell(sheet.getRow(0).getPhysicalNumberOfCells());
        errorTitle.setCellValue("error_message");
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            int column = sheet.getRow(0).getPhysicalNumberOfCells() - 1;
            if (sheet.getRow(i) != null && beans.size() >= i && beans.get(i - 1) != null) {
                Cell cell = sheet.getRow(i).getCell(column);
                if (cell == null) {
                    cell = sheet.getRow(i).createCell(column);
                }
                cell.setCellValue(beans.get(i - 1).getErrorMessage());
            }
        }
        FileOutputStream os = new FileOutputStream(excelFile.getPath());
        book.write(os);
        os.close();
        return excelFile;
    }

    /**
     * @param excelFile
     * @param clazz
     * @param beans
     * @throws Exception
     */
    public static void generateExcelData(File excelFile, Class clazz, List<? extends ExcelBean> beans) throws Exception {
        if (excelFile.exists()) {
            excelFile.delete();
        }
        if (!excelFile.exists()) {
            excelFile.createNewFile();
        }
        Workbook book = WorkbookFactory.create(new FileInputStream(excelFile.getPath()));
        Sheet sheet = book.createSheet();
        Field[] fields = clazz.getDeclaredFields();
        List<FieldWrapper> titles = new ArrayList<FieldWrapper>();
        for (Field f : fields) {
            ExcelWrap wrap = f.getAnnotation(ExcelWrap.class);
            if (wrap != null) {
                titles.add(new FieldWrapper(f, wrap));
            }
        }
        Row titleRow = sheet.createRow(0);
        for (int j = 0; j < titles.size(); j++) {
            Cell cell = titleRow.createCell(j);
            cell.setCellValue(titles.get(j).getE().title());
        }
        Cell errorCell = titleRow.createCell(titles.size());
        errorCell.setCellValue("errorMessage");
        StringBuffer errors = new StringBuffer();
        Collections.sort(titles);
        for (int i = 0; i < beans.size(); i++) {
            Row row = sheet.createRow(i + 1);
            Object bean = beans.get(i);
            for (int j = 0; j < titles.size(); j++) {
                Cell cell = row.createCell(j);
                ExcelWrap e = titles.get(j).getE();
                Field f = titles.get(j).getF();
                switch (e.dataType()) {
                    case TransferType.BOOLEAN:
                        writeBoolean(bean, cell, e, f);
                        break;
                    case TransferType.STRING:
                        writeString(bean, cell, f);
                        break;
                    case TransferType.INTEGER:
                        writeInteger(bean, cell, f);
                        break;
                    case TransferType.FLOAT:
                        break;
                    case TransferType.DATE:
                        break;
                    case TransferType.BIGDECIMAL:
                        break;
                    case TransferType.DOUBLE:
                        break;
                    case TransferType.LONG:
                        break;
                    case TransferType.MULTI_SELECT:
                        try {
                            String value = BeanUtils.getProperty(bean, f.getName());
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                            String[] values =value.split(e.separator());
                            String[] range = e.multiValue();

                            for (String valueExcel : values) {
                                for (int k = 0; k < range.length; k++) {
                                    if (range[k].trim().equals(valueExcel.trim())) {
                                        break;
                                    }
                                    if (k == range.length - 1) {
                                        errors.append(e.title() ).append("未找到可选值 (").append(valueExcel).append(");");
                                    }
                                }
                            }
                            if (StringUtils.isNotBlank(errors.toString())) {
                                return;
                            }
                            cell.setCellValue(value);

                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        break;
                    case TransferType.SINGLE_SELECT:
                        break;
                    case TransferType.LIST:


                        break;
                }

            }
        }
    }

    private static void writeInteger(Object bean, Cell cell, Field f) {
        try {
            String value = BeanUtils.getProperty(bean, f.getName());
            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
            cell.setCellValue(Integer.parseInt(value));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeString(Object bean, Cell cell, Field f) {
        try {
            String value = BeanUtils.getProperty(bean, f.getName());
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeBoolean(Object bean, Cell cell, ExcelWrap e, Field f) {
        try {
            String value = BeanUtils.getProperty(bean, f.getName());
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if (true == Boolean.parseBoolean(value)) {
                cell.setCellValue(e.trueWord());
            } else if (false == Boolean.parseBoolean(value)) {
                cell.setCellValue(e.falseWord());
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

}
