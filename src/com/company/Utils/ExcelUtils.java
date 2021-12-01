package com.company.Utils;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.File;

public class ExcelUtils {

    public static void writeExcelFrom2DList(String[][] data, String[] header, String fileName, String sheetName, int sheetPos) throws Exception {

        WritableWorkbook wb = Workbook.createWorkbook(new File(fileName));
        WritableSheet curSheet = wb.createSheet(sheetName, sheetPos);

        for (int i = 0; i < header.length; i++) {
            Label column = new Label(i, 0, header[i]);
            curSheet.addCell(column);
        }

        int j;
        for (int i = 0; i < data.length; i++) {//rows
            for (j = 0; j < data[i].length; j++) {//column
                Label row = new Label(j, i + 1, data[i][j]);
                curSheet.addCell(row);
            }
        }

        wb.write();
        wb.close();

    }


    public static void main(String[] args) throws Exception {
/*        String[] header = new String[]{"First", "sec", "third"};
        String[][] arr = new String[][]{{"aa", "bb", "cc"}, {"aa1", "bb2", "cc1"}, {"aa2", "bb3", "cc4"}, {"aasdf", "bbdsf", "ccsdf"}, {"aasdf", "bbdsf", "ccsdf"}, {"aasdf", "bbdsf", "ccsdf"}, {"aasdf", "bbdsf", "ccsdf"}, {"aasdf", "bbdsf", "ccsdf"}, {"aasdf", "bbdsf", "ccsdf"}, {"aasdf", "bbdsf", "ccsdf"}, {"aasdf", "bbdsf", "ccsdf"}, {"aasdf", "bbdsf", "ccsdf"}, {"aasdf", "bbdsf", "ccsdf"}};

        writeExcelFrom2DList(arr, header, "dat.xls", "first", 2);
        System.out.println("");*/
    }
}
