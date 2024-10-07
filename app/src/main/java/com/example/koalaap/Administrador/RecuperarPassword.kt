package com.example.koalaap.Administrador

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.koalaap.databinding.ActivityRecuperarPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class RecuperarPassword : AppCompatActivity() {

    private lateinit var binding: ActivityRecuperarPasswordBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecuperarPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        // Configuración del botón "regresar"
        binding.IbRegresar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Configurar el botón para recuperar contraseña
        getRecuperar()
    }

    private fun getRecuperar() {
        binding.BtnEnviarCorreo.setOnClickListener {
            val correo = binding.EtEmailPassword.text.toString().trim()

            if (correo.isNotEmpty()) {
                progressDialog = ProgressDialog(this)
                progressDialog.setMessage("Espere un momento")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()

                // Llamada al método para enviar el correo de recuperación
                getEnviarCorreo(correo)
            } else {
                Toast.makeText(this, "Por favor, ingrese su correo electrónico", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getEnviarCorreo(correo: String) {
        firebaseAuth.sendPasswordResetEmail(correo)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    progressDialog.dismiss()
                    Toast.makeText(this, "Correo enviado. Revise su bandeja de entrada.", Toast.LENGTH_SHORT).show()
                } else {
                    progressDialog.dismiss()
                    Toast.makeText(this, "Error al enviar el correo", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
