package com.shv.android.shopinglist.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.shv.android.shopinglist.data.ShopListRepositoryImpl
import com.shv.android.shopinglist.domain.DeleteShopItemUseCase
import com.shv.android.shopinglist.domain.EditShopItemUseCase
import com.shv.android.shopinglist.domain.GetShopItemListUseCase
import com.shv.android.shopinglist.domain.ShopItem

class MainViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = ShopListRepositoryImpl(application)

    private val getShopItemListUseCase = GetShopItemListUseCase(repository)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    val shopList = getShopItemListUseCase.getShopItemList()

    fun deleteShopItem(shopItem: ShopItem) {
        deleteShopItemUseCase.deleteShopItem(shopItem)
    }

    fun editShopItem(shopItem: ShopItem) {
        val newShopItem = shopItem.copy(enabled = !shopItem.enabled)
        editShopItemUseCase.editShopItem(newShopItem)
    }
}