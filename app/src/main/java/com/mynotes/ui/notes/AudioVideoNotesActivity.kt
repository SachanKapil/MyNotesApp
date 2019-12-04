package com.mynotes.ui.notes

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mynotes.R
import com.mynotes.util.dialog.AudioNoteDialogListener
import com.mynotes.util.dialog.DialogUtils
import kotlinx.android.synthetic.main.layout_audio_note_dialog.*

private const val REQUEST_PERMISSIONS_CODE = 200

class AudioVideoNotesActivity : AppCompatActivity(), View.OnClickListener, AudioNoteDialogListener,
    NotesAdapter.NotesClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: RecorderViewModel
    private lateinit var dialog: Dialog

    private val permissions = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)
        val ivVideoNote: AppCompatImageView = findViewById(R.id.iv_video_note)
        val ivAudioNote: AppCompatImageView = findViewById(R.id.iv_audio_note)
        ivVideoNote.setOnClickListener(this)
        ivAudioNote.setOnClickListener(this)
        checkNeededPermissions()
        viewModel = ViewModelProviders.of(this).get(RecorderViewModel::class.java)
        recyclerView = findViewById(R.id.rv_notes)
        setUpRecyclerView()
        addObserver()
    }

    private fun checkNeededPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS_CODE)
        }
    }

    private fun setUpRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = NotesAdapter()
        (recyclerView.adapter as NotesAdapter).loadData(viewModel.getRecordings())
    }

    override fun onPlayAudClick(name: String) {
        viewModel.playRecording(this, name)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_video_note -> getString(R.string.under_development)
            R.id.iv_audio_note -> DialogUtils.openAudioNoteDialog(this)
        }
    }

    private fun addObserver() {
        viewModel.getRecordingTime().observe(this, Observer {
            dialog.tv_counter.text = it
        })
    }

    override fun startRecording(dialog: Dialog) {
        this.dialog = dialog
        viewModel.startRecording()
    }

    override fun stopRecording() {
        viewModel.stopRecording()
        (recyclerView.adapter as NotesAdapter).loadData(viewModel.getRecordings())
    }
}

