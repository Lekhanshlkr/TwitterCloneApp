package com.example.twittercloneapp

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun buLoginEvent(view: View) {
        //user login
        var etPasswordLog = etPassword.text.toString()
        Log.d("Pass at Login:",etPasswordLog)
        val url = "http://10.0.2.2:8080/TwitterAndroidServer/login.php?email=${etEmail.text}&password=$etPasswordLog"
        MyAsyncTask().execute(url)
    }

    fun buRegisterEvent(view: View) {
        // send to register activity
        val intent = Intent(this,Register::class.java)
        startActivity(intent)
    }

    inner class MyAsyncTask: AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            //Before task started
        }
        override fun doInBackground(vararg p0: String?): String {
            try {

                val url= URL(p0[0])
                Log.d("URL to ASTASK: ",url.toString())

                val urlConnect=url.openConnection() as HttpURLConnection
                urlConnect.connectTimeout=7000

                val op=Operations()
                var inString= op.convertStreamToString(urlConnect.inputStream)
                //Cannot access to ui
                publishProgress(inString)
            }catch (ex:Exception){}


            return " "

        }

        override fun onProgressUpdate(vararg values: String?) {
            try{
                var json= JSONObject(values[0])


                if (json.getString("msg")== "pass login"){
                    val userInfo = JSONArray(json.getString("info"))
                    val userCredentials = userInfo.getJSONObject(0)
                    val user_id = userCredentials.getString("USER_ID")
                    Toast.makeText(applicationContext,userCredentials.getString("FULLL_NAME"),Toast.LENGTH_LONG).show()

                    var saveSettingsvar = SaveSettings(applicationContext)
                    saveSettingsvar.saveSettings(user_id)
                    finish()
                }else{
                    Toast.makeText(applicationContext,json.getString("msg"), Toast.LENGTH_LONG).show()
                }

            }catch (ex:Exception){}
        }

        override fun onPostExecute(result: String?) {

            //after task done
        }

    }

}