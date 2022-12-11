package com.shv.android.shopinglist.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.shv.android.shopinglist.R
import com.shv.android.shopinglist.domain.ShopItem

class ShopListAdapter :
    ListAdapter<ShopItem, ShopItemViewHolder>(ShopItemDiffCallback()) {

    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null
    var onShopItemClickListener: ((ShopItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val layoutId = when (viewType) {
            ITEM_STATE_ENABLED -> R.layout.shop_item_enabled
            ITEM_STATE_DISABLED -> R.layout.shop_item_disabled
            else -> throw RuntimeException("Unknown viewType: $viewType")
        }
        val view = LayoutInflater.from(parent.context).inflate(
            layoutId,
            parent,
            false
        )
        return ShopItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val shopItem = getItem(position)
        holder.itemView.setOnLongClickListener {
            onShopItemLongClickListener?.invoke(shopItem)
            true
        }
        holder.itemView.setOnClickListener {
            onShopItemClickListener?.invoke(shopItem)
        }
        holder.tvShopItem.text = shopItem.title
        holder.tvItemCount.text = shopItem.count.toString()
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