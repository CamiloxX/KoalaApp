package com.example.koalaap.Administrador

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.koalaap.databinding.ItemLibroAdminBinding
import android.content.Context
import android.content.Intent
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast

class AdaptadorPdfAdmin : RecyclerView.Adapter<AdaptadorPdfAdmin.HolderPdfAdmin> , Filterable{

    private lateinit var binding : ItemLibroAdminBinding

    private var m_context: Context
    public var pdfArrayList : ArrayList<Modelopdf>
    private var filtroLibro : ArrayList<Modelopdf>
    private var filtro : FiltroPdfAdmin?=null

    constructor(m_context: Context, pdfArrayList: ArrayList<Modelopdf>) : super() {
        this.m_context = m_context
        this.pdfArrayList = pdfArrayList
        this.filtroLibro = pdfArrayList
    }


    inner class HolderPdfAdmin(itemView: View) : RecyclerView.ViewHolder(itemView){
        val VisualizadorPDF = binding.VisualizadorPDF
        val progressBar = binding.progressBar
        val Txt_titulo_libro_item = binding.TxtTituloLibroItem
        val Txt_descripcion_libro_item = binding.TxtDescripcionLibroItem
        val Txt_categoria_libro_admin = binding.TxtCategoriaLibroAdmin
        val Txt_tamanio_libro_admin = binding.TxtTamanioLibroAdmin
        val Txt_fecha_libro_admin = binding.TxtFechaLibroAdmin
        val Ib_mas_opciones = binding.IbMasOpciones

}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderPdfAdmin {
        binding = ItemLibroAdminBinding.inflate(LayoutInflater.from(m_context),parent,false)
        return HolderPdfAdmin(binding.root)
    }

    override fun getItemCount(): Int {
        return pdfArrayList.size
    }

    override fun onBindViewHolder(holder: HolderPdfAdmin, position: Int) {
        val modelo = pdfArrayList[position]
        val pdfId = modelo.id
        val categoriaId = modelo.categoria
        val titulo  = modelo.titulo
        val descripcion = modelo.descipcion
        val pdfUrl = modelo.url
        val tiempo = modelo.tiempo

        val formatoTiempo = MisFunciones.formatoTiempo(tiempo)

        holder.Txt_titulo_libro_item.text = titulo
        holder.Txt_descripcion_libro_item.text = descripcion
        holder.Txt_fecha_libro_admin.text = formatoTiempo

        MisFunciones.CargarCategoria(categoriaId, holder.Txt_categoria_libro_admin)
        MisFunciones.CargarPdfUrl(pdfUrl, titulo, holder.VisualizadorPDF, holder.progressBar, null)
        MisFunciones.CargarTamanioPdf(pdfUrl,titulo,holder.Txt_tamanio_libro_admin)

        holder.Ib_mas_opciones.setOnClickListener{
            verOpciones(modelo, holder)
            
        }
         holder.itemView.setOnClickListener{
             val intent = Intent(m_context, DetalleLibro::class.java)
             intent.putExtra("idLibro",pdfId)
             m_context.startActivity(intent)
         }
    }

    private fun verOpciones(modelo: Modelopdf, holder: AdaptadorPdfAdmin.HolderPdfAdmin) {
        val idlibro = modelo.id
        val urlLibro = modelo.url
        val tituloLibro = modelo.titulo

        val opciones = arrayOf("Actualizar","Eliminar")

        //Alert Dialog
        val builder = AlertDialog.Builder(m_context)
        builder.setTitle("Seleccione una opcion")
            .setItems(opciones){dialog, posicion->
                if (posicion == 0){
                    //Actualizar
                    val intent = Intent(m_context, ActualizarLibro::class.java)
                    intent.putExtra("idLibro", idlibro)
                    m_context.startActivity(intent)
                }else if(posicion == 1){
                   val opcionesEliminacion = arrayOf("Si","Cancelar")
                   val builder = AlertDialog.Builder(m_context)
                       builder.setTitle("Estas seguro de eliminar el libro ${tituloLibro}?")
                       .setItems(opcionesEliminacion){dialog, posicion ->
                           if(posicion == 0){
                               MisFunciones.EliminarLibro(m_context, idlibro, urlLibro, tituloLibro)
                           }
                           else if (posicion == 1){
                             Toast.makeText(m_context,"Cancelado por el usuario",Toast.LENGTH_SHORT).show()
                           }

                       }
                       .show()
                }
            }
            .show()

    }

    override fun getFilter(): Filter {
        if (filtro == null){
            filtro = FiltroPdfAdmin(filtroLibro,this)
        }
        return filtro as FiltroPdfAdmin
    }


}