package com.example.moneysnap.data.map

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface IMapService {

    @GET("v1/search/local.json")
    suspend fun getPlaces(
        @Header("X-Naver-Client-Id") clientId: String, // Naver API Client ID
        @Header("X-Naver-Client-Secret") clientSecret: String, // Naver API Client Secret
        @Query("query") query: String, // Search keyword (e.g., "ATM" or "Bank")
        @Query("display") display: Int = 10, // 한 번에 표시할 검색 결과 개수(기본값: 1, 최댓값: 5)
        @Query("start") start: Int = 1, // 검색 시작 위치(기본값: 1, 최댓값: 1)
        // 검색 결과 정렬 방법: random: 정확도순으로 내림차순 정렬(기본값), comment: 업체 및 기관에 대한 카페, 블로그의 리뷰 개수순으로 내림차순 정렬
        @Query("sort") sort: String = "random"
    ): Root
}