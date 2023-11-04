package com.example.project6

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

/**
 * a [DialogFragment] that presents a confirmation dialog to the user
 * after the user confirms the action, a callback is invoked
 *
 * @property positiveCallBack this lambda function will be invoked when the user confirms the action
 */

class ConfirmDeleteDialogFragment(
    private val positiveCallBack: () -> Unit
) : DialogFragment() {

    /**
     * override to building a custom Dialog container
     * @param savedInstanceState if non-null, the dialog will be reconstructed from a saved state
     * @return a new dialog instance is returned to be displayed by the fragment
     */

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setMessage("Are you sure you want to delete this note?")
            .setPositiveButton("Confirm") { _, _ -> positiveCallBack.invoke() }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .create()
    }
}


