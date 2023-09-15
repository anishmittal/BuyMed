package com.bits.buymed.model

import android.content.Context
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.bits.buymed.R
import com.bits.buymed.interfaces.ApiService
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class StockListAdapter(context: Context, resource: Int, data: List<StockItem>) :
    ArrayAdapter<StockItem>(context, resource, data) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView = convertView ?: inflater.inflate(R.layout.item_stock, null)

        val titleTextView = itemView.findViewById<TextView>(R.id.titleTextView)
        val priceTextView = itemView.findViewById<TextView>(R.id.priceTextView)
        val categoryTextView = itemView.findViewById<TextView>(R.id.categoryTextView)
        val descriptionTextView = itemView.findViewById<TextView>(R.id.descTextView)

        val addToCartButton = itemView.findViewById<Button>(R.id.addToCartButton)

        val item = getItem(position)
        titleTextView.text = "Title: " + (item?.title ?: "")
        priceTextView.text = "Price: " + (item?.price ?: "")
        categoryTextView.text = "Category: " + (item?.category ?: "")
        descriptionTextView.text = "Description: " + (item?.description ?: "")

        addToCartButton.setOnClickListener {
            // Perform the API POST request here
            addItemToCart(item)
        }
        return itemView
    }
    private fun addItemToCart(item: StockItem?) {
        Log.d("$$$$$ response" ,"Add items to Cart")
        // Construct the JSON body for the API POST request
        val requestBody = JSONObject().apply {
            put("title", item?.title)
            put("price", item?.price)
        }

        Log.d("$$$$$ requestBody" ,""+requestBody.toString() )

        // Perform the API POST request using a networking library like Retrofit or Volley
        // Replace this with your actual network call code
        // Example with Retrofit:
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        val cartItem = CartItem(""+item?.title, ""+item?.price)
        val call = apiService.addToCart(cartItem)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Log.d("$$$$$ response" ,""+response.isSuccessful )
                if (response.isSuccessful) {
                    val toast = Toast.makeText(context, "Item added to cart", Toast.LENGTH_SHORT)
                    toast.show()

                    // Schedule the toast to disappear after 2 seconds
                    Handler().postDelayed({
                        toast.cancel()
                    }, 2000)
                } else {
                    val statusCode = response.code()
                    val errorResponseBody = response.errorBody()?.string()
                    Log.d("$$$$$ response", "Error, Status Code: $statusCode")
                    Log.d("$$$$$ response", "Error Body: $errorResponseBody")

                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Handle network failure
            }
        })
    }
}
