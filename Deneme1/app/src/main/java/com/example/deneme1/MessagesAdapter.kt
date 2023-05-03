package com.example.deneme1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.parse.ParseObject

class MessagesAdapter(context: Context, list: MutableList<ParseObject>) :
    RecyclerView.Adapter<MessagesHolder>() {
    var context: Context? = null
    var list: MutableList<ParseObject>? = null

    init {
        this.list = list
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesHolder {

        val v = LayoutInflater.from(context).inflate(R.layout.message_item, parent, false)
        return MessagesHolder(v)
    }

    override fun onBindViewHolder(holder: MessagesHolder, position: Int) {
        val `object` = list!![position]
        holder.message.setText(`object`["message"] as String?)

        holder.delete.setOnClickListener(View.OnClickListener { view: View? -> `object`.deleteInBackground() })
    }

    override fun getItemCount(): Int {
        return list!!.size

    }

    fun addItem(t: ParseObject?) {
        list!!.add(t!!)
        notifyDataSetChanged()
    }

    fun removeItem(`object`: ParseObject) {
        for (i in list!!.indices) {
            if (list!![i].objectId == `object`.objectId) {
                list!!.removeAt(i)
                notifyItemRemoved(i)
                notifyItemRangeChanged(i, list!!.size)
                return
            }
        }
    }

    fun updateItem(`object`: ParseObject) {
        for (i in list!!.indices) {
            if (list!![i].objectId == `object`.objectId) {
                list!![i] = `object`
                notifyDataSetChanged()
                return
            }
        }
    }
}

class MessagesHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var message: TextView = itemView.findViewById(R.id.message)

    var delete: ImageView = itemView.findViewById(R.id.delete)

}