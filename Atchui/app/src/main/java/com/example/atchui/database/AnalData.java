package com.example.atchui.database;

import com.google.gson.annotations.SerializedName;

public class AnalData {
    @SerializedName("anal_id")
    public int m_analID;

    @SerializedName("user_id")
    public String m_userID; // 사용자 토큰 값

    @SerializedName("cnf_id")
    public String m_cnfID; // 확진자 번호(#nnnnn)

    @SerializedName("user_route_id")
    public int m_userRouteID; // 사용자 경로 고유번호

    @SerializedName("cnf_route_id")
    public int m_cnfRouteID; // 확진자 경로 고유번호

    @SerializedName("user_latitude")
    public double m_userLatitude; // 사용자 위도

    @SerializedName("user_longitude")
    public double m_userLongitude; // 사용자 경도

    @SerializedName("user_visitDatetime")
    public String m_userVisitTime; // 사용자 방문 시간

    @SerializedName("cnf_latitude")
    public double m_cnfLatitude; // 확진자 위도

    @SerializedName("cnf_longitude")
    public double m_cnfLongitude; // 확진자 경도

    @SerializedName("cnf_visittime")
    public String m_cnfVisitTime; // 확진자 방문 시간

    @SerializedName("location_name")
    public String m_locationName; // 확진자 방문장소 이름

    @SerializedName("color")
    public int m_color; // 기간에 따른 라벨 컬러

    @SerializedName("anal_time")
    public String m_analTime; // 서버에서 분석한 시간

    @SerializedName("isPast")
    public int m_IsPast; // 과거 경로 기반인지 아닌지

    @SerializedName("isRead")
    public int m_IsRead; // 읽었는지 안읽었는지

    @SerializedName("infect_case")
    public String m_cnfInfectCase; // 확진자 감염 경로

    @SerializedName("cnf_date")
    public String m_cnfDate; // 확진 날짜

    @SerializedName("province")
    public String m_cnfProvince; // 확진자 거주지

    @SerializedName("isolation_facility")
    public String m_cnfIsoFacility; // 확진자 격리 시설
}
