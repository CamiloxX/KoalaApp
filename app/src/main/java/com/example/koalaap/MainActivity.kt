package com.example.koalaap

import com.example.koalaap.Administrador.Fragmentos_Admin.Fragment_admin_dashboard
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.koalaap.Administrador.Fragmentos_Admin.Fragment_admin_cuenta
import com.example.koalaap.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.example.koalaap.Administrador.Fragmentos_Admin.Fragment_admin_archivo


private lateinit var binding: ActivityMainBinding
private lateinit var firebaseAuth: FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        ComprobarSesion()
        VerFragmentoDashBoard()
        binding.BottomNavCliente.setOnItemSelectedListener { item->
            when(item.itemId){
                R.id.Menu_dashboard_cl->{
                    VerFragmentoDashBoard()
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
                else->{
                    false
                }
            }

        }
    }

    private fun  VerFragmentoDashBoard(){
        val nombre_titulo ="Dashboard"
        binding.TituloRLAdmin.text=nombre_titulo
        val fragment = Fragment_admin_dashboard()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.FragmentsAdmin.id ,fragment,"Fragment DashBoard")
        fragmentTransaction.commit()
    }

    private fun VerFragmentoCuenta(){
        val nombre_titulo ="Mi cuenta"
        binding.TituloRLAdmin.text=nombre_titulo

        val fragment = Fragment_admin_cuenta()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.FragmentsAdmin.id ,fragment,"Fragment Mi cuenta")
        fragmentTransaction.commit()

    }
    private fun VerFragmentoArchivo() {
        val nombre_titulo = ""
        binding.TituloRLAdmin.text = nombre_titulo

        val fragment = Fragment_admin_archivo()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.FragmentsAdmin.id, fragment, "Fragment Archivo")
        fragmentTransaction.commit()
    }

    private fun ComprobarSesion() {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null) {
            startActivity(Intent(this, Elegir_Rol::class.java))
            finishAffinity()
        } else {
            /*Toast.makeText(
                applicationContext, "Bienvenido ${firebaseUser.email}",
                Toast.LENGTH_SHORT
            ).show()*/
        }
    }
}

