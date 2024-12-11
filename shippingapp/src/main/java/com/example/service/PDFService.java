package com.example.service;
import com.example.helper.BarcodeHelper;
import com.example.helper.QRCodeHelper;
import com.example.model.ShippingBill;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PDFService {

    @Autowired
    private ExcelService excelService;

    public ByteArrayOutputStream generatePDF(String id) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            ShippingBill bill = excelService.getShippingBillById(id);
            if (bill == null) {
                throw new IllegalArgumentException("Shipping bill not found for ID: " + id);
            }

            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);


            document.add(new Paragraph("Shipping Bill").setFontSize(20).setBold());

            document.add(new Paragraph("ID: " + bill.getId()));
            document.add(new Paragraph("Customer Name: " + bill.getCustomerName()));
            document.add(new Paragraph("Address: " + bill.getAddress()));
            document.add(new Paragraph("Amount: " + bill.getAmount()));

            byte[] qrCodeImage = QRCodeHelper.generateQRCode(bill.getId());
            ImageData qrData = ImageDataFactory.create(qrCodeImage);
            Image qrImage = new Image(qrData).scaleToFit(100, 100);
            document.add(qrImage);


            byte[] barcodeImage = BarcodeHelper.generateBarcode(bill.getId());
            ImageData barcodeData = ImageDataFactory.create(barcodeImage);
            Image barcodeImageElement = new Image(barcodeData).scaleToFit(200, 50);
            document.add(barcodeImageElement);

            document.add(new Paragraph(" ").setFixedLeading(10));

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return outputStream;
    }
}

