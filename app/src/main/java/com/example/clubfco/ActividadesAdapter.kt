package com.example.clubfco

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView



class ActividadesAdapter (private val actividades: MutableList<Pair <String, String>>,
    private val dbHelper: DBHelper
    ) :
    RecyclerView.Adapter<ActividadesAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val tvActividades: TextView = itemView.findViewById(R.id.tvActividades)

}

override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActividadesAdapter.ViewHolder {
    val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.item_actividad, parent, false)
    return ActividadesAdapter.ViewHolder(view)
}

    override fun onBindViewHolder(holder: ActividadesAdapter.ViewHolder, position: Int) {
        val (nombre, fechaActividad) = actividades[position]
        holder.tvActividades.text = "$nombre - $fechaActividad"

        holder.itemView.setOnLongClickListener {
            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Eliminar Actividad")
                .setMessage("¿Estás seguro de que deseas eliminar esta actividad?")
                .setPositiveButton("Si") { _, _ ->
                    dbHelper.eliminarActividad(nombre, fechaActividad)
                    actividades.removeAt(position)
                    notifyItemRemoved(position)
                }
                .setNegativeButton("No", null)
                .show()
            true
        }
    }


override fun getItemCount(): Int = actividades.size

}