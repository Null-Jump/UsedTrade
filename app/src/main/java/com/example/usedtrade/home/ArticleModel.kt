package com.example.usedtrade.home

/**
 * 홈화면에서 게시물 내용을 저장하는 data class
 */
data class ArticleModel(
    val sellerId: String,
    val title: String,
    val createdAt: Long,
    val price: String,
    val imageUrl: String
)
