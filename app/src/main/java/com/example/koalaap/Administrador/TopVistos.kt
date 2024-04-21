package com.example.koalaap.Administrador

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.koalaap.R
import com.example.koalaap.databinding.ActivityTopVistosBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TopVistos : AppCompatActivity() {


    private lateinit var binding : ActivityTopVistosBinding
private lateinit var pdfArrayList: ArrayList<Modelopdf>
private lateinit var adapatadorPdfAdmin : AdaptadorPdfAdmin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityTopVistosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        topVistos()


    }

    private fun topVistos() {
       pdfArrayList= ArrayList()
       val ref = FirebaseDatabase.getInstance().getReference("Libros")
        ref.orderByChild("contadorvistas").limitToLast(10)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                     pdfArrayList.clear()
                    for(ds in snapshot.children){
                        val modelo= ds.getValue(Modelopdf::class.java)
                        pdfArrayList.add(modelo!!)
                    }
                    adapatadorPdfAdmin= AdaptadorPdfAdmin(this@TopVistos, pdfArrayList)
                    binding.RvTopVistos.adapter= adapatadorPdfAdmin
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }
}