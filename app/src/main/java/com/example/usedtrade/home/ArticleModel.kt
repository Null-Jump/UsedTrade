package com.example.usedtrade.home

/**
 * 홈화면에서 게시물 내용을 저장하는 data class
 * fireBase-RealTime Database 를 사용하기 위해선 빈 생성자가 필수적으로 필요함
 */
data class ArticleModel(
    val sellerId: String,
    val title: String,
    val createdAt: Long,
    val price: String,
    val imageUrl: String
){
    constructor(): this("","",0,"", "")
}
