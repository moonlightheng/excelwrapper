package com.baidu.excelwrapper.util;

import com.baidu.excelwrapper.model.ExcelBean;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by zhangheng07 on 2014/9/2.
 */
public class Bean2ExcelUtil {
    /**
     * the values of excelIndexs of beans must not be null
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
     *
     * @param excelFile
     * @param beans
     * @return excelFile
     * @throws Exception
     */
    public static File appendErrors2Excel(File excelFile, List<? extends ExcelBean > beans)
            throws Exception{
        Workbook book=WorkbookFactory.create(new FileInputStream(excelFile.getPath()));
        Sheet sheet =  book.getSheetAt(0);
        Cell errorTitle =sheet.getRow(0).createCell(sheet.getRow(0).getPhysicalNumberOfCells());
        errorTitle.setCellValue("error_message");
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            int column = sheet.getRow(0).getPhysicalNumberOfCells() - 1;
            if(sheet.getRow(i)!=null&&beans.size()>=i&&beans.get(i-1)!=null){
                Cell cell =sheet.getRow(i).getCell(column);
                if(cell==null){
                    cell=sheet.getRow(i).createCell(column);
                }
                cell.setCellValue(beans.get(i-1).getErrorMessage());
            }
        }
        FileOutputStream os = new FileOutputStream(excelFile.getPath());
        book.write(os);
        os.close();
        return excelFile;
    }


}
