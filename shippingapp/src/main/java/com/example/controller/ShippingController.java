package com.example.controller;

import com.example.service.ExcelService;
import com.example.service.PDFService;
import com.example.model.ShippingBill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;

@RestController
@RequestMapping("/api/shipping")
public class ShippingController {

    @Autowired
    private ExcelService excelService;

    @Autowired
    private PDFService pdfService;

    @PostMapping("/submit")
    public ResponseEntity<String> submitShippingBill(@RequestBody ShippingBill shippingBill) {
        excelService.saveShippingBill(shippingBill);
        return ResponseEntity.ok("Shipping bill saved successfully.");
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadPDF(@PathVariable String id) {
        ByteArrayOutputStream pdfStream = pdfService.generatePDF(id);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=shipping_bill_" + id + ".pdf")
                .body(pdfStream.toByteArray());
    }
}
