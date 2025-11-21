package com.example.clubfco

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SociosAdapter(
    private val lista: MutableList<Socio>
) : RecyclerView.Adapter<SociosAdapter.SocioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SocioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_socio, parent, false)
        return SocioViewHolder(view)
    }

    override fun onBindViewHolder(holder: SocioViewHolder, position: Int) {
        val socio = lista[position]
        holder.tvNumero.text = "N° ${socio.numeroSocio}"
        holder.tvNombre.text = "${socio.nombre} ${socio.apellido}"
        holder.tvDni.text = "DNI: ${socio.dni}"
        holder.estadoCuotaText.text = "Estado cuota: ${socio.estadoCuota}"
        holder.fechaPagoText.text = "Último pago: ${socio.fechaUltimoPago ?: "Sin registro"}"
    }

    override fun getItemCount(): Int = lista.size

    fun setData(nuevaLista: List<Socio>) {
        lista.clear()
        lista.addAll(nuevaLista)
        notifyDataSetChanged()
    }

    inner class SocioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNumero: TextView = itemView.findViewById(R.id.tvNumero)
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        val tvDni: TextView = itemView.findViewById(R.id.tvDni)
        val estadoCuotaText: TextView = itemView.findViewById(R.id.txtEstadoCuota)
        val fechaPagoText: TextView = itemView.findViewById(R.id.txtFechaUltimoPago)
    }
}
