package com.sbz.noteify.repository

import androidx.lifecycle.LiveData
import com.sbz.noteify.database.NoteDao
import com.sbz.noteify.model.NoteModel

class NoteRepository(private val dao: NoteDao) {


    fun getAllNotes(): LiveData<List<NoteModel>> {
        return dao.getAllNotes()
    }

    suspend fun insertNote(note: NoteModel) {
        dao.insertNote(note)
    }

    suspend fun deleteNote(note: NoteModel) {
        dao.deleteNote(note)
    }

    suspend fun updateNote(note: NoteModel) {
        dao.updateNote(note)
    }

    suspend fun getSingleNote(id: Int): NoteModel {
        return dao.getSingleNote(id)
    }
}