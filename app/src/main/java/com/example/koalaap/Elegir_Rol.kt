package com.example.koalaap
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.koalaap.Administrador.Registrar_Admin
import com.example.koalaap.databinding.ActivityElegirRolBinding

class Elegir_Rol : AppCompatActivity() {

    private lateinit var binding : ActivityElegirRolBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityElegirRolBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.BtnRolAdministrador.setOnClickListener{

           // Toast.makeText(applicationContext, "Rol administrador",Toast.LENGTH_SHORT).show()
        startActivity(Intent(this@Elegir_Rol,Registrar_Admin::class.java))
        }
        binding.BtnRolCliente.setOnClickListener {
            Toast.makeText(applicationContext, "Rol Cliente",Toast.LENGTH_SHORT).show()
        }
    }

}