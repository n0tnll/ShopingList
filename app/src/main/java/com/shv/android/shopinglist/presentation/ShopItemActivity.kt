package com.shv.android.shopinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.shv.android.shopinglist.R
import com.shv.android.shopinglist.domain.ShopItem

class ShopItemActivity : AppCompatActivity() {

    private lateinit var tilTitle: TextInputLayout
    private lateinit var tilCount: TextInputLayout
    private lateinit var etItemTitle: TextInputEditText
    private lateinit var etItemCount: TextInputEditText
    private lateinit var btnSave: MaterialButton

    private lateinit var shopItemViewModel: ShopItemViewModel

    private var screenMode = MODE_UNKNOWN
    private var shopItemId = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        parseIntent()
        initViews()
        shopItemViewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]

        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
        textWatchers()
        observeErrors()
    }

    private fun launchEditMode() {
        shopItemViewModel.getShopItem(shopItemId)
        shopItemViewModel.shopList.observe(this) {
            etItemTitle.setText(it.title)
            etItemCount.setText(it.count.toString())
        }
        btnSave.setOnClickListener {
            val name = etItemTitle.text.toString()
            val count = etItemCount.text.toString()
            shopItemViewModel.editShopItem(name, count)
            closeScreen()
        }
    }

    private fun launchAddMode() {
        btnSave.setOnClickListener {
            val name = etItemTitle.text.toString()
            val count = etItemCount.text.toString()
            shopItemViewModel.addShopItem(name, count)
            closeScreen()
        }
    }

    private fun closeScreen() {
        shopItemViewModel.isComplete.observe(this) {
            finish()
        }
    }

    private fun textWatchers() {
        etItemTitle.doOnTextChanged { text, _, _, _ ->
            text?.let {
                if (text.isBlank()) {
                    tilTitle.error = getString(R.string.error_input_name)
                } else
                    shopItemViewModel.resetErrorInputName()
            }
        }

        etItemCount.doOnTextChanged { text, _, _, count ->
            text?.let {
                if (it.isNotBlank()) {
                    val txtCount = text.toString().toInt()
                    if (txtCount <= 0)
                        tilCount.error = getString(R.string.error_input_count_zero)
                    else if (count == 0)
                        tilCount.error = getString(R.string.error_input_count)
                    else
                        shopItemViewModel.resetErrorInputCount()
                } else
                    tilCount.error = getString(R.string.error_input_count)
            }
        }
    }

    private fun observeErrors() {
        shopItemViewModel.errorInputName.observe(this) {
            if (it)
                tilTitle.error = getString(R.string.error_input_name)
            else
                tilTitle.error = null
        }

        shopItemViewModel.errorInputCount.observe(this) {
            if (it)
                tilCount.error = getString(R.string.error_input_count)
            else
                tilCount.error = null
        }
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

    private fun initViews() {
        tilTitle = findViewById(R.id.tilTitle)
        tilCount = findViewById(R.id.tilCount)
        etItemTitle = findViewById(R.id.etItemTitle)
        etItemCount = findViewById(R.id.etItemCount)
        btnSave = findViewById(R.id.btnSave)
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
}