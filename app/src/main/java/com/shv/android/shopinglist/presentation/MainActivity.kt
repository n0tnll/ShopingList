package com.shv.android.shopinglist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.shv.android.shopinglist.R
import com.shv.android.shopinglist.domain.ShopItem

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivityTest"
    private lateinit var mainViewModel: MainViewModel

    private lateinit var linearLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        linearLayout = findViewById(R.id.linear_layout_shops)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        mainViewModel.shopList.observe(this) {
            Log.d(TAG, it.toString())
            showList(it)
        }
    }

    private fun showList(list: List<ShopItem>) {
        linearLayout.removeAllViews()
        for (el in list) {
            val layoutId = if (el.enabled)
                R.layout.shop_item_enabled
            else
                R.layout.shop_item_disabled
            val view = LayoutInflater.from(this).inflate(
                layoutId,
                linearLayout,
                false
            )
            val tvTitle = view.findViewById<TextView>(R.id.tvShopItem)
            val tvCount = view.findViewById<TextView>(R.id.tvItemCount)
            tvTitle.text = el.title
            tvCount.text = el.count.toString()

            view.setOnLongClickListener {
                mainViewModel.editShopItem(el)
                true
            }

            linearLayout.addView(view)
        }
    }
}