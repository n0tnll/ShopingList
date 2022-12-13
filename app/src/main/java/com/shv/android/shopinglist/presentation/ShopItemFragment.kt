package com.shv.android.shopinglist.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.shv.android.shopinglist.R
import com.shv.android.shopinglist.domain.ShopItem

class ShopItemFragment : Fragment() {

    private lateinit var shopItemViewModel: ShopItemViewModel

    private lateinit var tilTitle: TextInputLayout
    private lateinit var tilCount: TextInputLayout
    private lateinit var etItemTitle: TextInputEditText
    private lateinit var etItemCount: TextInputEditText
    private lateinit var btnSave: MaterialButton

    private var screenMode: String = MODE_UNKNOWN
    private var shopItemId: Int = ShopItem.UNDEFINED_ID

    private lateinit var onEditingFinishedListener: OnEditingFinishedListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnEditingFinishedListener) {
            onEditingFinishedListener = context
        } else {
            throw RuntimeException("Activity must implement OnEditingFinishedListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shop_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shopItemViewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]

        initViews(view)
        textWatchers()

        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
        observeErrors()
    }

    private fun parseParams() {
        val args = requireArguments()
        if (!args.containsKey(SCREEN_MODE))
            throw RuntimeException("Param screen mode is absent")
        val mode = args.getString(SCREEN_MODE)
        if (mode != MODE_ADD && mode != MODE_EDIT)
            throw RuntimeException("Unknown screen mode $mode")
        screenMode = mode

        if (screenMode == MODE_EDIT) {
            if (!args.containsKey(SHOP_ITEM_ID))
                throw RuntimeException("Param shop item id is absent")
            shopItemId = args.getInt(SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }


    private fun launchEditMode() {
        shopItemViewModel.getShopItem(shopItemId)
        shopItemViewModel.shopList.observe(viewLifecycleOwner) {
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
        shopItemViewModel.isComplete.observe(viewLifecycleOwner) {
            onEditingFinishedListener.onEditingFinished()
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
        shopItemViewModel.errorInputName.observe(viewLifecycleOwner) {
            if (it)
                tilTitle.error = getString(R.string.error_input_name)
            else
                tilTitle.error = null
        }

        shopItemViewModel.errorInputCount.observe(viewLifecycleOwner) {
            if (it)
                tilCount.error = getString(R.string.error_input_count)
            else
                tilCount.error = null
        }
    }


    companion object {

        fun newInstanceAddItem(): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }
        }

        fun newInstanceEditItem(shopItemId: Int): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(SHOP_ITEM_ID, shopItemId)
                }
            }
        }

        private const val SCREEN_MODE = "extra_mode"
        private const val SHOP_ITEM_ID = "item_id"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_UNKNOWN = ""
    }

    private fun initViews(view: View) {
        tilTitle = view.findViewById(R.id.tilTitle)
        tilCount = view.findViewById(R.id.tilCount)
        etItemTitle = view.findViewById(R.id.etItemTitle)
        etItemCount = view.findViewById(R.id.etItemCount)
        btnSave = view.findViewById(R.id.btnSave)
    }

    interface OnEditingFinishedListener {
        fun onEditingFinished()
    }
}