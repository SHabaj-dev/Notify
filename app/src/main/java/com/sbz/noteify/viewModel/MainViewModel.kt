package com.sbz.noteify.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sbz.noteify.model.NoteModel
import com.sbz.noteify.repository.NoteRepository
import kotlinx.coroutines.launch

class MainViewModel(private val noteRepository: NoteRepository) : ViewModel() {

    val notes: LiveData<List<NoteModel>> = noteRepository.getAllNotes()


    fun insertNote(note: NoteModel) {

        viewModelScope.launch {
            noteRepository.insertNote(note)
        }
    }

    suspend fun deleteNote(note: NoteModel) {
        noteRepository.deleteNote(note)
    }
}