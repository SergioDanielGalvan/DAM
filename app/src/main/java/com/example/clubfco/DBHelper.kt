package com.example.clubfco

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.example.clubfco.NoSocio

class DBHelper(context: Context) : SQLiteOpenHelper(context, "ClubDB", null, 41) {

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override fun onCreate(db: SQLiteDatabase) {
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
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS no_socios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                apellido TEXT NOT NULL,
                dni TEXT NOT NULL,
                actividad TEXT NOT NULL,
                fecha TEXT NOT NULL
            )
        """)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS actividades")
        db.execSQL("DROP TABLE IF EXISTS socios")
        db.execSQL("DROP TABLE IF EXISTS no_socios")
        onCreate(db)
    }

    // --- ACTIVIDADES ---
    fun borrarTodasLasActividades() {
        val db = writableDatabase
        db.delete("actividades", null, null)
        db.close()
    }

    fun insertarActividad(nombre: String, fecha: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nombre", nombre)
            put("fecha", fecha)
        }
        db.insert("actividades", null, values)
        db.close()
    }

    fun eliminarActividad(nombre: String, fecha: String) {
        val db = writableDatabase
        db.delete("actividades", "nombre = ? AND fecha = ?", arrayOf(nombre, fecha))
        db.close()
    }

    // --- NO SOCIOS ---
    fun registrarNoSocio(nombre: String, apellido: String, dni: String, actividad: String, fecha: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nombre", nombre)
            put("apellido", apellido)
            put("dni", dni)
            put("actividad", actividad)
            put("fecha", fecha)
        }
        db.insert("no_socios", null, values)
        db.close()
    }

    fun obtenerNoSocios(): List<NoSocio> {
        val listaNoSocios = mutableListOf<NoSocio>()
        val db = this.readableDatabase
        // Asegúrate de que el nombre de la tabla ('actividades' o 'pagos_no_socios') es correcto.
        val cursor = db.rawQuery("SELECT * FROM no_socios", null)

        // El bloque 'use' se encarga de cerrar el cursor automáticamente.
        cursor.use {
            if (it.moveToFirst()) {
                do {
                    // Crea un objeto NoSocio usando los datos de cada fila del cursor.
                    val noSocio = NoSocio(
                        id = it.getInt(it.getColumnIndexOrThrow("id")),
                        nombre = it.getString(it.getColumnIndexOrThrow("nombre")),
                        apellido = it.getString(it.getColumnIndexOrThrow("apellido")),
                        dni = it.getString(it.getColumnIndexOrThrow("dni")),
                        actividad = it.getString(it.getColumnIndexOrThrow("actividad")),
                        fecha = it.getString(it.getColumnIndexOrThrow("fecha"))
                    )
                    listaNoSocios.add(noSocio)
                } while (it.moveToNext())
            }
        }
        // No cierres la base de datos aquí.
        return listaNoSocios
    }

    // --- SOCIOS ---
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
        db.close()
    }

    fun registrarPagoPorNumero(numeroSocio: Int) {
        val db = writableDatabase
        val hoy = LocalDate.now()
        val nuevoVencimiento = hoy.plusMonths(1)

        val values = ContentValues().apply {
            put("estadoCuota", "pagada")
            put("fechaUltimoPago", hoy.format(formatter))
            put("fechaProximoVencimiento", nuevoVencimiento.format(formatter))
        }

        db.update("socios", values, "numeroSocio = ?", arrayOf(numeroSocio.toString()))
        db.close()
    }

    fun eliminarSocio(dni: String) {
        val db = writableDatabase
        db.delete("socios", "dni = ?", arrayOf(dni))
        db.close()
    }

    fun obtenerSocios(): List<Socio> {
        val lista = mutableListOf<Socio>()
        val db = readableDatabase
        db.rawQuery("SELECT * FROM socios ORDER BY numeroSocio ASC", null).use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    lista.add(crearSocioDesdeCursor(cursor))
                } while (cursor.moveToNext())
            }
        }
        db.close()
        return lista
    }

    fun poblarSociosDePrueba() {
        val db = writableDatabase
        db.beginTransaction()
        try {
            db.delete("socios", null, null)

            val montoCuota = 100.0
            val diasEnMes = 30

            for (dia in 1..diasEnMes) {
                for (i in 1..3) {
                    val numero = (dia - 1) * 3 + i
                    val fechaVencimiento = LocalDate.of(2025, 11, dia)
                    val fechaUltimoPago = fechaVencimiento.minusMonths(1)
                    val fechaAlta = fechaUltimoPago.minusMonths(2)

                    val values = ContentValues().apply {
                        put("nombre", "Socio$numero")
                        put("apellido", "Apellido$numero")
                        put("dni", "DNI%03d".format(numero))
                        put("fechaAlta", fechaAlta.format(formatter))
                        put("fechaUltimoPago", fechaUltimoPago.format(formatter))
                        put("fechaProximoVencimiento", fechaVencimiento.format(formatter))
                        put("montoCuota", montoCuota)
                        put("estadoCuota", "pendiente")
                    }
                    db.insert("socios", null, values)
                }
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
            db.close()
        }
    }

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
