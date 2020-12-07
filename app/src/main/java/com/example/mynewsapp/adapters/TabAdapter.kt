package com.example.mynewsapp.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mynewsapp.fragments.NewsFragment
import com.example.mynewsapp.models.PreferenceModel

class TabAdapter(activity: AppCompatActivity, private val titles: ArrayList<PreferenceModel>): FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        return NewsFragment(this.titles[position])
    }

    override fun getItemCount(): Int {
        return this.titles.size
    }
}