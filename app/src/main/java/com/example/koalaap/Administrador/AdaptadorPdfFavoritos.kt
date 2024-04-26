package com.example.koalaap.Administrador

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.koalaap.databinding.ItemLibroFavoritoBinding
import android.content.Context
import android.content.Intent
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.example.koalaap.Administrador.Modelopdf

class AdaptadorPdfFavoritos(
    private val context: Context,
    private var arrayListLibros: ArrayList<Modelopdf>
) : RecyclerView.Adapter<AdaptadorPdfFavoritos.HolderPdfFavorito>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderPdfFavorito {
        val binding = ItemLibroFavoritoBinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderPdfFavorito(binding)
    }

    override fun getItemCount(): Int = arrayListLibros.size

    override fun onBindViewHolder(holder: HolderPdfFavorito, position: Int) {
        val modelo = arrayListLibros[position]
        cargarDetalleLibro(modelo, holder)

     holder.itemView.setOnClickListener{
         val intent = Intent(context, DetalleLibro::class.java)
         intent.putExtra("idLibro",modelo.id)
         context.startActivity(intent)
     }

        holder.ib_eliminar_fav.setOnClickListener{
            MisFunciones.eliminarFavoritos(context, modelo.id)
        }
    }

    private fun cargarDetalleLibro(modelo: Modelopdf, holder: HolderPdfFavorito) {
        val idLibro = modelo.id

        val ref= FirebaseDatabase.getInstance().getReference("Libros")
            ref.child(idLibro)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val categoria = "${snapshot.child("categoria").value}"
                    val descripcion = "${snapshot.child("descripcion").value}"
                    val conDescargas = "${snapshot.child("contadorDescargas").value}"
                    val tiempo = "${snapshot.child("tiempo").value}"
                    val titulo = "${snapshot.child("titulo").value}"
                    val uid = "${snapshot.child("uid").value}"
                    val url = "${snapshot.child("url").value}"
                    val conVistas = "${snapshot.child("contadorVistas").value}"

                    modelo.esFavoritos = true
                    modelo.titulo = titulo
                    modelo.descipcion = descripcion
                    modelo.categoria = categoria
                    modelo.titulo = tiempo
                    modelo.uid  = uid
                    modelo.url = url
                    modelo.contadorDescargas = conDescargas.toLong()
                    modelo.contadorVistas = conVistas.toLong()

                    val fecha = MisFunciones.formatoTiempo(tiempo.toLong())

                    MisFunciones.CargarCategoria("$categoria", holder.categoria)
                    MisFunciones.CargarPdfUrl("$url","$titulo", holder.visualizadorPdf, holder.progressBar, null)
                    MisFunciones.CargarTamanioPdf("$url", "$titulo",holder.tamanio)

                    holder.titulo.text = titulo
                    holder.descripcion.text = descripcion
                    holder.fecha.text = fecha
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    inner class HolderPdfFavorito(private val binding: ItemLibroFavoritoBinding) : RecyclerView.ViewHolder(binding.root) {
        var visualizadorPdf = binding.VisualizadorPDF
        var progressBar = binding.progressBar
        var titulo = binding.TxtTituloLibroItem
        var descripcion = binding.TxtDescripcionLibroItem
        var categoria = binding.TxtCategoriaLibroAdmin
        var tamanio = binding.TxtTamanioLibroAdmin
        var fecha = binding.TxtFechaLibroAdmin
        var ib_eliminar_fav = binding.IbEliminarFav
}
}