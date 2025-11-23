package com.example.clubfco

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ClientesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_clientes)

        // Ajuste de insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configuración inicial de botones
        val btnSocios = findViewById<Button>(R.id.btnSocios1)
        btnSocios.setOnClickListener {
            startActivity(Intent(this, SociosActivity::class.java))
        }

        val btnNoSocios = findViewById<Button>(R.id.btnNoSocios)
        btnNoSocios.setOnClickListener {
            startActivity(Intent(this, PagoNoSociosActivity::class.java))
        }

        val btnSalir = findViewById<Button>(R.id.btnSalir)
        btnSalir.setOnClickListener {
            finish()
        }
    }

    // 🔁 Este método se ejecuta cada vez que volvés a ClientesActivity
    override fun onResume() {
        super.onResume()

        findViewById<Button>(R.id.btnSocios1).setOnClickListener {
            startActivity(Intent(this, SociosActivity::class.java))
        }

        findViewById<Button>(R.id.btnNoSocios).setOnClickListener {
            startActivity(Intent(this, PagoNoSociosActivity::class.java))
        }

        findViewById<Button>(R.id.btnSalir).setOnClickListener {
            finish()
        }
    }
}
