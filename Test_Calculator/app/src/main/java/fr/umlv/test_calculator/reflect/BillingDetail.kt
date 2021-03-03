package fr.umlv.test_calculator.reflect

data class BillingDetail(
    var cardHolder: String,
    var cardNumber: String,
    var expirationMonth: Int,
    var expirationYear: Int,
    var cryptogram: Int
)
