package com.example.project6

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.project6.databinding.FragmentNoteBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * this handles the creating and updating of notes
 * [NoteFragment.newInstance] is used to create an instance of this fragment
 */

class NoteFragment : Fragment() {
    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: AppDatabase
    private var noteId: Long? = null // noteId is now a Long?

    companion object {
        private const val ARG_NOTE_ID = "noteID"

        /**
         * @param noteId the ID of the note to be edited, using null to create a new note
         * @return a new instance of the fragment
         */

        fun newInstance(noteId: Long?): NoteFragment {
            val fragment = NoteFragment()
            val args = Bundle()
            // if noteId is null, pass -1L to show no note should be loaded for editing
            args.putLong(ARG_NOTE_ID, noteId ?: -1L) // Put -1L if noteId is null
            fragment.arguments = args
            return fragment
        }
    }

    /**
     * this is called when the fragment is starting
     * get the noteId from the arguments if one is being edited
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // get the noteId as a Long, -1L if not found
        noteId = arguments?.getLong(ARG_NOTE_ID, -1L).takeIf { it != -1L }
    }

    /**
     * this is called to have the fragment instantiate its UI voew
     */

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * this is called after [onCreateView] has returned but before any saved state has been restored
     * subclasses can initialize once the view hierarchy is created
     */

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = AppDatabase.getInstance(requireContext())

        // if noteId is not null, get the note and populate the fields
        noteId?.let { id ->
            lifecycleScope.launch {
                val existingNote = database.noteDao().getNoteById(id)
                existingNote?.let { note ->
                    // populate the ui with the note's data
                    binding.title.setText(note.title)
                    binding.description.setText(note.description)
                }
            }
        }

        binding.saveButton.setOnClickListener {
            val title = binding.title.text.toString()
            val description = binding.description.text.toString()

            if (title.isBlank() || description.isBlank()) {
                Toast.makeText(
                    requireContext(), "Title and description should not be empty",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    if (noteId != null) {
                        // Note must take a Long for its id
                        val note = Note(noteId!!, title, description)
                        database.noteDao().update(note)
                    } else {
                        // Assuming Note has an auto-generate ID if not provided
                        val note = Note(title = title, description =  description)
                        database.noteDao().insert(note)
                    }
                }
                // Return to the previous fragment
                activity?.supportFragmentManager?.popBackStack()
            }
        }
    }

    /**
     * this is called when the view created by [onCreateView] is about to be destroyed
     * so that it can clean up the binding object
     */

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
