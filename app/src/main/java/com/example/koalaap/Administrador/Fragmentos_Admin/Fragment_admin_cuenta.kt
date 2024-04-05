package com.example.koalaap.Administrador.Fragmentos_Admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.koalaap.R
import com.example.koalaap.databinding.FragmentAdminCuentaBinding
import com.google.firebase.auth.FirebaseAuth
import android.content.Context
import android.content.Intent
import com.example.koalaap.Administrador.Login_Admin
import com.example.koalaap.Elegir_Rol

class Fragment_admin_cuenta : Fragment() {
    private lateinit var binding: FragmentAdminCuentaBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var mContext: Context

    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAdminCuentaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        binding.CerrarSesion.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(context, Login_Admin::class.java))
            activity?.finishAffinity()
        }
    }
}
