package com.wogoo.notes.webClient.dto

import com.wogoo.notes.model.Note
import java.util.*

class NoteResponse(
    val id: String?,
    val title: String?,
    val description: String?,
    val image: String?
) {
    val note: Note
        get() = Note(
            id = id ?: UUID.randomUUID().toString(),
            title = title ?: "",
            description = description ?: "",
            image = image ?: ""
        )
}
