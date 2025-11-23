package com.example.clubfco

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    var iContador = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val txtUsuario = findViewById<EditText>( R.id.txtUsuario)
        val txtClave = findViewById<EditText>( R.id.txtClave)
        val btnCancelar = findViewById<Button>( R.id.btnCancelar)
        val iLimiteIntentos = 5

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
            if ( txtUsuario.text.isEmpty() || txtClave.text.isEmpty() ) {
                // Nada haremos!
                if ( txtUsuario.text.isEmpty()  ) {
                    txtUsuario.requestFocus()
                }
                else {
                    txtClave.requestFocus()
                }
            }
            else if ( txtUsuario.text.toString() == strNombreUsuario && txtClave.text.toString() == strClaveUsuario ) {
                iContador = 0
                Toast.makeText( this, "Inicio Ok, Usuario: " + txtUsuario.text.toString(), Toast.LENGTH_SHORT ).show()
                // Enviar a activity menu
                //val intent = Intent( this, MenuPrincipalActivity::class.java )
                val intent = Intent( this, MenuPrincipalActivity::class.java )
                intent.putExtra("Usuario", txtUsuario.text.toString())
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

        val btnSalir = findViewById<Button>( R.id.btnSalir )
        btnSalir.setOnClickListener {
            // Salir de la aplicación
            finishAffinity()
        }


    }
}
