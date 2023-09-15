package com.bits.buymed

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.*
import com.bits.buymed.interfaces.ApiService
import com.bits.buymed.model.CartItem
import com.bits.buymed.model.CartItemsListAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CartScreen : AppCompatActivity() {
    private lateinit var resultListView: ListView
    private lateinit var adapter: CartItemsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cart_screen)

        resultListView = findViewById(R.id.resultListView)
        adapter = CartItemsListAdapter(this, R.layout.cart_item, mutableListOf())
        resultListView.adapter = adapter
        val placeOrderButton = findViewById<Button>(R.id.placeOrder)

        placeOrderButton.setOnClickListener {
            val intent = Intent(this, PlaceOrderScreen::class.java)
            startActivity(intent)
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000") // Replace with your API base URL
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
}