package com.shv.android.shopinglist.domain

import androidx.lifecycle.LiveData

class GetShopItemListUseCase(private val shopItemRepository: ShopListRepository) {

    fun getShopItemList(): LiveData<List<ShopItem>> {
        return shopItemRepository.getShopItemList()
    }
}