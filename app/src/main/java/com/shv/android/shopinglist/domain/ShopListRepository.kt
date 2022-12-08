package com.shv.android.shopinglist.domain

interface ShopListRepository {

    fun addShopItem(shopItem: ShopItem)

    fun deleteShopItem(shopItem: ShopItem)

    fun editShopItem(shopItem: ShopItem)

    fun getShopItemUseCase(shopItemId: Int): ShopItem

    fun getShopList(): List<ShopItem>
}