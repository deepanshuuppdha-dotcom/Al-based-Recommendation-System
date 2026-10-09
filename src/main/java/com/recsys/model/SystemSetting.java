package com.recsys.model;

/**
 * System setting entity. Primary key is the setting key (string).
 */
public class SystemSetting {
    private String settingKey;
    private String settingValue;
    private Long updatedBy; // nullable

    public String getSettingKey() { return settingKey; }
    public void setSettingKey(String settingKey) { this.settingKey = settingKey; }
    public String getSettingValue() { return settingValue; }
    public void setSettingValue(String settingValue) { this.settingValue = settingValue; }
    public Long getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(Long updatedBy) { this.updatedBy = updatedBy; }
}
