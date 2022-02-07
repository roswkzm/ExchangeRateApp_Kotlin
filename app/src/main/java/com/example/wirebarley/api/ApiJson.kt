package com.example.wirebarley.api

data class ApiJson(
    val privacy: String,    // 미사용
    val quotes: Quotes,     // 환전 대상
    val source: String,     // 환전 비교
    val success: Boolean,   // 성공여부
    val terms: String,      // 미사용
    val timestamp: Int      // 타임스태프
)