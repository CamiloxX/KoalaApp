package com.example.koalaap.Administrador

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.koalaap.MainActivity
import com.example.koalaap.MainActivityCliente
import com.example.koalaap.R
import com.example.koalaap.databinding.ActivityLoginAdminBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import java.lang.Exception

class Login_Admin : AppCompatActivity() {

    private lateinit var binding: ActivityLoginAdminBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

       //Se pone aviso de que se esta iniciando sesion
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Iniciando Sesion")
        progressDialog.setCanceledOnTouchOutside(false)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("1064191765387-gqiv8qvj24n27fd3883feva9ag4bs3k1.apps.googleusercontent.com")
            .requestEmail()
            .build()

        mGoogleSignInClient= GoogleSignIn.getClient(this, gso)


        binding.BtnLoginAdmin.setOnClickListener {
            validarInformacion()
        }

        binding.BtnLoginGoogle.setOnClickListener {
            iniciarSesionGoogle()
        }


        // Configuración del botón de registro
        binding.BtnRegister.setOnClickListener {
            val intent = Intent(this, Registrar_Admin::class.java)
            startActivity(intent)
        }
    }

    private fun iniciarSesionGoogle() {
        val googleSignIntent= mGoogleSignInClient.signInIntent
        googleSignInARL.launch(googleSignIntent)
    }

    private val googleSignInARL = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { resultado ->
        if (resultado.resultCode == RESULT_OK) {
            val data = resultado.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val cuenta = task.getResult(ApiException::class.java)
                autenticarGoogleFirebase(cuenta.idToken)
            } catch (apiException: ApiException) {
                Log.e("GoogleSignIn", "Sign in failed with status code: ${apiException.statusCode}")
                Toast.makeText(applicationContext, "Google Sign-In failed: ${apiException.message}", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("GoogleSignIn", "Error inesperado: ${e.message}")
                Toast.makeText(applicationContext, "Error inesperado: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            Log.e("GoogleSignIn", "Sign in canceled, resultCode: ${resultado.resultCode}")
            Toast.makeText(applicationContext, "Cancelado: resultCode: ${resultado.resultCode}", Toast.LENGTH_SHORT).show()
        }
    }



    private fun autenticarGoogleFirebase(idToken: String?) {
          val credencial= GoogleAuthProvider.getCredential(idToken,null)
        firebaseAuth.signInWithCredential(credencial)
            .addOnSuccessListener {authResult->
                if (authResult.additionalUserInfo!!.isNewUser){
                    GuardarInformacionBD()
                }else{
                    startActivity(Intent(this,MainActivityCliente::class.java))
                    finishAffinity()
                }

            }
            .addOnFailureListener{e->
                Toast.makeText(applicationContext,"${e.message}",Toast.LENGTH_SHORT).show()

            }
    }

    private fun  GuardarInformacionBD() {
        progressDialog.setMessage("Se está registrando su información")
        progressDialog.show()

        //Obtener información del gmail

        val uidGoogle = firebaseAuth.uid
        val emailGoogle = firebaseAuth.currentUser?.email
        val nombreGoogle = firebaseAuth.currentUser?.displayName

        val nombre_usuario_google= nombreGoogle.toString()
        val tiempo = System.currentTimeMillis() //Tiempo con el que se crea el id del usuario

        val datos_cliente = HashMap<String, Any?> ()
        datos_cliente["uid"] = uidGoogle
        datos_cliente["nombres"] = nombre_usuario_google
        datos_cliente["email"] = emailGoogle
        datos_cliente["tiempo de registro"] = tiempo
        datos_cliente["imagen"] = ""
        datos_cliente["rol"] = "cliente"

        val reference = FirebaseDatabase.getInstance().getReference("Usuarios")
        reference.child(uidGoogle!!)
            .setValue(datos_cliente)
            .addOnSuccessListener {
                progressDialog.dismiss()
                startActivity(Intent(applicationContext, MainActivityCliente::class.java))
                Toast.makeText(applicationContext, "Se ha registrado correctamente", Toast.LENGTH_SHORT).show()
                finishAffinity()
            }
            .addOnFailureListener {e->
                Toast.makeText(applicationContext,"${e.message}", Toast.LENGTH_SHORT).show()

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
//Autentica si el inicio de sesion es exitoso
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
