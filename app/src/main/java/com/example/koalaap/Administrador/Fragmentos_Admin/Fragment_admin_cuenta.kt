package com.example.koalaap.Administrador.Fragmentos_Admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.koalaap.databinding.FragmentAdminCuentaBinding
import com.google.firebase.auth.FirebaseAuth
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.koalaap.Administrador.EditarPerfilAdmin
import com.example.koalaap.Administrador.Login_Admin
import com.example.koalaap.Administrador.MisFunciones
import com.example.koalaap.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Fragment_admin_cuenta : Fragment() {
    private lateinit var binding: FragmentAdminCuentaBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAdminCuentaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.EditarPerfil.setOnClickListener {


            startActivity(Intent(mContext, EditarPerfilAdmin :: class.java))

        }


        cargarInformacion()
        binding.CerrarSesion.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(context, Login_Admin::class.java))
            activity?.finishAffinity()
        }
    }

    private fun cargarInformacion() {
        val user = firebaseAuth.currentUser
        if (user != null) {
            // Obtener el timestamp de la creación del usuario y formatearlo
            val fechaCreacionMillis = user.metadata?.creationTimestamp ?: 0
            val fechaCreacion = MisFunciones.formatoTiempo(fechaCreacionMillis)
            binding.TxtTiempoRegistro.text = fechaCreacion

            // Cargar otros datos del usuario
            val ref = FirebaseDatabase.getInstance().getReference("Usuarios")
            ref.child(user.uid)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val nombres = snapshot.child("nombres").value?.toString().orEmpty()
                        val email = snapshot.child("email").value?.toString().orEmpty()
                        val imagen = snapshot.child("imagen").value?.toString().orEmpty()

                        binding.TxtNombresAdmin.text = nombres
                        binding.TxtEmailAdmin.text = email

                        // Cargar imagen con Glide
                        Glide.with(mContext)
                            .load(imagen)
                            .placeholder(R.drawable.ic_img_perfil)
                            .into(binding.imgPerfilAdmin)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(mContext, "Failed to load user data: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}
