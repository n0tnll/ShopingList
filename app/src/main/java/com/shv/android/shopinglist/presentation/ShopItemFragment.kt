package com.shv.android.shopinglist.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.shv.android.shopinglist.databinding.FragmentShopItemBinding
import com.shv.android.shopinglist.domain.ShopItem

class ShopItemFragment : Fragment() {

    private val shopItemViewModel: ShopItemViewModel by lazy {
        ViewModelProvider(this)[ShopItemViewModel::class.java]
    }

    private var screenMode: String = MODE_UNKNOWN
    private var shopItemId: Int = ShopItem.UNDEFINED_ID

    private var _binding: FragmentShopItemBinding? = null
    private val binding: FragmentShopItemBinding
        get() = _binding ?: throw RuntimeException("FragmentShopItemBinding == null")

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
    ): View {
        _binding = FragmentShopItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = shopItemViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
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
        with(binding) {
            btnSave.setOnClickListener {
                val name = etItemTitle.text.toString()
                val count = etItemCount.text.toString()
                shopItemViewModel.editShopItem(name, count)
                closeScreen()
            }
        }
    }

    private fun launchAddMode() {
        with(binding) {
            btnSave.setOnClickListener {
                val name = etItemTitle.text.toString()
                val count = etItemCount.text.toString()
                shopItemViewModel.addShopItem(name, count)
                closeScreen()
            }
        }
    }

    private fun closeScreen() {
        shopItemViewModel.isComplete.observe(viewLifecycleOwner) {
            onEditingFinishedListener.onEditingFinished()
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

    interface OnEditingFinishedListener {
        fun onEditingFinished()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}