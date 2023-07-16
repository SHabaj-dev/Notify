package com.sbz.noteify

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sbz.noteify.adapter.NotesAdapter
import com.sbz.noteify.adapter.SwipeToDeleteCallBack
import com.sbz.noteify.database.AppDatabase
import com.sbz.noteify.repository.NoteRepository
import com.sbz.noteify.viewModel.MainViewModel
import com.sbz.noteify.viewModel.MainViewModelFactory
import java.util.Calendar
import java.util.Date

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var rvNotesList: RecyclerView
    private lateinit var fabAddNote: FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rvNotesList = findViewById(R.id.rv_notesList)
        fabAddNote = findViewById(R.id.fab_addNote)


        val noteDao = AppDatabase.getDatabase(applicationContext).noteDao()
        val noteRepository = NoteRepository(noteDao)
        mainViewModel =
            ViewModelProvider(this@MainActivity, MainViewModelFactory(noteRepository)).get(
                MainViewModel::class.java
            )

        setRecyclerView()

        fabAddNote.setOnClickListener {
            launchAddNoteActivity()
        }


    }

    private fun launchAddNoteActivity() {
        startActivity(Intent(this@MainActivity, AddNoteActivity::class.java))
    }

    private fun setRecyclerView() {

        rvNotesList.layoutManager =
            StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)

        val adapter = NotesAdapter(this@MainActivity, emptyList())
        rvNotesList.adapter = adapter

        val itemTouchHelper =
            ItemTouchHelper(SwipeToDeleteCallBack(adapter, mainViewModel, rvNotesList))
        itemTouchHelper.attachToRecyclerView(rvNotesList)

        mainViewModel.notes.observe(this@MainActivity) { notes ->
            adapter.updateNotesList(notes)
        }
    }



}