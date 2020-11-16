package com.example.mynewsapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class SearchListViewAdapter(private val activity: Activity, searchResults: List<PreferenceModel>): BaseAdapter() {

    private var searchResults = ArrayList<PreferenceModel>()

    init {
        this.searchResults = searchResults as ArrayList
    }

    override fun getCount(): Int {
        return searchResults.size
    }

    override fun getItem(i: Int): Any {
        return i
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    @SuppressLint("InflateParams", "ViewHolder")
    override fun getView(i: Int, convertView: View?, viewGroup: ViewGroup): View {
        var convertView = convertView
        val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        convertView = inflater.inflate(R.layout.list_item, null)
        val label = convertView.findViewById<TextView>(R.id.label)
        label.text = searchResults[i].preferenceName
        return convertView
    }
}