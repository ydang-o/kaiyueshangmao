package com.dingyangmall.mall.dto;

import lombok.Data;

import java.util.List;

/**
 * 快递100 实时查询结果（用于接口返回）
 *
 * @author dingyangmall
 */
@Data
public class ExpressTrackResult {

    /** 快递公司编码 */
    private String com;
    /** 快递单号 */
    private String num;
    /** 状态：0在途 1揽收 2疑难 3签收 4退签 5派件 6退回 7转投 等 */
    private String state;
    /** 状态描述 */
    private String stateDesc;
    /** 是否签收 0是 1否 */
    private String ischeck;
    /** 错误信息（查询失败时有值） */
    private String message;
    /** 轨迹明细 */
    private List<TrackItem> data;

    @Data
    public static class TrackItem {
        /** 时间 */
        private String time;
        /** 描述 */
        private String context;
        /** 城市/地点 */
        private String location;
    }
}
