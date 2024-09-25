package com.example.koalaap.Administrador.Fragmentos_cliente

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.koalaap.Administrador.AdaptadorPdfAdmin
import com.example.koalaap.Administrador.AdaptadorPdfCliente
import com.example.koalaap.Modelos.Modelopdf
import com.example.koalaap.databinding.FragmentAdminPrincipalBinding
import com.example.koalaap.databinding.FragmentClientePrincipalBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Fragment_cliente_principal : Fragment() {

    private lateinit var binding: FragmentClientePrincipalBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var librosArrayList: ArrayList<Modelopdf>
    private lateinit var adaptadorPdfCliente: AdaptadorPdfCliente
    private lateinit var mContext: Context

    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentClientePrincipalBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        listarLibros()
    }

    private fun listarLibros() {
        librosArrayList = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Libros")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                librosArrayList.clear()
                for (ds in snapshot.children) {
                    val modelopdf = ds.getValue(Modelopdf::class.java)
                    modelopdf?.let { librosArrayList.add(it) }
                }
                adaptadorPdfCliente = AdaptadorPdfCliente(mContext, librosArrayList)
                binding.RvLibrosFragment.adapter = adaptadorPdfCliente
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar el error
            }
        })
        binding.RvLibrosFragment.layoutManager = LinearLayoutManager(mContext)
    }
}
