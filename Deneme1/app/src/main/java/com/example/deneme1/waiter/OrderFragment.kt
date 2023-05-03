package com.example.deneme1.waiter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.deneme1.R
import com.example.deneme1.data.Notifications
import com.example.deneme1.databinding.FragmentOrderBinding
import com.google.android.material.button.MaterialButton
import com.parse.*
import com.parse.livequery.ParseLiveQueryClient
import com.parse.livequery.SubscriptionHandling
import timber.log.Timber
import java.util.*

class OrderFragment : Fragment(), OrderClickListener{

    private var companyID: String? = null
    private lateinit var orderAdapter: OrderAdapter
    private lateinit var binding: FragmentOrderBinding
    private var parseLiveQueryClient: ParseLiveQueryClient? = null
    private var subscriptionHandling: SubscriptionHandling<ParseObject>? = null
    private val notificationsList:MutableList<Notifications> = mutableListOf()
    var isOwnedButtonActive:CharSequence="0"//owned=aktifler başta açılırken tamamlananlar açılacak

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {

        binding = FragmentOrderBinding.inflate(layoutInflater)

        val layoutManager = LinearLayoutManager(context)
        orderAdapter= OrderAdapter(this)

        binding.recyclerView.adapter=orderAdapter
        binding.recyclerView.layoutManager = layoutManager

        dataInitialize()
        setupLiveQuery()
        binding.btnActives.setOnClickListener {
            setStyle(binding.btnActives,binding.btnCompleted)
            binding.loading.visibility=View.VISIBLE
            isOwnedButtonActive="0"
            orderAdapter.filter.filter(isOwnedButtonActive)
        }

        binding.btnCompleted.setOnClickListener {
            setStyle(binding.btnCompleted,binding.btnActives)
            binding.loading.visibility=View.VISIBLE
            isOwnedButtonActive="1"

            orderAdapter.filter.filter(isOwnedButtonActive)
            binding.loading.visibility=View.GONE
        }

        binding.fab.setOnClickListener {
            addRandomOrder()
            orderAdapter.notifyDataSetChanged()
            Timber.i("add random order")
        }

        return binding.root
    }

        private fun setStyle(activeButton: MaterialButton,otherButton: MaterialButton) {
        otherButton.setTextColor(resources.getColor(R.color.gray))
        activeButton.setTextColor(resources.getColor(R.color.blue))
        otherButton.setBackgroundColor(resources.getColor(R.color.blue))
        activeButton.setBackgroundColor(resources.getColor(R.color.gray))
        }

    override fun onOrderClick(notifications: Notifications) {
        orderDialog(notifications)
    }

    private fun addRandomOrder() {
        ParseObject.registerSubclass(Notifications::class.java)
        val notifications = Notifications()
        notifications.customerOrder = "Sipariş vermek istiyor."
        var num = Random().nextInt(30)
        notifications.tableNumber = num.toString()
        notifications.waiterID= null
        notifications.isCancelled=false
        notifications.isFinished=false
        notifications.saveInBackground { e ->
            if (e != null) {
                Timber.i("cannot saved: $e")
            } else {
                Timber.i("saved")
            }
        }
    }

    private fun dataInitialize() {
        Timber.i("DATA INITIALIZE")
        binding.loading.visibility = View.VISIBLE
        readOrder()
    }

    fun addOrUpdateOrder(parseObject: Notifications){
        parseObject.saveInBackground { e ->
            if (e != null) {
                Timber.i(e.message)
            }
        }
    }

    private fun orderDialog(notifications: Notifications) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Bu siparişi taşımak istediğinize emin misiniz?")
        builder.setMessage("Emin Misiniz?")
        builder.setNegativeButton("Hayır", null)
        builder.setPositiveButton("Evet",
            DialogInterface.OnClickListener { dialogInterface, i ->
                doWithObject(notifications)
            })
        builder.show()
    }
    private fun setupLiveQuery() {
        parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient()
        val parseQuery = ParseQuery<ParseObject>("Notifications")
        subscriptionHandling = parseLiveQueryClient!!.subscribe(parseQuery)
        subscriptionHandling!!.handleSubscribe { initLiveQuery()}
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initLiveQuery() {

        if (subscriptionHandling != null) {
            subscriptionHandling!!.handleEvent(SubscriptionHandling.Event.CREATE
            ) { _: ParseQuery<ParseObject?>?, notifications: ParseObject? ->
                Timber.i("threadin dışında read")
                activity?.runOnUiThread {
                    orderAdapter.addItem(notifications,isOwnedButtonActive)
                    Timber.i("read")
                    //burayı sil
                }
            }

            subscriptionHandling!!.handleEvent(SubscriptionHandling.Event.UPDATE
            ) { _: ParseQuery<ParseObject?>?, notifications: ParseObject? ->

                Timber.i("dışarda Add or update")
                activity?.runOnUiThread {
                    if(notifications!=null){
                        orderAdapter.removeItem(notifications)
                        orderAdapter.addItem(notifications, isOwnedButtonActive)
                        Timber.i("Add or update")
                    }
                }
            }

            subscriptionHandling!!.handleEvent(SubscriptionHandling.Event.DELETE
            ) { _: ParseQuery<ParseObject?>?, notifications: ParseObject? ->
                activity?.runOnUiThread {
                    if(notifications!=null){
                        orderAdapter.removeItem(notifications)
                        orderAdapter.filter.filter(isOwnedButtonActive)
                        Timber.i("remove")
                    }
                }
            }

        }else{
            Timber.i("null")
        }
    }

    private fun readOrder() {

        notificationsList.clear()
        ParseObject.registerSubclass(Notifications::class.java)
        val query: ParseQuery<Notifications> = ParseQuery.getQuery(Notifications::class.java)

        val DAY_IN_MS = 1000 * 60 * 60 * 24
        val oneDayAgo = Date(System.currentTimeMillis() - (1* DAY_IN_MS))
        val today= Date()
        Timber.v(oneDayAgo.toString())

        query.whereGreaterThanOrEqualTo("createdAt",oneDayAgo)
        query.whereLessThanOrEqualTo("createdAt",today)
        //TODO company id al ve ona göre filtrele

        val user = ParseUser.getCurrentUser()

        user.getRelation<ParseObject>("company").query.findInBackground{commentList, e ->
            Timber.d("company 1   : ${commentList.get(0).objectId}")
            companyID=commentList.get(0).objectId

        }
        //TODO tarihe göre filtrele
        query.whereContainedIn("username", Arrays.asList(null, ParseUser.getCurrentUser().username));
        //query.whereContainedIn("username", Arrays.asList("USER1", "USER2")); null ve waiterID için
        query.whereEqualTo("objectId",companyID)
        query.findInBackground { results, e ->
            if (e != null) {

            } else {

                orderAdapter.setData(results)
                orderAdapter.filter.filter("1")
                //notificationsList.addAll(results)
                orderAdapter.notifyDataSetChanged()
                binding.loading.visibility=View.GONE
            }
        }
    }

    private fun readOwnedOrder() {

        notificationsList.clear()

        ParseObject.registerSubclass(Notifications::class.java)
        val query: ParseQuery<Notifications> = ParseQuery.getQuery(Notifications::class.java)

        val DAY_IN_MS = 1000 * 60 * 60 * 24
        val oneDayAgo = (Date(System.currentTimeMillis() - (1* DAY_IN_MS)))

        query.whereGreaterThanOrEqualTo("createdAt",oneDayAgo)
        query.whereEqualTo("waiterID", ParseUser.getCurrentUser().username)
        query.findInBackground { results, e ->
            if (e != null) {

            } else {
                notificationsList.addAll(results)
                orderAdapter.notifyDataSetChanged()
                binding.loading.visibility=View.GONE
            }
        }
    }

    private fun doWithObject(parseObject: Notifications){
        if(parseObject.waiterID == null){
            notificationsList.let { parseObject.waiterID= ParseUser.getCurrentUser().username
                addOrUpdateOrder(parseObject)
            }
        }
    }

    fun deleteOrder(parseObject: Notifications, state:Int){
        parseObject.deleteInBackground { e ->
            if (e == null) {
                if(state==0){
                    readOrder()
                }else{
                    readOwnedOrder()
                }
                Toast.makeText(context,"DELETED SUCCESSFULLY",Toast.LENGTH_LONG)
                //success mesajı
            }else {
                Toast.makeText(context,"Silinemedi",Toast.LENGTH_LONG)
                //error mesajı
            }
        }
    }
}
