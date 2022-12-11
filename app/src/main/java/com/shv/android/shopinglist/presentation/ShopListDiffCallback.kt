package com.shv.android.shopinglist.presentation

import androidx.recyclerview.widget.DiffUtil
import com.shv.android.shopinglist.domain.ShopItem

class ShopListDiffCallback(
    private val oldList: List<ShopItem>,
    private val newList: List<ShopItem>,
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItemId = oldList[oldItemPosition].id
        val newItemId = newList[newItemPosition].id
        return oldItemId == newItemId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem == newItem
    }
}