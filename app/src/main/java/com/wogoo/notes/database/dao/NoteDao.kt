package com.wogoo.notes.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.wogoo.notes.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert(onConflict = REPLACE)
    suspend fun save(note: Note)

    @Insert(onConflict = REPLACE)
    suspend fun save(note: List<Note>)

    @Query("SELECT * FROM Note WHERE status = 0")
    fun findAll() : Flow<List<Note>>

    @Query("SELECT * FROM Note WHERE id = :id AND status = 0")
    fun findById(id: String): Flow<Note>

    @Query("DELETE FROM Note WHERE id = :id")
    suspend fun remove(id: String)

    @Query("SELECT * FROM Note WHERE sync = 0")
    fun findNotSynchronized() : Flow<List<Note>>

    @Query("UPDATE Note SET status = 1 WHERE id = :id")
    suspend fun disable(id: String)

    @Query("SELECT * FROM Note WHERE status = 1")
    fun findDisables() : Flow<List<Note>>

}