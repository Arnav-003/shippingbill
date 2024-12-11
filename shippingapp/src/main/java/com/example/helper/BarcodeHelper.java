package com.example.helper;

import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
public class BarcodeHelper {
    public static byte[] generateBarcode(String data) {
        try {
            Code128Bean barcode128Bean = new Code128Bean();
            barcode128Bean.setHeight(15f);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BitmapCanvasProvider canvasProvider = new BitmapCanvasProvider(baos, "image/png", 300, BufferedImage.TYPE_BYTE_BINARY, false, 0);
            barcode128Bean.generateBarcode(canvasProvider, data);
            canvasProvider.finish();
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
}
