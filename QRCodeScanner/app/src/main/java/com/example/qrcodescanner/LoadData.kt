package com.example.qrcodescanner

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import java.io.InputStreamReader
import java.lang.Exception
import java.net.ConnectException
import java.net.HttpRetryException
import java.net.HttpURLConnection
import java.net.URL
import java.text.DecimalFormat

private const val ENDPOINT = "https://attendance-e88b.restdb.io/rest"
private const val API_KEY = "14d881bc8b38acfa4d7bbdb834c123302b05c"
private const val CODES_URI = "/attend"
private const val CODE = "code"
private const val DATE = "date"
private const val TIME = "time"
private val df = DecimalFormat("000000")



class LoadData : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browse)
        Thread {
            getCodesAndShow()
        }.start()


    }

    @SuppressLint("SetTextI18n")
    @WorkerThread
    fun getCodesAndShow() {
        val textView: TextView = findViewById(R.id.outputTV)
        val httpUrlConnection: HttpURLConnection

        try {
            httpUrlConnection = URL(ENDPOINT + CODES_URI)
                .openConnection() as HttpURLConnection
            httpUrlConnection.apply {
                setRequestProperty("X-API-Key", API_KEY)
                connectTimeout = 10000
                requestMethod = "GET"
                doInput = true
            }
        } catch (exception: Exception) {
            Log.e("CONNECTION", "Connection failed! \n ${exception.message}")
            return
        }


        try {
            if (httpUrlConnection.responseCode != HttpURLConnection.HTTP_OK) {
                Log.e("CONNECTION", "Connection error!")
                return
            }
        } catch (exception : Exception){
            Log.e("CONNECTION", "Connection failed! \n ${exception.message}")
            return
        }



        val streamReader = InputStreamReader(httpUrlConnection.inputStream)
        var text: String = ""
        streamReader.use {
            text = it.readText()
        }

        val codes = mutableListOf<Pupil>()
        val json = JSONArray(text)

        for (i in 0 until json.length()) {
            val jsonCode = json.getJSONObject(i)
            val pupil =
                Pupil(jsonCode.getLong(CODE)%1000000, jsonCode.getString(DATE), jsonCode.getString(TIME))
            codes.add(pupil)
        }
        httpUrlConnection.disconnect()

        Handler(Looper.getMainLooper()).post {
            for (i in 0 until codes.size) {
                textView.text =
                    "" + textView.text + "\n " + df.format(codes[i].code.toInt()) + " | " + codes.get(i).date + " | " + codes.get(
                        i
                    ).time
            }
        }
    }

}