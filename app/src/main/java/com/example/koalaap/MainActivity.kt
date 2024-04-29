package com.example.koalaap

import com.example.koalaap.Administrador.Fragmentos_Admin.Fragment_admin_buscar
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.koalaap.Administrador.Fragmentos_Admin.Fragment_admin_cuenta
import com.example.koalaap.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.example.koalaap.Administrador.Fragmentos_Admin.Fragment_admin_archivo
import com.example.koalaap.Administrador.Fragmentos_Admin.Fragment_admin_favoritos
import com.example.koalaap.Administrador.Fragmentos_Admin.Fragment_admin_principal


private lateinit var binding: ActivityMainBinding
private lateinit var firebaseAuth: FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        ComprobarSesion()
        VerFragmentoPrincipal()
        binding.BottomNavCliente.setOnItemSelectedListener { item->
            when(item.itemId){
                R.id.Menu_dashboard_cl->{
                    VerFragmentoPrincipal()
                    true
                }
                R.id.Menu_cuenta_cl->{
                    VerFragmentoCuenta()
                    true

                }
                R.id.Menu_subir_cl->{
                    VerFragmentoArchivo()
                    true
                }
                R.id.Menu_favoritos_cl->{
                    VerFragmentoFavoritos()
                    true
                }
                R.id.Menu_buscar_cl->{
                    VerFragmentoBuscar()
                    true
                }
                else->{
                    false
                }
            }

        }
    }

    private fun  VerFragmentoBuscar(){
        val nombre_titulo ="Dashboard"
        binding.TituloRLAdmin.text=nombre_titulo
        binding.ImagenRLAdmin.visibility = View.GONE
        val fragment = Fragment_admin_buscar()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.FragmentsAdmin.id ,fragment,"Fragment DashBoard")
        fragmentTransaction.commit()
    }

    private fun VerFragmentoCuenta(){
        val nombre_titulo ="Mi cuenta"
        binding.TituloRLAdmin.text=nombre_titulo
        binding.ImagenRLAdmin.visibility = View.GONE

        val fragment = Fragment_admin_cuenta()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.FragmentsAdmin.id ,fragment,"Fragment Mi cuenta")
        fragmentTransaction.commit()

    }

    private fun VerFragmentoPrincipal() {
        val nombre_titulo = ""
        binding.TituloRLAdmin.text = nombre_titulo

        val imagenResId = R.drawable.bienvenida_img
            binding.ImagenRLAdmin.setImageResource(imagenResId)
            binding.ImagenRLAdmin.visibility = View.VISIBLE // Hacer visible el ImageView

            val fragment = Fragment_admin_principal()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(binding.FragmentsAdmin.id, fragment, "Fragment Principal")
            fragmentTransaction.commit()


    }

    private fun VerFragmentoArchivo() {
        val nombre_titulo = "Agregar PDF"
        binding.TituloRLAdmin.text = nombre_titulo
        binding.ImagenRLAdmin.visibility = View.GONE

        val fragment = Fragment_admin_archivo()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.FragmentsAdmin.id, fragment, "Fragment Archivo")
        fragmentTransaction.commit()
    }
    private fun VerFragmentoFavoritos() {
        val nombre_titulo = "Favoritos"
        binding.TituloRLAdmin.text = nombre_titulo
        binding.ImagenRLAdmin.visibility = View.GONE

        val fragment = Fragment_admin_favoritos()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.FragmentsAdmin.id, fragment, "Fragment Favoritos")
        fragmentTransaction.commit()
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

