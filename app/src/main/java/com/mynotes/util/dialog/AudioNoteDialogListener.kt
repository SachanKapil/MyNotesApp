package com.mynotes.util.dialog

import android.app.Dialog

interface AudioNoteDialogListener {
    fun startRecording(dialog: Dialog)
    fun stopRecording()
}