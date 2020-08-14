package com.manimarank.websitemonitor.data.model

data class WebSiteStatus (
    val name: String,
    val url: String,
    val status: Int,
    val isSuccessful: Boolean,
    val message: String
)