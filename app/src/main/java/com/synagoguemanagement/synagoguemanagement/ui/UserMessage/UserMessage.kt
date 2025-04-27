package com.synagoguemanagement.synagoguemanagement.ui.UserMessage;

data class UserMessage(
    val date: String = "",
    val content: String = "",
    val userEmail: String = ""  // This will help identify the sender
)
