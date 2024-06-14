package com.openclassrooms.notes.service

import com.openclassrooms.notes.model.Note

/**
 * Defines the contract for the API that manages the notes
 */
interface NotesApiService {

    /**
     * Add a note
     * @param note The note to add
     */

    /*FUNCTION WITH no return type {}
    The primary purpose of this type of function
    is to perform an action (side effect)
    rather than produce a value.
    NB: It takes a parameter, which means it requires an input to perform its action.
    This is called when you need to modify the state by adding new data
     */
    fun addNote(note: Note)

    /**
     * Returns all the notes
     * @return the list of notes
     */

    /*FUNCTION WITH no parameters ()
    The primary purpose of this type of function
    is to work with internal state or global context
    to fetch data, to retrieve data
     */
    fun getAllNotes(): List<Note>

}