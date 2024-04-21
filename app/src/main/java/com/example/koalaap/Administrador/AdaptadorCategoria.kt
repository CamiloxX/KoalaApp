package com.example.koalaap.Administrador

import android.app.AlertDialog
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.example.koalaap.databinding.FragmentAdminDashboardBinding
import com.example.koalaap.databinding.ItemCategoriaAdminBinding
import android.content.Context
import android.content.Intent
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase

import java.nio.file.attribute.AclFileAttributeView

class AdaptadorCategoria : RecyclerView.Adapter<AdaptadorCategoria.HolderCategoria>, Filterable {


    private lateinit var binding: ItemCategoriaAdminBinding

    private val m_context : Context
    public var categoriaArrayList : ArrayList<ModeloCategoria>

    private var filtroLista : ArrayList<ModeloCategoria>
    private var filtro : FiltroCategoria? = null


    constructor(m_context: Context, categoriaArrayList: ArrayList<ModeloCategoria>) : super() {
        this.m_context = m_context
        this.categoriaArrayList = categoriaArrayList
        this.filtroLista = categoriaArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderCategoria {
       binding= ItemCategoriaAdminBinding.inflate(LayoutInflater.from(m_context),parent,false)
       return HolderCategoria(binding.root)
    }

    override fun getItemCount(): Int {
        return categoriaArrayList.size
    }

    override fun onBindViewHolder(holder: HolderCategoria, position: Int) {
        val modelo= categoriaArrayList[position]
        val id=modelo.id
        val categoria= modelo.categoria
        val tiempo= modelo.tiempo
        val uid=modelo.uid

        holder.categoriaTv.text=categoria



        holder.itemView.setOnClickListener{
            val intent = Intent(m_context, ListaPdfAdmin::class.java)
            intent.putExtra("idCategoria", id )
            intent.putExtra("tituloCategoria", categoria )
            m_context.startActivity(intent)
        }
    }

    private fun EliminarCategoria(modelo: ModeloCategoria, holder: AdaptadorCategoria.HolderCategoria) {
         val id=modelo.id
        val ref= FirebaseDatabase.getInstance().getReference("Categorías")
        ref.child(id).removeValue()
            .addOnSuccessListener {
                Toast.makeText(m_context,"Categoria eliminada",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{e->
                Toast.makeText(m_context,"No se eliminó la categoria",Toast.LENGTH_SHORT).show()

            }
    }


    inner class HolderCategoria(itemView : View) : RecyclerView.ViewHolder(itemView){

        var categoriaTv : TextView = binding.ItemNombreCatA


    }

    override fun getFilter(): Filter {
        if(filtro==null){
            filtro = FiltroCategoria(filtroLista,this)
        }
        return filtro as FiltroCategoria
    }


}