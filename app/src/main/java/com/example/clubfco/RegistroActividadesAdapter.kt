package com.example.clubfco


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RegistroActividadesAdapter(private val lista: List<NoSocio>) :
    RecyclerView.Adapter<RegistroActividadesAdapter.ViewHolder>() {

    // ViewHolder: representa cada fila del RecyclerView
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDatos: TextView = itemView.findViewById(R.id.tvDatos)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_registro_actividad, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val noSocio = lista[position]
        holder.tvDatos.text =
            "DNI: ${noSocio.dni}\n" +
                    "Nombre: ${noSocio.nombre}\n" +
                    "Apellido: ${noSocio.apellido}\n" +
                    "Actividad: ${noSocio.actividad}\n" +
                    "Fecha: ${noSocio.fecha}"
    }

    override fun getItemCount(): Int = lista.size
}
