package com.mynotes.ui.notes

import android.content.Context
import androidx.lifecycle.ViewModel

class RecorderViewModel : ViewModel() {

    private val recorderRepository: RecorderRepository = RecorderRepository.getInstance()

    fun startRecording() = recorderRepository.startAudRecording()

    fun stopRecording() = recorderRepository.stopAudRecording()

    fun pauseRecording() = recorderRepository.pauseAudRecording()

    fun resumeRecording() = recorderRepository.resumeAudRecording()

    fun getRecordingTime() = recorderRepository.getRecordingTime()

    fun getRecordings() = recorderRepository.getRecordings()

    fun playRecording(context: Context, name: String) =
        recorderRepository.playRecording(context, name)

}