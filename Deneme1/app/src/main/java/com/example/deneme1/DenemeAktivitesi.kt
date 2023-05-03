package com.example.deneme1
import android.annotation.SuppressLint
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.livequery.ParseLiveQueryClient
import com.parse.livequery.SubscriptionHandling
import com.parse.livequery.SubscriptionHandling.HandleEventCallback
import com.parse.livequery.SubscriptionHandling.HandleSubscribeCallback

class DenemeAktivitesi : AppCompatActivity() {

    private var messages: RecyclerView? = null
    private var message: EditText? = null
    private var send: ImageView? = null
    var progressDialog: ProgressDialog? = null


    private var parseLiveQueryClient: ParseLiveQueryClient? = null
    private var subscriptionHandling: SubscriptionHandling<ParseObject>? = null
    private val TAG = "MainActivity"
    private var messagesAdapter: MessagesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deneme_aktivitesi)
        progressDialog = ProgressDialog(this)
        messages = findViewById(R.id.messages)
        message = findViewById(R.id.message)
        send = findViewById(R.id.send)
        getMessages()
        setupLiveQuery()

        send?.setOnClickListener { sendMessage() }
    }


    private fun sendMessage() {
        progressDialog!!.show()
        val `object` = ParseObject("Message")
        `object`.put("message", message!!.text.toString())
        `object`.saveInBackground { e: ParseException? ->
            progressDialog!!.hide()
            if (e != null) Toast.makeText(
                this@DenemeAktivitesi,
                e.localizedMessage,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun getMessages() {
        val query = ParseQuery<ParseObject>("Message")
        query.findInBackground { objects: List<ParseObject>, e: ParseException? ->
            if (e == null) {
                initMessages(objects.toMutableList())
            } else {
                Toast.makeText(this@DenemeAktivitesi, e.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initLiveQuery() {
        if (subscriptionHandling != null) {
            subscriptionHandling!!.handleEvent(SubscriptionHandling.Event.CREATE
            ) { _: ParseQuery<ParseObject?>?, `object`: ParseObject? ->
                runOnUiThread { messagesAdapter!!.addItem(`object`) }
            }
            subscriptionHandling!!.handleEvent(SubscriptionHandling.Event.DELETE
            ) { _: ParseQuery<ParseObject?>?, `object`: ParseObject? ->
                runOnUiThread { messagesAdapter!!.removeItem(`object`!!) }
            }
            subscriptionHandling!!.handleEvent(SubscriptionHandling.Event.UPDATE
            ) { _: ParseQuery<ParseObject?>?, `object`: ParseObject? ->
                runOnUiThread { messagesAdapter!!.updateItem(`object`!!) }
            }
        }
    }


    private fun setupLiveQuery() {
        parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient()
        val parseQuery = ParseQuery<ParseObject>("Message")
        subscriptionHandling = parseLiveQueryClient!!.subscribe(parseQuery)
        subscriptionHandling!!.handleSubscribe { initLiveQuery() }
    }

    private fun initMessages(messages: MutableList<ParseObject>) {
        messagesAdapter = MessagesAdapter(this, messages)
        this.messages!!.layoutManager = LinearLayoutManager(this@DenemeAktivitesi)
        this.messages!!.adapter = messagesAdapter
    }
}