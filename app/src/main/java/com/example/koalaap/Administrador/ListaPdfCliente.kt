package com.example.koalaap.Administrador

import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.example.koalaap.Modelos.Modelopdf
import com.example.koalaap.databinding.ActivityListaPdfAdminBinding
import com.example.koalaap.databinding.ActivityListaPdfClienteBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception

class ListaPdfCliente : AppCompatActivity() {

    private lateinit var binding: ActivityListaPdfClienteBinding

    private var idCategoria = ""
    private var tituloCategoria = ""

    private lateinit var pdfArrayList: ArrayList<Modelopdf>
    private lateinit var  adaptadorPdfCliente: AdaptadorPdfCliente


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaPdfClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        idCategoria = intent.getStringExtra("idCategoria")!!
        tituloCategoria = intent.getStringExtra("tituloCategoria")!!

        binding.TxtCategoriaLibro.text = tituloCategoria

        binding.IbRegresar.setOnClickListener(){
            onBackPressedDispatcher.onBackPressed()
        }

        listarLibros()

        binding.EtBuscarLibroCliente.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(libro: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    adaptadorPdfCliente.filter.filter(libro)
                }catch (e: Exception){

                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }

    private fun listarLibros() {
        pdfArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Libros")
        ref.orderByChild("categoria").equalTo(idCategoria)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    pdfArrayList.clear()
                    for(ds in snapshot.children){
                        val modelo = ds.getValue(Modelopdf::class.java)
                        if(modelo != null){
                            pdfArrayList.add(modelo)
                        }
                    }
                    adaptadorPdfCliente = AdaptadorPdfCliente(this@ListaPdfCliente, pdfArrayList)
                    binding.RvLibrosCliente.adapter = adaptadorPdfCliente
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })


    }
}