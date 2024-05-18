package com.example.careertree.adapter.holders

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.careertree.databinding.RecyclerRowEventListBinding
import com.example.careertree.repository.EventQueryRepository
import com.example.careertree.utility.ClickListener
import com.example.careertree.utility.eventFollowClickedUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EventListHolder(val binding: RecyclerRowEventListBinding,val eventQueryRepository: EventQueryRepository): RecyclerView.ViewHolder(binding.root),
    ClickListener {

    override fun dataClicked(view: View) {

        val event = binding.event

        event?.let {

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.link))
            this.itemView.context.startActivity(intent)
        }
    }

    override fun starClicked(view: View) {

        CoroutineScope(Dispatchers.Default).launch {

            val event = binding.event

            event?.let {

                eventQueryRepository.updateFollowedEvent(event)
                binding.eventFollowClickButton.eventFollowClickedUtil(!event.followedEvent)
            }
        }
    }

    override fun compareClicked(view: View) {
        TODO("Not yet implemented")
    }
}
