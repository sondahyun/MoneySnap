package com.example.moneysnap.data.map
import android.content.Context
import com.example.moneysnap.R
import com.example.moneysnap.data.map.IMapService
import com.example.moneysnap.data.map.Root
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MapService (val context: Context) {
    val TAG = "ExchangeService"
    val mapService: IMapService // IBoxOfficeService의 객체

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(context.resources.getString(R.string.url_map)) // url 입력
            .addConverterFactory(GsonConverterFactory.create()) // 가져온 json을 DTO로 parsing -> converter 이용해서 변환 -> gson 이용
            .build()

        // retrofit에 interface 구현 시킴 : IBoxOfficeService 객체 생성
        // IBoxOfficeService 내의 함수 사용 가능
        // Call<Root> 반환
        mapService = retrofit.create(IMapService::class.java)
    }

    // suspend는 suspend안에서만 실행 가능함
    suspend fun getPlaces(
        clientId: String,
        clientSecret: String,
        query: String,
        display: Int,
        start: Int,
        sort: String
    ): List<Place> {
        val root: Root = mapService.getPlaces(clientId, clientSecret, query, display, start, sort)
        return root.items
    }
}