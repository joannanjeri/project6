package com.example.project6

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/**
 * dao for the notes table
 */

@Dao
interface NoteDao {
    /**
     * get all the notes from the database as a LiveData list
     *
     * @return a LiveData list of all notes in the database
     */
    @Query("SELECT * FROM notes")
    fun getAllNotes(): LiveData<List<Note>>

    /**
     * get a single note from the database by its id
     *
     * @param noteId the id of the note
     * @return the note with the specified id or null if it does not exist
     */

    @Query("SELECT * FROM notes WHERE id = :noteId LIMIT 1")
    fun getNoteById(noteId: Long): Note?

    /**
     * deletes a note from the database by its id
     *
     * @param noteId the specific id of the note that's deleted
     */

    @Query("DELETE FROM notes WHERE id = :noteId")
    suspend fun deleteById(noteId: Int)

    /**
     * this inserts a new note into the database and if the note already exists, it replaces the old
     * note with the new note
     *
     * @param note the note that is going to be inserted
     */

    @Insert
    fun insert(note: Note)

    /**
     * this updates an existing note in the database
     *
     * @param note the note with updated info to save to the database
     */

    @Update
    fun update(note: Note)

    /**
     * this deletes a note from the database
     *
     * @param note the note to be deleted from the database
     */

    @Delete
    fun delete(note: Note)
}