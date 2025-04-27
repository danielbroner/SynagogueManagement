package com.synagoguemanagement.synagoguemanagement.data

data class ShabbatTimesResponse(
        val title: String,
        val items: List<ShabbatItem>
)

data class ShabbatItem(
        val title: String,
        val date: String,
        val category: String
)


