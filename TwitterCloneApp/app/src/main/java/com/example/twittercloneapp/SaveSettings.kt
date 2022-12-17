package com.example.twittercloneapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences

class SaveSettings {

    var context:Context?=null
    var sharedPreferences:SharedPreferences?=null

    constructor(context: Context){
        this.context=context
        sharedPreferences=context.getSharedPreferences("myRef",Context.MODE_PRIVATE)
    }

    fun saveSettings(userID:String){
        val editor = sharedPreferences!!.edit()
        editor.putString("userID",userID)
        editor.commit()
    }

    fun loadSettings(){
        userID = sharedPreferences!!.getString("userID","0")

        if(userID=="0"){
            val intent = Intent(context,Login::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context!!.startActivity(intent)
        }
    }

    companion object{
        var userID:String?=null
    }
}