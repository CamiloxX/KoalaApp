package com.example.koalaap.Administrador.Fragmentos_Admin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.koalaap.Administrador.AdaptadorCategoria
import com.example.koalaap.Modelos.ModeloCategoria
import com.example.koalaap.Administrador.TopDescargados
import com.example.koalaap.Administrador.TopVistos
import com.example.koalaap.databinding.FragmentAdminDashboardBinding
import com.google.firebase.database.*
import java.lang.Exception

class Fragment_admin_buscar : Fragment(){

    private lateinit var binding : FragmentAdminDashboardBinding
    private lateinit var mContext : Context
    private lateinit var categoriaArrayList : ArrayList<ModeloCategoria>
    private lateinit var adaptadorCategoria: AdaptadorCategoria

    override fun onAttach(context: Context) {

        mContext = context
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAdminDashboardBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ListarCategorias()

        binding.BuscarCategoria.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(categoria: CharSequence?, p1: Int, p2: Int, p3: Int) {
                  try {
                      adaptadorCategoria.filter.filter(categoria)
                  }catch (e: Exception){

                  }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })


        binding.BtnMasVistos.setOnClickListener{
              startActivity(Intent(mContext, TopVistos::class.java))
        }

        binding.BtnMasDescargados.setOnClickListener{
              startActivity(Intent(mContext,TopDescargados::class.java))
        }

    }

    private fun ListarCategorias() {
        categoriaArrayList = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Categorías").orderByChild("categoria")
        ref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                categoriaArrayList.clear()
                for (ds in snapshot.children) {
                    val modelo = ds.getValue(ModeloCategoria::class.java)
                    categoriaArrayList.add(modelo!!)
                }
                adaptadorCategoria = AdaptadorCategoria(mContext, categoriaArrayList)
                binding.categoriasRv.adapter = adaptadorCategoria
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}
