package com.example.project6

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * this sets up the recycler view and its adapter that creates and deletes notes. also, this intiates
 * the NoteFragment for note details
 */

class MainActivity : AppCompatActivity() {
    private lateinit var database: AppDatabase
    private lateinit var notesAdapter: NotesAdapter

    /**
     * @param savedInstanceState if the activity has been previously shut down, this bundle
     * has the data it most recently gave in onSaveInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = AppDatabase.getInstance(this)

        // set up the recycler view with its layout manager and adapter
        val recyclerView: RecyclerView = findViewById(R.id.notesRecycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        notesAdapter = NotesAdapter(
            onClick = { note ->
                // handles click for editing a note, start the NoteFragment for the selected note
                val fragment = NoteFragment.newInstance(note.id.toLong())
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .addToBackStack(null)
                    .commit()
            },
            // handles deletion of a note, showing a confirmation dialog before deleting
            onDelete = { note ->
                val dialog = ConfirmDeleteDialogFragment {
                    lifecycleScope.launch(Dispatchers.IO) {
                        // deletes the note from the database
                        database.noteDao().delete(note)
                    }
                }
                dialog.show(supportFragmentManager, "ConfirmDeleteDialog")
            }
        )

        // attaches the adapter to the recycler view
        recyclerView.adapter = notesAdapter
        database.noteDao().getAllNotes().observe(this, Observer { notes ->
            notesAdapter.submitList(notes)
        })

        // sets up the button for adding a new note
        val addNoteButton: Button = findViewById(R.id.addNoteButton)
        addNoteButton.setOnClickListener {
            val fragment = NoteFragment.newInstance(null)
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit()
        }


    }
}