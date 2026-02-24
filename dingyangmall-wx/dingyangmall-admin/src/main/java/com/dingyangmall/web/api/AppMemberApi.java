package com.dingyangmall.web.api;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.common.core.domain.entity.SysUser;
import com.dingyangmall.common.core.domain.model.LoginUser;
import com.dingyangmall.framework.web.service.SmsService;
import com.dingyangmall.framework.web.service.TokenService;
import com.dingyangmall.mall.constant.MallReturnCode;
import com.dingyangmall.mall.dto.AppRegisterBody;
import com.dingyangmall.mall.entity.UmsMember;
import com.dingyangmall.mall.dto.IntegralPacketDTO;
import com.dingyangmall.mall.entity.TbIntegralRule;
import com.dingyangmall.mall.service.TbIntegralFlowService;
import com.dingyangmall.mall.service.TbIntegralRuleService;
import com.dingyangmall.mall.service.UmsMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.dingyangmall.mall.utils.MemberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * C端会员接口
 */
@RestController
@RequestMapping("/app/member")
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

    /**
     * 发送短信验证码
     */
    @GetMapping("/send-sms-code")
    public AjaxResult sendSmsCode(@RequestParam String phone) {
        if (StringUtils.isBlank(phone)) {
            return AjaxResult.error("手机号不能为空");
        }
        // 校验手机号格式（简单校验）
        if (phone.length() != 11) {
            return AjaxResult.error("手机号格式不正确");
        }
        smsService.sendSmsCode(phone);
        return AjaxResult.success("验证码已发送");
    }

    /**
     * 获取当前会员信息（包含会员码）
     */
    @GetMapping("/info")
    public AjaxResult getMemberInfo() {
        String memberIdStr = MemberUtils.getMemberId();
        if (StringUtils.isEmpty(memberIdStr)) {
            return AjaxResult.error("未登录");
        }
        
        try {
            Long memberId = Long.parseLong(memberIdStr);
            UmsMember member = umsMemberService.getById(memberId);
            if (member == null) {
                return AjaxResult.error("用户不存在");
            }
            
            // 脱敏处理，不返回密码
            member.setPassword(null);
            
            return AjaxResult.success(member);
        } catch (NumberFormatException e) {
            return AjaxResult.error("无效的用户ID");
        }
    }

    /**
     * 每日签到
     */
    @PostMapping("/sign-in")
    public AjaxResult signIn() {
        String memberIdStr = MemberUtils.getMemberId();
        if (StringUtils.isEmpty(memberIdStr)) {
            return AjaxResult.error("未登录");
        }
        
        try {
            Long memberId = Long.parseLong(memberIdStr);
            boolean success = integralRuleService.distributeSignInPoints(memberId);
            if (success) {
                return AjaxResult.success("签到成功");
            } else {
                return AjaxResult.error("今日已签到或暂无签到活动");
            }
        } catch (NumberFormatException e) {
            return AjaxResult.error("无效的用户ID");
        }
    }

    /**
     * C端用户登录
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody AppRegisterBody loginBody) {
        String phone = loginBody.getPhone();
        String password = loginBody.getPassword();
        
        if (StringUtils.isAnyBlank(phone, password)) {
            return AjaxResult.error("手机号和密码不能为空");
        }
        
        UmsMember member = umsMemberService.getOne(Wrappers.<UmsMember>lambdaQuery().eq(UmsMember::getPhone, phone));
        if (member == null) {
            return AjaxResult.error(MallReturnCode.ERR_70003.getCode(), MallReturnCode.ERR_70003.getMsg());
        }
        
        if (!bCryptPasswordEncoder.matches(password, member.getPassword())) {
            return AjaxResult.error(MallReturnCode.ERR_70007.getCode(), MallReturnCode.ERR_70007.getMsg());
        }
        
        // 生成Token
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(member.getId());
        
        // 填充SysUser以避免LoginUser.getUsername()空指针异常
        SysUser sysUser = new SysUser();
        sysUser.setUserId(member.getId());
        sysUser.setUserName(member.getPhone());
        sysUser.setNickName(member.getNickname());
        sysUser.setPassword(member.getPassword());
        loginUser.setUser(sysUser);
        
        String token = tokenService.createToken(loginUser);
        
        // 脱敏处理，不返回密码
        member.setPassword(null);
        
        AjaxResult ajax = AjaxResult.success(member);
        ajax.put("token", token);
        return ajax;
    }

    /**
     * C端用户注册
     */
    @PostMapping("/register")
    public AjaxResult register(@RequestBody AppRegisterBody registerBody) {
        String phone = registerBody.getPhone();
        String code = registerBody.getCode();
        String password = registerBody.getPassword();
        
        // 校验参数
        if (StringUtils.isAnyBlank(phone, code, password)) {
            return AjaxResult.error("参数不能为空");
        }
        
        // 校验验证码
        smsService.validateSmsCode(phone, code);
        
        // 校验手机号是否已注册
        long count = umsMemberService.count(Wrappers.<UmsMember>lambdaQuery().eq(UmsMember::getPhone, phone));
        if (count > 0) {
            return AjaxResult.error("该手机号已注册");
        }
        
        // 创建用户
        UmsMember member = new UmsMember();
        member.setPhone(phone);
        member.setPassword(bCryptPasswordEncoder.encode(password));
        member.setNickname("用户" + phone.substring(phone.length() - 4));
        member.setCreateTime(LocalDateTime.now());
        member.setUpdateTime(LocalDateTime.now());
        member.setPoints(0);
        member.setBalance(BigDecimal.ZERO);
        member.setLevel(0);
        member.setDelFlag("0");
        
        boolean success = umsMemberService.save(member);
        if (success) {
            // 触发注册积分规则
            integralRuleService.distributeRegisterPoints(member.getId());
            
            // 处理邀请码逻辑
            if (StringUtils.isNotEmpty(registerBody.getInviteCode())) {
                UmsMember inviter = umsMemberService.getByMemberCode(registerBody.getInviteCode());
                if (inviter != null) {
                    // 给邀请人赠送积分
                    integralRuleService.distributeInvitePoints(member.getId(), inviter.getId());
                }
            }
            
            return AjaxResult.success("注册成功");
        } else {
            return AjaxResult.error("注册失败");
        }
    }

    /**
     * 发送积分红包
     */
    @PostMapping("/send-packet")
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult sendPacket(@RequestBody IntegralPacketDTO packetDTO) {
        // 1. 校验用户登录
        String memberIdStr = MemberUtils.getMemberId();
        if (StringUtils.isEmpty(memberIdStr)) {
            return AjaxResult.error("未登录");
        }
        Long senderId = Long.parseLong(memberIdStr);

        // 2. 校验参数
        if (StringUtils.isBlank(packetDTO.getPhone()) || packetDTO.getAmount() == null || packetDTO.getAmount() <= 0) {
            return AjaxResult.error("参数错误");
        }
        if (StringUtils.isBlank(packetDTO.getCode())) {
            return AjaxResult.error("验证码不能为空");
        }

        // 3. 校验短信验证码 (发送方手机号)
        UmsMember sender = umsMemberService.getById(senderId);
        if (sender == null) {
            return AjaxResult.error("发送方账户异常");
        }
        // 这里假设前端传的验证码是针对sender手机号的
        // 实际场景可能需要前端先调send-sms-code发给sender
        smsService.validateSmsCode(sender.getPhone(), packetDTO.getCode());

        // 4. 校验红包开关
        TbIntegralRule rule = integralRuleService.list().stream().findFirst().orElse(null);
        if (rule == null || rule.getRedPacketSwitch() == null || rule.getRedPacketSwitch() == 0) {
            return AjaxResult.error("积分红包功能未开启");
        }

        // 5. 校验接收方
        UmsMember receiver = umsMemberService.getOne(Wrappers.<UmsMember>lambdaQuery().eq(UmsMember::getPhone, packetDTO.getPhone()));
        if (receiver == null) {
            return AjaxResult.error("接收方手机号未注册");
        }
        if (receiver.getId().equals(senderId)) {
            return AjaxResult.error("不能给自己发送红包");
        }

        // 6. 校验余额并扣减
        if (sender.getPoints() < packetDTO.getAmount()) {
            return AjaxResult.error("积分余额不足");
        }
        
        // 扣减发送方
        sender.setPoints(sender.getPoints() - packetDTO.getAmount());
        umsMemberService.updateById(sender);
        integralFlowService.addPoints(senderId, -packetDTO.getAmount(), 4, "发送积分红包给用户：" + receiver.getPhone()); // 4: 赠送扣减

        // 增加接收方
        receiver.setPoints(receiver.getPoints() + packetDTO.getAmount());
        umsMemberService.updateById(receiver);
        integralFlowService.addPoints(receiver.getId(), packetDTO.getAmount(), 5, "收到用户：" + sender.getPhone() + " 的积分红包"); // 5: 收到赠送

        return AjaxResult.success("红包发送成功");
    }
}
