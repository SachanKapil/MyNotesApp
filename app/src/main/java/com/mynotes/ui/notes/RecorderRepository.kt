package com.mynotes.ui.notes
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class RecorderRepository private constructor(){


    companion object {
        @Volatile
        private var instance: RecorderRepository? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: RecorderRepository().also { instance = it }
            }
    }

    private var output: String? = null
    private var mediaRecorder: MediaRecorder? = null
    private val recorderDirectory: File =
        File(Environment.getExternalStorageDirectory().absolutePath + "/AudVidNotes/")
    private var recordingTime: Long = 0
    private var timer = Timer()
    private val recordingTimeString = MutableLiveData<String>()

    init {
        try {
            // have the object build the directory structure, if needed.
            recorderDirectory.mkdirs()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @SuppressLint("RestrictedApi")
    fun startAudRecording() {
        initAudRecorder()
        try {
            mediaRecorder?.prepare()
            mediaRecorder?.start()
            startTimer()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    @SuppressLint("RestrictedApi")
    fun stopAudRecording() {
        mediaRecorder?.stop()
        mediaRecorder?.release()
        stopTimer()
        resetTimer()
    }


    @TargetApi(Build.VERSION_CODES.N)
    @SuppressLint("RestrictedApi")
    fun pauseAudRecording() {
        stopTimer()
        mediaRecorder?.pause()
    }

    @TargetApi(Build.VERSION_CODES.N)
    @SuppressLint("RestrictedApi")
    fun resumeAudRecording() {
        timer = Timer()
        startTimer()
        mediaRecorder?.resume()
    }

    fun playRecording(context: Context, title: String) {
        val path =
            Uri.parse(Environment.getExternalStorageDirectory().absolutePath + "/AudVidNotes/$title")


        val manager: AudioManager =
            context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (manager.isMusicActive) {
            Toast.makeText(
                context,
                "Another recording is just playing! Wait until it's finished!",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            MediaPlayer().apply {
                setAudioStreamType(AudioManager.STREAM_MUSIC)
                setDataSource(context, path)
                prepare()
                start()
            }
        }
    }

    fun getRecordingTime() = recordingTimeString

    private fun initAudRecorder() {
        mediaRecorder = MediaRecorder()

        if (recorderDirectory.exists()) {
            val count = recorderDirectory.listFiles().size
            output =
                Environment.getExternalStorageDirectory().absolutePath + "/AudVidNotes/recording" + count + ".mp3"
        }

        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mediaRecorder?.setOutputFile(output)
    }

    private fun startTimer() {
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                recordingTime += 1
                updateTimer()
            }
        }, 1000, 1000)
    }

    private fun stopTimer() {
        timer.cancel()
    }


    private fun resetTimer() {
        timer.cancel()
        recordingTime = 0
        recordingTimeString.postValue("00:00")
    }

    private fun updateTimer() {
        val minutes = recordingTime / (60)
        val seconds = recordingTime % 60
        val str = String.format("%d:%02d", minutes, seconds)
        recordingTimeString.postValue(str)
    }

    fun getRecordings(): ArrayList<String> {
        val files: Array<out File>? = recorderDirectory.listFiles()
        val file: ArrayList<String> = ArrayList()
        for (i in files!!) {
            file.add(i.name)
        }
        return file
    }
}