/*
 * Decompiled with CFR.
 */
package com.dingyangmall.web.controller.mall;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dingyangmall.common.annotation.RepeatSubmit;
import com.dingyangmall.common.core.controller.BaseController;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.common.core.domain.entity.SysUser;
import com.dingyangmall.common.core.domain.model.LoginUser;
import com.dingyangmall.framework.web.service.SmsService;
import com.dingyangmall.framework.web.service.TokenService;
import com.dingyangmall.mall.entity.TbIntegralFlow;
import com.dingyangmall.mall.entity.UmsMember;
import com.dingyangmall.mall.service.TbIntegralFlowService;
import com.dingyangmall.mall.service.UmsMemberService;
import com.dingyangmall.system.service.ISysUserService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Generated;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/integralflow"})
public class TbIntegralFlowController
extends BaseController {
    private static final DateTimeFormatter DATETIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final TbIntegralFlowService integralFlowService;
    private final UmsMemberService umsMemberService;
    private final ISysUserService sysUserService;
    private final TokenService tokenService;
    private final SmsService smsService;

    @GetMapping(value={"/page"})
    @PreAuthorize(value="@ss.hasPermi('mall:integralflow:list')")
    public AjaxResult page(Page<TbIntegralFlow> page, @RequestParam(required=false) Long userId, final @RequestParam(required=false) String phone, @RequestParam(required=false) Integer operType, @RequestParam(required=false) String userType, @RequestParam(required=false) String beginTime, @RequestParam(required=false) String endTime) {
        HashSet<Long> matchedUserIds = new HashSet<Long>();
        if (StringUtils.hasText(phone)) {
            List members = this.umsMemberService.list((Wrapper)Wrappers.lambdaQuery().like(UmsMember::getPhone, phone));
            members.stream().map(UmsMember::getId).forEach(matchedUserIds::add);
            List<SysUser> sysUsers = this.sysUserService.selectUserList(new SysUser(){
                {
                    this.setPhonenumber(phone);
                }
            });
            sysUsers.stream().map(SysUser::getUserId).forEach(matchedUserIds::add);
            if (matchedUserIds.isEmpty()) {
                Page emptyPage = new Page();
                emptyPage.setCurrent(page.getCurrent());
                emptyPage.setSize(page.getSize());
                emptyPage.setTotal(0L);
                emptyPage.setPages(0L);
                emptyPage.setRecords(List.of());
                return AjaxResult.success(emptyPage);
            }
            if (userId != null) {
                if (!matchedUserIds.contains(userId)) {
                    Page emptyPage = new Page();
                    emptyPage.setCurrent(page.getCurrent());
                    emptyPage.setSize(page.getSize());
                    emptyPage.setTotal(0L);
                    emptyPage.setPages(0L);
                    emptyPage.setRecords(List.of());
                    return AjaxResult.success(emptyPage);
                }
                matchedUserIds.clear();
                matchedUserIds.add(userId);
            }
            LocalDateTime begin = TbIntegralFlowController.parseTime(beginTime, true);
            LocalDateTime end = TbIntegralFlowController.parseTime(endTime, false);
            LambdaQueryWrapper wrapper = (LambdaQueryWrapper)((LambdaQueryWrapper)((LambdaQueryWrapper)((LambdaQueryWrapper)((LambdaQueryWrapper)Wrappers.lambdaQuery().in(TbIntegralFlow::getUserId, matchedUserIds)).eq(operType != null, TbIntegralFlow::getOperType, (Object)operType)).ge(begin != null, TbIntegralFlow::getOperTime, (Object)begin)).le(end != null, TbIntegralFlow::getOperTime, (Object)end)).orderByDesc(TbIntegralFlow::getOperTime);
            Page<TbIntegralFlow> result = this.integralFlowService.page(page, wrapper);
            return this.buildResultWithUserInfo(result, matchedUserIds);
        }
        if (StringUtils.hasText(userType)) {
            HashSet typeUserIds = new HashSet();
            if ("\u4f1a\u5458".equals(userType)) {
                List allMembers = this.umsMemberService.list();
                allMembers.stream().map(UmsMember::getId).forEach(typeUserIds::add);
            } else if ("\u7ba1\u7406\u5458".equals(userType)) {
                List<SysUser> allSysUsers = this.sysUserService.selectUserList(new SysUser());
                allSysUsers.stream().map(SysUser::getUserId).forEach(typeUserIds::add);
            }
            if (matchedUserIds.isEmpty()) {
                matchedUserIds.addAll(typeUserIds);
            } else {
                matchedUserIds.retainAll(typeUserIds);
            }
            if (matchedUserIds.isEmpty()) {
                Page emptyPage = new Page();
                emptyPage.setCurrent(page.getCurrent());
                emptyPage.setSize(page.getSize());
                emptyPage.setTotal(0L);
                emptyPage.setPages(0L);
                emptyPage.setRecords(List.of());
                return AjaxResult.success(emptyPage);
            }
        }
        LocalDateTime begin = TbIntegralFlowController.parseTime(beginTime, true);
        LocalDateTime end = TbIntegralFlowController.parseTime(endTime, false);
        LambdaQueryWrapper wrapper = (LambdaQueryWrapper)((LambdaQueryWrapper)((LambdaQueryWrapper)((LambdaQueryWrapper)((LambdaQueryWrapper)((LambdaQueryWrapper)Wrappers.lambdaQuery().eq(userId != null, TbIntegralFlow::getUserId, (Object)userId)).in(!matchedUserIds.isEmpty(), TbIntegralFlow::getUserId, matchedUserIds)).eq(operType != null, TbIntegralFlow::getOperType, (Object)operType)).ge(begin != null, TbIntegralFlow::getOperTime, (Object)begin)).le(end != null, TbIntegralFlow::getOperTime, (Object)end)).orderByDesc(TbIntegralFlow::getOperTime);
        Page<TbIntegralFlow> result = this.integralFlowService.page(page, wrapper);
        return this.buildResultWithUserInfo(result, matchedUserIds.isEmpty() ? null : matchedUserIds);
    }

    private AjaxResult buildResultWithUserInfo(IPage<TbIntegralFlow> result, Set<Long> targetUserIds) {
        List<TbIntegralFlow> records = result.getRecords();
        if (!records.isEmpty()) {
            List userIds = records.stream().map(TbIntegralFlow::getUserId).distinct().collect(Collectors.toList());
            List members = this.umsMemberService.listByIds(userIds);
            Map<Long, UmsMember> memberMap = members.stream().collect(Collectors.toMap(UmsMember::getId, m -> m));
            ArrayList<SysUser> sysUsers = new ArrayList<SysUser>();
            for (Long uid : userIds) {
                SysUser sysUser = this.sysUserService.selectUserById(uid);
                if (sysUser == null) continue;
                sysUsers.add(sysUser);
            }
            Map<Long, SysUser> sysUserMap = sysUsers.stream().collect(Collectors.toMap(SysUser::getUserId, u -> u));
            List voList = records.stream().map(flow -> {
                IntegralFlowVO vo = new IntegralFlowVO();
                vo.setId(flow.getId());
                vo.setUserId(flow.getUserId());
                vo.setOperType(flow.getOperType());
                vo.setIntegralNum(flow.getIntegralNum());
                vo.setSourceUserId(flow.getSourceUserId());
                vo.setBusinessId(flow.getBusinessId());
                vo.setRemark(flow.getRemark());
                vo.setOperTime(flow.getOperTime());
                vo.setCreateTime(flow.getCreateTime());
                vo.setCreateBy(flow.getCreateBy());
                vo.setDelFlag(flow.getDelFlag());
                UmsMember member = (UmsMember)memberMap.get(flow.getUserId());
                if (member != null) {
                    vo.setPhone(member.getPhone());
                    vo.setNickname(member.getNickname());
                    vo.setRealName(member.getRealName());
                    vo.setUserType("\u4f1a\u5458");
                    vo.setPoints(member.getPoints());
                    Integer currentPoints = member.getPoints() != null ? member.getPoints() : 0;
                    Integer changeNum = flow.getIntegralNum() != null ? flow.getIntegralNum() : 0;
                    vo.setBeforePoints(currentPoints - changeNum);
                } else {
                    SysUser sysUser = (SysUser)sysUserMap.get(flow.getUserId());
                    if (sysUser != null) {
                        vo.setPhone(sysUser.getPhonenumber());
                        vo.setNickname(sysUser.getNickName());
                        vo.setRealName(sysUser.getNickName());
                        vo.setUserType("\u7ba1\u7406\u5458");
                        vo.setDealerPoints(sysUser.getDealerPoints());
                        Integer currentDealerPoints = sysUser.getDealerPoints() != null ? sysUser.getDealerPoints() : 0;
                        Integer changeNum = flow.getIntegralNum() != null ? flow.getIntegralNum() : 0;
                        vo.setBeforePoints(currentDealerPoints - changeNum);
                    }
                }
                return vo;
            }).collect(Collectors.toList());
            Page voPage = new Page();
            voPage.setCurrent(result.getCurrent());
            voPage.setSize(result.getSize());
            voPage.setTotal(result.getTotal());
            voPage.setPages(result.getPages());
            voPage.setRecords(voList);
            return AjaxResult.success(voPage);
        }
        return AjaxResult.success(result);
    }

    @RepeatSubmit(interval=2000, message="\u8bf7\u52ff\u91cd\u590d\u63d0\u4ea4")
    @PostMapping(value={"/grant"})
    @PreAuthorize(value="@ss.hasPermi('mall:integralflow:grant')")
    public AjaxResult grant(@RequestParam String phone, @RequestParam Integer points, @RequestParam(required=false) String remark, @RequestParam(required=false) String smsCode) {
        boolean isAdmin;
        if (!StringUtils.hasText(phone)) {
            return AjaxResult.error("\u624b\u673a\u53f7\u4e0d\u80fd\u4e3a\u7a7a");
        }
        if (points == null || points <= 0) {
            return AjaxResult.error("\u79ef\u5206\u6570\u5fc5\u987b\u5927\u4e8e0");
        }
        LoginUser loginUser = this.getLoginUser();
        SysUser currentUser = loginUser.getUser();
        if (currentUser == null) {
            return AjaxResult.error("\u5f53\u524d\u7528\u6237\u672a\u767b\u5f55");
        }
        if (!StringUtils.hasText(smsCode)) {
            return AjaxResult.error("\u8bf7\u8f93\u5165\u77ed\u4fe1\u9a8c\u8bc1\u7801");
        }
        String operatorPhone = currentUser.getPhonenumber();
        if (!StringUtils.hasText(operatorPhone)) {
            return AjaxResult.error("\u5f53\u524d\u8d26\u53f7\u672a\u8bbe\u7f6e\u624b\u673a\u53f7\uff0c\u65e0\u6cd5\u8fdb\u884c\u77ed\u4fe1\u9a8c\u8bc1\uff0c\u8bf7\u5148\u7ed1\u5b9a\u624b\u673a\u53f7");
        }
        try {
            this.smsService.validateSmsCode(operatorPhone, smsCode);
        }
        catch (Exception e) {
            return AjaxResult.error("\u9a8c\u8bc1\u7801\u9519\u8bef\u6216\u5df2\u8fc7\u671f");
        }
        List members = this.umsMemberService.list((Wrapper)Wrappers.lambdaQuery().eq(UmsMember::getPhone, phone));
        if (members.isEmpty()) {
            return AjaxResult.error("\u672a\u627e\u5230\u8be5\u624b\u673a\u53f7\u5bf9\u5e94\u7684\u4f1a\u5458");
        }
        UmsMember member = (UmsMember)members.get(0);
        boolean bl = isAdmin = currentUser != null && currentUser.isAdmin();
        if (currentUser != null && !isAdmin) {
            List dealerMembers = this.umsMemberService.list((Wrapper)Wrappers.lambdaQuery().eq(UmsMember::getPhone, currentUser.getPhonenumber()));
            if (dealerMembers.isEmpty()) {
                return AjaxResult.error("\u5f53\u524d\u7ecf\u9500\u5546\u672a\u7ed1\u5b9a\u4f1a\u5458\uff0c\u8bf7\u8054\u7cfb\u7ba1\u7406\u5458");
            }
            UmsMember dealerMember = (UmsMember)dealerMembers.get(0);
            this.integralFlowService.addPoints(dealerMember.getId(), -points.intValue(), 2, "\u53d1\u653e\u79ef\u5206\u7ed9\u4f1a\u5458[" + member.getNickname() + "]");
        }
        String finalRemark = StringUtils.hasText(remark) ? remark : "\u8d85\u7ea7\u7ba1\u7406\u5458\u53d1\u653e\u79ef\u5206";
        this.integralFlowService.addPoints(member.getId(), points, 2, finalRemark);
        return AjaxResult.success("\u53d1\u653e\u6210\u529f");
    }

    private static LocalDateTime parseTime(String s, boolean startOfDay) {
        if (!StringUtils.hasText(s)) {
            return null;
        }
        try {
            if (s.length() > 10) {
                return LocalDateTime.parse(s, DATETIME);
            }
            return startOfDay ? LocalDate.parse(s, DATE).atStartOfDay() : LocalDate.parse(s, DATE).atTime(23, 59, 59);
        }
        catch (Exception e) {
            return null;
        }
    }

    @Generated
    public TbIntegralFlowController(TbIntegralFlowService integralFlowService, UmsMemberService umsMemberService, ISysUserService sysUserService, TokenService tokenService, SmsService smsService) {
        this.integralFlowService = integralFlowService;
        this.umsMemberService = umsMemberService;
        this.sysUserService = sysUserService;
        this.tokenService = tokenService;
        this.smsService = smsService;
    }

    public static class IntegralFlowVO {
        private Long id;
        private Long userId;
        private Integer operType;
        private Integer integralNum;
        private Long sourceUserId;
        private String businessId;
        private String remark;
        private LocalDateTime operTime;
        private LocalDateTime createTime;
        private String createBy;
        private Integer delFlag;
        private String phone;
        private String nickname;
        private String realName;
        private String userType;
        private Integer points;
        private Integer dealerPoints;
        private Integer beforePoints;

        @Generated
        public IntegralFlowVO() {
        }

        @Generated
        public Long getId() {
            return this.id;
        }

        @Generated
        public Long getUserId() {
            return this.userId;
        }

        @Generated
        public Integer getOperType() {
            return this.operType;
        }

        @Generated
        public Integer getIntegralNum() {
            return this.integralNum;
        }

        @Generated
        public Long getSourceUserId() {
            return this.sourceUserId;
        }

        @Generated
        public String getBusinessId() {
            return this.businessId;
        }

        @Generated
        public String getRemark() {
            return this.remark;
        }

        @Generated
        public LocalDateTime getOperTime() {
            return this.operTime;
        }

        @Generated
        public LocalDateTime getCreateTime() {
            return this.createTime;
        }

        @Generated
        public String getCreateBy() {
            return this.createBy;
        }

        @Generated
        public Integer getDelFlag() {
            return this.delFlag;
        }

        @Generated
        public String getPhone() {
            return this.phone;
        }

        @Generated
        public String getNickname() {
            return this.nickname;
        }

        @Generated
        public String getRealName() {
            return this.realName;
        }

        @Generated
        public String getUserType() {
            return this.userType;
        }

        @Generated
        public Integer getPoints() {
            return this.points;
        }

        @Generated
        public Integer getDealerPoints() {
            return this.dealerPoints;
        }

        @Generated
        public Integer getBeforePoints() {
            return this.beforePoints;
        }

        @Generated
        public void setId(Long id) {
            this.id = id;
        }

        @Generated
        public void setUserId(Long userId) {
            this.userId = userId;
        }

        @Generated
        public void setOperType(Integer operType) {
            this.operType = operType;
        }

        @Generated
        public void setIntegralNum(Integer integralNum) {
            this.integralNum = integralNum;
        }

        @Generated
        public void setSourceUserId(Long sourceUserId) {
            this.sourceUserId = sourceUserId;
        }

        @Generated
        public void setBusinessId(String businessId) {
            this.businessId = businessId;
        }

        @Generated
        public void setRemark(String remark) {
            this.remark = remark;
        }

        @Generated
        public void setOperTime(LocalDateTime operTime) {
            this.operTime = operTime;
        }

        @Generated
        public void setCreateTime(LocalDateTime createTime) {
            this.createTime = createTime;
        }

        @Generated
        public void setCreateBy(String createBy) {
            this.createBy = createBy;
        }

        @Generated
        public void setDelFlag(Integer delFlag) {
            this.delFlag = delFlag;
        }

        @Generated
        public void setPhone(String phone) {
            this.phone = phone;
        }

        @Generated
        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        @Generated
        public void setRealName(String realName) {
            this.realName = realName;
        }

        @Generated
        public void setUserType(String userType) {
            this.userType = userType;
        }

        @Generated
        public void setPoints(Integer points) {
            this.points = points;
        }

        @Generated
        public void setDealerPoints(Integer dealerPoints) {
            this.dealerPoints = dealerPoints;
        }

        @Generated
        public void setBeforePoints(Integer beforePoints) {
            this.beforePoints = beforePoints;
        }

        @Generated
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof IntegralFlowVO)) {
                return false;
            }
            IntegralFlowVO other = (IntegralFlowVO)o;
            if (!other.canEqual(this)) {
                return false;
            }
            Long this$id = this.getId();
            Long other$id = other.getId();
            if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
                return false;
            }
            Long this$userId = this.getUserId();
            Long other$userId = other.getUserId();
            if (this$userId == null ? other$userId != null : !((Object)this$userId).equals(other$userId)) {
                return false;
            }
            Integer this$operType = this.getOperType();
            Integer other$operType = other.getOperType();
            if (this$operType == null ? other$operType != null : !((Object)this$operType).equals(other$operType)) {
                return false;
            }
            Integer this$integralNum = this.getIntegralNum();
            Integer other$integralNum = other.getIntegralNum();
            if (this$integralNum == null ? other$integralNum != null : !((Object)this$integralNum).equals(other$integralNum)) {
                return false;
            }
            Long this$sourceUserId = this.getSourceUserId();
            Long other$sourceUserId = other.getSourceUserId();
            if (this$sourceUserId == null ? other$sourceUserId != null : !((Object)this$sourceUserId).equals(other$sourceUserId)) {
                return false;
            }
            Integer this$delFlag = this.getDelFlag();
            Integer other$delFlag = other.getDelFlag();
            if (this$delFlag == null ? other$delFlag != null : !((Object)this$delFlag).equals(other$delFlag)) {
                return false;
            }
            Integer this$points = this.getPoints();
            Integer other$points = other.getPoints();
            if (this$points == null ? other$points != null : !((Object)this$points).equals(other$points)) {
                return false;
            }
            Integer this$dealerPoints = this.getDealerPoints();
            Integer other$dealerPoints = other.getDealerPoints();
            if (this$dealerPoints == null ? other$dealerPoints != null : !((Object)this$dealerPoints).equals(other$dealerPoints)) {
                return false;
            }
            Integer this$beforePoints = this.getBeforePoints();
            Integer other$beforePoints = other.getBeforePoints();
            if (this$beforePoints == null ? other$beforePoints != null : !((Object)this$beforePoints).equals(other$beforePoints)) {
                return false;
            }
            String this$businessId = this.getBusinessId();
            String other$businessId = other.getBusinessId();
            if (this$businessId == null ? other$businessId != null : !this$businessId.equals(other$businessId)) {
                return false;
            }
            String this$remark = this.getRemark();
            String other$remark = other.getRemark();
            if (this$remark == null ? other$remark != null : !this$remark.equals(other$remark)) {
                return false;
            }
            LocalDateTime this$operTime = this.getOperTime();
            LocalDateTime other$operTime = other.getOperTime();
            if (this$operTime == null ? other$operTime != null : !((Object)this$operTime).equals(other$operTime)) {
                return false;
            }
            LocalDateTime this$createTime = this.getCreateTime();
            LocalDateTime other$createTime = other.getCreateTime();
            if (this$createTime == null ? other$createTime != null : !((Object)this$createTime).equals(other$createTime)) {
                return false;
            }
            String this$createBy = this.getCreateBy();
            String other$createBy = other.getCreateBy();
            if (this$createBy == null ? other$createBy != null : !this$createBy.equals(other$createBy)) {
                return false;
            }
            String this$phone = this.getPhone();
            String other$phone = other.getPhone();
            if (this$phone == null ? other$phone != null : !this$phone.equals(other$phone)) {
                return false;
            }
            String this$nickname = this.getNickname();
            String other$nickname = other.getNickname();
            if (this$nickname == null ? other$nickname != null : !this$nickname.equals(other$nickname)) {
                return false;
            }
            String this$realName = this.getRealName();
            String other$realName = other.getRealName();
            if (this$realName == null ? other$realName != null : !this$realName.equals(other$realName)) {
                return false;
            }
            String this$userType = this.getUserType();
            String other$userType = other.getUserType();
            return !(this$userType == null ? other$userType != null : !this$userType.equals(other$userType));
        }

        @Generated
        protected boolean canEqual(Object other) {
            return other instanceof IntegralFlowVO;
        }

        @Generated
        public int hashCode() {
            int PRIME = 59;
            int result = 1;
            Long $id = this.getId();
            result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
            Long $userId = this.getUserId();
            result = result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
            Integer $operType = this.getOperType();
            result = result * 59 + ($operType == null ? 43 : ((Object)$operType).hashCode());
            Integer $integralNum = this.getIntegralNum();
            result = result * 59 + ($integralNum == null ? 43 : ((Object)$integralNum).hashCode());
            Long $sourceUserId = this.getSourceUserId();
            result = result * 59 + ($sourceUserId == null ? 43 : ((Object)$sourceUserId).hashCode());
            Integer $delFlag = this.getDelFlag();
            result = result * 59 + ($delFlag == null ? 43 : ((Object)$delFlag).hashCode());
            Integer $points = this.getPoints();
            result = result * 59 + ($points == null ? 43 : ((Object)$points).hashCode());
            Integer $dealerPoints = this.getDealerPoints();
            result = result * 59 + ($dealerPoints == null ? 43 : ((Object)$dealerPoints).hashCode());
            Integer $beforePoints = this.getBeforePoints();
            result = result * 59 + ($beforePoints == null ? 43 : ((Object)$beforePoints).hashCode());
            String $businessId = this.getBusinessId();
            result = result * 59 + ($businessId == null ? 43 : $businessId.hashCode());
            String $remark = this.getRemark();
            result = result * 59 + ($remark == null ? 43 : $remark.hashCode());
            LocalDateTime $operTime = this.getOperTime();
            result = result * 59 + ($operTime == null ? 43 : ((Object)$operTime).hashCode());
            LocalDateTime $createTime = this.getCreateTime();
            result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
            String $createBy = this.getCreateBy();
            result = result * 59 + ($createBy == null ? 43 : $createBy.hashCode());
            String $phone = this.getPhone();
            result = result * 59 + ($phone == null ? 43 : $phone.hashCode());
            String $nickname = this.getNickname();
            result = result * 59 + ($nickname == null ? 43 : $nickname.hashCode());
            String $realName = this.getRealName();
            result = result * 59 + ($realName == null ? 43 : $realName.hashCode());
            String $userType = this.getUserType();
            result = result * 59 + ($userType == null ? 43 : $userType.hashCode());
            return result;
        }

        @Generated
        public String toString() {
            return "TbIntegralFlowController.IntegralFlowVO(id=" + this.getId() + ", userId=" + this.getUserId() + ", operType=" + this.getOperType() + ", integralNum=" + this.getIntegralNum() + ", sourceUserId=" + this.getSourceUserId() + ", businessId=" + this.getBusinessId() + ", remark=" + this.getRemark() + ", operTime=" + String.valueOf(this.getOperTime()) + ", createTime=" + String.valueOf(this.getCreateTime()) + ", createBy=" + this.getCreateBy() + ", delFlag=" + this.getDelFlag() + ", phone=" + this.getPhone() + ", nickname=" + this.getNickname() + ", realName=" + this.getRealName() + ", userType=" + this.getUserType() + ", points=" + this.getPoints() + ", dealerPoints=" + this.getDealerPoints() + ", beforePoints=" + this.getBeforePoints() + ")";
        }
    }
}

