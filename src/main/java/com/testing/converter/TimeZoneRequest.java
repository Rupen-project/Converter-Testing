package com.testing.converter;

public class TimeZoneRequest {
    private String sourceTime;
    private String sourceTimeZone;
    private String targetTimeZone;

    // getters and setters

    public String getSourceTime() {
        return sourceTime;
    }

    public void setSourceTime(String sourceTime) {
        this.sourceTime = sourceTime;
    }

    public String getSourceTimeZone() {
        return sourceTimeZone;
    }

    public void setSourceTimeZone(String sourceTimeZone) {
        this.sourceTimeZone = sourceTimeZone;
    }

    public String getTargetTimeZone() {
        return targetTimeZone;
    }

    public void setTargetTimeZone(String targetTimeZone) {
        this.targetTimeZone = targetTimeZone;
    }
}
