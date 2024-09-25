package com.example.koalaap

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.koalaap.Administrador.Fragmentos_Admin.Agregar_Categoria
import com.example.koalaap.Administrador.Fragmentos_Admin.Fragment_admin_archivo
import com.example.koalaap.Administrador.Fragmentos_Admin.Fragment_admin_buscar
import com.example.koalaap.Administrador.Fragmentos_Admin.Fragment_admin_cuenta
import com.example.koalaap.Administrador.Fragmentos_Admin.Fragment_admin_favoritos
import com.example.koalaap.Administrador.Fragmentos_Admin.Fragment_admin_principal
import com.example.koalaap.Administrador.Fragmentos_cliente.Fragment_cliente_principal
import com.example.koalaap.databinding.ActivityMainBinding
import com.example.koalaap.databinding.ActivityMainClienteBinding
import com.google.firebase.auth.FirebaseAuth

private lateinit var binding: ActivityMainClienteBinding
private lateinit var firebaseAuth: FirebaseAuth

class MainActivityCliente : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        ComprobarSesion()
        VerFragmentoPrincipal()


        // Manejar selección de los items del BottomNavigationView
        binding.BottomNavCliente.setOnItemSelectedListener { item->
            when (item.itemId) {
                R.id.Menu_dashboard_cl -> {
                    VerFragmentoPrincipal()
                    true
                }
                R.id.Menu_cuenta_cl -> {
                    VerFragmentoCuenta()
                    true
                }
                R.id.Menu_subir_cl -> {
                    VerFragmentoArchivo()
                    true
                }
                R.id.Menu_favoritos_cl -> {
                    VerFragmentoFavoritos()
                    true
                }
                R.id.Menu_buscar_cl -> {
                    VerFragmentoBuscar()
                    true
                }
                else -> false
            }
        }
    }


    private fun  VerFragmentoBuscar(){
        val nombre_titulo ="Dashboard"
        binding.TituloRLCliente.text=nombre_titulo
        binding.ImagenRLCliente.visibility = View.GONE
        val fragment = Fragment_admin_buscar()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.FragmentsCliente.id ,fragment,"Fragment DashBoard")
        fragmentTransaction.commit()
    }

    private fun VerFragmentoCuenta(){
        val nombre_titulo ="Mi cuenta"
        binding.TituloRLCliente.text=nombre_titulo
        binding.ImagenRLCliente.visibility = View.GONE

        val fragment = Fragment_admin_cuenta()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.FragmentsCliente.id ,fragment,"Fragment Mi cuenta")
        fragmentTransaction.commit()

    }

    private fun VerFragmentoPrincipal() {
        val nombre_titulo = ""
        binding.TituloRLCliente.text = nombre_titulo

        val imagenResId = R.drawable.bienvenida_img
            binding.ImagenRLCliente.setImageResource(imagenResId)
            binding.ImagenRLCliente.visibility = View.VISIBLE // Hacer visible el ImageView

            val fragment = Fragment_cliente_principal()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(binding.FragmentsCliente.id, fragment, "Fragment Principal")
            fragmentTransaction.commit()


    }

    private fun VerFragmentoArchivo() {
        val nombre_titulo = "Agregar PDF"
        binding.TituloRLCliente.text = nombre_titulo
        binding.ImagenRLCliente.visibility = View.GONE

        val fragment = Fragment_admin_archivo()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.FragmentsCliente.id, fragment, "Fragment Archivo")
        fragmentTransaction.commit()
    }
    private fun VerFragmentoFavoritos() {
        val nombre_titulo = "Favoritos"
        binding.TituloRLCliente.text = nombre_titulo
        binding.ImagenRLCliente.visibility = View.GONE

        val fragment = Fragment_admin_favoritos()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.FragmentsCliente.id, fragment, "Fragment Favoritos")
        fragmentTransaction.commit()
    }

    private fun VerActividadCategoria() {
        // Cambiar el título y ocultar la imagen en la toolbar
        val nombre_titulo = "Agregar Categoria"
        binding.TituloRLCliente.text = nombre_titulo
        binding.ImagenRLCliente.visibility = View.GONE

        // Crear un intent para abrir la actividad Agregar_Categoria
        val intent = Intent(this, Agregar_Categoria::class.java)
        startActivity(intent)
    }


    private fun ComprobarSesion() {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null) {
            startActivity(Intent(this, pueba::class.java))
            finishAffinity()
        } else {
            /*Toast.makeText(
                applicationContext, "Bienvenido ${firebaseUser.email}",
                Toast.LENGTH_SHORT
            ).show()*/
        }
    }
}