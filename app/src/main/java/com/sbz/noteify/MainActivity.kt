package com.sbz.noteify

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sbz.noteify.adapter.NotesAdapter
import com.sbz.noteify.adapter.SwipeToDeleteCallBack
import com.sbz.noteify.database.AppDatabase
import com.sbz.noteify.model.NoteModel
import com.sbz.noteify.repository.NoteRepository
import com.sbz.noteify.viewModel.MainViewModel
import com.sbz.noteify.viewModel.MainViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var rvNotesList: RecyclerView
    private lateinit var fabAddNote: FloatingActionButton
    private var originalNoteList: List<NoteModel> = emptyList()
    private lateinit var searchView: AutoCompleteTextView
    private lateinit var adapter: NotesAdapter
    private lateinit var autoCompleteAdapter: ArrayAdapter<String>
    private val autoCompleteList = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rvNotesList = findViewById(R.id.rv_notesList)
        fabAddNote = findViewById(R.id.fab_addNote)
        searchView = findViewById(R.id.sv_searchNotes)


        val noteDao = AppDatabase.getDatabase(applicationContext).noteDao()
        val noteRepository = NoteRepository(noteDao)
        adapter = NotesAdapter(this@MainActivity, emptyList())
        mainViewModel =
            ViewModelProvider(this@MainActivity, MainViewModelFactory(noteRepository)).get(
                MainViewModel::class.java
            )
        autoCompleteFunction()


        setRecyclerView()

        fabAddNote.setOnClickListener {
            launchAddNoteActivity()
        }

        searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterNotes(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })


    }

    private fun autoCompleteFunction() {
        autoCompleteAdapter =
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, autoCompleteList)
        searchView.setAdapter(autoCompleteAdapter)
        searchView.threshold = 2
        /*searchView.setDropDownBackgroundResource(R.drawable.dropdown_background)
        searchView.setTextColor(ContextCompat.getColor(this, R.color.black))*/
    }

    private fun launchAddNoteActivity() {
        startActivity(Intent(this@MainActivity, AddNoteActivity::class.java))
    }

    private fun setRecyclerView() {

        rvNotesList.layoutManager =
            StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)


        rvNotesList.adapter = adapter

        val itemTouchHelper =
            ItemTouchHelper(SwipeToDeleteCallBack(adapter, mainViewModel, rvNotesList))
        itemTouchHelper.attachToRecyclerView(rvNotesList)

        mainViewModel.notes.observe(this@MainActivity) { notes ->
            originalNoteList = notes
            adapter.updateNotesList(notes)
        }
    }

    private fun filterNotes(query: String?) {
        query?.let { searchText ->
            val filteredNotes = originalNoteList.filter { note ->
                note.title.contains(searchText, ignoreCase = true) ||
                        note.body.contains(searchText, ignoreCase = true)
            }
            adapter.updateNotesList(filteredNotes)

            autoCompleteList.clear()
            filteredNotes.forEach { note ->
                autoCompleteList.add(note.title)
            }
            autoCompleteAdapter.notifyDataSetChanged()
        } ?: run {
            adapter.updateNotesList(originalNoteList)
            autoCompleteList.clear()
            autoCompleteAdapter.notifyDataSetChanged()
        }
    }


}