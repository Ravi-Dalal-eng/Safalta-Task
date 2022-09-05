package com.messaging.safaltatask.utils

import android.os.AsyncTask
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.messaging.safaltatask.ShowHospitalActivity
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class FetchData: AsyncTask<Array<Any>, String, String>() {
    private lateinit var nearByHospital:String
    private lateinit var googleMap:GoogleMap
    private lateinit var url:String
    override fun doInBackground( vararg p0: Array<Any>): String {
        try {

            googleMap= p0[0].get(0) as GoogleMap
            url= p0[0].get(1) as String
            val downloadUrl=DownloadUrl()
            nearByHospital=downloadUrl.getUrl(url)
        }
        catch (e:IOException){}
        return nearByHospital
    }

    override fun onPostExecute(result: String?) {
        try {
            val jsonObject=JSONObject(result)
            val jsonArray=jsonObject.getJSONArray("results")
            for (i in 0..jsonArray.length()-1){
                val jsonObject1=jsonArray.getJSONObject(i)
                val getLocation=jsonObject1.getJSONObject("geometry").getJSONObject("location")
                val lat=getLocation.getString("lat")
                val lng=getLocation.getString("lng")
                val getName=jsonArray.getJSONObject(i)
                val name=getName.getString("name")
                val latLng=LatLng(lat.toDouble(),lng.toDouble())
                val markerOptions=MarkerOptions()
                markerOptions.title(name)
                markerOptions.position(latLng)
                googleMap.addMarker(markerOptions)
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,13f))
            }
        }
        catch (e : JSONException){
            e.printStackTrace()
        }
    }

}