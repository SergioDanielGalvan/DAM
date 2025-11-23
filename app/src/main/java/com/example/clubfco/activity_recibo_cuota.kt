package com.example.clubfco

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ReciboCuotaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recibo_cuota)

        // Recuperar datos enviados desde SociosActivity
        val numeroSocio = intent.getIntExtra("numeroSocio", -1)
        val nombre = intent.getStringExtra("nombre")
        val apellido = intent.getStringExtra("apellido")
        val dni = intent.getStringExtra("dni")
        val monto = intent.getDoubleExtra("montoCuota", 0.0)
        val fechaPago = intent.getStringExtra("fechaPago")
        val fechaVencimiento = intent.getStringExtra("fechaVencimiento")

        // Asignar valores a los TextView
        findViewById<TextView>(R.id.txtNroRecibo).text = "RECIBO N° $numeroSocio"
        findViewById<TextView>(R.id.txtNroSocio).text = "Socio N°: $numeroSocio"
        findViewById<TextView>(R.id.txtNombreSocio).text = "Nombre: $nombre"
        findViewById<TextView>(R.id.txtApellido).text = "Apellido: $apellido"
        findViewById<TextView>(R.id.txtNroDNI).text = "DNI: $dni"
        findViewById<TextView>(R.id.txtPagoRecibido).text = "Pago Recibido $: $monto"
        findViewById<TextView>(R.id.txtFechaPago).text = "Fecha de Pago: $fechaPago"
        findViewById<TextView>(R.id.txtFechaVencimiento).text = "Próximo vencimiento: $fechaVencimiento"

        // Botones
        findViewById<Button>(R.id.btnImprimirTicket).setOnClickListener {
            // Aquí podrías implementar lógica de impresión o exportación
            // Por ahora mostramos un mensaje
            android.widget.Toast.makeText(this, "Ticket enviado a impresión", android.widget.Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.btnCancelar).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.btnSalir).setOnClickListener {
            finish()
        }
    }
}
