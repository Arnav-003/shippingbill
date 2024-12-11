package com.example.service;

import com.example.model.ShippingBill;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class ExcelService {

    private static final String FILE_PATH = "shipping_data.xlsx";

    public void saveShippingBill(ShippingBill shippingBill) {
        try {
            Workbook workbook;
            File file = new File(FILE_PATH);
            if (file.exists()) {
                workbook = new XSSFWorkbook(new FileInputStream(file));
            } else {
                workbook = new XSSFWorkbook();
                workbook.createSheet("ShippingBills");
                Sheet sheet = workbook.getSheet("ShippingBills");
                Row header = sheet.createRow(0);
                header.createCell(0).setCellValue("ID");
                header.createCell(1).setCellValue("Customer Name");
                header.createCell(2).setCellValue("Address");
                header.createCell(3).setCellValue("Amount");
            }

            Sheet sheet = workbook.getSheet("ShippingBills");
            int rowNum = sheet.getLastRowNum() + 1;
            Row row = sheet.createRow(rowNum);
            row.createCell(0).setCellValue(shippingBill.getId());
            row.createCell(1).setCellValue(shippingBill.getCustomerName());
            row.createCell(2).setCellValue(shippingBill.getAddress());
            row.createCell(3).setCellValue(shippingBill.getAmount());

            FileOutputStream fos = new FileOutputStream(FILE_PATH);
            workbook.write(fos);
            fos.close();
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ShippingBill getShippingBillById(String id) {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                return null;
            }

            Workbook workbook = new XSSFWorkbook(new FileInputStream(file));
            Sheet sheet = workbook.getSheet("ShippingBills");

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row
                if (row.getCell(0).getStringCellValue().equals(id)) {
                    ShippingBill bill = new ShippingBill();
                    bill.setId(row.getCell(0).getStringCellValue());
                    bill.setCustomerName(row.getCell(1).getStringCellValue());
                    bill.setAddress(row.getCell(2).getStringCellValue());
                    bill.setAmount(row.getCell(3).getNumericCellValue());
                    return bill;
                }
            }

            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
