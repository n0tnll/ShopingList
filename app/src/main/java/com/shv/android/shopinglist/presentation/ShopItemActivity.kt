package com.shv.android.shopinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.shv.android.shopinglist.R
import com.shv.android.shopinglist.databinding.ActivityShopItemBinding
import com.shv.android.shopinglist.domain.ShopItem

class ShopItemActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishedListener {

    private var screenMode = MODE_UNKNOWN
    private var shopItemId = ShopItem.UNDEFINED_ID

    private lateinit var binding: ActivityShopItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        parseIntent()
        if (savedInstanceState == null)
            launchRightMode()
    }

    private fun launchRightMode() {
        val fragment = when (screenMode) {
            MODE_EDIT -> ShopItemFragment.newInstanceEditItem(shopItemId)
            MODE_ADD -> ShopItemFragment.newInstanceAddItem()
            else -> throw RuntimeException("Unknown screen mode: $screenMode")
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.shop_item_container, fragment)
            .commit()
    }

    companion object {
        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent
        }

        fun newIntentEditItem(context: Context, itemShopId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, itemShopId)
            return intent
        }

        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_SHOP_ITEM_ID = "item_id"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_UNKNOWN = ""
    }

    private fun parseIntent() {
        if (!intent.hasExtra(EXTRA_SCREEN_MODE))
            throw RuntimeException("Param screen mode is absent")
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if (mode != MODE_ADD && mode != MODE_EDIT)
            throw RuntimeException("Unknown screen mode $mode")
        screenMode = mode

        if (screenMode == MODE_EDIT) {
            if (!intent.hasExtra(EXTRA_SHOP_ITEM_ID))
                throw RuntimeException("Param shop item id is absent")
            shopItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    override fun onEditingFinished() {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        finish()
    }
}