package com.sbz.noteify.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.sbz.noteify.model.NoteModel

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes")
    fun getAllNotes(): LiveData<List<NoteModel>>

    @Insert
    suspend fun insertNote(note: NoteModel)

    @Update
    suspend fun updateNote(note: NoteModel)

    @Delete
    suspend fun deleteNote(note: NoteModel)

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getSingleNote(id: Int): NoteModel


}