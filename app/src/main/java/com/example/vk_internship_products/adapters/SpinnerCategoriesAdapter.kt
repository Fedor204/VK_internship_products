package com.example.vk_internship_products.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.vk_internship_products.R

class SpinnerCategoriesAdapter(
    context: Context,
    private val resource: Int,
    private val categories: List<String>
) :

    ArrayAdapter<String>(context, resource, categories) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    private fun createView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.spinner_item_layout, parent, false)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = categories[position]

        return view
    }

}