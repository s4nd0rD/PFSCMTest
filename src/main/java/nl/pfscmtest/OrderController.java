package nl.pfscmtest;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@RestController
public class OrderController {

    @Value("${pdf.filepath}")
    private String pdfFilePath;

    @PostMapping("/order")
    public GraphQLWrapper getOrder() {

        // Create order responses
        var acceptedOrder = new OrderResponse();
        acceptedOrder.setId(1);
        acceptedOrder.setExternalOrderId(UUID.randomUUID().toString());
        acceptedOrder.setRemarks("This is a test order");
        acceptedOrder.setStatus("QUOTATIONACCEPTED");
        acceptedOrder.setStatusDate("2021-01-01T00:00:00Z");

        var rejectedOrder = new OrderResponse();
        rejectedOrder.setId(2);
        rejectedOrder.setExternalOrderId(UUID.randomUUID().toString());
        rejectedOrder.setRemarks("This is a test order");
        rejectedOrder.setStatus("QUOTATIONREJECTED");
        rejectedOrder.setStatusDate("2021-01-01T00:00:00Z");

        var reviewRequestedOrder = new OrderResponse();
        reviewRequestedOrder.setId(3);
        reviewRequestedOrder.setExternalOrderId(UUID.randomUUID().toString());
        reviewRequestedOrder.setRemarks("This is a test order");
        reviewRequestedOrder.setStatus("QUOTATIONREVIEWREQUESTED");
        reviewRequestedOrder.setStatusDate("2021-01-01T00:00:00Z");

        // Add orders to a list
        List<OrderResponse> items = new ArrayList<>();
        items.add(acceptedOrder);
        items.add(rejectedOrder);
        items.add(reviewRequestedOrder);

        // Populate OrderData
        OrderData orderData = new OrderData();
        orderData.setItems(items);

        // Populate GraphQLResponse
        GraphQLResponse graphQLResponse = new GraphQLResponse();
        graphQLResponse.setOrders(orderData);

        // Wrap it in the "data" object
        GraphQLWrapper graphQLWrapper = new GraphQLWrapper();
        graphQLWrapper.setData(graphQLResponse);

        return graphQLWrapper;
    }

    @PostMapping("/media-attachment")
    public MediaAttachmentResponse getMediaAttachment() throws IOException {
        // Read the PDF file
        var pdfResource = new FileSystemResource(pdfFilePath);
        var pdfBytes = Files.readAllBytes(pdfResource.getFile().toPath());
        var base64Pdf = Base64.getEncoder().encodeToString(pdfBytes);

        // Create the attachment response
        var response = new MediaAttachmentResponse();
        var mediaAttachment = new MediaAttachment();

        var attachments = new ArrayList<FileAttachment>();
        attachments.add(new FileAttachment(base64Pdf));
        mediaAttachment.setAttachments(attachments);
        response.setMedia(mediaAttachment);
        return response;
    }

    @Data
    static class OrderResponse {
        private int id;
        private String externalOrderId;
        private String remarks;
        private String status;
        private String statusDate;
    }

    @Data
    public class OrderData {
        private List<OrderResponse> items;
    }

    @Data
    public class GraphQLResponse {
        private OrderData orders;
    }

    @Data
    public class GraphQLWrapper {
        private GraphQLResponse data;
    }

    @Data
    static class MediaAttachmentResponse {
        private MediaAttachment media;
    }

    public class MediaAttachment {
        private List<FileAttachment> attachments;

        // Getters and setters
        public List<FileAttachment> getAttachments() {
            return attachments;
        }

        public void setAttachments(List<FileAttachment> attachments) {
            this.attachments = attachments;
        }
    }

    @Setter
    @Getter
    public class FileAttachment {
        private String base64;

        public FileAttachment(String base64) {
            this.base64 = base64;
        }
    }
}
