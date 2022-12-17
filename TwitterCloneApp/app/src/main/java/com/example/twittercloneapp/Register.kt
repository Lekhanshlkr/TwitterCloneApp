package com.example.twittercloneapp

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*

class Register : AppCompatActivity() {

    var mAuth:FirebaseAuth?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth  = FirebaseAuth.getInstance()
        signInAnonymously()

        ivUserImg.setOnClickListener {
            checkPermission()
        }
    }

    fun buRegisterEvent(view: View) {
        // register new user
        buRegister.isEnabled=false
        saveImageInFirebase()
    }

    fun signInAnonymously(){
        mAuth!!.signInAnonymously().addOnCompleteListener(this) { task ->
            Log.d("LoginInfo: ", task.isSuccessful.toString())
        }
    }

    val READIMAGE:Int=253
    fun checkPermission(){

        if(Build.VERSION.SDK_INT>=29){
            if(ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)!=
                PackageManager.PERMISSION_GRANTED){

                requestPermissions(arrayOf( android.Manifest.permission.READ_EXTERNAL_STORAGE),READIMAGE)
                return
            }
        }

        loadImage()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        when(requestCode){
            READIMAGE->{
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    loadImage()
                }else{
                    Toast.makeText(applicationContext,"Cannot access your images",Toast.LENGTH_LONG).show()
                }
            }
            else-> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    val PICK_IMAGE_CODE=123
    fun loadImage(){

        var intent= Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent,PICK_IMAGE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(requestCode==PICK_IMAGE_CODE  && data!=null && resultCode != RESULT_CANCELED){

            val selectedImage=data.data
            val filePathColum= arrayOf(MediaStore.Images.Media.DATA)
            val cursor= contentResolver.query(selectedImage!!, filePathColum,null,null,null)
            cursor!!.moveToFirst()
            val columnIndex=cursor!!.getColumnIndex(filePathColum[0])
            val picturePath=cursor!!.getString(columnIndex)
            cursor!!.close()
            ivUserImg.setImageBitmap(BitmapFactory.decodeFile(picturePath))
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun saveImageInFirebase(){
        var currentUser =mAuth!!.currentUser
        val email:String=currentUser!!.email.toString()

        val storage= FirebaseStorage.getInstance()
        val storgaRef=storage.getReferenceFromUrl("gs://twittercloneapp-2ef88.appspot.com")
        val df= SimpleDateFormat("ddMMyyHHmmss")
        val dataobj= Date()

        val imagePath= SplitString(email) + "."+ df.format(dataobj)+ ".jpg"
        val ImageRef=storgaRef.child("images/"+imagePath )
        ivUserImg.isDrawingCacheEnabled=true
        ivUserImg.buildDrawingCache()
        val drawable=ivUserImg.drawable as BitmapDrawable
        val bitmap=drawable.bitmap
        val baos= ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos)
        val data= baos.toByteArray()

        var uploadTask=ImageRef.putBytes(data)
        uploadTask.addOnFailureListener{
            Toast.makeText(applicationContext,"fail to upload",Toast.LENGTH_LONG).show()
        }.addOnSuccessListener { taskSnapshot ->

           ImageRef.downloadUrl.addOnSuccessListener{task ->
               DownloadURL=task.toString()
               Log.d("Download URL:", DownloadURL!!)

               val name = URLEncoder.encode(etName.text.toString(),"utf-8")
               DownloadURL=URLEncoder.encode(DownloadURL!!,"utf-8")
               var etPasswordSend = etPasswordReg.text.toString()
               Log.d("Pasword:",etPasswordSend)
               val url="http://10.0.2.2:8080/TwitterAndroidServer/register.php?fulll_name=$name&email=${etEmailReg.text}&password=$etPasswordSend&picture_path=$DownloadURL"
               Log.d("PHP URL:",url)
               MyAsyncTask().execute(url)
           }
        }
    }

    fun SplitString(email:String):String{
        val split= email.split("@")
        return split[0]
    }

    companion object{
        var DownloadURL:String?=null
    }

    //cal http
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


                Toast.makeText(applicationContext,json.getString("msg"),Toast.LENGTH_LONG).show()

                if (json.getString("msg")== "user is added"){
                    finish()
                }else{
                    buRegister.isEnabled=true
                }

            }catch (ex:Exception){}
        }

        override fun onPostExecute(result: String?) {

            //after task done
        }

    }



}