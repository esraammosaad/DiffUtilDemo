package com.example.diffutildemo.presentation.feature.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.diffutildemo.R
import com.example.diffutildemo.data.dto.CharacterDto

class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<CharacterDto>() {
        override fun areItemsTheSame(
            oldItem: CharacterDto,
            newItem: CharacterDto
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: CharacterDto,
            newItem: CharacterDto
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffUtil)

    lateinit var listener: HomeListener

    fun submitList(list: List<CharacterDto>) {
        differ.submitList(list)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.character_item_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item = differ.currentList[position]
        holder.apply {
            characterImage.setImageResource(item.imageResId)
            characterName.text = item.name
            characterDescription.text = item.description
            deleteButton.setOnClickListener {
                listener.onDeleteButtonClicked(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var characterImage: ImageView = itemView.findViewById(R.id.characterImage)
        var characterName: TextView = itemView.findViewById(R.id.characterName)
        var characterDescription: TextView = itemView.findViewById(R.id.characterDescription)
        var deleteButton: Button = itemView.findViewById(R.id.deleteButton)
    }
}