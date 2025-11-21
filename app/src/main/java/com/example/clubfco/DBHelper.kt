package com.example.clubfco

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DBHelper(context: Context) : SQLiteOpenHelper(context, "ClubDB", null, 9) { // Versión incrementada para forzar actualización

    // Usa un formato estándar y consistente en toda la app. "yyyy-MM-dd" es ideal para ordenar y comparar.
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override fun onCreate(db: SQLiteDatabase) {
        // La creación de tablas está bien.
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS actividades (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                fecha TEXT NOT NULL
            )
        """)
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS socios (
                numeroSocio INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                apellido TEXT NOT NULL,
                dni TEXT NOT NULL UNIQUE,
                fechaAlta TEXT NOT NULL,
                fechaUltimoPago TEXT,
                fechaProximoVencimiento TEXT,
                montoCuota REAL NOT NULL DEFAULT 0,
                estadoCuota TEXT NOT NULL DEFAULT 'pagada'
            )
        """)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Esta implementación es simple y efectiva para desarrollo.
        db.execSQL("DROP TABLE IF EXISTS actividades")
        db.execSQL("DROP TABLE IF EXISTS socios")
        onCreate(db)
    }

    // --- CORRECCIÓN GENERAL: Se han eliminado todos los db.close() ---
    // SQLiteOpenHelper gestiona el ciclo de vida de la conexión. No la cierres manualmente.

    // ---------------- ACTIVIDADES ----------------

    fun borrarTodasLasActividades() {
        val db = writableDatabase
        db.delete("actividades", null, null)
    }

    fun insertarActividad(nombre: String, fecha: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nombre", nombre)
            put("fecha", fecha)
        }
        db.insert("actividades", null, values)
    }

    fun eliminarActividad(nombre: String, fecha: String) {
        val db = writableDatabase
        db.delete("actividades", "nombre = ? AND fecha = ?", arrayOf(nombre, fecha))
    }

    // ---------------- SOCIOS (CORREGIDO Y OPTIMIZADO) ----------------

    fun insertarSocio(nombre: String, apellido: String, dni: String, montoCuota: Double) {
        val db = writableDatabase
        val hoy = LocalDate.now()
        val vencimiento = hoy.plusMonths(1)

        val values = ContentValues().apply {
            put("nombre", nombre)
            put("apellido", apellido)
            put("dni", dni)
            put("fechaAlta", hoy.format(formatter))
            put("fechaUltimoPago", hoy.format(formatter))
            put("fechaProximoVencimiento", vencimiento.format(formatter))
            put("montoCuota", montoCuota)
            put("estadoCuota", "pagada")
        }
        db.insert("socios", null, values)
    }

    fun registrarPago(dni: String) {
        val db = writableDatabase
        val hoy = LocalDate.now()
        // El nuevo vencimiento es siempre 1 mes DESPUÉS de la fecha de pago actual.
        val vencimiento = hoy.plusMonths(1)

        val values = ContentValues().apply {
            put("fechaUltimoPago", hoy.format(formatter))
            put("fechaProximoVencimiento", vencimiento.format(formatter))
            put("estadoCuota", "pagada")
        }
        db.update("socios", values, "dni = ?", arrayOf(dni))
    }

    // FUNCIÓN OPTIMIZADA: Actualiza todos los vencidos con una sola consulta SQL. Es mucho más rápido.
    fun actualizarEstadosVencidos() {
        val db = writableDatabase
        val hoy = LocalDate.now().format(formatter)
        val values = ContentValues().apply {
            put("estadoCuota", "vencida")
        }
        // Actualiza solo los socios cuya fecha de vencimiento ya pasó y no están marcados como "vencida".
        db.update("socios", values, "fechaProximoVencimiento < ? AND estadoCuota != ?", arrayOf(hoy, "vencida"))
    }

    fun eliminarSocio(dni: String) {
        val db = writableDatabase
        db.delete("socios", "dni = ?", arrayOf(dni))
    }

    // FUNCIÓN CORREGIDA: La lógica interna estaba fuera de la función.
    fun obtenerSocios(): List<Socio> {
        val lista = mutableListOf<Socio>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM socios ORDER BY numeroSocio ASC", null)

        // Usar 'use' asegura que el cursor se cierre automáticamente, incluso si hay errores.
        cursor.use { c ->
            if (c.moveToFirst()) {
                do {
                    lista.add(crearSocioDesdeCursor(c))
                } while (c.moveToNext())
            }
        }
        // No cierres la base de datos aquí.
        return lista
    }

    // FUNCIÓN CORREGIDA: Movida aquí, al nivel de la clase, y optimizada.
    fun poblarSociosDePrueba() {
        val db = writableDatabase
        // Usar una transacción para inserciones masivas es muchísimo más rápido.
        db.beginTransaction()
        try {
            db.delete("socios", null, null) // Limpia la tabla para evitar duplicados.

            val montoCuota = 100.0
            val diasEnMes = 30 // Noviembre

            for (dia in 1..diasEnMes) {
                for (i in 1..3) {
                    val numero = (dia - 1) * 3 + i
                    val fechaVencimiento = LocalDate.of(2025, 11, dia)
                    val fechaAlta = fechaVencimiento.minusMonths(2) // Se dieron de alta hace 2 meses
                    val fechaUltimoPago = fechaVencimiento.minusMonths(1) // Pagaron hace 1 mes

                    val values = ContentValues().apply {
                        put("nombre", "Socio$numero")
                        put("apellido", "Apellido$numero")
                        put("dni", "DNI%03d".format(numero))
                        // Usa el formatter para consistencia.
                        put("fechaAlta", fechaAlta.format(formatter))
                        put("fechaUltimoPago", fechaUltimoPago.format(formatter))
                        put("fechaProximoVencimiento", fechaVencimiento.format(formatter))
                        put("montoCuota", montoCuota)
                        put("estadoCuota", "pagada") // Inicialmente todos están 'pagada'
                    }
                    db.insert("socios", null, values)
                }
            }
            db.setTransactionSuccessful() // Marca la transacción como exitosa.
        } finally {
            db.endTransaction() // Finaliza la transacción (confirma si fue exitosa, o revierte si no).
        }
    }

    // FUNCIÓN DE AYUDA: Evita repetir código y hace el código más limpio.
    private fun crearSocioDesdeCursor(cursor: android.database.Cursor): Socio {
        return Socio(
            numeroSocio = cursor.getInt(cursor.getColumnIndexOrThrow("numeroSocio")),
            nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
            apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido")),
            dni = cursor.getString(cursor.getColumnIndexOrThrow("dni")),
            fechaAlta = cursor.getString(cursor.getColumnIndexOrThrow("fechaAlta")),
            fechaUltimoPago = cursor.getString(cursor.getColumnIndexOrThrow("fechaUltimoPago")),
            fechaProximoVencimiento = cursor.getString(cursor.getColumnIndexOrThrow("fechaProximoVencimiento")),
            montoCuota = cursor.getDouble(cursor.getColumnIndexOrThrow("montoCuota")),
            estadoCuota = cursor.getString(cursor.getColumnIndexOrThrow("estadoCuota"))
        )
    }
}
