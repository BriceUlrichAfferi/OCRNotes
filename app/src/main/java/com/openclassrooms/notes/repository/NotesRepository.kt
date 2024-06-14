package com.openclassrooms.notes.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.openclassrooms.notes.model.Note
import com.openclassrooms.notes.service.LocalNotesApiService
import com.openclassrooms.notes.service.NotesApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository class for the notes.
 */
@Singleton
class NotesRepository @Inject constructor(private val apiService: NotesApiService)  {


    /**
     * The API service for interacting with notes.
     */

    private val _noteLiveData = MutableLiveData<List<Note>>()
    /**
     *
     */
    val noteLiveData: LiveData<List<Note>> get() = _noteLiveData
    /**
     * A flow that emits a list of all notes.
     */

    //the producer
    val notes: Flow<List<Note>> = flow {
        emit(apiService.getAllNotes())
    }

    init {
            fetchNotes()
    }

    /*private fun fetchNotes(){
        val noteFromApi = notesApiService.getAllNotes()
    }*/

    fun fetchNotes() {
        // This fetches notes in the background using Executors and posts the value to LiveData
        Executors.newSingleThreadExecutor().execute {
            val notesFromApi = apiService.getAllNotes()
            _noteLiveData.postValue(notesFromApi)
        }
    }

    fun updateNotes(notes: List<Note>) {
        _noteLiveData.postValue(notes)
    }

    fun addNoteToRepository(note: Note) {
        apiService.addNote(note)
        //fetchNotes() // Fetch updated notes to reflect the addition
        // Fetch updated notes to reflect the addition
        val updatedNotes = apiService.getAllNotes()
        _noteLiveData.postValue(updatedNotes)
    }
}