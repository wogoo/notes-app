package com.wogoo.notes.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.wogoo.notes.database.dao.NoteDao
import com.wogoo.notes.model.Note

@Database(
    version = 1,
    entities = [Note::class],
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao() : NoteDao

    companion object {
        @Volatile
        private var db: AppDatabase? = null

        fun instance(context: Context): AppDatabase {
            return db ?: Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "note.db"
            ).build()
        }
    }
}