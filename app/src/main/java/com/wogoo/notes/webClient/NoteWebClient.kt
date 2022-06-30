package com.wogoo.notes.webClient

import android.util.Log
import com.wogoo.notes.model.Note
import com.wogoo.notes.webClient.dto.NoteRequest
import com.wogoo.notes.webClient.service.NoteService

private const val TAG = "NoteWebClient"

class NoteWebClient {
    private val noteService: NoteService = RetrofitInit().notaService

    suspend fun findAll() : List<Note>?{
        return try {
            val notesResponse = noteService
                .findAll()

            notesResponse.map { notaResposta ->
                notaResposta.note
            }
        } catch (e: Exception) {
            Log.e(TAG, "findAll: ",e )
            null
        }
    }

    suspend fun save(note: Note) : Boolean {
        try {
            val response = noteService.save(
                note.id, NoteRequest(
                    title = note.title,
                    description = note.description,
                    image = note.image
                )
            )
            return response.isSuccessful
        } catch (e: Exception) {
            Log.e(TAG, "salva: error in save", e)
        }
        return false
    }

    suspend fun remove(id: String) : Boolean {
        try {
            noteService.remove(id)
            return true
        } catch (e: Exception) {
            Log.e(TAG, "remove: falha ao excluir nota",e )
        }
        return false
    }
}