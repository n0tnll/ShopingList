package com.shv.android.shopinglist.presentation

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shv.android.shopinglist.R

class ShopItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvShopItem = itemView.findViewById<TextView>(R.id.tvShopItem)
    val tvItemCount = itemView.findViewById<TextView>(R.id.tvItemCount)
}