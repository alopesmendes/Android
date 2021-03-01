package fr.umlv.test_confroid

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_display_config.*
import java.lang.StringBuilder


class ConfigActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_config)

        val intent = intent
        if (intent != null) {
            var config: Config
            if (intent.hasExtra("config")) {
                config = intent.extras?.get("config") as Config
                AppNameConfig.text = config.app
                textVersionConfig.text = config.version.toString()
                textTagConfig.text = config.tag
                var content:String = config.content
                textContent.text = content
                Log.i("JSON", toListField(toList(content)).toString())
            }
        }
    }

    fun firstField(content: String): Pair<String,String> {
        var i=0
        var nb_rec = 0
        while (i<content.length){
            if (content.get(i)=='{'){
                nb_rec++
            }else if (content.get(i)=='}'){
                nb_rec--
            }else if (content.get(i)==',' && (nb_rec==0)){
                return Pair(content.subSequence(0,i).toString(),content.subSequence(i+1,content.length).toString())
            }
            i++
        }
        return Pair(content, "")
    }

    fun toList(content: String):List<String>{
        var test = content.subSequence(1,content.length-1).toString()
        var pair = firstField(test)
        var res = arrayListOf(pair.first)
        var bool = pair.second
        while (bool.length!=0){
            pair = firstField(bool)
            res.add(pair.first)
            Log.i("nouvelle entree",pair.first)
            Log.i("reste a check",pair.second)
            bool = pair.second
        }
        return res
    }

    fun toListField(strs: List<String>): List<Field>{
        var lst = arrayListOf<Field>()
        for(str in strs){
            var field = str.substring(0,str.indexOf("="))
            var content = str.substring(str.indexOf("=")+1)
            Log.i("field",field )
            Log.i("content", content)
            var testList = toList(content)
            if (testList.size==1){
                lst.add(Field(field,content, null))
            }else{
                lst.add(Field(field,null,toListField(testList)))
            }

        }
        return lst
    }
}