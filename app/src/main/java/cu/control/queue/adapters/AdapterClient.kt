package cu.control.queue.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.turingtechnologies.materialscrollbar.ICustomAdapter
import cu.control.queue.R
import cu.control.queue.adapters.viewHolders.ViewHolderClient
import cu.control.queue.interfaces.OnClientClickListener
import cu.control.queue.repository.dataBase.entitys.Client
import cu.control.queue.utils.Conts.Companion.formatDateOnlyTime


class AdapterClient(private val onClientClickListener: OnClientClickListener) :
    RecyclerView.Adapter<ViewHolderClient>(), ICustomAdapter {

    var contentList: List<Client> = ArrayList()
    var checkMode = true
    var done: Boolean = false
    var queueId: Long = 0
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClient {
        context = parent.context
        return ViewHolderClient(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_client,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return contentList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolderClient, position: Int) {
        val client = contentList[position]

        holder.layoutBackground.background =
            ContextCompat.getDrawable(
                holder.layoutBackground.context,
                when {
                    client.isInteresting ?: false -> R.drawable.item_orange_bg
                    client.repeatedClient ?: false -> R.drawable.item_llelow_bg
                    client.searched ?: false -> R.drawable.item_accent_bg
                    client.selected ?: false && done -> {
                        R.drawable.item_green_bg
                    }
                    client.selected ?: false && !done -> {
                        R.drawable.item_red_bg
                    }
                    position % 2 != 0 -> R.drawable.item_white_bg
                    else -> R.drawable.bg_item_dark
                }
            )

        holder.clientNumber.background = ContextCompat.getDrawable(
            context,
            when {
                client.isChecked -> R.drawable.round_green_bg
                checkMode -> R.drawable.item_save_count_queue
                else -> R.drawable.round_accent_bg
            }
        )

        holder.layoutBackground.setOnLongClickListener {
            showPopup(it, client)
            true
        }

        when {
            !client.isChecked -> {
                holder.imageViewCheck.visibility = View.GONE
                holder.imageView.visibility = View.GONE
            }
            client.isChecked -> {
                holder.imageView.visibility = View.GONE
                holder.imageViewCheck.visibility = View.VISIBLE
            }
        }

        holder.clientNumber.visibility = View.VISIBLE
        holder.clientNumber.text = client.number.toString()

        holder.textViewName.text = client.name
        holder.textViewID.text = "CI: ${client.ci}"
        holder.textViewDate.text = formatDateOnlyTime.format(client.lastRegistry)
        holder.textViewReIntents.visibility = if (client.reIntent > 0) {
            holder.textViewReIntents.text = if (client.reIntent > 9) {
                "+9"
            } else {
                client.reIntent.toString()
            }
            View.VISIBLE
        } else {
            View.GONE
        }

        holder.layoutBackground.setOnClickListener {
            onClientClickListener.onClick(client)
        }
    }

    @SuppressLint("RestrictedApi")
    private fun showPopup(view: View, client: Client) {

        onClientClickListener.onLongClick(view, client)
    }

    override fun getCustomStringForElement(element: Int): String {
        return if (contentList.isEmpty())
            ""
        else
            contentList[element].number.toString()
    }

    fun swipeItem(position: Int, direction: Int) {
        onClientClickListener.onSwipe(direction, contentList[position])
    }
}