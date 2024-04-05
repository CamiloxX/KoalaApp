package com.example.koalaap.Administrador

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.koalaap.MainActivity
import com.example.koalaap.databinding.ActivityLoginAdminBinding
import com.google.firebase.auth.FirebaseAuth

class Login_Admin : AppCompatActivity() {

    private lateinit var binding: ActivityLoginAdminBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Iniciando Sesion")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.BtnLoginAdmin.setOnClickListener {
            validarInformacion()
        }

        // Configuración del botón de registro
        binding.BtnRegister.setOnClickListener {
            val intent = Intent(this, Registrar_Admin::class.java)
            startActivity(intent)
        }
    }

    private var email = ""
    private var password = ""

    private fun validarInformacion() {
        email = binding.EtEmailAdmin.text.toString().trim()
        password = binding.EtPasswordAdmin.text.toString().trim()

        if (email.isEmpty()) {
            binding.EtEmailAdmin.error = "Ingrese su Correo"
            binding.EtEmailAdmin.requestFocus()
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.EtEmailAdmin.error = "Correo Invalido"
            binding.EtEmailAdmin.requestFocus()
        } else if (password.isEmpty()) {
            binding.EtPasswordAdmin.error = "Ingrese la contraseña"
            binding.EtPasswordAdmin.requestFocus()
        } else {
            loginAdmin()
        }
    }

    private fun loginAdmin() {
        progressDialog.setMessage("Iniciando Sesion")
        progressDialog.show()

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                progressDialog.dismiss()
                startActivity(Intent(this@Login_Admin, MainActivity::class.java))
                finishAffinity()
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(
                    applicationContext,
                    "No se pudo iniciar sesión debido a ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}
