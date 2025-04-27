package com.synagoguemanagement.synagoguemanagement.ui.newMessage;

import java.util.Date

data class Messages(
    val date: Date = Date(),
    val text: String = "",
    val userEmail: String = ""
)
