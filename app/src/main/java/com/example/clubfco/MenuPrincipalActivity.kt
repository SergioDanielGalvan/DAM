package com.example.clubfco

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.appcompat.app.AlertDialog

class MenuPrincipalActivity : AppCompatActivity() {
    @SuppressLint("StringFormatInvalid")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_principal)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnSalir = findViewById<Button>(R.id.btnSalir)
        val btnSocios = findViewById<Button>(R.id.btnSocios)
        val btnNoSocios = findViewById<Button>(R.id.btnNoSocios)
        //val btnListarMorosos = findViewById<Button>(R.id.btnListaVenceHoy)

        btnSocios.setOnClickListener {
            val intent = Intent(this, SociosActivity::class.java)
            startActivity(intent)

        }

        btnNoSocios.setOnClickListener {
            val intent = Intent(this, PagoNoSociosActivity::class.java)
            startActivity(intent)
        }

        val nombreUsuario = intent.getStringExtra("Usuario") ?: "usuario"
        val txtUsuario = findViewById<TextView>(R.id.Usuario)
        txtUsuario.text = "Bienvenido: $nombreUsuario"
        //txtUsuario.text = getString(R.string.bienvenido_usuario, nombreUsuario )
        btnSalir.setOnClickListener {
            AlertDialog.Builder(this)
            .setTitle("Cerrar Sesión")
            .setMessage("¿Estás seguro de que deseas salir?")
                .setPositiveButton("Si") { _, _ ->
                //finishAffinity()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            .setNegativeButton("No", null)
            .show()

        }
    }
}
