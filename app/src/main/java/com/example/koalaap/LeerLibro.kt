package com.example.koalaap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.koalaap.Administrador.Constantes
import com.example.koalaap.databinding.ActivityDetalleLibroBinding
import com.example.koalaap.databinding.ActivityLeerLibroBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class LeerLibro : AppCompatActivity() {

    private lateinit var binding: ActivityLeerLibroBinding

    var idLibro= ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLeerLibroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idLibro= intent.getStringExtra("idLibro")!!

        binding.IbRegresar.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }

        cargarInformacionLibro()

    }

    private fun cargarInformacionLibro() {
        val ref= FirebaseDatabase.getInstance().getReference("Libros")
        ref.child(idLibro)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val pdfUrl= snapshot.child("url").value
                    cargarLibroStorage("$pdfUrl")
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    private fun cargarLibroStorage(pdfUrl: String) {
       val reference= FirebaseStorage.getInstance().getReferenceFromUrl(pdfUrl)
        reference.getBytes(Constantes.Maximo_bytes_pdf)
            .addOnSuccessListener { bytes->

                //Cargar pdf
                binding.VisualizadorPDF.fromBytes(bytes)
                    .swipeHorizontal(false)
                    .onPageChange{pagina, contadorPaginas->
                           val paginaActual = pagina+1
                        binding.TxtLeerLibro.text= "$paginaActual / $contadorPaginas"
                    }
                    .load()
                binding.progressBar.visibility= View.GONE
            }
            .addOnFailureListener{e->
                binding.progressBar.visibility= View.GONE
            }

    }
}