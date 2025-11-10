package com.example.clubfco

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val txtUsuario : EditText = findViewById<EditText>( R.id.txtUsuario)
        val txtClave : EditText = findViewById<EditText>( R.id.txtClave)
        val btnCancelar : Button = findViewById<Button>( R.id.btnCancelar)

        // Credenciales, por ahora hardcodeadas
        val strNombreUsuario = "Admin"
        val strClaveUsuario = "123456"
        btnCancelar.setOnClickListener{
            Toast.makeText( this, "Boton Cancelar", Toast.LENGTH_SHORT ).show()
            // Pongo vacías a txtUsuario y txtContraseña
            // y el foco en txtContraseña
            txtClave.setText("")
            txtUsuario.setText("")
            txtUsuario.requestFocus()
        }

        val btnIniciarSesion : Button = findViewById<Button>( R.id.btnIniciarSesion)
        btnIniciarSesion.setOnClickListener{
            Toast.makeText( this, "Inicio Ok", Toast.LENGTH_SHORT ).show()
            // Verificar credenciales
            if ( txtUsuario.text.toString() == strNombreUsuario && txtClave.text.toString() == strClaveUsuario ) {
                // Enviar a activity menu
                val intent = Intent( this, menuActivity::class.java )
            }
            else {
                txtClave.setText("")
                txtUsuario.requestFocus()
            }
        }
    }
}
