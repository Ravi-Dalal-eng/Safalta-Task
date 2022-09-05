package com.messaging.safaltatask.utils

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection

class DownloadUrl {
    fun getUrl(url:String):String {
        var urlData=""
        var httpURLConnection: URLConnection?=null
        var inputStream: InputStream? =null
        try {
            val getUrl= URL(url)

            httpURLConnection=getUrl.openConnection() as HttpURLConnection
            httpURLConnection.connect()
            inputStream=httpURLConnection.getInputStream()
            val bufferedReader=BufferedReader(InputStreamReader(inputStream))
            val stringBuffer=StringBuffer()
            var line:String? = ""
            line=bufferedReader.readLine()
            while (line!=null){
                stringBuffer.append(line)
                line=bufferedReader.readLine()
            }
          urlData=stringBuffer.toString()
            bufferedReader.close()
        }
        catch (e:Exception){

        }
        finally {
            inputStream?.close()
        }

        return urlData
    }
}