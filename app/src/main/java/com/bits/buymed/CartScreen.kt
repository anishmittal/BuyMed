package com.bits.buymed

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.*
import com.bits.buymed.interfaces.ApiService
import com.bits.buymed.model.CartItem
import com.bits.buymed.model.CartItemsListAdapter
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class CartScreen : AppCompatActivity() {
    private lateinit var resultListView: ListView
    private lateinit var adapter: CartItemsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cart_screen)

        val baseUrl = resources.getString(R.string.base_url)
        resultListView = findViewById(R.id.resultListView)
        adapter = CartItemsListAdapter(this, R.layout.cart_item, mutableListOf())
        resultListView.adapter = adapter
        val placeOrderButton = findViewById<Button>(R.id.placeOrder)

        placeOrderButton.setOnClickListener {
            val cartItems = mutableListOf<CartItem>()
            for (i in 0 until adapter.count) {
                val item = adapter.getItem(i)
                if (item != null) {
                    cartItems.add(item)
                }
            }
            sendCartItemsToApi(cartItems)

            val intent = Intent(this, PlaceOrderScreen::class.java)
            startActivity(intent)
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl) // Replace with your API base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        apiService.getCartItems().enqueue(object : Callback<List<CartItem>> {
                override fun onResponse(call: Call<List<CartItem>>, response: Response<List<CartItem>>) {
                    Log.d("$$$$$ response" ,""+response.isSuccessful )
                    if (response.isSuccessful) {
                        val cartItems = response.body()
                        Log.d("$$$$$ response" ,""+cartItems.toString() )
                        cartItems?.let {
                            adapter.clear()
                            adapter.addAll(it)
                            adapter.notifyDataSetChanged()
                        }
                    } else {
                        val statusCode = response.code()
                        val errorResponseBody = response.errorBody()?.string()
                        Log.d("$$$$$ response", "Error, Status Code: $statusCode")
                        Log.d("$$$$$ response", "Error Body: $errorResponseBody")

                    }
                }

                override fun onFailure(call: Call<List<CartItem>>, t: Throwable) {
                    Log.d("$$$ fail Get CartItems", "Fail")
                }
            })
    }

    private fun sendCartItemsToApi(cartItems: List<CartItem>) {
        val baseUrl = resources.getString(R.string.base_url)
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        // Convert cartItems to a JSON string
        val cartItemsJson = Gson().toJson(cartItems)

        // Create a request body from the JSON string
        val requestBody = cartItemsJson.toRequestBody("application/json; charset=utf-8".toMediaType())

        apiService.placeOrder(requestBody).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                val statusCode = response.code()
                Log.d("Place Order", "Status Code: $statusCode")
                if (response.isSuccessful) {
                    // Handle a successful response
                    val responseData = response.body()
                    if (responseData != null) {
                        // Process the response data as needed
                    }
                } else {
                    // Handle an unsuccessful response
                    val statusCode = response.code()
                    val errorResponseBody = response.errorBody()?.string()
                    Log.d("Place Order", "Error, Status Code: $statusCode")
                    Log.d("Place Order", "Error Body: $errorResponseBody")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Handle network failure
                Log.d("Place Order", "Network request failed")
            }
        })
    }

}