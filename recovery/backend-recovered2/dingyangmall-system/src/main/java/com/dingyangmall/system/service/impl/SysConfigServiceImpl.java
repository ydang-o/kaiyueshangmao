/*
 * Decompiled with CFR.
 */
package com.dingyangmall.system.service.impl;

import com.dingyangmall.common.annotation.DataSource;
import com.dingyangmall.common.core.redis.RedisCache;
import com.dingyangmall.common.core.text.Convert;
import com.dingyangmall.common.enums.DataSourceType;
import com.dingyangmall.common.exception.ServiceException;
import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.system.domain.SysConfig;
import com.dingyangmall.system.mapper.SysConfigMapper;
import com.dingyangmall.system.service.ISysConfigService;
import jakarta.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysConfigServiceImpl
implements ISysConfigService {
    @Autowired
    private SysConfigMapper configMapper;
    @Autowired
    private RedisCache redisCache;

    @PostConstruct
    public void init() {
        try {
            this.loadingConfigCache();
        }
        catch (Exception e) {
            System.err.println("[SysConfig] Redis \u8fde\u63a5\u5931\u8d25\uff0c\u8df3\u8fc7\u914d\u7f6e\u7f13\u5b58\u52a0\u8f7d: " + e.getMessage());
        }
    }

    @Override
    @DataSource(value=DataSourceType.MASTER)
    public SysConfig selectConfigById(Long configId) {
        SysConfig config = new SysConfig();
        config.setConfigId(configId);
        return this.configMapper.selectConfig(config);
    }

    @Override
    public String selectConfigByKey(String configKey) {
        try {
            String configValue = Convert.toStr(this.redisCache.getCacheObject(this.getCacheKey(configKey)));
            if (StringUtils.isNotEmpty(configValue)) {
                return configValue;
            }
        }
        catch (Exception configValue) {
            // empty catch block
        }
        SysConfig config = new SysConfig();
        config.setConfigKey(configKey);
        SysConfig retConfig = this.configMapper.selectConfig(config);
        if (StringUtils.isNotNull(retConfig)) {
            try {
                this.redisCache.setCacheObject(this.getCacheKey(configKey), retConfig.getConfigValue());
            }
            catch (Exception exception) {
                // empty catch block
            }
            return retConfig.getConfigValue();
        }
        return "";
    }

    @Override
    public boolean selectCaptchaEnabled() {
        String captchaEnabled = this.selectConfigByKey("sys.account.captchaEnabled");
        if (StringUtils.isEmpty(captchaEnabled)) {
            return true;
        }
        return Convert.toBool(captchaEnabled);
    }

    @Override
    public List<SysConfig> selectConfigList(SysConfig config) {
        return this.configMapper.selectConfigList(config);
    }

    @Override
    public int insertConfig(SysConfig config) {
        int row = this.configMapper.insertConfig(config);
        if (row > 0) {
            this.redisCache.setCacheObject(this.getCacheKey(config.getConfigKey()), config.getConfigValue());
        }
        return row;
    }

    @Override
    public int updateConfig(SysConfig config) {
        int row;
        SysConfig temp = this.configMapper.selectConfigById(config.getConfigId());
        if (!StringUtils.equals(temp.getConfigKey(), config.getConfigKey())) {
            this.redisCache.deleteObject(this.getCacheKey(temp.getConfigKey()));
        }
        if ((row = this.configMapper.updateConfig(config)) > 0) {
            this.redisCache.setCacheObject(this.getCacheKey(config.getConfigKey()), config.getConfigValue());
        }
        return row;
    }

    @Override
    public void deleteConfigByIds(Long[] configIds) {
        for (Long configId : configIds) {
            SysConfig config = this.selectConfigById(configId);
            if (StringUtils.equals("Y", config.getConfigType())) {
                throw new ServiceException(String.format("\u5185\u7f6e\u53c2\u6570\u3010%1$s\u3011\u4e0d\u80fd\u5220\u9664 ", config.getConfigKey()));
            }
            this.configMapper.deleteConfigById(configId);
            this.redisCache.deleteObject(this.getCacheKey(config.getConfigKey()));
        }
    }

    @Override
    public void loadingConfigCache() {
        List<SysConfig> configsList = this.configMapper.selectConfigList(new SysConfig());
        for (SysConfig config : configsList) {
            this.redisCache.setCacheObject(this.getCacheKey(config.getConfigKey()), config.getConfigValue());
        }
    }

    @Override
    public void clearConfigCache() {
        Collection<String> keys = this.redisCache.keys("sys_config:*");
        this.redisCache.deleteObject(keys);
    }

    @Override
    public void resetConfigCache() {
        this.clearConfigCache();
        this.loadingConfigCache();
    }

    @Override
    public boolean checkConfigKeyUnique(SysConfig config) {
        Long configId = StringUtils.isNull(config.getConfigId()) ? -1L : config.getConfigId();
        SysConfig info = this.configMapper.checkConfigKeyUnique(config.getConfigKey());
        return !StringUtils.isNotNull(info) || info.getConfigId().longValue() == configId.longValue();
    }

    private String getCacheKey(String configKey) {
        return "sys_config:" + configKey;
    }
}

