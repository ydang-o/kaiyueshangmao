/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.service;

import com.dingyangmall.common.utils.crypto.AesEncryptUtils;
import com.dingyangmall.mall.dto.DynamicCodeDTO;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class DynamicCodeService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private static final String DYNAMIC_MEMBER_CODE_PREFIX = "dynamic:member:";
    private static final String DYNAMIC_COUPON_CODE_PREFIX = "dynamic:coupon:";
    private static final String USED_CODE_PREFIX = "used:code:";
    private static final long DYNAMIC_CODE_EXPIRE_SECONDS = 300L;
    private static final long TIMESTAMP_TOLERANCE = 300L;

    public String generateDynamicMemberCode(Long memberId, String memberCode, String phoneNumber) {
        long timestamp = Instant.now().getEpochSecond();
        String nonce = UUID.randomUUID().toString().substring(0, 6);
        String data = timestamp + "|" + phoneNumber + "|" + memberCode + "|" + nonce;
        String encrypted = AesEncryptUtils.encrypt(data);
        String redisKey = DYNAMIC_MEMBER_CODE_PREFIX + memberId + ":" + timestamp;
        this.redisTemplate.opsForValue().set(redisKey, (Object)phoneNumber, Duration.ofSeconds(600L));
        return encrypted;
    }

    public DynamicCodeDTO generateDynamicMemberCodeDTO(Long memberId, String memberCode, String phoneNumber) {
        long timestamp = Instant.now().getEpochSecond();
        String nonce = UUID.randomUUID().toString().substring(0, 6);
        String data = timestamp + "|" + phoneNumber + "|" + memberCode + "|" + nonce;
        String encrypted = AesEncryptUtils.encrypt(data);
        String redisKey = DYNAMIC_MEMBER_CODE_PREFIX + memberId + ":" + timestamp;
        this.redisTemplate.opsForValue().set(redisKey, (Object)phoneNumber, Duration.ofSeconds(600L));
        DynamicCodeDTO dto = new DynamicCodeDTO();
        dto.setEncryptedCode(encrypted);
        dto.setTimestamp(timestamp);
        dto.setExpireSeconds(300);
        dto.setId(memberId);
        return dto;
    }

    public boolean verifyDynamicMemberCode(String encryptedCode, Long memberId, String originalCode, String phoneNumber) {
        try {
            String decrypted = AesEncryptUtils.decrypt(encryptedCode);
            String[] parts = decrypted.split("\\|");
            if (parts.length != 4) {
                return false;
            }
            long timestamp = Long.parseLong(parts[0]);
            String codePhone = parts[1];
            String codeMemberCode = parts[2];
            if (!codePhone.equals(phoneNumber)) {
                return false;
            }
            if (!codeMemberCode.equals(originalCode)) {
                return false;
            }
            long now = Instant.now().getEpochSecond();
            if (Math.abs(now - timestamp) > 300L) {
                return false;
            }
            String redisKey = DYNAMIC_MEMBER_CODE_PREFIX + memberId + ":" + timestamp;
            Object storedPhone = this.redisTemplate.opsForValue().get(redisKey);
            return storedPhone != null && storedPhone.toString().equals(phoneNumber);
        }
        catch (Exception e) {
            return false;
        }
    }

    public String generateDynamicCouponCode(Long couponId, String couponCode, Long userId, String phoneNumber) {
        long timestamp = Instant.now().getEpochSecond();
        String nonce = UUID.randomUUID().toString().substring(0, 6);
        String data = timestamp + "|" + phoneNumber + "|" + couponId + "|" + couponCode + "|" + nonce;
        String encrypted = AesEncryptUtils.encrypt(data);
        String redisKey = DYNAMIC_COUPON_CODE_PREFIX + couponId + ":" + userId + ":" + timestamp;
        this.redisTemplate.opsForValue().set(redisKey, (Object)phoneNumber, Duration.ofSeconds(600L));
        return encrypted;
    }

    public DynamicCodeDTO generateDynamicCouponCodeDTO(Long couponId, String couponCode, Long userId, String phoneNumber, String goodsName) {
        long timestamp = Instant.now().getEpochSecond();
        String nonce = UUID.randomUUID().toString().substring(0, 6);
        String data = timestamp + "|" + phoneNumber + "|" + couponId + "|" + couponCode + "|" + nonce;
        String encrypted = AesEncryptUtils.encrypt(data);
        String redisKey = DYNAMIC_COUPON_CODE_PREFIX + couponId + ":" + userId + ":" + timestamp;
        this.redisTemplate.opsForValue().set(redisKey, (Object)phoneNumber, Duration.ofSeconds(600L));
        DynamicCodeDTO dto = new DynamicCodeDTO();
        dto.setEncryptedCode(encrypted);
        dto.setTimestamp(timestamp);
        dto.setExpireSeconds(300);
        dto.setId(couponId);
        dto.setName(goodsName);
        return dto;
    }

    public boolean verifyDynamicCouponCode(String encryptedCode, Long couponId, Long userId, String originalCode, String phoneNumber) {
        try {
            String decrypted = AesEncryptUtils.decrypt(encryptedCode);
            String[] parts = decrypted.split("\\|");
            if (parts.length != 5) {
                return false;
            }
            long timestamp = Long.parseLong(parts[0]);
            String codePhone = parts[1];
            Long codeCouponId = Long.parseLong(parts[2]);
            String codeCouponCode = parts[3];
            if (!codePhone.equals(phoneNumber)) {
                return false;
            }
            if (!codeCouponId.equals(couponId)) {
                return false;
            }
            if (!codeCouponCode.equals(originalCode)) {
                return false;
            }
            long now = Instant.now().getEpochSecond();
            if (Math.abs(now - timestamp) > 300L) {
                return false;
            }
            String redisKey = DYNAMIC_COUPON_CODE_PREFIX + couponId + ":" + userId + ":" + timestamp;
            Object storedPhone = this.redisTemplate.opsForValue().get(redisKey);
            return storedPhone != null && storedPhone.toString().equals(phoneNumber);
        }
        catch (Exception e) {
            return false;
        }
    }

    public void clearDynamicMemberCode(Long memberId) {
        String pattern = DYNAMIC_MEMBER_CODE_PREFIX + memberId + ":*";
        this.deleteByPattern(pattern);
    }

    public void clearDynamicCouponCode(Long couponId, Long userId) {
        String pattern = DYNAMIC_COUPON_CODE_PREFIX + couponId + ":" + userId + ":*";
        this.deleteByPattern(pattern);
    }

    private void deleteByPattern(String pattern) {
        try {
            this.redisTemplate.delete(this.redisTemplate.keys(pattern));
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

