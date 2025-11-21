package com.example.clubfco

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class SociosActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_socios)

        dbHelper = DBHelper(this)

        val btnAltaSocio = findViewById<Button>(R.id.btnAltaSocio)
        val btnBajaSocio = findViewById<Button>(R.id.btnBajaSocio)
        val btnListaSocios = findViewById<Button>(R.id.btnListaSocios)
        val btnSalir = findViewById<Button>(R.id.btnSalir)

        // Alta Socio
        btnAltaSocio.setOnClickListener {
            val inputNombre = EditText(this).apply { hint = "Nombre" }
            val inputApellido = EditText(this).apply { hint = "Apellido" }
            val inputDni = EditText(this).apply { hint = "DNI" }

            val checkHabilitar = CheckBox(this).apply {
                text = "Confirmar alta de socio"
            }

            val layout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(50, 20, 50, 20)
                addView(inputNombre)
                addView(inputApellido)
                addView(inputDni)
                addView(checkHabilitar)
            }

            AlertDialog.Builder(this)
                .setTitle("Alta Socio")
                .setView(layout)
                .setPositiveButton("Guardar") { _, _ ->
                    val nombre = inputNombre.text.toString().trim()
                    val apellido = inputApellido.text.toString().trim()
                    val dni = inputDni.text.toString().trim()
                    val montoCuota = 100.0 // Valor fijo

                    if (nombre.isNotEmpty() && apellido.isNotEmpty() && dni.isNotEmpty() && checkHabilitar.isChecked) {
                        dbHelper.insertarSocio(nombre, apellido, dni, montoCuota)
                        Toast.makeText(this, "Socio guardado correctamente", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(
                            this,
                            "Completa los campos y confirma el alta",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

        // Baja Socio
        btnBajaSocio.setOnClickListener {
            val inputDni = EditText(this).apply { hint = "DNI del socio a eliminar" }

            val layout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(50, 20, 50, 20)
                addView(inputDni)
            }

            AlertDialog.Builder(this)
                .setTitle("Baja Socio")
                .setView(layout)
                .setPositiveButton("Eliminar") { _, _ ->
                    val dni = inputDni.text.toString().trim()
                    if (dni.isNotEmpty()) {
                        dbHelper.eliminarSocio(dni)
                        Toast.makeText(this, "Socio eliminado", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

        // Lista Socios
        btnListaSocios.setOnClickListener {
            startActivity(Intent(this, ListaSociosActivity::class.java))
        }
        //Socios vencen

        val btnListaVenceHoy = findViewById<Button>(R.id.btnListaVenceHoy)
        btnListaVenceHoy.setOnClickListener {
            startActivity(Intent(this, SociosVencimientoHoyActivity::class.java))
            //Carga Socios
            val btnCargarSociosPrueba = findViewById<Button>(R.id.btnCargarSociosPrueba)
            btnCargarSociosPrueba.setOnClickListener {
                dbHelper.poblarSociosDePrueba()
                Toast.makeText(this, "Se cargaron 93 socios de prueba", Toast.LENGTH_SHORT).show()
            }

            // Salir
            btnSalir.setOnClickListener {
                finish()
            }
        }

    }
}