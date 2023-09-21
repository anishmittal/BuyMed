package com.bits.buymed.model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.bits.buymed.R

class OrderItemAdapter(context: Context, resource: Int, objects: List<String>) :
    ArrayAdapter<String>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView = convertView ?: inflater.inflate(R.layout.list_item, null)

        val itemText = getItem(position)
        val itemTextView = itemView.findViewById<TextView>(R.id.itemTextView)
        itemTextView.text = itemText

        return itemView
    }
}
