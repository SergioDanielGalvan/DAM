package com.example.clubfco

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PagosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pagos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnSocios = findViewById<Button>(R.id.btnSocios)
        btnSocios.setOnClickListener {
            val intent = Intent(this, PagoSociosActivity::class.java)
            startActivity(intent)

        }

        val btnNoSocios = findViewById<Button>(R.id.btnNoSocios)
        btnNoSocios.setOnClickListener {
            val intent = Intent(this, PagoNoSociosActivity::class.java)
            startActivity(intent)
        }

        val btnSalir = findViewById<Button>(R.id.btnSalir)

        btnSalir.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Cerrar Sesión")
                .setMessage("¿Estás seguro de que deseas salir?")
                .setPositiveButton("Si") { _, _ ->
                    finishAffinity()
                }
                .setNegativeButton("No", null)
                .show()
        }
    }
}
