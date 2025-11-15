package com.example.clubfco

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MenuPrincipalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_principal)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val usuario = intent.getStringExtra("usuario" ) ?: "usuario"
        val btnSalir = findViewById<Button>( R.id.btnSalir )
        val txtUsuario = findViewById<TextView>( R.id.txtUsuario )

        txtUsuario.setText( txtUsuario.text.toString() + usuario )

        btnSalir.setOnClickListener {
            // Vuelta a Inicio
            // Toast.makeText( this, "Boton Cancelar", Toast.LENGTH_SHORT ).show()
            val intent = Intent( this, MainActivity::class.java )
            startActivity( intent )
        }
    }
}