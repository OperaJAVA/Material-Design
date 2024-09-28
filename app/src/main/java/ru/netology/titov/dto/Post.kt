package ru.netology.titov.dto

data class Post(
    val id: Long,
    val author: String,
    val authorAvatar: String,
    val published: String,
    val content: String,
    val liked: Boolean,

    val likeNum: Long,
    val shareNum: Long
)