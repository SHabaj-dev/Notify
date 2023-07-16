package com.sbz.noteify.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sbz.noteify.AddNoteActivity
import com.sbz.noteify.R
import com.sbz.noteify.model.NoteModel
import com.sbz.noteify.util.Converter

class NotesAdapter(private val context: Context, private var notesList: List<NoteModel>) :
    RecyclerView.Adapter<NotesAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDate: TextView = itemView.findViewById(R.id.tv_noteTime)
        val tvTitle: TextView = itemView.findViewById(R.id.tv_noteTitle)
        val tvBody: TextView = itemView.findViewById(R.id.tv_noteBody)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val note = notesList[position]
                    openAddNoteActivity(note)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.list_item_note, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentNote = notesList[position]
        holder.tvDate.text = Converter.longToDate(currentNote.date).toString()
        holder.tvTitle.text = currentNote.title
        holder.tvBody.text = currentNote.body
    }

    fun updateNotesList(newNotesList: List<NoteModel>) {
        notesList = newNotesList
        notifyDataSetChanged()
    }

    fun getNoteAtPosition(position: Int): NoteModel {
        return notesList[position]
    }

    private fun openAddNoteActivity(note: NoteModel) {
        val intent = Intent(context, AddNoteActivity::class.java)
        intent.putExtra("note_id", note.id)
        /*intent.putExtra("note_title", note.title)
        intent.putExtra("note_body", note.body)*/
        context.startActivity(intent)
    }
}