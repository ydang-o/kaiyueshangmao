/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.dto;

import java.util.List;
import lombok.Generated;

public class ExpressTrackResult {
    private String com;
    private String num;
    private String state;
    private String stateDesc;
    private String ischeck;
    private String message;
    private List<TrackItem> data;

    @Generated
    public ExpressTrackResult() {
    }

    @Generated
    public String getCom() {
        return this.com;
    }

    @Generated
    public String getNum() {
        return this.num;
    }

    @Generated
    public String getState() {
        return this.state;
    }

    @Generated
    public String getStateDesc() {
        return this.stateDesc;
    }

    @Generated
    public String getIscheck() {
        return this.ischeck;
    }

    @Generated
    public String getMessage() {
        return this.message;
    }

    @Generated
    public List<TrackItem> getData() {
        return this.data;
    }

    @Generated
    public void setCom(String com) {
        this.com = com;
    }

    @Generated
    public void setNum(String num) {
        this.num = num;
    }

    @Generated
    public void setState(String state) {
        this.state = state;
    }

    @Generated
    public void setStateDesc(String stateDesc) {
        this.stateDesc = stateDesc;
    }

    @Generated
    public void setIscheck(String ischeck) {
        this.ischeck = ischeck;
    }

    @Generated
    public void setMessage(String message) {
        this.message = message;
    }

    @Generated
    public void setData(List<TrackItem> data) {
        this.data = data;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ExpressTrackResult)) {
            return false;
        }
        ExpressTrackResult other = (ExpressTrackResult)o;
        if (!other.canEqual(this)) {
            return false;
        }
        String this$com = this.getCom();
        String other$com = other.getCom();
        if (this$com == null ? other$com != null : !this$com.equals(other$com)) {
            return false;
        }
        String this$num = this.getNum();
        String other$num = other.getNum();
        if (this$num == null ? other$num != null : !this$num.equals(other$num)) {
            return false;
        }
        String this$state = this.getState();
        String other$state = other.getState();
        if (this$state == null ? other$state != null : !this$state.equals(other$state)) {
            return false;
        }
        String this$stateDesc = this.getStateDesc();
        String other$stateDesc = other.getStateDesc();
        if (this$stateDesc == null ? other$stateDesc != null : !this$stateDesc.equals(other$stateDesc)) {
            return false;
        }
        String this$ischeck = this.getIscheck();
        String other$ischeck = other.getIscheck();
        if (this$ischeck == null ? other$ischeck != null : !this$ischeck.equals(other$ischeck)) {
            return false;
        }
        String this$message = this.getMessage();
        String other$message = other.getMessage();
        if (this$message == null ? other$message != null : !this$message.equals(other$message)) {
            return false;
        }
        List<TrackItem> this$data = this.getData();
        List<TrackItem> other$data = other.getData();
        return !(this$data == null ? other$data != null : !((Object)this$data).equals(other$data));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof ExpressTrackResult;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $com = this.getCom();
        result = result * 59 + ($com == null ? 43 : $com.hashCode());
        String $num = this.getNum();
        result = result * 59 + ($num == null ? 43 : $num.hashCode());
        String $state = this.getState();
        result = result * 59 + ($state == null ? 43 : $state.hashCode());
        String $stateDesc = this.getStateDesc();
        result = result * 59 + ($stateDesc == null ? 43 : $stateDesc.hashCode());
        String $ischeck = this.getIscheck();
        result = result * 59 + ($ischeck == null ? 43 : $ischeck.hashCode());
        String $message = this.getMessage();
        result = result * 59 + ($message == null ? 43 : $message.hashCode());
        List<TrackItem> $data = this.getData();
        result = result * 59 + ($data == null ? 43 : ((Object)$data).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "ExpressTrackResult(com=" + this.getCom() + ", num=" + this.getNum() + ", state=" + this.getState() + ", stateDesc=" + this.getStateDesc() + ", ischeck=" + this.getIscheck() + ", message=" + this.getMessage() + ", data=" + String.valueOf(this.getData()) + ")";
    }

    public static class TrackItem {
        private String time;
        private String context;
        private String location;

        @Generated
        public TrackItem() {
        }

        @Generated
        public String getTime() {
            return this.time;
        }

        @Generated
        public String getContext() {
            return this.context;
        }

        @Generated
        public String getLocation() {
            return this.location;
        }

        @Generated
        public void setTime(String time) {
            this.time = time;
        }

        @Generated
        public void setContext(String context) {
            this.context = context;
        }

        @Generated
        public void setLocation(String location) {
            this.location = location;
        }

        @Generated
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof TrackItem)) {
                return false;
            }
            TrackItem other = (TrackItem)o;
            if (!other.canEqual(this)) {
                return false;
            }
            String this$time = this.getTime();
            String other$time = other.getTime();
            if (this$time == null ? other$time != null : !this$time.equals(other$time)) {
                return false;
            }
            String this$context = this.getContext();
            String other$context = other.getContext();
            if (this$context == null ? other$context != null : !this$context.equals(other$context)) {
                return false;
            }
            String this$location = this.getLocation();
            String other$location = other.getLocation();
            return !(this$location == null ? other$location != null : !this$location.equals(other$location));
        }

        @Generated
        protected boolean canEqual(Object other) {
            return other instanceof TrackItem;
        }

        @Generated
        public int hashCode() {
            int PRIME = 59;
            int result = 1;
            String $time = this.getTime();
            result = result * 59 + ($time == null ? 43 : $time.hashCode());
            String $context = this.getContext();
            result = result * 59 + ($context == null ? 43 : $context.hashCode());
            String $location = this.getLocation();
            result = result * 59 + ($location == null ? 43 : $location.hashCode());
            return result;
        }

        @Generated
        public String toString() {
            return "ExpressTrackResult.TrackItem(time=" + this.getTime() + ", context=" + this.getContext() + ", location=" + this.getLocation() + ")";
        }
    }
}

