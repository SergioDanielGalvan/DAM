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

        val txtUsuario = findViewById<EditText>( R.id.txtUsuario)
        val txtClave = findViewById<EditText>( R.id.txtClave)
        val btnCancelar = findViewById<Button>( R.id.btnCancelar)
        val iLimiteIntentos = 5
        var iContador : Int = 0

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

        val btnIniciarSesion = findViewById<Button>( R.id.btnIniciarSesion )
        btnIniciarSesion.setOnClickListener{
            // Verificar credenciales
            if ( txtUsuario.text.toString() == strNombreUsuario && txtClave.text.toString() == strClaveUsuario ) {
                iContador = 0
                Toast.makeText( this, "Inicio Ok", Toast.LENGTH_SHORT ).show()
                // Enviar a activity menu
                val intent = Intent( this, menuActivity::class.java )
                startActivity( intent )
            }
            else {
                iContador++
                Toast.makeText( this, "Fallo Login", Toast.LENGTH_SHORT ).show()
                if ( iContador > iLimiteIntentos ) {
                    finishAffinity()
                }
                txtClave.setText("")
                txtUsuario.requestFocus()
            }
        }
    }
}
