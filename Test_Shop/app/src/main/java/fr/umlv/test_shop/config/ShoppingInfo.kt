package fr.umlv.test_shop.config

data class ShoppingInfo(
    var address: ShippingAddress,
    var billing: BillingDetail,
    var favorite: Boolean
)
