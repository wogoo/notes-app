package com.wogoo.notes.repository

import com.wogoo.notes.database.dao.NoteDao
import com.wogoo.notes.model.Note
import com.wogoo.notes.webClient.NoteWebClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first


class NoteRepository(private val dao: NoteDao, private val webClient: NoteWebClient) {

    fun findAll() : Flow<List<Note>> {
        return dao.findAll()
    }

    private suspend fun updateAll() {
        webClient.findAll()?.let { notes ->
            val notesSync = notes.map { note ->
                note.copy(sync = true)
            }
            dao.save(notesSync)
        }
    }

    fun findById(id: String): Flow<Note> {
        return dao.findById(id)
    }

    suspend fun remove(id: String) {
        dao.disable(id)
        if(webClient.remove(id)) {
            dao.remove(id)
        }
    }

    suspend fun save(note: Note) {
        dao.save(note)
        if(webClient.save(note)) {
            val noteSync = note.copy(sync = true)
            dao.save(noteSync)
        }
    }

    suspend fun synchronize() {
        val disableNotes = dao.findDisables().first()
        disableNotes.forEach { notesDisabled ->
            remove(notesDisabled.id)
        }
        val notesNotSynchronized = dao.findNotSynchronized().first()
        notesNotSynchronized.forEach { notesNotSynchronized ->
            save(notesNotSynchronized)
        }
        updateAll()
    }

}