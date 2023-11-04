package com.example.project6

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * an entity class that represents a note in the database
 *
 * @property id this identifies the note
 * @property title this is the title of the note and it cannot be null
 * @property description this is the content of the note and it cannont be null
 */

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String
)