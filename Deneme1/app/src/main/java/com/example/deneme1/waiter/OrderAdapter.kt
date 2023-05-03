package com.example.deneme1.waiter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.deneme1.data.Notifications
import com.example.deneme1.databinding.OrderCardViewBinding
import com.parse.ParseObject
import com.parse.ParseUser
import timber.log.Timber


class OrderAdapter(private val clickListener:OrderClickListener): RecyclerView.Adapter<OrderAdapter.ViewHolder>(),
    Filterable {

    private var itemList: MutableList<Notifications> = mutableListOf()
    private var itemListAll: MutableList<Notifications> = mutableListOf()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

        val binding = OrderCardViewBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(itemList[position])

    override fun getItemCount() = itemList.size


    fun addItem(t: ParseObject?, charSequence: CharSequence) {
        itemListAll.add(t!! as Notifications)
        filter.filter(charSequence)
        notifyDataSetChanged()
    }

    fun removeItem(`object`: ParseObject) {
        for (i in itemList.indices) {
            if (itemList[i].objectId == `object`.objectId) {
                itemList.removeAt(i)
                notifyItemRemoved(i)
                notifyItemRangeChanged(i, itemList.size)
                return
            }
        }
    }

    fun updateItem(`object`: ParseObject) {
        for (i in itemList.indices) {
            if (itemList[i].objectId == `object`.objectId) {
                itemList[i] = `object` as Notifications
                notifyDataSetChanged()
                return
            }
        }
    }

    fun setData(items: MutableList<Notifications>) {
        this.itemList = items

        itemList.forEach{
            this.itemListAll.add(it)
        }

        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return myFilter
    }

    var myFilter: Filter = object : Filter() {
        //Automatic on background thread

        override fun performFiltering(charSequence: CharSequence): FilterResults {
            val filteredList: MutableList<Notifications> = mutableListOf()
            Timber.i("${itemListAll.size}")

            for (order in itemListAll) {
                if(charSequence.equals("1")){
                    if (order.waiterID == null) {
                        Timber.i("nullda")
                        filteredList.add(order)
                    }
                }else{//null değilse ve KENDİYSE
                    if (order.waiterID == ParseUser.getCurrentUser().username) {
                        Timber.i("nullda değil")
                        filteredList.add(order)
                    }
                }
            }
            val filterResults = FilterResults()
            filterResults.values = filteredList

            return filterResults
        }

        override fun publishResults(p0: CharSequence?, filterResults: FilterResults?) {
            itemList.clear()

            if (filterResults?.values != null) {
                itemList.addAll((filterResults.values as MutableList<Notifications>))
            }
            notifyDataSetChanged()
        }
    }


        inner class ViewHolder(private val binding: OrderCardViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Notifications ){
            binding.order= item
            binding.listener=clickListener
            if(item.waiterID==null){
                binding.cardView.setCardBackgroundColor(Color.parseColor("#87D288"))
            }else{
                binding.cardView.setCardBackgroundColor(Color.parseColor("#F57F68"))
            }
        }
    }
}

interface OrderClickListener {
    fun onOrderClick(notifications: Notifications)
}
