package com.synagoguemanagement.synagoguemanagement.data

data public class ShabbatTimesResponse(
        val title: String,
        val items: List<ShabbatItem>
)

data class ShabbatItem(
        val title: String,
        val date: String,
        val category: String
)

