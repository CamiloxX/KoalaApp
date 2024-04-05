package com.example.koalaap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.example.koalaap.Administrador.Login_Admin
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


private lateinit var firebaseAuth: FirebaseAuth
class Bienvenida : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenida)
        firebaseAuth = FirebaseAuth.getInstance()
        verBienvenida()
    }
    fun verBienvenida(){
        object : CountDownTimer(2000, 1000){
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {

                ComprobarSesion()
                //Dirigirnos a la actividad MainActivity
             //   val intent = Intent(this@Bienvenida,Elegir_Rol::class.java)
             //   startActivity(intent)
             //   finishAffinity()
            }

        }.start()
    }


    fun ComprobarSesion(){
        val  firebaseUser = firebaseAuth.currentUser
        if(firebaseUser ==  null){
            startActivity(Intent(this, Login_Admin::class.java))
            finishAffinity()
        }else{
            val reference = FirebaseDatabase.getInstance().getReference("Usuarios")
            reference.child(firebaseUser.uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val rol = snapshot.child("rol").value
                        if(rol == "admin"){
                            startActivity(Intent(this@Bienvenida,MainActivity::class.java))
                            finishAffinity()
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }
    }
}