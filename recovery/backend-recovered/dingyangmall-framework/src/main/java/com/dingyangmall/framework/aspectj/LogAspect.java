/*
 * Decompiled with CFR.
 */
package com.dingyangmall.framework.aspectj;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.dingyangmall.common.annotation.Log;
import com.dingyangmall.common.core.domain.entity.SysUser;
import com.dingyangmall.common.core.domain.model.LoginUser;
import com.dingyangmall.common.enums.BusinessStatus;
import com.dingyangmall.common.enums.HttpMethod;
import com.dingyangmall.common.filter.PropertyPreExcludeFilter;
import com.dingyangmall.common.utils.SecurityUtils;
import com.dingyangmall.common.utils.ServletUtils;
import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.common.utils.ip.IpUtils;
import com.dingyangmall.framework.manager.AsyncManager;
import com.dingyangmall.framework.manager.factory.AsyncFactory;
import com.dingyangmall.system.domain.SysOperLog;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

@Aspect
@Component
public class LogAspect {
    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);
    public static final String[] EXCLUDE_PROPERTIES = new String[]{"password", "oldPassword", "newPassword", "confirmPassword"};
    private static final ThreadLocal<Long> TIME_THREADLOCAL = new NamedThreadLocal<Long>("Cost Time");

    @Before(value="@annotation(controllerLog)")
    public void boBefore(JoinPoint joinPoint, Log controllerLog) {
        TIME_THREADLOCAL.set(System.currentTimeMillis());
    }

    @AfterReturning(pointcut="@annotation(controllerLog)", returning="jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Log controllerLog, Object jsonResult) {
        this.handleLog(joinPoint, controllerLog, null, jsonResult);
    }

    @AfterThrowing(value="@annotation(controllerLog)", throwing="e")
    public void doAfterThrowing(JoinPoint joinPoint, Log controllerLog, Exception e) {
        this.handleLog(joinPoint, controllerLog, e, null);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void handleLog(JoinPoint joinPoint, Log controllerLog, Exception e, Object jsonResult) {
        try {
            LoginUser loginUser = SecurityUtils.getLoginUser();
            SysOperLog operLog = new SysOperLog();
            operLog.setStatus(BusinessStatus.SUCCESS.ordinal());
            String ip = IpUtils.getIpAddr();
            operLog.setOperIp(ip);
            operLog.setOperUrl(StringUtils.substring(ServletUtils.getRequest().getRequestURI(), 0, 255));
            if (loginUser != null) {
                operLog.setOperName(loginUser.getUsername());
                SysUser currentUser = loginUser.getUser();
                if (StringUtils.isNotNull(currentUser) && StringUtils.isNotNull(currentUser.getDept())) {
                    operLog.setDeptName(currentUser.getDept().getDeptName());
                }
            }
            if (e != null) {
                operLog.setStatus(BusinessStatus.FAIL.ordinal());
                operLog.setErrorMsg(StringUtils.substring(e.getMessage(), 0, 2000));
            }
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            operLog.setMethod(className + "." + methodName + "()");
            operLog.setRequestMethod(ServletUtils.getRequest().getMethod());
            this.getControllerMethodDescription(joinPoint, controllerLog, operLog, jsonResult);
            operLog.setCostTime(System.currentTimeMillis() - TIME_THREADLOCAL.get());
            AsyncManager.me().execute(AsyncFactory.recordOper(operLog));
        }
        catch (Exception exp) {
            log.error("\u64cd\u4f5c\u65e5\u5fd7\u8bb0\u5f55\u5f02\u5e38", exp);
        }
        finally {
            TIME_THREADLOCAL.remove();
        }
    }

    public void getControllerMethodDescription(JoinPoint joinPoint, Log log, SysOperLog operLog, Object jsonResult) throws Exception {
        operLog.setBusinessType(log.businessType().ordinal());
        operLog.setTitle(log.title());
        operLog.setOperatorType(log.operatorType().ordinal());
        if (log.isSaveRequestData()) {
            this.setRequestValue(joinPoint, operLog, log.excludeParamNames());
        }
        if (log.isSaveResponseData() && StringUtils.isNotNull(jsonResult)) {
            operLog.setJsonResult(StringUtils.substring(JSON.toJSONString(jsonResult), 0, 2000));
        }
    }

    private void setRequestValue(JoinPoint joinPoint, SysOperLog operLog, String[] excludeParamNames) throws Exception {
        Map<String, String> paramsMap = ServletUtils.getParamMap(ServletUtils.getRequest());
        String requestMethod = operLog.getRequestMethod();
        if (StringUtils.isEmpty(paramsMap) && (HttpMethod.PUT.name().equals(requestMethod) || HttpMethod.POST.name().equals(requestMethod))) {
            String params = this.argsArrayToString(joinPoint.getArgs(), excludeParamNames);
            operLog.setOperParam(StringUtils.substring(params, 0, 2000));
        } else {
            operLog.setOperParam(StringUtils.substring(JSON.toJSONString(paramsMap, this.excludePropertyPreFilter(excludeParamNames), new JSONWriter.Feature[0]), 0, 2000));
        }
    }

    private String argsArrayToString(Object[] paramsArray, String[] excludeParamNames) {
        Object params = "";
        if (paramsArray != null && paramsArray.length > 0) {
            for (Object o : paramsArray) {
                if (!StringUtils.isNotNull(o) || this.isFilterObject(o)) continue;
                try {
                    String jsonObj = JSON.toJSONString(o, this.excludePropertyPreFilter(excludeParamNames), new JSONWriter.Feature[0]);
                    params = (String)params + jsonObj.toString() + " ";
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
        }
        return ((String)params).trim();
    }

    public PropertyPreExcludeFilter excludePropertyPreFilter(String[] excludeParamNames) {
        return new PropertyPreExcludeFilter().addExcludes(ArrayUtils.addAll(EXCLUDE_PROPERTIES, excludeParamNames));
    }

    public boolean isFilterObject(Object o) {
        Map map;
        Iterator iterator;
        Class<?> clazz = o.getClass();
        if (clazz.isArray()) {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        }
        if (Collection.class.isAssignableFrom(clazz)) {
            Collection collection = (Collection)o;
            Iterator iterator2 = collection.iterator();
            if (iterator2.hasNext()) {
                Object value = iterator2.next();
                return value instanceof MultipartFile;
            }
        } else if (Map.class.isAssignableFrom(clazz) && (iterator = (map = (Map)o).entrySet().iterator()).hasNext()) {
            Map.Entry value;
            Map.Entry entry = value = iterator.next();
            return entry.getValue() instanceof MultipartFile;
        }
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse || o instanceof BindingResult;
    }
}

