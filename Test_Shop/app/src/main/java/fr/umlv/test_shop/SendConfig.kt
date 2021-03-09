package fr.umlv.test_shop

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import fr.umlv.test_shop.config.BillingDetail
import fr.umlv.test_shop.config.ShippingAddress
import fr.umlv.test_shop.config.ShoppingInfo
import fr.umlv.test_shop.config.ShoppingPreferences
import fr.umlv.test_shop.utils.ConfroidUtils
import kotlinx.android.synthetic.main.activity_send_config.*
import kotlinx.android.synthetic.main.activity_send_config.version_edit_text

class SendConfig : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_config)

        /////////////////////////////////////////////////////////

        val prefs = ShoppingPreferences(mutableMapOf())

        add_address_button.setOnClickListener {
            val address = ShippingAddress(
                name_et.text.toString(),
                street_et.text.toString(),
                city_et.text.toString(),
                country_et.text.toString()
            )
            val billingDetail = BillingDetail(
                card_holder_et.text.toString(),
                card_number_et.text.toString(),
                month_et.text.toString().toInt(),
                year_et.text.toString().toInt(),
                cryptogram_et.text.toString().toInt()
            )
            val shopInfo = ShoppingInfo(address, billingDetail, favorite_cb.isChecked)
            prefs.shoppingInfos[info_title_et.text.toString()] = shopInfo
        }

        show_config_button.setOnClickListener {
            val fields = ConfroidUtils.deepGetFields(mutableMapOf(), prefs)
            val content = ConfroidUtils.convertToBundle(fields)
            config_text_view.text = content.toString()
        }

        /////////////////////////////////////////////////////////

        send_to_confroid_button.setOnClickListener {
            val version = version_edit_text.text.toString()
            if (version.isBlank()) {
                Toast.makeText(this, "version required", Toast.LENGTH_SHORT).show()
            } else {
                ConfroidUtils.saveConfiguration(this, "Shop", prefs, version)
            }
        }
    }
}