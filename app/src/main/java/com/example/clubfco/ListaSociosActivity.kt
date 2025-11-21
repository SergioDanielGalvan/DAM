package com.example.clubfco

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListaSociosActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var adapter: SociosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_socios)

        dbHelper = DBHelper(this)

        val rvSocios = findViewById<RecyclerView>(R.id.rvSocios)
        rvSocios.layoutManager = LinearLayoutManager(this)

        val socios = dbHelper.obtenerSocios()
        adapter = SociosAdapter(socios.toMutableList())
        rvSocios.adapter = adapter

        findViewById<Button>(R.id.btnSalir).setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.setData(dbHelper.obtenerSocios())
    }
}
