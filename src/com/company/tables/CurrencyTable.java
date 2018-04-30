package com.company.tables;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileInputStream;
import java.util.ArrayList;


public class CurrencyTable {
    int traderLotCount = 15;
    Workbook workBook;
    public ArrayList<Currency> currencies;

    public CurrencyTable(String path) {
        fillCurrencyTable(path);
    }

    public void fillCurrencyTable(String path) {
        currencies = new ArrayList<>();
        try {
            workBook = new HSSFWorkbook(new FileInputStream(path));
            for (int i = 1; i < traderLotCount * 2 + 1; i++) {
                currencies.add(new Currency(
                        getCellContent(workBook.getSheetAt(0).getRow(i).getCell(0)),
                        getCellContent(workBook.getSheetAt(0).getRow(i).getCell(1)),
                        (int)Math.floor(Double.parseDouble(getCellContent(workBook.getSheetAt(0).getRow(i).getCell(2)))),
                        (int)Math.floor(Double.parseDouble(getCellContent(workBook.getSheetAt(0).getRow(i).getCell(3)))),
                        (int)Math.floor(Double.parseDouble(getCellContent(workBook.getSheetAt(0).getRow(i).getCell(4))))
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCellContent(Cell cell){
        String text = "";
        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
            if(DateUtil.isCellDateFormatted(cell)){
                text = cell.getDateCellValue().toString();
            }else {
                text = Double.toString(cell.getNumericCellValue());
            }
        }
        if (cell.getCellType() == Cell.CELL_TYPE_STRING){
            text = cell.getRichStringCellValue().getString();
        }
        return text;
    }

    public void print() {
        System.out.println("---------------------------------------------------------------------");
        System.out.println("\t\t\tCurrency Table");
        System.out.println("---------------------------------------------------------------------");
        for (int i = 0; i < traderLotCount * 2; i++){
            currencies.get(i).print();
        }
        System.out.println("---------------------------------------------------------------------");
    }
}
