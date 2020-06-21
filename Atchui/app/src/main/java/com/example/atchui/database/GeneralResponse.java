package com.example.atchui.database;

import com.google.gson.annotations.SerializedName;

public class GeneralResponse {
        @SerializedName("code")
        private int m_iCode;

        @SerializedName("message")
        private String m_strMessage;

        public int getCode() { return m_iCode; }
        public String getMessage() { return m_strMessage; }
}