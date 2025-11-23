package com.example.clubfco

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RegistroActividadesActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var adapter: RegistroActividadesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_actividades)

        // Inicializar DBHelper
        dbHelper = DBHelper(this)

        // Configurar RecyclerView
        val rvRegistro = findViewById<RecyclerView>(R.id.rvRegistroActividades)
        rvRegistro.layoutManager = LinearLayoutManager(this)

        // Obtener lista de no socios desde la base
        val listaNoSocios = dbHelper.obtenerNoSocios()

        // Configurar Adapter
        adapter = RegistroActividadesAdapter(listaNoSocios)
        rvRegistro.adapter = adapter
    }
}
