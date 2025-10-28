package com.example.clubfco

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val txtUsuario : EditText = findViewById<EditText>( R.id.txtUsuario)
        val txtContraseña : EditText = findViewById<EditText>( R.id.txtContraseña)
        val btnCancelar : Button = findViewById<Button>( R.id.btnCancelar)
        btnCancelar.setOnClickListener{
            Toast.makeText( this, "Boton Cancelar", Toast.LENGTH_SHORT ).show()
            // Pongo vacias a txtUsuario y txtContraseña
            // y el foco en txtContraseña
            txtContraseña.setText("")
            txtUsuario.setText("")
            txtUsuario.requestFocus()
        }

        val btnIniciarSesion : Button = findViewById<Button>( R.id.btnIniciarSesion)
        btnCancelar.setOnClickListener{
            Toast.makeText( this, "Inicio Ok", Toast.LENGTH_SHORT ).show()
            // Verificar credenciales
            // Enviar a activity menu
        }
    }
}
