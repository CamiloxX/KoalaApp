package com.example.koalaap.Administrador

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.koalaap.MainActivity
import com.example.koalaap.databinding.ActivityRegistrarAdminBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class Registrar_Admin : AppCompatActivity() {


    private lateinit var binding:ActivityRegistrarAdminBinding
    private lateinit var  firebaseAuth: FirebaseAuth
    private lateinit var  progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistrarAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth=FirebaseAuth.getInstance()
        progressDialog=ProgressDialog(this)
        progressDialog.setTitle("Espere Por favor")
        progressDialog.setCanceledOnTouchOutside(false)
        binding.IbRegresar.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }

        binding.BtnRegistrarAdmin.setOnClickListener {
            ValidarInformacion()
        }
    }
    var nombres=""
    var apellidos=""
    var username=""
    var email=""
    var password=""
    var r_password=""

    private fun ValidarInformacion() {
    nombres = binding.EtNombresAdmin.text.toString().trim()
        apellidos = binding.EtApellidosAdmin.text.toString().trim()
        username = binding.EtUserAdmin.text.toString().trim()
        email = binding.EtEmailAdmin.text.toString().trim()+ "@utadeo.edu.co"
        password =binding.EtPasswordAdmin.text.toString().trim()
        r_password = binding.EtPasswordAdminR.text.toString().trim()


        if(nombres.isEmpty()){
            binding.EtNombresAdmin.error="Ingrese Nombres"
            binding.EtNombresAdmin.requestFocus()
        }
        else if(apellidos.isEmpty()) {
            binding.EtApellidosAdmin.error = "Ingrese Apellido"
            binding.EtApellidosAdmin.requestFocus()
        }

        else if(username.isEmpty()){
            binding.EtUserAdmin.error="Ingrese Nombre Usuario"
            binding.EtUserAdmin.requestFocus()
        }
        else if (email.isEmpty()){
            binding.EtEmailAdmin.error = "Ingrese Email"
            binding.EtEmailAdmin.requestFocus()
        }

        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.EtEmailAdmin.error = " Email no es valido"
            binding.EtEmailAdmin.requestFocus()
        }
        else if(password.isEmpty()){
            binding.EtPasswordAdmin.error="Ingrese Contraseña"
            binding.EtPasswordAdmin.requestFocus()

        }
        else if(password.length <6){
            binding.EtPasswordAdmin.error="La contraseña debe tener mas de 6 caracteres"
            binding.EtPasswordAdmin.requestFocus()
        }
        else if(r_password.isEmpty()){
            binding.EtPasswordAdminR.error="Repita la contraseña"
            binding.EtPasswordAdminR.requestFocus()

        }

        else if(password != r_password){
            binding.EtPasswordAdminR.error="Contraseña no Coinciden"
            binding.EtPasswordAdminR.requestFocus()
        }
        else{
            CrearCuentaAdmin(email, password)
        }
    }

    private fun CrearCuentaAdmin(email: String, password: String) {
        progressDialog.setMessage("Creando Cuenta")
        progressDialog.show()

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                // Enviar correo de verificación
                firebaseAuth.currentUser?.sendEmailVerification()
                    ?.addOnSuccessListener {
                        Toast.makeText(applicationContext, "Correo de verificación enviado. Verifique su bandeja.", Toast.LENGTH_LONG).show()

                        // Agregar datos a la base de datos
                        AgregarInfoBD()

                        // Volver a la ventana anterior después de enviar el correo
                        startActivity(Intent(this, Login_Admin::class.java))
                    }
                    ?.addOnFailureListener { e ->
                        progressDialog.dismiss()
                        Toast.makeText(applicationContext, "Error al enviar el correo de verificación: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(applicationContext, "Cuenta no creada: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun AgregarInfoBD() {


        progressDialog.setMessage("Guardando Informacion")
        val tiempo = System.currentTimeMillis()
        val uid = firebaseAuth.uid
        val datos_admin : HashMap<String , Any?> = HashMap()
        datos_admin["uid"]= uid
        datos_admin["nombres"] = nombres
        datos_admin["apellidos"]= apellidos
        datos_admin["username"]= username
        datos_admin["email"]= email
        datos_admin["rol"]= "cliente"
        datos_admin["tiempo de registro"]=tiempo
        datos_admin["imagen"]= ""

        val reference = FirebaseDatabase.getInstance().getReference("Usuarios")
        reference.child(uid!!)
            .setValue(datos_admin)
            .addOnSuccessListener{
                progressDialog.dismiss()
                Toast.makeText(applicationContext, "Cuenta creada. Por favor, verifique su correo.", Toast.LENGTH_SHORT).show()
                // Redirigir solo si el correo fue verificado
                VerificarCorreo()
            }
            .addOnFailureListener {e->
            progressDialog.dismiss()
                Toast.makeText(applicationContext, "No se pudo guardar la informacion  ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }

    }

    private fun VerificarCorreo() {
        firebaseAuth.currentUser?.reload()?.addOnSuccessListener {
            if (firebaseAuth.currentUser?.isEmailVerified == true) {
                startActivity(Intent(this, MainActivity::class.java))
                finishAffinity()
            } else {
                Toast.makeText(applicationContext, "Por favor, verifique su correo antes de continuar.", Toast.LENGTH_LONG).show()
            }
        }
    }
}



