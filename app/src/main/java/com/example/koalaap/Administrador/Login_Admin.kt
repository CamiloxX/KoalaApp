package com.example.koalaap.Administrador

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.koalaap.MainActivity
import com.example.koalaap.MainActivityCliente
import com.example.koalaap.databinding.ActivityLoginAdminBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class Login_Admin : AppCompatActivity() {

    private lateinit var binding: ActivityLoginAdminBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()


        binding.BtnRecuperarPassword.setOnClickListener {
            val intent = Intent(this, RecuperarPassword::class.java)
            startActivity(intent)
        }

        //Se pone aviso de que se esta iniciando sesion
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

    // Autentica si el inicio de sesión es exitoso
    private fun loginAdmin() {
        progressDialog.setMessage("Iniciando Sesión")
        progressDialog.show()

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                val user = authResult.user
                if (user != null) {
                    // Verificar si el correo está verificado
                    if (user.isEmailVerified) {
                        val reference = FirebaseDatabase.getInstance().getReference("Usuarios")
                        reference.child(user.uid)
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val rol =
                                        snapshot.child("rol").value as? String // Asegúrate de que 'rol' sea el nombre del campo
                                    when (rol) {
                                        "admin" -> {
                                            progressDialog.dismiss()
                                            startActivity(
                                                Intent(
                                                    this@Login_Admin,
                                                    MainActivity::class.java
                                                )
                                            )
                                            finishAffinity()
                                        }

                                        "cliente" -> {
                                            progressDialog.dismiss()
                                            startActivity(
                                                Intent(
                                                    this@Login_Admin,
                                                    MainActivityCliente::class.java
                                                )
                                            )
                                            finishAffinity()
                                        }
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    progressDialog.dismiss()
                                    // Manejar error aquí, si es necesario
                                }
                            })
                    } else {
                        progressDialog.dismiss()
                        // Si el usuario no está verificado, enviar correo de verificación
                        enviarCorreoVerificacion(email)
                        startActivity(Intent(this, Login_Admin::class.java))
                        finishAffinity()
                    }
                }
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                // Manejar errores específicos de inicio de sesión
                Toast.makeText(
                    applicationContext,
                    "No se pudo iniciar sesión debido a: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    // Método para enviar correo de verificación al correo ingresado
    private fun enviarCorreoVerificacion(email: String) {
        // Solo necesitamos enviar el correo de verificación al usuario ya autenticado
        firebaseAuth.currentUser?.sendEmailVerification()
            ?.addOnCompleteListener { verificationTask ->
                if (verificationTask.isSuccessful) {
                    Toast.makeText(
                        applicationContext,
                        "Correo de verificación enviado a $email.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Error al enviar el correo de verificación.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}