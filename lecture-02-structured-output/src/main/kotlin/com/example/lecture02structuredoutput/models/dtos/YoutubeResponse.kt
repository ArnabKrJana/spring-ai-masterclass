package com.example.lecture02structuredoutput.models.dtos


data class YoutubeResponse(
    val title: String,
    val description: String,
    val hashTags:List<String> = emptyList(),
)


data class YoutubeResponseWrapper(
    val ideas: List<YoutubeResponse>
)