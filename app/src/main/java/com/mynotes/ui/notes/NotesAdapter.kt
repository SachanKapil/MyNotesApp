package com.mynotes.ui.notes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.mynotes.R
import kotlinx.android.synthetic.main.layout_aud_recording_item.view.*

class NotesAdapter : RecyclerView.Adapter<NotesAdapter.MyViewHolder>() {

    private lateinit var listener: NotesClickListener
    private val notesList = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        listener = parent.context as NotesClickListener
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_aud_recording_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.ivName.text = notesList[position]
        holder.ivPlay.setOnClickListener(View.OnClickListener {
            listener.onPlayAudClick(notesList[position])
        })
    }

    fun loadData(notesList: ArrayList<String>) {
        this.notesList.clear()
        this.notesList.addAll(notesList)
        notifyDataSetChanged()
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivPlay: AppCompatImageView = itemView.iv_aud_play
        val ivName: AppCompatTextView = itemView.tv_aud_recording_name
    }

    interface NotesClickListener {
        fun onPlayAudClick(name: String)
    }
}