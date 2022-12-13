package com.shv.android.shopinglist.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.shv.android.shopinglist.R

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var shopListAdapter: ShopListAdapter

    private var shop_item_container: FragmentContainerView? = null
    private lateinit var btnAddShopItem: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()

        setOnAddButtonClickListener()

        setupRecyclerView()

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        mainViewModel.shopList.observe(this) {
            shopListAdapter.submitList(it)
        }
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.shop_item_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onBackPressed() {
        supportFragmentManager.popBackStack("add", 0)
    }

    private fun initViews() {
        btnAddShopItem = findViewById(R.id.btnAddShopItem)
        shop_item_container = findViewById(R.id.shop_item_container)
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.rvShopList)
        shopListAdapter = ShopListAdapter()
        with(recyclerView) {
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.ITEM_STATE_ENABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.ITEM_STATE_DISABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
            adapter = shopListAdapter
        }

        setupOnLongClickListener()
        setupOnItemClickListener()
        setupSwipeToDelete()
    }

    private fun setupSwipeToDelete() {
        val swipeToDeleteItemCallback = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val item = shopListAdapter.currentList[position]
                mainViewModel.deleteShopItem(item)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteItemCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun setOnAddButtonClickListener() {
        btnAddShopItem.setOnClickListener {
            if (shop_item_container == null) {
                val intent = ShopItemActivity.newIntentAddItem(this)
                startActivity(intent)
            } else {
                launchFragment(ShopItemFragment.newInstanceAddItem())
            }
        }
    }

    private fun setupOnItemClickListener() {
        shopListAdapter.onShopItemClickListener = {
            if (shop_item_container == null) {
                val intent = ShopItemActivity.newIntentEditItem(this, it.id)
                startActivity(intent)
            } else {
                launchFragment(ShopItemFragment.newInstanceEditItem(it.id))
            }
        }
    }

    private fun setupOnLongClickListener() {
        shopListAdapter.onShopItemLongClickListener = {
            mainViewModel.editShopItem(it)
        }
    }
}