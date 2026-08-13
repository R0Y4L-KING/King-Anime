package com.example.animewatcher

data class Channel(
    val pckg_id: String,
    val base_domain: String,
    val home_url: String,
    val mega_domains: List<String>?,
    val name: String
)
