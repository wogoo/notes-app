package com.wogoo.notes.webClient.dto

data class NoteRequest (
    val title: String,
    val description: String,
    val image: String? = null
        )