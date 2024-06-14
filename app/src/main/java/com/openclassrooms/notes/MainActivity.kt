package com.openclassrooms.notes

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.openclassrooms.notes.databinding.ActivityMainBinding
import com.openclassrooms.notes.model.Note
import com.openclassrooms.notes.repository.NotesRepository
import com.openclassrooms.notes.viewmodel.NoteViewModel
import com.openclassrooms.notes.widget.NoteItemDecoration
import com.openclassrooms.notes.widget.NoteViewHolder
import com.openclassrooms.notes.widget.NotesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * The main activity for the app.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    /**
     * The binding for the main layout.
     */
    private lateinit var binding: ActivityMainBinding

    private val notesAdapter = NotesAdapter(emptyList())


    private val noteViewModel: NoteViewModel by viewModels ()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        noteViewModel.note.observe(this, Observer { notesAdapter.updateNotes(it) })

        initRecyclerView()
        initFABButton()

    }


    /**
     * Initializes the FAB button.
     */
    private fun initFABButton() {
        binding.btnAdd.setOnClickListener {
            showAddNoteDialog()
        }
    }

    private fun showAddNoteDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_note, null)
        val titleInput = dialogView.findViewById<EditText>(R.id.titleInput)
        val descriptionInput = dialogView.findViewById<EditText>(R.id.descriptionInput)

        MaterialAlertDialogBuilder(this).apply {
            setTitle("Add Note")
            setView(dialogView)
            setPositiveButton("Add") { dialog, _ ->
                val title = titleInput.text.toString()
                val description = descriptionInput.text.toString()
                if (title.isNotBlank() && description.isNotBlank()) {
                    val newNote = Note(title, description)
                    noteViewModel.addNoteToViewModel(newNote)
                }
                dialog.dismiss()
            }
            setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
        }.show()
    }

    /**
     * Initializes the RecyclerView.
     */
    private fun initRecyclerView() {
        with(binding.recycler) {
            addItemDecoration(
                NoteItemDecoration(
                    resources.getDimensionPixelSize(R.dimen.default_margin),
                    resources.getInteger(R.integer.span_count)
                )
            )

            adapter = notesAdapter
        }

    }

}
