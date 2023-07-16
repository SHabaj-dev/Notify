package com.sbz.noteify.adapter

import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.sbz.noteify.viewModel.MainViewModel
import kotlinx.coroutines.launch

class SwipeToDeleteCallBack(
    private val adapter: NotesAdapter,
    private val viewModel: MainViewModel,
    private val rvItemList: RecyclerView
) : ItemTouchHelper.SimpleCallback(
    0,
    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        val noteToDelete = adapter.getNoteAtPosition(position)
        viewModel.viewModelScope.launch {
            viewModel.deleteNote(noteToDelete)
            Snackbar.make(rvItemList, "Note deleted", Snackbar.LENGTH_LONG)
                .setAction("Undo") {
                    viewModel.insertNote(noteToDelete)
                }
                .show()
        }

        adapter.notifyItemRemoved(position)
    }
}