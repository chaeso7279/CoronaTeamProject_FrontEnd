package com.example.atchui.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private final static String BASE_URL = "ec2-3-15-217-110.us-east-2.compute.amazonaws.com";
    private static Retrofit retrofit = null;

    private RetrofitClient() {}
    public static Retrofit getClient()
    {
        // retrofit 객체가 아직 생성되지 않았을 경우 만들어서 리턴
        if(retrofit == null)
        {
            retrofit = new Retrofit.Builder().
                    baseUrl(BASE_URL). // 요청을 보낼 base url 설정
                    addConverterFactory(GsonConverterFactory.create()). // JSON 파싱을 위한 GsonConverterFactory를 추가
                    build();
        }

        // 이미 retrofit 객체가 존재하면 생성해둔 retrofit 을 반환
        return retrofit;
    }
}
