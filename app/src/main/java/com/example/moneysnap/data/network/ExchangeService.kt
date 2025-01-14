package com.example.moneysnap.data.network

import android.content.Context
import android.util.Log
import com.example.moneysnap.R
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyStore
import java.security.cert.CertificateFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

class ExchangeService (val context: Context) {
    val TAG = "ExchangeService"

    // Retrofit 서비스 객체
    val exchangeService: IExchangeService // IBoxOfficeService의 객체

    init {
        val client = getSecureOkHttpClient(context)
        val retrofit : Retrofit = Retrofit.Builder()
            .baseUrl(context.resources.getString(R.string.url)) // url 입력
            .addConverterFactory(GsonConverterFactory.create()) // 가져온 json을 DTO로 parsing -> converter 이용해서 변환 -> gson 이용
            .client(client) // Secure Client
            .build()

        // retrofit에 interface 구현 시킴 : IBoxOfficeService 객체 생성
        // IBoxOfficeService 내의 함수 사용 가능
        // Call<Root> 반환
        exchangeService = retrofit.create(IExchangeService::class.java)
    }

    // HTTPS SSL 통신을 위한 OkHttpClient 설정
    private fun getSecureOkHttpClient(context: Context): OkHttpClient {
        try {
            // 인증서 로드 (raw 디렉토리의 인증서 파일 로드)
            val cf = CertificateFactory.getInstance("X.509")
            val caInput = context.resources.openRawResource(R.raw.thawte_tls_rsa_ca_g1) // 인증서 파일
            val ca = cf.generateCertificate(caInput)
            caInput.close()

            // 신뢰 저장소 생성
            val keyStore = KeyStore.getInstance(KeyStore.getDefaultType()).apply {
                load(null, null)
                setCertificateEntry("ca", ca)
            }

            // TrustManagerFactory 초기화
            val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()).apply {
                init(keyStore)
            }

            // SSLContext 생성
            val sslContext = SSLContext.getInstance("TLS").apply {
                init(null, tmf.trustManagers, null)
            }

            val trustManager = tmf.trustManagers.first() as X509TrustManager

            // OkHttpClient 구성
            return OkHttpClient.Builder()
                .sslSocketFactory(sslContext.socketFactory, trustManager)
                .build()
        } catch (e: Exception) {
            throw RuntimeException("Failed to create a secure client", e)
        }
    }


    // suspend는 suspend안에서만 실행 가능함
    suspend fun getExchanges(authkey: String, searchdate: String, data: String)  : List<Exchange> {
//        // 응답이 날아왔을 때 호출 (결과 받음), Call 타입으로 반환 날아오면 CallBack 호출
//        val movieCallback = object : Callback<Root> { // 객체
//            // 서버로부터 응답 제대로 날아옴 (성공)
//            override fun onResponse(call: Call<Root>, response: Response<Root>) { // Call을 요청할때 사용, 결과: Response<Root>
//                if (response.isSuccessful) {
//                    val boxOfficeRoot = response.body() // body(): Root 값
//                    val movies = boxOfficeRoot?.movieResult?.movieList // List<Movie> 튀어나옴
//                    movies?.forEach { movie ->
//                        Log.d(TAG, movie.toString())
//                    }
//                }
//            }
//            // 서버로부터 응답 제대로 날아오지 않음
//            override fun onFailure(call: Call<Root>, t: Throwable) {
//                Log.d(TAG, t.stackTraceToString())
//            }
//        }
//        // 호출할 수 있는 service 만듦
//        val movieCall : Call<Root> = movieService.getDailyBoxOffice("json", key, date) /* IBoxOfficeService 의 함수 호출 */
//
//        // 비동기: enqueue (언젠가 Call<Root> 날아오면 movieCallback 실행)
//        movieCall.enqueue(movieCallback)
        //val response = movieCall.execute() // 동기


        // coroutine (Retrofit에서 Coroutine 자체 지원)
        // suspend 함수 호출
        Log.d("MoneyFragment", "ExchangeService 입장 성공")

        val root : List<Exchange> = exchangeService.getExchanges(authkey, searchdate, data)
        return root

        // return null // response.body()?.boxOfficeResult?.boxOfficeList
    }

}