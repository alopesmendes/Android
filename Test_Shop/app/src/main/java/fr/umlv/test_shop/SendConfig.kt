package fr.umlv.test_shop

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import fr.umlv.confroidutils.ConfroidUtils
import fr.umlv.test_shop.config.BillingDetail
import fr.umlv.test_shop.config.ShippingAddress
import fr.umlv.test_shop.config.ShoppingInfo
import fr.umlv.test_shop.config.ShoppingPreferences
import kotlinx.android.synthetic.main.activity_send_config.*

class SendConfig : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_config)

        val prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE)

        /////////////////////////////////////////////////////////

        val shopPrefs = ShoppingPreferences(mutableMapOf())

        add_address_button.setOnClickListener {
            val name = name_et.text.toString()
            val street = street_et.text.toString()
            val city = city_et.text.toString()
            val country = country_et.text.toString()
            val card_holder = card_holder_et.text.toString()
            val card_number = card_number_et.text.toString()
            val month = month_et.text.toString()
            val year = year_et.text.toString()
            val cryptogram = cryptogram_et.text.toString()
            val label = info_title_et.text.toString()

            if (name.isBlank() || street.isBlank() || city.isBlank() || country.isBlank() || card_holder.isBlank() || card_number.isBlank() || month.isBlank() || year.isBlank() || cryptogram.isBlank() || label.isBlank()) {
                Toast.makeText(this, "please fill the fields", Toast.LENGTH_SHORT).show()
            } else {
                val address = ShippingAddress(
                    name,
                    street,
                    city,
                    country
                )

                val billingDetail = BillingDetail(
                    card_holder,
                    card_number,
                    month.toInt(),
                    year.toInt(),
                    cryptogram.toInt()
                )
                val shopInfo = ShoppingInfo(address, billingDetail, favorite_cb.isChecked)
                shopPrefs.shoppingInfos[label] = shopInfo
            }
        }

        show_config_button.setOnClickListener {
            val fields = ConfroidUtils.deepGetFields(mutableMapOf(), shopPrefs)
            val content = ConfroidUtils.convertToBundle(fields)
            config_text_view.text = content.toString()
        }

        /////////////////////////////////////////////////////////

        send_to_confroid_button.setOnClickListener {
            val version = version_edit_text.text.toString()
            if (version.isBlank() || shopPrefs.shoppingInfos.isEmpty()) {
                Toast.makeText(this, "please fill the fields", Toast.LENGTH_SHORT).show()
            } else {
                ConfroidUtils.saveConfiguration(this, "Shop", shopPrefs, version)
                prefs.edit().putLong("request", ConfroidUtils.REQUEST).apply()
            }
        }
    }
}