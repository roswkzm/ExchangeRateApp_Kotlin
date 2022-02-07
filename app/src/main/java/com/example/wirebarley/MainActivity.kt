package com.example.wirebarley

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.NumberPicker
import android.widget.Toast
import com.example.wirebarley.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var mBinding: ActivityMainBinding? = null   // 전역 변수로 바인딩 객체 선언
    private val binding get() = mBinding!!      // 매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재 선언
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 자동 생성된 뷰 바인딩 클래스에서의 inflate라는 메서드를 활용해서
        // 액티비티에서 사용할 바인딩 클래스의 인스턴스 생성
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        // getRoot 메서드로 레이아웃 내부의 최상위 위치 뷰의 인스턴스를 활용하여 생선된 뷰를 액티비티에 표시.
        setContentView(binding.root)

        val picker1 = binding.picker1

        setFrag(0)

        picker1.minValue = 0
        picker1.maxValue = 2
        picker1.wrapSelectorWheel = false
        picker1.displayedValues = arrayOf("한국(KRW)", "일본(JPY)", "필리핀(PHP)")

        picker1.setOnValueChangedListener { numberPicker, i, i2 ->
            when(i2){
                0 -> setFrag(0)
                1 -> setFrag(1)
                2 -> setFrag(2)
            }
        }
    }

    private fun setFrag(fragNum : Int) {

        val ft = supportFragmentManager.beginTransaction()
        when(fragNum){
            0 -> ft.replace(R.id.main_frame, Fragment1()).commit()
            1 -> ft.replace(R.id.main_frame, Fragment2()).commit()
            2 -> ft.replace(R.id.main_frame, Fragment3()).commit()
        }
    }

    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }
}