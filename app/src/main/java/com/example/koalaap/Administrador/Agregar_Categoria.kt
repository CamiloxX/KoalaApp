package com.example.koalaap.Administrador.Fragmentos_Admin

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.koalaap.MainActivity
import com.example.koalaap.databinding.ActivityAgregarCategoriaBinding
import com.example.koalaap.databinding.FragmentAdminBuscarBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class Agregar_Categoria : AppCompatActivity(){

    private lateinit var binding : ActivityAgregarCategoriaBinding
    private lateinit var  firebaseAuth: FirebaseAuth
    private lateinit var progressDialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAgregarCategoriaBinding.inflate(layoutInflater)
        setContentView(binding.root)


        firebaseAuth= FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere por favor...")
        progressDialog.setCanceledOnTouchOutside(false)

    binding.IbRegresar.setOnClickListener{
    onBackPressedDispatcher.onBackPressed()
}

        binding.AgregarCategoriaBD.setOnClickListener{
            ValidarDatos()
        }

    }


    private var categoria= ""

    private fun ValidarDatos() {

        categoria = binding.EtCategoria.text.toString().trim()
        if(categoria.isEmpty()){
            Toast.makeText(applicationContext,"Ingrese una categoria", Toast.LENGTH_SHORT).show()
        }else{
            AgregarCategoriaBD()
        }
    }

    private fun AgregarCategoriaBD() {
        progressDialog.setMessage("Agregando categoria...")
        progressDialog.show()

        val tiempo= System.currentTimeMillis()

        val hashMap= HashMap<String, Any>()
        hashMap["id"]= "$tiempo"
        hashMap["categoria"]= categoria
        hashMap["tiempo"]=tiempo
        hashMap["uid"]="${firebaseAuth.uid}"

        val ref= FirebaseDatabase.getInstance().getReference("Categorías")
        ref.child("$tiempo")
            .setValue(hashMap)
            .addOnSuccessListener {

                progressDialog.dismiss()
                Toast.makeText(applicationContext,"Se agregó la categoría a la BD", Toast.LENGTH_SHORT).show()
                binding.EtCategoria.setText("")
                startActivity(Intent(this@Agregar_Categoria, MainActivity::class.java))
                finishAffinity()
            }
            .addOnFailureListener {e->
                progressDialog.dismiss()
                Toast.makeText(applicationContext,"No se agregó la categoría debido a ${e.message}",Toast.LENGTH_SHORT).show()
            }
    }




    }

