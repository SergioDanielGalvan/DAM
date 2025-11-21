package com.example.clubfco

data class Socio(
    val numeroSocio: Int,
    val nombre: String,
    val apellido: String,
    val dni: String,
    val fechaAlta: String,
    val fechaUltimoPago: String?,
    val fechaProximoVencimiento: String?,
    val montoCuota: Double,
    val estadoCuota: String
)
