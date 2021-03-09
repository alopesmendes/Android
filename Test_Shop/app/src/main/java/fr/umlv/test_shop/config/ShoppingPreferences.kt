package fr.umlv.test_shop.config

import fr.umlv.test_shop.config.ShoppingInfo

data class ShoppingPreferences(var shoppingInfos: MutableMap<String, ShoppingInfo>)
