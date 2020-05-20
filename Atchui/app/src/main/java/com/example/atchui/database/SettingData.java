package com.example.atchui.database;

import com.google.gson.annotations.SerializedName;

public class SettingData {

    @SerializedName("radius_setting")
    public int m_iRadius;

    @SerializedName("period_setting")
    public int m_iPeriod;

    @SerializedName("push_agreement")
    public boolean m_bPushAgreement;

    @SerializedName("sound_setting")
    public boolean m_bOnSound;

    @SerializedName("gps_agreement")
    public boolean m_bGPSAgreement;

    public SettingData() {}
    public SettingData(boolean bPushAgreement, boolean bOnSound, boolean bGPSAgreement, int iRadius, int iPeriod)
    {
        this.m_bPushAgreement = bPushAgreement;
        this.m_bOnSound = bOnSound;
        this.m_bGPSAgreement = bGPSAgreement;

        this.m_iRadius = iRadius;
        this.m_iPeriod = iPeriod;

    }
}
