package com.example.animewatcher

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

// CHANGE THIS to your own GitHub raw JSON URL
const val CHANNEL_LIST_URL =
    "https://raw.githubusercontent.com/Hawsi420/Anime-Watcher-/refs/heads/main/Anime%20Watcher"

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchChannels()
    }

    private fun fetchChannels() {
        val request = Request.Builder().url(CHANNEL_LIST_URL).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Failed to load: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                val body = response.body?.string() ?: return
                val type = object : TypeToken<List<Channel>>() {}.type
                val channels: List<Channel> = Gson().fromJson(body, type)

                runOnUiThread {
                    recyclerView.adapter = ChannelAdapter(channels) { channel ->
                        val intent = Intent(this@MainActivity, WebViewActivity::class.java)
                        intent.putExtra("url", channel.home_url)
                        intent.putExtra("name", channel.name)
                        startActivity(intent)
                    }
                }
            }
        })
    }
}

class ChannelAdapter(
    private val channels: List<Channel>,
    private val onClick: (Channel) -> Unit
) : RecyclerView.Adapter<ChannelAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val text: TextView = view.findViewById(R.id.channelName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_channel, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val channel = channels[position]
        holder.text.text = channel.name
        holder.itemView.setOnClickListener { onClick(channel) }
    }

    override fun getItemCount() = channels.size
}
