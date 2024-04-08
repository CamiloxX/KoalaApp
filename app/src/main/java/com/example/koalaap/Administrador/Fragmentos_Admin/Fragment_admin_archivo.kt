package com.example.koalaap.Administrador.Fragmentos_Admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.koalaap.R
import com.example.koalaap.databinding.FragmentAdminArchivoBinding // Asegúrate de tener el correcto import para tu binding

class Fragment_admin_archivo : Fragment() {
    // Utilizando View Binding para acceder a los elementos del layout de forma segura
    private var _binding: FragmentAdminArchivoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout para este fragmento con View Binding
        _binding = FragmentAdminArchivoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




    }
}
