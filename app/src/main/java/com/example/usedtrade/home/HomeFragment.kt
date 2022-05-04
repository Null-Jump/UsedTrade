package com.example.usedtrade.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.usedtrade.DBKey.Companion.DB_ARTICLES
import com.example.usedtrade.R
import com.example.usedtrade.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var articleDB: DatabaseReference
    private lateinit var articleAdapter: ArticleAdapter

    private val articleList = mutableListOf<ArticleModel>()
    private val listener = object : ChildEventListener {
        //snapshot 하나하나가 articleModel 이기 때문에 articleModel 에 맞는 데이터 형식으로 치환해서 articleList 에 넣어줘야 함
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val articleModel = snapshot.getValue(ArticleModel::class.java)
            articleModel ?: return //articleModel 이 null 경우 return 해서 리스너를 종료

            articleList.add(articleModel)
            articleAdapter.submitList(articleList)
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

        override fun onChildRemoved(snapshot: DataSnapshot) {}

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

        override fun onCancelled(error: DatabaseError) {}
    }

    private var binding: FragmentHomeBinding? = null
    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentHomeBinding = FragmentHomeBinding.bind(view)
        binding = fragmentHomeBinding

        articleList.clear() // articleDB.addChildEventListener(listener)가 실행되기전에 List 를 먼저 비워줌
        articleDB = Firebase.database.reference.child(DB_ARTICLES)
        articleAdapter = ArticleAdapter()

        fragmentHomeBinding.articleRecyclerView.layoutManager = LinearLayoutManager(context)
        fragmentHomeBinding.articleRecyclerView.adapter = articleAdapter

        fragmentHomeBinding.addFloatingButton.setOnClickListener {
            if(auth.currentUser != null){
                val intent = Intent(requireContext(), AddArticleActivity::class.java)
                startActivity(intent)
           }else{
                Snackbar.make(view, "Please use it after logging in", Snackbar.LENGTH_LONG).show()
            }

            /* 위와 동일한 내용으로 어느 것을 사용해도 무관
            context?.let {
                val intent = Intent(it, ArticleAddActivity::class.java)
                startActivity()
            }*/
        }
        /**
         * addListenerForSingleValueEvent -> 즉시성이고 1회만 호출이 됨
         * addChildEventListener -> 한번 등록을 하면 이벤트가 발생할때마다 등록이 됨
         *  -> Activity 와 달리 Fragment 는 재사용이 되므로 onViewCreated()가 실행 될 때마다 중복으로 실행될 가능성이 있음
         *  => 따라서, 이벤트리스너를 전역으로 선언해서 onViewCreated()마다 attach 를 붙이고 onDestroyView()마다 remove 를 해야함
         *      (이벤트리스너가 현재 onViewCreated()마다 있어서 실행 될때마다 아이템을 articleList 에 추가되므로 리스너 시작전 List 를 비워줘야함)
         *
         *  1. HomeFragment 가 시작될 때 onViewCreated()에서 addChildEventListener()를 통해 ArticleDB 내용을 보여줌
         *  2. 다른 프래그먼트로 넘어가거나 어플을 종료했을 때 onDestroyView()에서 removeEventListener()를 통해 ArticleDB 의 내용을 제거함
         *  3. 잠깐 종료했던 HomeFragment 를 다시 실행했을 때 onResume()에서 notifyDataSetChanged()를 통해 변경사항을 확인하고 반영
         */
        articleDB.addChildEventListener(listener)

    }

    override fun onResume() {
        super.onResume()

        articleAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        articleDB.removeEventListener(listener)
    }
}