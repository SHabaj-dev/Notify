package com.sbz.noteify.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("notes")
data class NoteModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val body: String,
    val date: Long
)
