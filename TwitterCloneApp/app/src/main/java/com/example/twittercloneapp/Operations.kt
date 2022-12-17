package com.example.twittercloneapp

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class Operations {

    fun convertStreamToString(inputStream: InputStream):String{

        val bufferReader = BufferedReader(InputStreamReader(inputStream))
        var line:String?
        var allString =""
        try {
            do{
                line  = bufferReader.readLine()
                if(line!=null){
                    allString += line
                }
            }while(line!=null)
            inputStream.close()
        }catch (ex: java.lang.Exception){}

        return allString
    }

}