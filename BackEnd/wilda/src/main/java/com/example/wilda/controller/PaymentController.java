package com.example.wilda.controller;

import com.example.wilda.model.request.PaymentRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Value("${momo.accessKey}")
    private String accessKey;
    @Value("${momo.secretKey}")
    private String secretKey;
    @Value("${momo.partnerCode}")
    private String partnerCode;
    @Value("${momo.redirectUrl}")
    private String redirectUrl;
    @Value("${momo.ipnUrl}")
    private String ipnUrl;

    @PostMapping("/create")
    public ResponseEntity<String> createPayment(@RequestBody PaymentRequest paymentRequest) throws Exception {
        String orderId = partnerCode + Instant.now().getEpochSecond();
        String requestId = orderId;
        String requestType = "payWithMethod";
        String extraData = "";
        String lang = "vi";

        String rawSignature = "accessKey=" + accessKey + "&amount=" + paymentRequest.getAmount() + "&extraData=" + extraData
                + "&ipnUrl=" + ipnUrl + "&orderId=" + orderId + "&orderInfo=" + paymentRequest.getOrderInfo() + "&partnerCode=" + partnerCode
                + "&redirectUrl=" + redirectUrl + "&requestId=" + requestId + "&requestType=" + requestType;

        String signature = signHmacSHA256(rawSignature, secretKey);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("partnerCode", partnerCode);
        requestBody.put("partnerName", "Test");
        requestBody.put("storeId", "MomoTestStore");
        requestBody.put("requestId", requestId);
        requestBody.put("amount", paymentRequest.getAmount());
        requestBody.put("orderId", orderId);
        requestBody.put("orderInfo", paymentRequest.getOrderInfo());
        requestBody.put("redirectUrl", redirectUrl);
        requestBody.put("ipnUrl", ipnUrl);
        requestBody.put("lang", lang);
        requestBody.put("requestType", requestType);
        requestBody.put("autoCapture", true);
        requestBody.put("extraData", extraData);
        requestBody.put("signature", signature);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange("https://test-payment.momo.vn/v2/gateway/api/create",
                HttpMethod.POST, entity, String.class);

        return ResponseEntity.ok(response.getBody());
    }

    private String signHmacSHA256(String data, String key) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(secretKeySpec);
        byte[] hmacData = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hmacData);
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
