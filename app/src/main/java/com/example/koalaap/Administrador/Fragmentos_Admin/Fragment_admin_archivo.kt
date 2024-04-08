package com.example.koalaap.Administrador.Fragmentos_Admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.example.koalaap.databinding.FragmentAdminArchivoBinding
import com.example.koalaap.Administrador.ModeloCategoria
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Fragment_admin_archivo : Fragment() {
    private var _binding: FragmentAdminArchivoBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoriaArrayList: ArrayList<ModeloCategoria>
    private var id_categoria: String = ""
    private var titulo_categoria: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAdminArchivoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoriaArrayList = ArrayList()
        cargarCategorias()

        binding.TvCategoriaLibro.setOnClickListener {
            seleccionarCategoria()
        }
    }

    private fun cargarCategorias() {
        val ref = FirebaseDatabase.getInstance().getReference("Categorías")
        ref.orderByChild("categoria").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                categoriaArrayList.clear()
                for (ds in snapshot.children) {
                    val modelo = ds.getValue(ModeloCategoria::class.java)
                    modelo?.let { categoriaArrayList.add(it) }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejo de error, por ejemplo, mostrar un mensaje al usuario
            }
        })
    }

    private fun seleccionarCategoria() {
        val categoriasArray = arrayOfNulls<String>(categoriaArrayList.size)
        for (i in categoriasArray.indices) {
            categoriasArray[i] = categoriaArrayList[i].categoria
        }

        context?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Seleccionar categoría")
                .setItems(categoriasArray) { dialog, which ->
                    id_categoria = categoriaArrayList[which].id
                    titulo_categoria = categoriaArrayList[which].categoria
                    binding.TvCategoriaLibro.text = titulo_categoria
                }
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
