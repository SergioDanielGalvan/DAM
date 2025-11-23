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
        val btnListaVenceHoy = findViewById<Button>(R.id.btnListaVenceHoy)
        val btnCargarSociosPrueba = findViewById<Button>(R.id.btnCargarSociosPrueba)
        val btnPagoCuota = findViewById<Button>(R.id.btnPagoCuota)

        // Listener para Alta Socio
        btnAltaSocio.setOnClickListener {
            // ... tu código de alta (sin cambios)
            val inputNombre = EditText(this).apply { hint = "Nombre" }
            val inputApellido = EditText(this).apply { hint = "Apellido" }
            val inputDni = EditText(this).apply { hint = "DNI" }
            val checkHabilitar = CheckBox(this).apply { text = "Confirmar alta de socio" }

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
                    val montoCuota = 100.0

                    if (nombre.isNotEmpty() && apellido.isNotEmpty() && dni.isNotEmpty() && checkHabilitar.isChecked) {
                        dbHelper.insertarSocio(nombre, apellido, dni, montoCuota)
                        Toast.makeText(this, "Socio guardado correctamente", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Completa los campos y confirma el alta", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

        // Listener para Baja Socio
        btnBajaSocio.setOnClickListener {
            // ... tu código de baja (sin cambios)
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

        // --- Pago Cuota
        btnPagoCuota.setOnClickListener {
            val inputDni = EditText(this).apply { hint = "DNI del socio" }
            val checkPago = CheckBox(this).apply { text = "Confirmar pago" }

            val layout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(50, 20, 50, 20)
                addView(inputDni)
                addView(checkPago)
            }

            AlertDialog.Builder(this)
                .setTitle("Registrar Pago de Cuota")
                .setView(layout)
                .setPositiveButton("Registrar") { _, _ ->
                    val dni = inputDni.text.toString().trim()
                    if (dni.isNotEmpty() && checkPago.isChecked) {

                        // 1. Buscar al socio por DNI
                        val socioOriginal = dbHelper.obtenerSocios().find { it.dni == dni }

                        if (socioOriginal != null) {
                            // 2. Verificar si el socio realmente debe una cuota
                            if (socioOriginal.estadoCuota == "pendiente" || socioOriginal.estadoCuota == "vencida") {

                                // 3. Registrar el pago en la base de datos
                                dbHelper.registrarPagoPorNumero(socioOriginal.numeroSocio)


                                // 4. (¡EL PASO CLAVE!) Volver a obtener los datos ACTUALIZADOS del socio

                                val socioActualizado = dbHelper.obtenerSocios().find { it.dni == dni }

                                // 5. Preparar el Intent para abrir la actividad del recibo CON LOS DATOS NUEVOS
                                val intent = Intent(this, ReciboCuotaActivity::class.java).apply {
                                    putExtra("numeroSocio", socioActualizado?.numeroSocio ?: -1)
                                    putExtra("nombre", socioActualizado?.nombre)
                                    putExtra("apellido", socioActualizado?.apellido)
                                    putExtra("dni", socioActualizado?.dni)
                                    putExtra("montoCuota", socioActualizado?.montoCuota ?: 0.0)
                                    putExtra("fechaPago", socioActualizado?.fechaUltimoPago)
                                    putExtra("fechaVencimiento", socioActualizado?.fechaProximoVencimiento)
                                }

                                // 6. Iniciar la actividad del recibo
                                startActivity(intent)

                            } else {
                                Toast.makeText(this, "El socio está al día con sus pagos.", Toast.LENGTH_LONG).show()
                            }
                        } else {
                            Toast.makeText(this, "Socio no encontrado con el DNI proporcionado.", Toast.LENGTH_LONG).show()
                        }

                    } else {
                        Toast.makeText(this, "Por favor, ingrese el DNI y confirme el pago", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
        // --- FIN DE LA CORRECCIÓN ---

        // Listener para Lista de Socios
        btnListaSocios.setOnClickListener {
            startActivity(Intent(this, ListaSociosActivity::class.java))
        }

        // Listener para Socios que vencen hoy
        btnListaVenceHoy.setOnClickListener {
            startActivity(Intent(this, SociosVencimientoHoyActivity::class.java))
        }

        // Listener para Cargar Socios de Prueba
        btnCargarSociosPrueba.setOnClickListener {
            dbHelper.poblarSociosDePrueba()
            Toast.makeText(this, "Se cargaron socios de prueba", Toast.LENGTH_SHORT).show()
        }

        // Listener para Salir
        btnSalir.setOnClickListener {
            finish()
        }
    }
}
