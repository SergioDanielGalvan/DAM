package com.example.clubfco

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup

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
            val checkHabilitar = CheckBox(this).apply { text = "Apto Médico" }

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

            val context = this
            val inputDni = EditText(context).apply { hint = "DNI del socio" }

            //  MODIFICACIÓN Pago

            // Efectivo vs Tarjeta
            val labelMetodo = TextView(context).apply { text = "Método de Pago:"; setPadding(0, 20, 0, 0) }
            val rgMetodo = RadioGroup(context).apply { orientation = RadioGroup.HORIZONTAL }
            val rbEfectivo = RadioButton(context).apply { text = "Efectivo"; id = View.generateViewId(); isChecked = true }
            val rbTarjeta = RadioButton(context).apply { text = "Tarjeta"; id = View.generateViewId() }

            rgMetodo.addView(rbEfectivo)
            rgMetodo.addView(rbTarjeta)

            // Cuotas
            val labelCuotas = TextView(context).apply { text = "Cuotas:"; visibility = View.GONE; setPadding(0, 10, 0, 0) }
            val rgCuotas = RadioGroup(context).apply { orientation = RadioGroup.HORIZONTAL; visibility = View.GONE }
            val rb3 = RadioButton(context).apply { text = "3 cuotas" }
            val rb6 = RadioButton(context).apply { text = "6 cuotas" }

            rgCuotas.addView(rb3)
            rgCuotas.addView(rb6)

            // muestra las cuotas y oculta.
            rgMetodo.setOnCheckedChangeListener { _, checkedId ->
                if (checkedId == rbTarjeta.id) {
                    labelCuotas.visibility = View.VISIBLE
                    rgCuotas.visibility = View.VISIBLE
                    rb3.isChecked = true
                } else {
                    labelCuotas.visibility = View.GONE
                    rgCuotas.visibility = View.GONE
                    rgCuotas.clearCheck()
                }
            }
            // FIN MODIFICACIÓN

            val checkPago = CheckBox(context).apply { text = "Confirmar pago" }

            val layout = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(50, 20, 50, 20)
                addView(inputDni)
                // Agregamos los elementos nuevos al layout antes del check de confirmar
                addView(labelMetodo)
                addView(rgMetodo)
                addView(labelCuotas)
                addView(rgCuotas)
                addView(checkPago)
            }

            AlertDialog.Builder(context)
                .setTitle("Registrar Pago de Cuota")
                .setView(layout)
                .setPositiveButton("Registrar") { _, _ ->
                    val dni = inputDni.text.toString().trim()

                    // Lógica original intacta
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
                                val intent = Intent(context, ReciboCuotaActivity::class.java).apply {
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
                                Toast.makeText(context, "El socio está al día con sus pagos.", Toast.LENGTH_LONG).show()
                            }
                        } else {
                            Toast.makeText(context, "Socio no encontrado con el DNI proporcionado.", Toast.LENGTH_LONG).show()
                        }

                    } else {
                        Toast.makeText(context, "Por favor, ingrese el DNI y confirme el pago", Toast.LENGTH_SHORT).show()
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
