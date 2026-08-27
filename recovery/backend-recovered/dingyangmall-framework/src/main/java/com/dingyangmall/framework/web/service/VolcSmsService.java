/*
 * Decompiled with CFR.
 */
package com.dingyangmall.framework.web.service;

import com.dingyangmall.framework.config.VolcSmsProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class VolcSmsService {
    private static final Logger log = LoggerFactory.getLogger(VolcSmsService.class);
    @Autowired
    private VolcSmsProperties volcSmsProperties;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String SERVICE = "volcSMS";
    private static final String REGION = "cn-north-1";
    private static final String ACTION_SEND = "SendSms";
    private static final String ACTION_CHECK = "CheckSmsVerifyCode";
    private static final String VERSION = "2020-01-01";

    public SmsSendResult sendSmsCode(String phone, String code) {
        if (this.isConfigured()) {
            return this.sendVerifyCode(phone, code);
        }
        log.debug("\u3010\u6a21\u62df\u77ed\u4fe1\u3011\u53d1\u9001\u7ed9 {} \u7684\u9a8c\u8bc1\u7801\u5df2\u751f\u6210\uff08\u672a\u914d\u7f6e\u706b\u5c71\u5f15\u64ce\u6216\u672a\u542f\u7528\uff09", (Object)phone);
        return SmsSendResult.success(null, "{\"mode\": \"local_fallback\"}");
    }

    private SmsSendResult sendVerifyCode(String phone, String code) {
        try {
            String normalizedPhone = this.normalizePhone(phone);
            HashMap<String, Object> bodyMap = new HashMap<String, Object>();
            bodyMap.put("SmsAccount", this.volcSmsProperties.getSmsAccount());
            bodyMap.put("Sign", this.volcSmsProperties.getSignName());
            bodyMap.put("TemplateID", this.volcSmsProperties.getTemplateId());
            bodyMap.put("TemplateParam", "{\"code\":\"" + code + "\"}");
            bodyMap.put("PhoneNumbers", normalizedPhone);
            String response = this.doRequest(ACTION_SEND, bodyMap);
            JsonNode root = this.objectMapper.readTree(response);
            JsonNode errorNode = root.path("ResponseMetadata").path("Error");
            if (!errorNode.isMissingNode() && errorNode.has("Code")) {
                String errorCode = errorNode.get("Code").asText();
                String errorMessage = errorNode.get("Message").asText();
                log.warn("\u706b\u5c71\u5f15\u64ce\u77ed\u4fe1\u53d1\u9001\u5931\u8d25 phone={} code={} message={}", phone, errorCode, errorMessage);
                return SmsSendResult.fail(errorCode + " - " + errorMessage, response);
            }
            JsonNode resultNode = root.path("Result");
            JsonNode msgIdsNode = resultNode.path("MessageID");
            String messageId = null;
            if (msgIdsNode.isArray() && msgIdsNode.size() > 0) {
                messageId = msgIdsNode.get(0).asText();
            }
            if (messageId == null || messageId.isEmpty()) {
                return SmsSendResult.fail("\u672a\u8fd4\u56de MessageID", response);
            }
            log.debug("\u706b\u5c71\u5f15\u64ce\u77ed\u4fe1\u53d1\u9001\u6210\u529f phone={} messageId={}", (Object)phone, (Object)messageId);
            return SmsSendResult.success(messageId, response);
        }
        catch (Exception e) {
            log.error("\u706b\u5c71\u5f15\u64ce\u77ed\u4fe1\u53d1\u9001\u5f02\u5e38 phone={}", (Object)phone, (Object)e);
            return SmsSendResult.fail("\u77ed\u4fe1\u53d1\u9001\u5931\u8d25: " + e.getMessage(), null);
        }
    }

    public boolean checkVerifyCode(String phone, String code) {
        if (!this.isConfigured()) {
            log.warn("\u706b\u5c71\u5f15\u64ce AK/SK \u672a\u914d\u7f6e\uff0c\u65e0\u6cd5\u6821\u9a8c\u9a8c\u8bc1\u7801");
            return false;
        }
        try {
            String normalizedPhone = this.normalizePhone(phone);
            HashMap<String, Object> bodyMap = new HashMap<String, Object>();
            bodyMap.put("SmsAccount", this.volcSmsProperties.getSmsAccount());
            bodyMap.put("PhoneNumber", normalizedPhone);
            bodyMap.put("Scene", "verify");
            bodyMap.put("Code", code);
            String response = this.doRequest(ACTION_CHECK, bodyMap);
            JsonNode root = this.objectMapper.readTree(response);
            JsonNode errorNode = root.path("ResponseMetadata").path("Error");
            if (!errorNode.isMissingNode() && errorNode.has("Code")) {
                String errorCode = errorNode.get("Code").asText();
                String errorMessage = errorNode.get("Message").asText();
                log.warn("\u706b\u5c71\u5f15\u64ce\u9a8c\u8bc1\u7801\u6821\u9a8c\u5931\u8d25 phone={} code={} message={}", phone, errorCode, errorMessage);
                return false;
            }
            String result = root.path("Result").asText();
            boolean success = "0".equals(result);
            log.debug("\u706b\u5c71\u5f15\u64ce\u9a8c\u8bc1\u7801\u6821\u9a8c\u7ed3\u679c phone={} result={} success={}", phone, result, success);
            return success;
        }
        catch (Exception e) {
            log.error("\u706b\u5c71\u5f15\u64ce\u9a8c\u8bc1\u7801\u6821\u9a8c\u5f02\u5e38 phone={}", (Object)phone, (Object)e);
            return false;
        }
    }

    private String doRequest(String action, Map<String, Object> bodyMap) throws Exception {
        String body = this.objectMapper.writeValueAsString(bodyMap);
        String bodyHash = VolcSmsService.sha256Hex(body);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String xDate = sdf.format(new Date());
        String shortDate = xDate.substring(0, 8);
        String credentialScope = shortDate + "/cn-north-1/volcSMS/request";
        String canonicalQuery = "Action=" + URLEncoder.encode(action, "UTF-8") + "&Version=" + URLEncoder.encode(VERSION, "UTF-8");
        String canonicalHeaders = "content-type:application/json\nhost:" + this.volcSmsProperties.getHost() + "\nx-content-sha256:" + bodyHash + "\nx-date:" + xDate + "\n";
        String signedHeaders = "content-type;host;x-content-sha256;x-date";
        String canonicalRequest = "POST\n/\n" + canonicalQuery + "\n" + canonicalHeaders + "\n" + signedHeaders + "\n" + bodyHash;
        String algorithm = "HMAC-SHA256";
        String canonicalRequestHash = VolcSmsService.sha256Hex(canonicalRequest);
        String stringToSign = algorithm + "\n" + xDate + "\n" + credentialScope + "\n" + canonicalRequestHash;
        byte[] kDate = VolcSmsService.hmacSha256(this.volcSmsProperties.getSecretKey().getBytes(StandardCharsets.UTF_8), shortDate);
        byte[] kRegion = VolcSmsService.hmacSha256(kDate, REGION);
        byte[] kService = VolcSmsService.hmacSha256(kRegion, SERVICE);
        byte[] kSigning = VolcSmsService.hmacSha256(kService, "request");
        String signature = VolcSmsService.hmacSha256Hex(kSigning, stringToSign);
        String authorization = algorithm + " Credential=" + this.volcSmsProperties.getAccessKey() + "/" + credentialScope + ", SignedHeaders=" + signedHeaders + ", Signature=" + signature;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Host", this.volcSmsProperties.getHost());
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Date", xDate);
        headers.set("X-Content-Sha256", bodyHash);
        headers.set("Authorization", authorization);
        HttpEntity<String> request = new HttpEntity<String>(body, headers);
        String url = "https://" + this.volcSmsProperties.getHost() + "/?" + canonicalQuery;
        ResponseEntity<String> response = this.restTemplate.exchange(url, HttpMethod.POST, request, String.class, new Object[0]);
        return (String)response.getBody();
    }

    private static byte[] hmacSha256(byte[] key, String data) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(key, "HmacSHA256"));
        return mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
    }

    private static String hmacSha256Hex(byte[] key, String data) throws Exception {
        byte[] result = VolcSmsService.hmacSha256(key, data);
        return VolcSmsService.bytesToHex(result);
    }

    private static String sha256Hex(String data) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
        return VolcSmsService.bytesToHex(hash);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private String normalizePhone(String phone) {
        String p = phone.trim().replace(" ", "");
        if (p.startsWith("+86")) {
            p = p.substring(3);
        } else if (p.startsWith("86") && p.length() == 13) {
            p = p.substring(2);
        }
        return "+86" + p;
    }

    public boolean isConfigured() {
        return this.volcSmsProperties.isEnabled() && this.volcSmsProperties.getAccessKey() != null && !this.volcSmsProperties.getAccessKey().isEmpty() && this.volcSmsProperties.getSecretKey() != null && !this.volcSmsProperties.getSecretKey().isEmpty();
    }

    public static class SmsSendResult {
        private final boolean success;
        private final String messageId;
        private final String rawResponse;
        private final String error;

        private SmsSendResult(boolean success, String messageId, String rawResponse, String error) {
            this.success = success;
            this.messageId = messageId;
            this.rawResponse = rawResponse;
            this.error = error;
        }

        public static SmsSendResult success(String messageId, String rawResponse) {
            return new SmsSendResult(true, messageId, rawResponse, null);
        }

        public static SmsSendResult fail(String error, String rawResponse) {
            return new SmsSendResult(false, null, rawResponse, error);
        }

        public boolean isSuccess() {
            return this.success;
        }

        public String getMessageId() {
            return this.messageId;
        }

        public String getRawResponse() {
            return this.rawResponse;
        }

        public String getError() {
            return this.error;
        }
    }
}

