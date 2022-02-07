package com.example.wirebarley

import android.icu.number.IntegerWidth
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.wirebarley.api.ApiJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


class Fragment3 : Fragment() {

    private var TAG = "MainActivity"

    private val BASE_URL = "http://api.currencylayer.com"


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.frag1, container, false)

        getCurrentData(view)

        return view
    }

    // Retrofit2 를 사용해서 API 연결
    private fun getCurrentData(view: View) {

        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiRequests::class.java)

        // 코루틴을 사용해서 값 받아오
        GlobalScope.launch(Dispatchers.IO){
            try {
                val reponse : Response<ApiJson> = api.getmoney().awaitResponse()
                if(reponse.isSuccessful){
                    val data = reponse.body()!!
                    Log.d(TAG, data.toString())

                    // xml에 값들 집어넣기
                    withContext(Dispatchers.Main){
                        // xml에 있는 id값 지정
                        var tv_exchange = view.findViewById<TextView>(R.id.tv_exchange) // 환율
                        var tv_time = view.findViewById<TextView>(R.id.tv_time) // 시간
                        var et_price = view.findViewById<TextView>(R.id.et_price)   // 송금액
                        var tv_result = view.findViewById<TextView>(R.id.tv_result) // 환율 * 송금액
                        var btn_result = view.findViewById<TextView>(R.id.btn_result)   // 계산하기 버튼

                        val dec = DecimalFormat("#,###.00") // 포멧을 하면 값이 String으로 변해서 문제가 생

                        // 환율을 소수점 2자리 까지 나타내기
                        var exchangeMoney = String.format("%.2f",data.quotes.USDPHP).toDouble()
                        var exchangeMonetForText = dec.format(exchangeMoney)

                        // timestamp -> Date
                        var timestamp : Long = data.timestamp.toLong()
                        var timeStampDate = Date(timestamp*1000)
                        var formattedDate : String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timeStampDate)

                        // TextView에 값넣기
                        tv_exchange.text = "환율 : ${exchangeMonetForText} PHP/USD"
                        tv_time.text = "조회시간 : ${formattedDate}"

                        // 버튼을 눌렀을때
                        btn_result.setOnClickListener {

                            try {
                                var et_priceValue = et_price.text.toString().toInt()    // EditText의 값을 Int형으로 받아옴
                                if (et_priceValue >10000 || et_priceValue <=0){  // 송금액이 10000초과 0이하일때
                                    Toast.makeText(activity,"송금액이 바르지 않습니다.",Toast.LENGTH_SHORT).show()
                                }else{  // 송금액이 정상적으로 입력되었을 때
                                    // BigDecimal을 사용해서 소수점의 정밀도와 표시형식이 달라지는것을 방지
                                    var bigDecimal :BigDecimal = BigDecimal(et_priceValue * exchangeMoney)
                                    var bigDecimalForText = dec.format(bigDecimal)
                                    tv_result.text = "수취금액은 ${bigDecimalForText} PHP 입니다."
                                }
                            }catch (e:Exception){       // 송금액이 빈값이거나 int형을 넘는경우에 대한 예외처리
                                Toast.makeText(activity,"숫자를 입력하세요",Toast.LENGTH_SHORT).show()
                            }
                        }



                    }
                }
            }catch (e :Exception){      // api를 사용하다보니 일반적인 API 오류에 대해 예외처리
                withContext(Dispatchers.Main){
                    Toast.makeText(activity,"정상적으로 실행되지 않았습니다.",Toast.LENGTH_SHORT).show()
                }
            }

        }
    }
}
