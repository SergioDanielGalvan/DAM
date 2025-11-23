

package com.example.clubfco

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ReciboNoSocioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recibo_no_socio)

        val nombre = intent.getStringExtra("nombre")
        val apellido = intent.getStringExtra("apellido")
        val dni = intent.getStringExtra("dni")
        val resumenActividades = intent.getStringExtra("resumenActividades") ?: ""



        // Mostrar datos personales
        findViewById<TextView>(R.id.textView2).text = "Nombre: $nombre"
        findViewById<TextView>(R.id.textView4).text = "Apellido: $apellido"
        findViewById<TextView>(R.id.textView5).text = "DNI: $dni"

        // Mostrar actividades
        findViewById<TextView>(R.id.textViewActividades).text = resumenActividades

        // Calcular total
        val total = calcularTotal(resumenActividades)

        // Mostrar pago recibido
        findViewById<TextView>(R.id.textViewPago).text = "Pago Recibido $: $total"

        findViewById<Button>(R.id.btnImprimirTicket).setOnClickListener {
            // Aquí podrías implementar lógica de impresión o exportación
            // Por ahora mostramos un mensaje
            android.widget.Toast.makeText(this, "Ticket enviado a impresión", android.widget.Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.btnVolver).setOnClickListener {
            finish()
        }
    }

    private fun calcularTotal(resumen: String): Int {
        var total = 0
        val lineas = resumen.split("\n")

        for (linea in lineas) {
            when {
                linea.contains("Fútbol", ignoreCase = true) -> total += 15000
                linea.contains("Básquet", ignoreCase = true) -> total += 10000
                linea.contains("Vóley", ignoreCase = true) -> total += 12000
                linea.contains("Handball", ignoreCase = true) -> total += 12000
            }
        }
        return total
    }
}




