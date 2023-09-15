package com.bits.buymed

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bits.buymed.interfaces.ApiService
import com.bits.buymed.model.StockItem
import com.bits.buymed.model.StockListAdapter
import com.google.android.material.navigation.NavigationView
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.appcompat.app.AlertDialog

class SearchMedicines : AppCompatActivity() {

    private lateinit var spinner: Spinner
    private lateinit var searchButton: Button
    private lateinit var viewCartButton: Button
    private lateinit var resultListView: ListView
    private lateinit var adapter: StockListAdapter
    private lateinit var selectedItem: String
    private lateinit var drawerLayout: DrawerLayout

    private val PREFERENCES_FILE_NAME = "MyAppPreferences"
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_medicines)

        sharedPreferences = getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)

        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        val headerView = navView.getHeaderView(0)

        val usernameTextView = headerView.findViewById<TextView>(R.id.username)
        usernameTextView.setText(sharedPreferences.getString("username", null))

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_profile -> {
                    val intent = Intent(this, UserProfileActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_sign_out -> {
                    val alertDialog = AlertDialog.Builder(this)
                        .setView(R.layout.dialog_sign_out)
                        .setPositiveButton("Yes") { _, _ ->
                            val editor = sharedPreferences.edit()
                            editor.remove("username")
                            editor.remove("password")
                            editor.apply()
                            finish()
                        }
                        .setNegativeButton("No") { dialog, _ ->
                            dialog.dismiss() // Dismiss the dialog if "No" is clicked
                        }
                        .create()

                    alertDialog.show()
                }
            }

            // Close the drawer when an item is selected
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        val staticItems = arrayOf("Vitamins", "paracetomal", "cough", "Nasal_Spray", "Gastric")
        val adapterCategories = ArrayAdapter(this, android.R.layout.simple_spinner_item, staticItems)


        spinner = findViewById(R.id.spinner)
        searchButton = findViewById(R.id.searchButton)
        viewCartButton = findViewById(R.id.viewCartButton)
        resultListView = findViewById(R.id.resultListView)
        adapter = StockListAdapter(this, R.layout.item_stock, mutableListOf())
        resultListView.adapter = adapter
        spinner.adapter = adapterCategories

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                // Get the selected item as a string
                selectedItem = spinner.selectedItem.toString()
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {

            }
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000") // Replace with your API base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        searchButton.setOnClickListener {
            // Disable the button while the request is in progress
            searchButton.isEnabled = false
            Log.d("$$$$$ ", "Search button clicked" )

            apiService.getStockItemsByCategory(selectedItem).enqueue(object : Callback<List<StockItem>> {
                override fun onResponse(call: Call<List<StockItem>>, response: Response<List<StockItem>>) {
                    Log.d("$$$$$ response" ,""+response.isSuccessful )
                    if (response.isSuccessful) {
                        val stockItems = response.body()
                        Log.d("$$$$$ response" ,""+stockItems.toString() )
                        stockItems?.let {
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

                    // Enable the button again after the request is complete
                    searchButton.isEnabled = true
                }

                override fun onFailure(call: Call<List<StockItem>>, t: Throwable) {
                    // Handle failure

                    // Enable the button again after the request is complete
                    searchButton.isEnabled = true
                }
            })
        }

        viewCartButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                // Create an Intent to start the new activity
                val intent = Intent(this@SearchMedicines, CartScreen::class.java)

                startActivity(intent)
            }
        })
    }




}