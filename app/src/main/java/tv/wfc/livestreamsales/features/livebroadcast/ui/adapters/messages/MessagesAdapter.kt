package tv.wfc.livestreamsales.features.livebroadcast.ui.adapters.messages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.model.chat.ChatMessage

class MessagesAdapter(
    messagesDiffUtilItemCallback: DiffUtil.ItemCallback<ChatMessage>
): ListAdapter<ChatMessage, MessageViewHolder>(messagesDiffUtilItemCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val messageView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_chat_message, parent, false)

        return MessageViewHolder(messageView)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}