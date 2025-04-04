package com.synagoguemanagement.synagoguemanagement.data

data class Seat(var userId: String, var position: Int) {
    constructor() : this("", 0)
}