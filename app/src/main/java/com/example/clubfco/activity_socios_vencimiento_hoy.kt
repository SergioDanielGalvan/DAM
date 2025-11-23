package com.example.clubfco

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SociosVencimientoHoyActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var adapter: SociosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_socios_vencimiento_hoy)

        dbHelper = DBHelper(this)

        val rvSociosHoy = findViewById<RecyclerView>(R.id.rvSociosHoy)
        rvSociosHoy.layoutManager = LinearLayoutManager(this)

        // Fecha actual en formato ISO
        val hoy = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)

        // Filtrar socios cuyo vencimiento sea hoy
        val sociosHoy = dbHelper.obtenerSocios().filter {
            it.fechaProximoVencimiento == hoy
        }

        adapter = SociosAdapter(sociosHoy.toMutableList())
        rvSociosHoy.adapter = adapter

        findViewById<Button>(R.id.btnSalir).setOnClickListener {
            finish()
        }
    }
}
