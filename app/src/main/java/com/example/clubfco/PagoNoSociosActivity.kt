package com.example.clubfco

import android.content.Intent
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

class PagoNoSociosActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var actividades: MutableList<Pair<String, String>>
    private lateinit var adapter: ActividadesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pago_no_socios)

        dbHelper = DBHelper(this)

        // ✅ Limpiar tabla al entrar (después de crear DBHelper)
        dbHelper.borrarTodasLasActividades()

        // ✅ Inicializar lista después de limpiar
        actividades = mutableListOf()

        val rvActividades = findViewById<RecyclerView>(R.id.rvActividades)
        rvActividades.layoutManager = LinearLayoutManager(this)
        adapter = ActividadesAdapter(actividades, dbHelper)
        rvActividades.adapter = adapter

        val btnActividades = findViewById<Button>(R.id.btnActividades)
        btnActividades.setOnClickListener {
            val opciones = arrayOf("Fútbol", "Vóley", "Básquet", "Handball")

            AlertDialog.Builder(this)
                .setTitle("Seleccionar Actividad")
                .setItems(opciones) { _, which ->
                    val nombreActividad = opciones[which]

                    val calendario = Calendar.getInstance()
                    DatePickerDialog(
                        this,
                        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                            val fecha = String.format(
                                Locale.getDefault(),
                                "%02d/%02d/%04d",
                                dayOfMonth,
                                month + 1,
                                year
                            )

                            dbHelper.insertarActividad(nombreActividad, fecha)
                            actividades.add(nombreActividad to fecha)
                            adapter.notifyItemInserted(actividades.size - 1)
                        },
                        calendario.get(Calendar.YEAR),
                        calendario.get(Calendar.MONTH),
                        calendario.get(Calendar.DAY_OF_MONTH)
                    ).show()
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

        val btnPagoActividades = findViewById<Button>(R.id.btnPagoActividades)
        btnPagoActividades.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_datos_no_socio, null)

            val etNombre = dialogView.findViewById<EditText>(R.id.etNombre)
            val etApellido = dialogView.findViewById<EditText>(R.id.etApellido)
            val etDni = dialogView.findViewById<EditText>(R.id.etDni)

            val resumenActividades = actividades.joinToString("\n") { (nombre, fecha) ->
                "- $nombre: $fecha"
            }

            AlertDialog.Builder(this)
                .setTitle("Datos No Socio")
                .setView(dialogView)
                .setPositiveButton("Confirmar Pago") { _, _ ->
                    val nombre = etNombre.text.toString().trim()
                    val apellido = etApellido.text.toString().trim()
                    val dni = etDni.text.toString().trim()

                    if (nombre.isNotEmpty() && apellido.isNotEmpty() && dni.isNotEmpty()) {
                        // 🔹 Registrar cada actividad en la tabla no_socios
                        actividades.forEach { (actividad, fecha) ->
                            dbHelper.registrarNoSocio(nombre, apellido, dni, actividad, fecha)
                        }

                        // 🔹 Generar comprobante
                        val resumenActividades = actividades.joinToString("\n") { (act, fec) -> "- $act: $fec" }
                        val intent = Intent(this, ReciboNoSocioActivity::class.java).apply {
                            putExtra("nombre", nombre)
                            putExtra("apellido", apellido)
                            putExtra("dni", dni)
                            putExtra("resumenActividades", resumenActividades)
                        }
                        startActivity(intent)
                    }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
        val btnRegistroActividades = findViewById<Button>(R.id.btnRegistroActividades)
        btnRegistroActividades.setOnClickListener {
            val intent = Intent(this, RegistroActividadesActivity::class.java)
            startActivity(intent)
        }

        val btnSalir = findViewById<Button>(R.id.btnSalir)
        btnSalir.setOnClickListener {
            finish()
        }
    }
}
