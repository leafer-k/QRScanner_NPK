package com.example.qrcodescanner

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

private const val ENDPOINT = "https://attendance-e88b.restdb.io/rest"
private const val CODES_URI = "/attend"
private const val CODE = "code"
private const val DATE = "date"
private const val TIME = "time"
private const val API_KEY = "14d881bc8b38acfa4d7bbdb834c123302b05c"
private const val ORG_CODE = 943020

class SendData : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val code = intent.getStringExtra("code");
        Log.d("INTENT", "INTENT EXTRA: $code")

        try {
            if (code != null && (code.toLong() / 1000000).toInt() == ORG_CODE) {
                Thread {
                    addCode(code)
                }.start()
            } else {
                Toast.makeText(applicationContext, "Wrong code", Toast.LENGTH_SHORT).show()
            }
        }
        catch (exception : Exception){
            Toast.makeText(applicationContext, "Wrong QR", Toast.LENGTH_SHORT).show()
        }

        finish()
    }

    @WorkerThread
    fun addCode(intentCode: String) {
        val sdf = SimpleDateFormat("dd.M.yyyy")
        val stf = SimpleDateFormat("HH:mm:ss")

        val currentDate = sdf.format(Date())
        val currentTime = stf.format(Date())


        val httpUrlConnection = URL(ENDPOINT + CODES_URI).openConnection() as HttpURLConnection
        val body = JSONObject().apply {
            put(DATE, currentDate)
            put(TIME, currentTime)
            put(CODE, intentCode)
        }

        httpUrlConnection.apply {
            connectTimeout = 10000
            requestMethod = "POST"
            doOutput = true
            setRequestProperty("Content-Type", "application/json")
            setRequestProperty("X-API-Key", API_KEY)
        }

        Log.d("JSON", body.toString())

        OutputStreamWriter(httpUrlConnection.outputStream).use {
            it.write(body.toString())
        }
        Log.d("HTTP", httpUrlConnection.responseMessage)
        httpUrlConnection.disconnect()
    }

}