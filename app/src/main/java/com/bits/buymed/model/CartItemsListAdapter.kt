package com.bits.buymed.model

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import com.bits.buymed.R


class CartItemsListAdapter(context: Context, resource: Int, data: List<CartItem>) :
    ArrayAdapter<CartItem>(context, resource, data) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView = convertView ?: inflater.inflate(R.layout.cart_item, null)

        lateinit var incButton: Button
        lateinit var decButton: Button

        val titleTextView = itemView.findViewById<TextView>(R.id.titleTextView)
        val priceTextView = itemView.findViewById<TextView>(R.id.priceTextView)
        val quantityTextView = itemView.findViewById<TextView>(R.id.quantityTextView)
        incButton = itemView.findViewById<Button>(R.id.incButton)
        decButton = itemView.findViewById<Button>(R.id.decButton)

        val item = getItem(position)
        titleTextView.text = "Title: " + (item?.title ?: "")
        priceTextView.text = "Price: " + (item?.price ?: "")

        var quantity = 1 // Initial quantity value

        incButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                // Increment the quantity and update the TextView
                quantity++
                quantityTextView.text = quantity.toString()
            }
        })

        decButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                // Increment the quantity and update the TextView
                if(quantity > 1)
                    quantity--
                quantityTextView.text = quantity.toString()
            }
        })

        item?.quantity = quantity
        return itemView
    }

}
