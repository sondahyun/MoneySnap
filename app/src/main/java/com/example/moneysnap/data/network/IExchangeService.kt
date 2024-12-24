package com.example.moneysnap.data.network

import retrofit2.http.GET
import retrofit2.http.Query

interface IExchangeService {
    @GET("site/program/financial/exchangeJSON")
    suspend fun getExchanges(
        @Query("authkey") authkey: String, // 인증키 (OpenAPI 신청시 발급된 인증키)
        @Query("searchdate") searchdate: String, // 검색 요청 날짜 (ex) 2015-01-01, 20150101, (DEFAULT)현재일)
        @Query("data") data: String, // 검색 요쳥 API 타입 (AP01 : 환율, AP02 : 대출금리, AP03 : 국제금리)
    ): List<Exchange>

}