package com.ams.downloadfiles.presentation.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.ams.downloadfiles.R
import com.ams.downloadfiles.data.source.model.File
import java.util.*
import kotlinx.android.synthetic.main.item_file.view.*

import javax.inject.Inject


class FileAdapter @Inject constructor() : RecyclerView.Adapter<FileAdapter.ViewHolder>() {

    var files: ArrayList<File>? = null

    val onPostClickListener: MutableLiveData<File> = MutableLiveData()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_file, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return files?.size ?: 0
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(files!![position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(file: File) {
            itemView.tvFileTitle.text = file.name

            itemView.setOnClickListener {
                onPostClickListener.value = files!![absoluteAdapterPosition]
            }

        }

    }

}