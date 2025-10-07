package com.example.eventplanner.ui


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.eventplanner.data.Event
import com.example.eventplanner.databinding.ItemEventBinding
import java.text.SimpleDateFormat
import java.util.*

class EventAdapter(
    private val onEditClick: (Event) -> Unit,
    private val onDeleteClick: (Event) -> Unit
) : ListAdapter<Event, EventAdapter.EventViewHolder>(DiffCallback()) {

    inner class EventViewHolder(private val binding: ItemEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(event: Event) {
            binding.tvTitle.text = event.title
            binding.tvDescription.text = event.description

            // Handle date stored as string, e.g. "2025-10-07"
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

            val formattedDate = try {
                val date = inputFormat.parse(event.date)
                outputFormat.format(date!!)
            } catch (e: Exception) {
                event.date // fallback if parsing fails
            }
            binding.tvDate.text = formattedDate

            // Handle time
            binding.tvTime.text = event.time.toString()

            // Buttons
            binding.btnEdit.setOnClickListener { onEditClick(event) }
            binding.btnDelete.setOnClickListener { onDeleteClick(event) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean =
            oldItem == newItem
    }
}
