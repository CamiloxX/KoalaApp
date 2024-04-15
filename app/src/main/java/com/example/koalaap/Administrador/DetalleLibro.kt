package com.example.koalaap.Administrador

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.koalaap.LeerLibro
import com.example.koalaap.R
import com.example.koalaap.databinding.ActivityDetalleLibroBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DetalleLibro : AppCompatActivity() {

    private var idLibro = ""
    private lateinit var binding : ActivityDetalleLibroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDetalleLibroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idLibro= intent.getStringExtra("idLibro")!!

        binding.IbRegresar.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }

        binding.BtnLeerLibro.setOnClickListener{
            val intent = Intent(this@DetalleLibro, LeerLibro::class.java)
            intent.putExtra("idLibro",idLibro)
            startActivity(intent)
        }

        cargarDetalleLibro()

    }

    private fun cargarDetalleLibro() {

        val ref= FirebaseDatabase.getInstance().getReference("Libros")
        ref.child(idLibro)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    //Obtener información del libro a travez del id
                    val categoria = "${snapshot.child("categoria").value}"
                    val contadorDescargas = "${snapshot.child("contadorDescargas").value}"
                    val contadorVistas = "${snapshot.child("contadorVistas").value}"
                    val descripcion = "${snapshot.child("descripcion").value}"
                    val tiempo = "${snapshot.child("tiempo").value}"
                    val titulo = "${snapshot.child("titulo").value}"
                    val url = "${snapshot.child("url").value}"


                    //Formato del tiempo
                    val fecha = MisFunciones.formatoTiempo(tiempo.toLong())
                    //Cargar categoria del libro
                    MisFunciones.CargarCategoria(categoria, binding.categoriaD)
                    //Cargar la miniatura del libro y el contador de páginas
                    MisFunciones.CargarPdfUrl("$url","$titulo",binding.VisualizadorPDF,binding.progressBar,binding.paginasD)
                    //Cargar tamaño
                    MisFunciones.CargarTamanioPdf("$url","$titulo",binding.tamanioD)

                    //Incrustamos la información faltante
                    binding.tituloLibroD.text= titulo
                    binding.descripcionD.text= descripcion
                    binding.vistasD.text= contadorVistas
                    binding.descargasD.text= contadorDescargas
                    binding.fechaD.text= fecha


                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

    }
}