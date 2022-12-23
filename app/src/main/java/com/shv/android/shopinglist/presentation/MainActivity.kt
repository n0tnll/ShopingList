package com.shv.android.shopinglist.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.shv.android.shopinglist.R
import com.shv.android.shopinglist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishedListener {

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    private lateinit var shopListAdapter: ShopListAdapter

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setOnAddButtonClickListener()

        setupRecyclerView()
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

    private fun setupRecyclerView() {
        shopListAdapter = ShopListAdapter()
        with(binding) {
            rvShopList.recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.ITEM_STATE_ENABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
            rvShopList.recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.ITEM_STATE_DISABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
            rvShopList.adapter = shopListAdapter
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
        itemTouchHelper.attachToRecyclerView(binding.rvShopList)
    }

    private fun setOnAddButtonClickListener() {
        binding.btnAddShopItem.setOnClickListener {
            if (isOnePaneMode()) {
                val intent = ShopItemActivity.newIntentAddItem(this)
                startActivity(intent)
            } else {
                launchFragment(ShopItemFragment.newInstanceAddItem())
            }
        }
    }

    private fun setupOnItemClickListener() {
        shopListAdapter.onShopItemClickListener = {
            if (isOnePaneMode()) {
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

    private fun isOnePaneMode(): Boolean {
        return binding.shopItemContainer == null
    }

    override fun onEditingFinished() {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        supportFragmentManager.popBackStack()
    }
}