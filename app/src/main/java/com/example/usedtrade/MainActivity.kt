package com.example.usedtrade

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.usedtrade.chatlist.ChatListFragment
import com.example.usedtrade.home.HomeFragment
import com.example.usedtrade.mypage.MyPageFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

/**
 * Fragment : Layout 에서 재사용 되는 부분을 따로 쓰는 화면으로 Activity 와는 다른 독립적인 생명주기를 가졌지만 Fragment 만 따로 사용할 수 없음
 * BottomNavigationView : 화면 하단에 3개의 Navigation 버튼이 있어서 해당 버튼으로 설정해 놓은 Fragment 를 보여주는 View
 * RecyclerView
 * ViewBinding
 * Firebase Storage
 * Firebase Realtime Database
 * Firebase Authentication
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val homeFragment = HomeFragment()
        val chatListFragment = ChatListFragment()
        val myPageFragment = MyPageFragment()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        replaceFragment(homeFragment) // 초기값으로 homeFragment 를 지정

        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId){
                R.id.home -> replaceFragment(homeFragment)
                R.id.chatList -> replaceFragment(chatListFragment)
                R.id.myPage -> replaceFragment(myPageFragment)
            }
            true
        }


    }

    /**
     * bottomNavigationMenu 선택에 따른 Fragment 화면 전환을 위한 메소드
     * Fragment 를 import 할땐 androidx.fragment.app.Fragment 를 import 해야한다
     * supportFragmentManager : 가져온 Fragment 를 관리
     * beginTransaction() : transaction 부터 commit 까지 우선적으로 처리할 내용을 알림
     */
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .apply {
                replace(R.id.fragment_container, fragment)
                commit()
            }
    }
}