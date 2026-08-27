/*
 * Decompiled with CFR.
 */
package com.dingyangmall.web.api;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.common.core.domain.entity.SysUser;
import com.dingyangmall.common.core.domain.model.LoginUser;
import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.framework.web.service.SmsService;
import com.dingyangmall.framework.web.service.TokenService;
import com.dingyangmall.mall.constant.MallReturnCode;
import com.dingyangmall.mall.dto.AppRegisterBody;
import com.dingyangmall.mall.dto.IntegralPacketDTO;
import com.dingyangmall.mall.entity.TbIntegralRule;
import com.dingyangmall.mall.entity.UmsMember;
import com.dingyangmall.mall.service.TbIntegralFlowService;
import com.dingyangmall.mall.service.TbIntegralRuleService;
import com.dingyangmall.mall.service.UmsMemberService;
import com.dingyangmall.mall.utils.MemberUtils;
import com.dingyangmall.system.service.ISysUserService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/app/member"})
public class AppMemberApi {
    private static final Logger log = LoggerFactory.getLogger(AppMemberApi.class);
    @Autowired
    private UmsMemberService umsMemberService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private SmsService smsService;
    @Autowired
    private TbIntegralRuleService integralRuleService;
    @Autowired
    private TbIntegralFlowService integralFlowService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private ISysUserService sysUserService;

    @GetMapping(value={"/send-sms-code"})
    public AjaxResult sendSmsCode(@RequestParam String phone) {
        if (StringUtils.isBlank(phone)) {
            return AjaxResult.error("\u624b\u673a\u53f7\u4e0d\u80fd\u4e3a\u7a7a");
        }
        if (phone.length() != 11) {
            return AjaxResult.error("\u624b\u673a\u53f7\u683c\u5f0f\u4e0d\u6b63\u786e");
        }
        this.smsService.sendSmsCode(phone);
        return AjaxResult.success("\u9a8c\u8bc1\u7801\u5df2\u53d1\u9001");
    }

    @GetMapping(value={"/info"})
    public AjaxResult getMemberInfo() {
        String memberIdStr = MemberUtils.getMemberId();
        if (StringUtils.isEmpty(memberIdStr)) {
            return AjaxResult.error("\u672a\u767b\u5f55");
        }
        try {
            Long memberId = Long.parseLong(memberIdStr);
            UmsMember member = (UmsMember)this.umsMemberService.getById(memberId);
            if (member == null) {
                return AjaxResult.error("\u7528\u6237\u4e0d\u5b58\u5728");
            }
            member.setPassword(null);
            member.setMemberCode(this.maskMemberCode(member.getMemberCode()));
            return AjaxResult.success(member);
        }
        catch (NumberFormatException e) {
            return AjaxResult.error("\u65e0\u6548\u7684\u7528\u6237ID");
        }
    }

    private String maskMemberCode(String memberCode) {
        if (StringUtils.isEmpty(memberCode) || memberCode.length() <= 4) {
            return "****";
        }
        return memberCode.substring(0, 2) + "****" + memberCode.substring(memberCode.length() - 2);
    }

    @PostMapping(value={"/sign-in"})
    public AjaxResult signIn() {
        String memberIdStr = MemberUtils.getMemberId();
        if (StringUtils.isEmpty(memberIdStr)) {
            return AjaxResult.error("\u672a\u767b\u5f55");
        }
        try {
            Long memberId = Long.parseLong(memberIdStr);
            boolean success = this.integralRuleService.distributeSignInPoints(memberId);
            if (success) {
                return AjaxResult.success("\u7b7e\u5230\u6210\u529f");
            }
            return AjaxResult.error("\u4eca\u65e5\u5df2\u7b7e\u5230\u6216\u6682\u65e0\u7b7e\u5230\u6d3b\u52a8");
        }
        catch (NumberFormatException e) {
            return AjaxResult.error("\u65e0\u6548\u7684\u7528\u6237ID");
        }
    }

    @PostMapping(value={"/login"})
    public AjaxResult login(@RequestBody AppRegisterBody loginBody) {
        String phone = loginBody.getPhone();
        String password = loginBody.getPassword();
        if (StringUtils.isAnyBlank(phone, password)) {
            return AjaxResult.error("\u624b\u673a\u53f7\u548c\u5bc6\u7801\u4e0d\u80fd\u4e3a\u7a7a");
        }
        UmsMember member = (UmsMember)this.umsMemberService.getOne((Wrapper)Wrappers.lambdaQuery().eq(UmsMember::getPhone, phone));
        if (member == null) {
            SysUser dealerUser = this.getDealerSysUserByPhone(phone);
            if (dealerUser == null) {
                return AjaxResult.error(MallReturnCode.ERR_70003.getCode(), (Object)MallReturnCode.ERR_70003.getMsg());
            }
            if (!this.bCryptPasswordEncoder.matches(password, dealerUser.getPassword())) {
                return AjaxResult.error(MallReturnCode.ERR_70007.getCode(), (Object)MallReturnCode.ERR_70007.getMsg());
            }
            member = this.ensureUmsMemberForDealerPhone(phone, dealerUser);
        } else if (!this.bCryptPasswordEncoder.matches(password, member.getPassword())) {
            return AjaxResult.error(MallReturnCode.ERR_70007.getCode(), (Object)MallReturnCode.ERR_70007.getMsg());
        }
        return this.buildLoginResult(member);
    }

    @PostMapping(value={"/login-by-sms"})
    public AjaxResult loginBySms(@RequestBody Map<String, String> body) {
        String code;
        String phone = body != null ? body.get("phone") : null;
        String string = code = body != null ? body.get("code") : null;
        if (StringUtils.isAnyBlank(phone, code)) {
            return AjaxResult.error("\u624b\u673a\u53f7\u548c\u9a8c\u8bc1\u7801\u4e0d\u80fd\u4e3a\u7a7a");
        }
        if (phone.length() != 11) {
            return AjaxResult.error("\u624b\u673a\u53f7\u683c\u5f0f\u4e0d\u6b63\u786e");
        }
        this.smsService.validateSmsCode(phone, code);
        UmsMember member = (UmsMember)this.umsMemberService.getOne((Wrapper)Wrappers.lambdaQuery().eq(UmsMember::getPhone, phone));
        if (member == null) {
            SysUser dealerUser = this.getDealerSysUserByPhone(phone);
            if (dealerUser == null) {
                return AjaxResult.error("\u8be5\u624b\u673a\u53f7\u672a\u6ce8\u518c\uff0c\u8bf7\u5148\u6ce8\u518c");
            }
            member = this.ensureUmsMemberForDealerPhone(phone, dealerUser);
        }
        return this.buildLoginResult(member);
    }

    @PostMapping(value={"/register-by-sms"})
    public AjaxResult registerBySms(@RequestBody Map<String, String> body) {
        String inviteCode;
        String phone = body != null ? body.get("phone") : null;
        String code = body != null ? body.get("code") : null;
        String string = inviteCode = body != null ? body.get("inviteCode") : null;
        if (StringUtils.isAnyBlank(phone, code)) {
            return AjaxResult.error("\u624b\u673a\u53f7\u548c\u9a8c\u8bc1\u7801\u4e0d\u80fd\u4e3a\u7a7a");
        }
        if (phone.length() != 11) {
            return AjaxResult.error("\u624b\u673a\u53f7\u683c\u5f0f\u4e0d\u6b63\u786e");
        }
        this.smsService.validateSmsCode(phone, code);
        UmsMember existMember = (UmsMember)this.umsMemberService.getOne((Wrapper)Wrappers.lambdaQuery().eq(UmsMember::getPhone, phone));
        if (existMember != null) {
            return AjaxResult.error("\u8be5\u624b\u673a\u53f7\u5df2\u6ce8\u518c\uff0c\u8bf7\u76f4\u63a5\u767b\u5f55");
        }
        UmsMember member = new UmsMember();
        member.setPhone(phone);
        member.setNickname("\u7528\u6237" + phone.substring(phone.length() - 4));
        member.setCreateTime(LocalDateTime.now());
        member.setUpdateTime(LocalDateTime.now());
        member.setPoints(0);
        member.setBalance(BigDecimal.ZERO);
        member.setLevel(0);
        member.setDelFlag("0");
        boolean success = this.umsMemberService.save(member);
        if (!success) {
            return AjaxResult.error("\u6ce8\u518c\u5931\u8d25");
        }
        this.integralRuleService.distributeRegisterPoints(member.getId());
        log.info("\u65b0\u7528\u6237\u6ce8\u518c\u6210\u529f phone={} memberId={}", (Object)phone, (Object)member.getId());
        if (StringUtils.isNotEmpty(inviteCode)) {
            SysUser dealer;
            UmsMember inviter = this.umsMemberService.getByMemberCode(inviteCode);
            if (inviter == null && (dealer = this.sysUserService.selectUserByInviteCode(inviteCode)) != null) {
                inviter = this.ensureUmsMemberForDealerPhone(dealer.getPhonenumber(), dealer);
            }
            if (inviter != null) {
                this.integralRuleService.distributeInvitePoints(member.getId(), inviter.getId());
            }
        }
        return AjaxResult.success("\u6ce8\u518c\u6210\u529f");
    }

    private AjaxResult buildLoginResult(UmsMember member) {
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(member.getId());
        SysUser sysUser = new SysUser();
        sysUser.setUserId(member.getId());
        sysUser.setUserName(member.getPhone());
        sysUser.setNickName(member.getNickname());
        sysUser.setPassword(member.getPassword());
        loginUser.setUser(sysUser);
        String token = this.tokenService.createLongTermToken(loginUser);
        member.setPassword(null);
        AjaxResult ajax = AjaxResult.success(member);
        ajax.put("token", (Object)token);
        ajax.put("expireDays", (Object)180);
        Integer dealerLevel = this.getDealerLevelByPhone(member.getPhone());
        if (dealerLevel != null && dealerLevel > 0) {
            ajax.put("dealerLevel", (Object)dealerLevel);
            ajax.put("isDealer", (Object)true);
        } else {
            ajax.put("isDealer", (Object)false);
        }
        return ajax;
    }

    private Integer getDealerLevelByPhone(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return null;
        }
        try {
            SysUser sysUser = this.sysUserService.selectUserByUserName(phone);
            if (sysUser == null) {
                sysUser = this.sysUserService.selectUserByPhoneNumber(phone);
            }
            if (sysUser != null && sysUser.getDealerLevel() != null && sysUser.getDealerLevel() > 0) {
                return sysUser.getDealerLevel();
            }
        }
        catch (Exception e) {
            log.debug("\u67e5\u8be2\u7ecf\u9500\u5546\u7b49\u7ea7\u5931\u8d25: {}", (Object)e.getMessage());
        }
        return null;
    }

    private SysUser getDealerSysUserByPhone(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return null;
        }
        try {
            SysUser sysUser = this.sysUserService.selectUserByUserName(phone);
            if (sysUser == null) {
                sysUser = this.sysUserService.selectUserByPhoneNumber(phone);
            }
            if (sysUser == null) {
                return null;
            }
            if (sysUser.getDealerLevel() == null || sysUser.getDealerLevel() <= 0) {
                return null;
            }
            return sysUser;
        }
        catch (Exception e) {
            log.debug("\u67e5\u8be2\u7ecf\u9500\u5546\u7cfb\u7edf\u7528\u6237\u5931\u8d25: {}", (Object)e.getMessage());
            return null;
        }
    }

    private UmsMember ensureUmsMemberForDealerPhone(String phone, SysUser dealerUser) {
        Object nick;
        UmsMember member = (UmsMember)this.umsMemberService.getOne((Wrapper)Wrappers.lambdaQuery().eq(UmsMember::getPhone, phone));
        if (member != null) {
            return member;
        }
        UmsMember newMember = new UmsMember();
        newMember.setPhone(phone);
        Object object = nick = dealerUser != null ? dealerUser.getNickName() : null;
        if (StringUtils.isEmpty((String)nick)) {
            nick = "\u7ecf\u9500\u5546" + (phone != null && phone.length() >= 4 ? phone.substring(phone.length() - 4) : "");
        }
        newMember.setNickname((String)nick);
        if (dealerUser != null && StringUtils.isNotEmpty(dealerUser.getPassword())) {
            newMember.setPassword(dealerUser.getPassword());
        }
        newMember.setCreateTime(LocalDateTime.now());
        newMember.setUpdateTime(LocalDateTime.now());
        newMember.setPoints(0);
        newMember.setBalance(BigDecimal.ZERO);
        newMember.setLevel(0);
        newMember.setDelFlag("0");
        this.umsMemberService.save(newMember);
        return newMember;
    }

    @PostMapping(value={"/register"})
    public AjaxResult register(@RequestBody AppRegisterBody registerBody) {
        String password;
        String code;
        String phone = registerBody.getPhone();
        if (StringUtils.isAnyBlank(phone, code = registerBody.getCode(), password = registerBody.getPassword())) {
            return AjaxResult.error("\u53c2\u6570\u4e0d\u80fd\u4e3a\u7a7a");
        }
        this.smsService.validateSmsCode(phone, code);
        long count = this.umsMemberService.count((Wrapper)Wrappers.lambdaQuery().eq(UmsMember::getPhone, phone));
        if (count > 0L) {
            return AjaxResult.error("\u8be5\u624b\u673a\u53f7\u5df2\u6ce8\u518c");
        }
        UmsMember member = new UmsMember();
        member.setPhone(phone);
        member.setPassword(this.bCryptPasswordEncoder.encode(password));
        member.setNickname("\u7528\u6237" + phone.substring(phone.length() - 4));
        member.setCreateTime(LocalDateTime.now());
        member.setUpdateTime(LocalDateTime.now());
        member.setPoints(0);
        member.setBalance(BigDecimal.ZERO);
        member.setLevel(0);
        member.setDelFlag("0");
        boolean success = this.umsMemberService.save(member);
        if (success) {
            UmsMember inviter;
            this.integralRuleService.distributeRegisterPoints(member.getId());
            if (StringUtils.isNotEmpty(registerBody.getInviteCode()) && (inviter = this.umsMemberService.getByMemberCode(registerBody.getInviteCode())) != null) {
                this.integralRuleService.distributeInvitePoints(member.getId(), inviter.getId());
            }
            return AjaxResult.success("\u6ce8\u518c\u6210\u529f");
        }
        return AjaxResult.error("\u6ce8\u518c\u5931\u8d25");
    }

    @PostMapping(value={"/send-packet"})
    @Transactional(rollbackFor={Exception.class})
    public AjaxResult sendPacket(@RequestBody IntegralPacketDTO packetDTO) {
        String memberIdStr = MemberUtils.getMemberId();
        if (StringUtils.isEmpty(memberIdStr)) {
            return AjaxResult.error("\u672a\u767b\u5f55");
        }
        Long senderId = Long.parseLong(memberIdStr);
        if (StringUtils.isBlank(packetDTO.getPhone()) || packetDTO.getAmount() == null || packetDTO.getAmount() <= 0) {
            return AjaxResult.error("\u53c2\u6570\u9519\u8bef");
        }
        if (StringUtils.isBlank(packetDTO.getCode())) {
            return AjaxResult.error("\u9a8c\u8bc1\u7801\u4e0d\u80fd\u4e3a\u7a7a");
        }
        UmsMember sender = (UmsMember)this.umsMemberService.getById(senderId);
        if (sender == null) {
            return AjaxResult.error("\u53d1\u9001\u65b9\u8d26\u6237\u5f02\u5e38");
        }
        this.smsService.validateSmsCode(sender.getPhone(), packetDTO.getCode());
        TbIntegralRule rule = this.integralRuleService.list().stream().findFirst().orElse(null);
        if (rule == null || rule.getRedPacketSwitch() == null || rule.getRedPacketSwitch() == 0) {
            return AjaxResult.error("\u79ef\u5206\u7ea2\u5305\u529f\u80fd\u672a\u5f00\u542f");
        }
        UmsMember receiver = (UmsMember)this.umsMemberService.getOne((Wrapper)Wrappers.lambdaQuery().eq(UmsMember::getPhone, packetDTO.getPhone()));
        if (receiver == null) {
            return AjaxResult.error("\u63a5\u6536\u65b9\u624b\u673a\u53f7\u672a\u6ce8\u518c");
        }
        if (receiver.getId().equals(senderId)) {
            return AjaxResult.error("\u4e0d\u80fd\u7ed9\u81ea\u5df1\u53d1\u9001\u7ea2\u5305");
        }
        if (sender.getPoints() < packetDTO.getAmount()) {
            return AjaxResult.error("\u79ef\u5206\u4f59\u989d\u4e0d\u8db3");
        }
        this.integralFlowService.addPoints(senderId, -packetDTO.getAmount().intValue(), 4, "\u53d1\u9001\u79ef\u5206\u7ea2\u5305\u7ed9\u7528\u6237\uff1a" + receiver.getPhone());
        this.integralFlowService.addPoints(receiver.getId(), packetDTO.getAmount(), 5, "\u6536\u5230\u7528\u6237\uff1a" + sender.getPhone() + " \u7684\u79ef\u5206\u7ea2\u5305");
        return AjaxResult.success("\u7ea2\u5305\u53d1\u9001\u6210\u529f");
    }

    @PostMapping(value={"/reset-password"})
    public AjaxResult resetPassword(@RequestBody AppRegisterBody body) {
        String phone = body.getPhone();
        String code = body.getCode();
        String password = body.getPassword();
        if (StringUtils.isAnyBlank(phone, code, password)) {
            return AjaxResult.error("\u53c2\u6570\u4e0d\u80fd\u4e3a\u7a7a");
        }
        this.smsService.validateSmsCode(phone, code);
        UmsMember member = (UmsMember)this.umsMemberService.getOne((Wrapper)Wrappers.lambdaQuery().eq(UmsMember::getPhone, phone));
        if (member == null) {
            return AjaxResult.error("\u7528\u6237\u4e0d\u5b58\u5728");
        }
        member.setPassword(this.bCryptPasswordEncoder.encode(password));
        member.setUpdateTime(LocalDateTime.now());
        this.umsMemberService.updateById(member);
        return AjaxResult.success("\u5bc6\u7801\u91cd\u7f6e\u6210\u529f");
    }

    @PutMapping(value={"/update-pwd"})
    public AjaxResult updatePassword(@RequestBody Map<String, String> body) {
        String oldPassword = body.get("oldPassword");
        String newPassword = body.get("newPassword");
        if (StringUtils.isAnyBlank(oldPassword, newPassword)) {
            return AjaxResult.error("\u53c2\u6570\u4e0d\u80fd\u4e3a\u7a7a");
        }
        String memberIdStr = MemberUtils.getMemberId();
        if (StringUtils.isEmpty(memberIdStr)) {
            return AjaxResult.error("\u672a\u767b\u5f55");
        }
        Long memberId = Long.parseLong(memberIdStr);
        UmsMember member = (UmsMember)this.umsMemberService.getById(memberId);
        if (member == null) {
            return AjaxResult.error("\u7528\u6237\u4e0d\u5b58\u5728");
        }
        if (!this.bCryptPasswordEncoder.matches(oldPassword, member.getPassword())) {
            return AjaxResult.error("\u65e7\u5bc6\u7801\u9519\u8bef");
        }
        member.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
        member.setUpdateTime(LocalDateTime.now());
        this.umsMemberService.updateById(member);
        return AjaxResult.success("\u5bc6\u7801\u4fee\u6539\u6210\u529f");
    }
}

