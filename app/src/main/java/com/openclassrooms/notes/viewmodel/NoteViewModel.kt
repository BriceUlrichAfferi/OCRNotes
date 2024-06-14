package com.openclassrooms.notes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.openclassrooms.notes.model.Note
import com.openclassrooms.notes.repository.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the notes.
 */

@HiltViewModel
class NoteViewModel @Inject constructor(private val notesRepository: NotesRepository) : ViewModel() {


        /*
        The collection logic updates the LiveData,
        and the getter allows the UI to react to these updates.
         */

        val note: LiveData<List<Note>> get() = notesRepository.noteLiveData

        init {
                collectNotes()
        }

        /**
         * Collects notes from the repository and updates the LiveData.
         */

        //the consumer of the flow produced
        private fun collectNotes() {
                viewModelScope.launch {
                        notesRepository.notes.collect {
                                notesRepository.updateNotes(it)
                        }
                }
        }

        fun addNoteToViewModel(note: Note) {
                viewModelScope.launch {
                        notesRepository.addNoteToRepository(note)
                }

                /*/**
         * Factory for creating a NoteViewModel with a NotesRepository.
         */
        class Factory(private val notesRepository: NotesRepository) : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
                                @Suppress("UNCHECKED_CAST")
                                return NoteViewModel(notesRepository) as T
                        }
                        throw IllegalArgumentException("Unknown ViewModel class")
                }
        }*/
        }
}