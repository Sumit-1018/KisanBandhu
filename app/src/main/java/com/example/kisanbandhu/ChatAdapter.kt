package com.example.kisanbandhu

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load

// Sealed class to represent different types of chat items
sealed class ChatItem {
    data class Query(
        val text: String,
        val image: Bitmap? = null, // Use Bitmap for adapter
        val id: Long = System.currentTimeMillis()
    ) : ChatItem()

    data class Response(
        val text: String,
        val id: Long = System.currentTimeMillis()
    ) : ChatItem()

    data class Loading(val id: Long = System.currentTimeMillis()) : ChatItem()
}

class ChatAdapter : ListAdapter<ChatItem, RecyclerView.ViewHolder>(ChatDiffCallback()) {

    private val VIEW_TYPE_QUERY = 1
    private val VIEW_TYPE_RESPONSE = 2
    private val VIEW_TYPE_LOADING = 3

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ChatItem.Query -> VIEW_TYPE_QUERY
            is ChatItem.Response -> VIEW_TYPE_RESPONSE
            is ChatItem.Loading -> VIEW_TYPE_LOADING
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_QUERY -> {
                val view = inflater.inflate(R.layout.item_chat_query, parent, false)
                QueryViewHolder(view)
            }
            VIEW_TYPE_RESPONSE -> {
                val view = inflater.inflate(R.layout.item_chat_response, parent, false)
                ResponseViewHolder(view)
            }
            else -> { // Loading
                // Use the response layout but we'll modify it in onBind
                val view = inflater.inflate(R.layout.item_chat_response, parent, false)
                ResponseViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is ChatItem.Query -> (holder as QueryViewHolder).bind(item)
            is ChatItem.Response -> (holder as ResponseViewHolder).bind(item)
            is ChatItem.Loading -> (holder as ResponseViewHolder).bindLoading()
        }
    }

    // ViewHolder for Farmer's Query
    inner class QueryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val queryText: TextView = itemView.findViewById(R.id.tvQueryText)
        private val queryImage: ImageView = itemView.findViewById(R.id.ivQueryImage)

        fun bind(item: ChatItem.Query) {
            queryText.text = item.text
            queryText.visibility = if (item.text.isNotEmpty()) View.VISIBLE else View.GONE

            if (item.image != null) {
                queryImage.visibility = View.VISIBLE
                queryImage.load(item.image)
            } else {
                queryImage.visibility = View.GONE
            }
        }
    }

    // ViewHolder for AI's Response
    inner class ResponseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val responseText: TextView = itemView.findViewById(R.id.tvResponseText)
        // Find the avatar and name, handling potential nulls if loading
        private val avatarNameGroup: View? = itemView.findViewById(R.id.avatarNameGroup)


        fun bind(item: ChatItem.Response) {
            avatarNameGroup?.visibility = View.VISIBLE
            responseText.text = item.text
            responseText.setTextIsSelectable(true) // Makes text copyable
        }

        fun bindLoading() {
            // Hide the avatar/name header and show "Thinking..."
            avatarNameGroup?.visibility = View.GONE
            responseText.text = "Thinking..."
            responseText.setTextIsSelectable(false)
        }
    }
}

// DiffUtil for efficient RecyclerView updates
class ChatDiffCallback : DiffUtil.ItemCallback<ChatItem>() {
    override fun areItemsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
        return when {
            oldItem is ChatItem.Query && newItem is ChatItem.Query -> oldItem.id == newItem.id
            oldItem is ChatItem.Response && newItem is ChatItem.Response -> oldItem.id == newItem.id
            oldItem is ChatItem.Loading && newItem is ChatItem.Loading -> oldItem.id == newItem.id
            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
        return oldItem == newItem
    }
}