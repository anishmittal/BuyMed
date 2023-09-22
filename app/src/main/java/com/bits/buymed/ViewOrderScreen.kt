package com.bits.buymed

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.bits.buymed.model.OrderItem
import com.bits.buymed.model.OrderItemAdapter
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class ViewOrderScreen : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_order)


        val baseUrl = resources.getString(R.string.base_url)
        listView = findViewById(R.id.listView)
        adapter = OrderItemAdapter(this, R.layout.list_item, mutableListOf())
        listView.adapter = adapter

        val client = OkHttpClient()
        val request = Request.Builder()
            .url(baseUrl + "/api/vieworder/redirect")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let { json ->
                    val gson = Gson()
                    val orderItems = gson.fromJson(json, Array<OrderItem>::class.java).toList()
                    runOnUiThread {
                        adapter.clear()
                        for (item in orderItems) {
                            val formattedItem = "title: ${item.title}\nprice: ${item.price}\nquantity: ${item.quantity}"
                            adapter.add(formattedItem)
                        }
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        })
    }
}
