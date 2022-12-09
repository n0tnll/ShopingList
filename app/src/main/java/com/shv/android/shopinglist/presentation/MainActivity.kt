package com.shv.android.shopinglist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.shv.android.shopinglist.R

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivityTest"
    private lateinit var mainViewModel: MainViewModel

    private var count = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        mainViewModel.shopList.observe(this) {
            Log.d(TAG, it.toString())
            if (count == 1) {
                val item = it[0]
                mainViewModel.editShopItem(item)
                count = 0
            }
        }
    }
}