package com.sbz.noteify

import android.app.Dialog
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.sbz.noteify.database.AppDatabase
import com.sbz.noteify.databinding.ActivityAddNoteBinding
import com.sbz.noteify.model.NoteModel
import com.sbz.noteify.repository.NoteRepository
import com.sbz.noteify.util.Converter
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var repository: NoteRepository
    private var noteId: Int = 0
    private lateinit var title: String
    private lateinit var body: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_note)
        val dao = AppDatabase.getDatabase(applicationContext).noteDao()
        repository = NoteRepository(dao)

        noteId = intent.getIntExtra("note_id", 0)

        val time = getCurrentDateWithoutTime()



        binding.tvAddNoteTime.text = time.toString().trim()

        if (noteId != 0) {
            binding.btnSaveNote.text = "Update"
            updateNotes()
        }

        binding.btnSaveNote.setOnClickListener {
            title = binding.tvAddNoteTitle.text.toString()
            body = binding.tvAddNoteBody.text.toString()
            if (noteId != 0) {
                lifecycleScope.launch {
                    repository.updateNote(
                        NoteModel(
                            noteId,
                            title,
                            body,
                            Converter.dateToLong(time)
                        )
                    )
                    showDialogueBox("Updated")
                }
            } else {
                saveDataToTable(time, title, body)
                showDialogueBox("Saved")
            }

        }

    }

    private fun showDialogueBox(status: String) {
        val dialog = Dialog(this@AddNoteActivity)
        dialog.setContentView(R.layout.dialogue_box)

        val btnDismiss: AppCompatButton = dialog.findViewById(R.id.btn_dismis)
        val tvStatus: TextView = dialog.findViewById(R.id.tv_status)
        val ivStatus: ImageView = dialog.findViewById(R.id.iv_status)
        val tvTitle: TextView = dialog.findViewById(R.id.tv_dialogBoxTitle)
        val msg = "Successfully $status"
        tvStatus.text = msg
        tvTitle.text = status
        if (status == "Saved" || status == "Updated") {
            ivStatus.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_check_circle_done
                )
            )
        } else {
            ivStatus.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_error_outline
                )
            )
        }


        btnDismiss.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()

    }

    private fun updateNotes() {
        lifecycleScope.launch {
            val noteMode = repository.getSingleNote(noteId)
            val currentTime = Converter.longToDate(noteMode.date)
            binding.tvAddNoteTime.text = currentTime.toString()
            binding.tvAddNoteTitle.setText(noteMode.title)
            binding.tvAddNoteBody.setText(noteMode.body)
        }
    }

    private fun saveDataToTable(time: Date, title: String, body: String) {
        lifecycleScope.launch {
            repository.insertNote(NoteModel(0, title, body, Converter.dateToLong(time)))
        }
    }

    private fun getCurrentDateWithoutTime(): Date {
        val currentDateTime = LocalDateTime.now()
        val zoneId = ZoneId.systemDefault()
        return Date.from(currentDateTime.atZone(zoneId).toInstant())

    }
}