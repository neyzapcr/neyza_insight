package com.example.neyza_insight.data.model

data class NewsResponse(
    val meta: Meta,
    val data: List<Article>
)

data class Meta(
    val found: Int,
    val returned: Int,
    val limit: Int,
    val page: Int
)

data class Article(
    val uuid: String?,
    val title: String?,
    val description: String?,
    val url: String?,
    val image_url: String?,
    val published_at: String?,
    val source: String?
)

data class NewsItem(
    val title: String,
    val link: String,
    val isoDate: String,
    val image: ImageUrl?,
    val description: String
)

data class ImageUrl(
    val small: String?,
    val large: String?
)
