package com.example.koalaap.Administrador

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.koalaap.Modelos.Modelopdf
import com.example.koalaap.R
import com.example.koalaap.databinding.ActivityTopDescargadosBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TopDescargados : AppCompatActivity() {

    private lateinit var binding : ActivityTopDescargadosBinding
    private lateinit var pdfArrayList: ArrayList<Modelopdf>
    private lateinit var adapatadorPdfAdmin : AdaptadorPdfAdmin




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityTopDescargadosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        topDescargados()
    }

    private fun topDescargados() {
        pdfArrayList= ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Libros")
        ref.orderByChild("contadorDescargas").limitToLast(5)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    pdfArrayList.clear()
                    for(ds in snapshot.children){
                        val modelo= ds.getValue(Modelopdf::class.java)
                        pdfArrayList.add(modelo!!)
                    }
                    adapatadorPdfAdmin= AdaptadorPdfAdmin(this@TopDescargados, pdfArrayList)
                    binding.RvTopDescargados.adapter= adapatadorPdfAdmin
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

}