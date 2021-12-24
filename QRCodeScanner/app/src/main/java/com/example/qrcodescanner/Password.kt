package com.example.qrcodescanner

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Password : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)

        val okButton: Button = findViewById(R.id.button)
        val passET : EditText = findViewById(R.id.editTextPassword)
        val mainIntent = Intent(this, MainActivity::class.java)

        okButton.setOnClickListener {
            if(passET.text.toString() == getString(R.string.pass)){
                startActivity(mainIntent)
                finish()
            }
            else{
                Toast.makeText(applicationContext, "Неверный пароль!", Toast.LENGTH_SHORT).show()
            }
        }

    }
}