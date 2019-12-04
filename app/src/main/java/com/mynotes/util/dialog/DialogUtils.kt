package com.mynotes.util.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.mynotes.R
import kotlinx.android.synthetic.main.layout_audio_note_dialog.*

class DialogUtils {

    companion object {
        @JvmStatic
        fun openAudioNoteDialog(listener: AudioNoteDialogListener) {

            val dialog = Dialog(listener as Context)
            dialog.setContentView(R.layout.layout_audio_note_dialog)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val ivStartRecording: AppCompatImageView = dialog.iv_start_recording
            val ivStopRecording: AppCompatImageView = dialog.iv_stop_recording
            val tvCounter: AppCompatTextView = dialog.tv_counter
            val tvTitle: AppCompatTextView = dialog.tv_title
            ivStartRecording.setOnClickListener(View.OnClickListener {
                listener.startRecording(dialog)
                ivStartRecording.visibility = View.INVISIBLE
                ivStopRecording.visibility = View.VISIBLE
                tvCounter.visibility = View.VISIBLE
                tvTitle.text = (listener as Context).getString(R.string.tap_to_stop_recording)
            })
            ivStopRecording.setOnClickListener(View.OnClickListener {
                listener.stopRecording()
                ivStartRecording.visibility = View.VISIBLE
                ivStopRecording.visibility = View.INVISIBLE
                tvCounter.visibility = View.INVISIBLE
                dialog.dismiss()
            })
            dialog.show()
        }
    }
}