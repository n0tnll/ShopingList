package com.shv.android.shopinglist.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import com.shv.android.shopinglist.R
import com.shv.android.shopinglist.databinding.ShopItemDisabledBinding
import com.shv.android.shopinglist.databinding.ShopItemEnabledBinding
import com.shv.android.shopinglist.domain.ShopItem

class ShopListAdapter :
    ListAdapter<ShopItem, ShopItemViewHolder>(ShopItemDiffCallback()) {

    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null
    var onShopItemClickListener: ((ShopItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val layoutId = when (viewType) {
            ITEM_STATE_ENABLED -> R.layout.shop_item_enabled
            ITEM_STATE_DISABLED -> R.layout.shop_item_disabled
            else -> throw RuntimeException("Unknown viewType:  $viewType")
        }
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            layoutId,
            parent,
            false
        )
        return ShopItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val shopItem = getItem(position)
        val binding = holder.binding
        binding.root.setOnLongClickListener {
            onShopItemLongClickListener?.invoke(shopItem)
            true
        }
        binding.root.setOnClickListener {
            onShopItemClickListener?.invoke(shopItem)
        }
        when (binding) {
            is ShopItemDisabledBinding -> {
                binding.tvShopItem.text = shopItem.title
                binding.tvItemCount.text = shopItem.count.toString()
            }
            is ShopItemEnabledBinding -> {
                binding.tvShopItem.text = shopItem.title
                binding.tvItemCount.text = shopItem.count.toString()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).enabled)
            ITEM_STATE_ENABLED
        else
            ITEM_STATE_DISABLED
    }

    companion object {
        const val ITEM_STATE_ENABLED = 100
        const val ITEM_STATE_DISABLED = 101

        const val MAX_POOL_SIZE = 30
    }
}