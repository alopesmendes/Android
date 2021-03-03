package fr.umlv.test_calculator

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import fr.umlv.test_calculator.reflect.BillingDetail
import fr.umlv.test_calculator.reflect.ShippingAddress
import fr.umlv.test_calculator.reflect.ShoppingInfo
import fr.umlv.test_calculator.reflect.ShoppingPreferences
import fr.umlv.test_calculator.utils.ConfroidUtils
import kotlinx.android.synthetic.main.activity_send_config.*
import kotlinx.android.synthetic.main.activity_send_config.version_edit_text

class SendConfig : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_config)

        /////////////////////////////////////////////////////////

        val prefs = ShoppingPreferences(mutableMapOf())
        val address1 = ShippingAddress("Bugdroid", "Bd Descartes", "Champs-sur-Marne", "France")
        val address2 =
            ShippingAddress("Bugdroid", "Rue des tartes au nougat", "Lollipop City", "Oreo Country")
        val billingDetail = BillingDetail(
            "Bugdroid",
            "123456789",
            12,
            2021,
            123
        )
        prefs.shoppingInfos["home"] = ShoppingInfo(address1, billingDetail, true)
        prefs.shoppingInfos["work"] = ShoppingInfo(address2, billingDetail, false)

        val fields = ConfroidUtils.deepGetFields(mutableMapOf(), prefs)
        val content = ConfroidUtils.convertToBundle(fields)

        show_config_button.setOnClickListener {
            config_text_view.text = content.toString()
        }

        /////////////////////////////////////////////////////////

        send_to_confroid_button.setOnClickListener {
            val version = version_edit_text.text.toString()
            if (version.isBlank()) {
                Toast.makeText(this, "version required", Toast.LENGTH_SHORT).show()
            } else {
                ConfroidUtils.saveConfiguration(this, "Calculator", prefs, version)
            }
        }
    }
}