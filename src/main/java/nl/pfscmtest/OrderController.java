package nl.pfscmtest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

@RestController
public class OrderController {

    @Value("${pdf.filepath}")
    private String pdfFilePath;

    @GetMapping("/order")
    public OrderResponse getOrder(@RequestParam int orderId) throws IOException {
        // Read the PDF file
        Resource pdfResource = new org.springframework.core.io.FileSystemResource(pdfFilePath);
        byte[] pdfBytes = Files.readAllBytes(pdfResource.getFile().toPath());
        String base64Pdf = Base64Utils.encodeToString(pdfBytes);

        // Create a response
        OrderResponse response = new OrderResponse();
        response.setOrderId(orderId);
        response.setExternalOrderId(UUID.randomUUID().toString());
        response.setPdfBase64(base64Pdf);

        return response;
    }

    static class OrderResponse {
        private int orderId;
        private String externalOrderId;
        private String pdfBase64;

        // Getters and setters
        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public String getExternalOrderId() {
            return externalOrderId;
        }

        public void setExternalOrderId(String externalOrderId) {
            this.externalOrderId = externalOrderId;
        }

        public String getPdfBase64() {
            return pdfBase64;
        }

        public void setPdfBase64(String pdfBase64) {
            this.pdfBase64 = pdfBase64;
        }
    }
}

