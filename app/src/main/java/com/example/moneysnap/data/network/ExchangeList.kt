package com.example.moneysnap.data.network

import com.google.gson.annotations.SerializedName


typealias Root = List<Exchange>;


data class Exchange(
    val result: Long, // 조회 결과 1: 성공, 2: DATA코드 오류, 3: 인증코드 오류, 4: 일일제한횟수 마감
    @SerializedName("cur_unit")
    val curUnit: String, // 통화코드
    @SerializedName("cur_nm")
    val curNm: String, // 국가/통화명
    val ttb: String, // 전신환(송금) 받으실때
    val tts: String, // 전신환(송금) 보내실때
    @SerializedName("deal_bas_r")
    val dealBasR: String, // 매매 기준율
    val bkpr: String, // 장부가격
    @SerializedName("yy_efee_r")
    val yyEfeeR: String, // 년환가료율
    @SerializedName("ten_dd_efee_r")
    val tenDdEfeeR: String, // 10일환가료율
    @SerializedName("kftc_bkpr")
    val kftcBkpr: String, // 서울외국환중개 장부가격
    @SerializedName("kftc_deal_bas_r")
    val kftcDealBasR: String, // 서울외국환중개 매매기준율

)

