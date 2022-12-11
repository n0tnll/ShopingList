package com.shv.android.shopinglist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shv.android.shopinglist.domain.ShopItem
import com.shv.android.shopinglist.domain.ShopListRepository
import kotlin.random.Random

object ShopListRepositoryImpl : ShopListRepository {

    private val shopListLD = MutableLiveData<List<ShopItem>>()
    val shopList = sortedSetOf<ShopItem>({ o1, o2 -> o1.id.compareTo(o2.id) })

    private var autoincrementId = 0

    init {
        for (i in 0 until 1000) {
            val shopItem = ShopItem("Test $i", i, Random.nextBoolean())
            addShopItem(shopItem)
        }
    }

    override fun getShopItemList(): LiveData<List<ShopItem>> {
        return shopListLD
    }

    override fun getShopItem(shopItemId: Int): ShopItem {
        return shopList.find {
            it.id == shopItemId
        } ?: throw RuntimeException("Element with id{$shopItemId} not found.")
    }

    override fun addShopItem(shopItem: ShopItem) {
        if (shopItem.id == ShopItem.UNDEFINED_ID) {
            shopItem.id = autoincrementId++
        }
        shopList.add(shopItem)
        updateList()
    }

    override fun editShopItem(shopItem: ShopItem) {
        val oldItem = getShopItem(shopItem.id)
        shopList.remove(oldItem)
        addShopItem(shopItem)
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
        updateList()
    }

    private fun updateList() {
        shopListLD.value = shopList.toList()
    }
}