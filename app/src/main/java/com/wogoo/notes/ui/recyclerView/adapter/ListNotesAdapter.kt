package com.wogoo.notes.ui.recyclerView.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wogoo.notes.databinding.ItemNoteBinding
import com.wogoo.notes.extensions.tryLoadImage
import com.wogoo.notes.model.Note

class ListNotesAdapter(
    private val context: Context,
    var whenClickItem: (nota: Note) -> Unit = {},
    notes: List<Note> = emptyList()
) : RecyclerView.Adapter<ListNotesAdapter.ViewHolder>() {

    private val notes = notes.toMutableList()

    class ViewHolder(
        private val binding: ItemNoteBinding,
        private val whenClickItem: (note: Note) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var note: Note

        init {
            itemView.setOnClickListener {
                if (::note.isInitialized) {
                    whenClickItem(note)
                }
            }
        }

        fun bind(note: Note) {
            this.note = note
            val imagemNota = binding.noteItemImage
            imagemNota.visibility =
                if (note.image.isNullOrBlank()) {
                    GONE
                } else {
                    imagemNota.tryLoadImage(note.image)
                    VISIBLE

                }
            binding.noteItemTitle.text = note.title
            binding.noteItemDesc.text = note.description
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder =
        ViewHolder(
            ItemNoteBinding
                .inflate(
                    LayoutInflater.from(context)
                ),
            whenClickItem
        )

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(notes[position])
    }

    override fun getItemCount(): Int = notes.size

    fun update(notes: List<Note>) {
        notifyItemRangeRemoved(0, this.notes.size)
        this.notes.clear()
        this.notes.addAll(notes)
        notifyItemInserted(this.notes.size)
    }

}