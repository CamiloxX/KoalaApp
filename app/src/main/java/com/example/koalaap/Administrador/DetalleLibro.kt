package com.example.koalaap.Administrador

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import com.example.koalaap.LeerLibro
import com.example.koalaap.R
import com.example.koalaap.databinding.ActivityDetalleLibroBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.io.FileOutputStream

class DetalleLibro : AppCompatActivity() {

    private var idLibro = ""
    private lateinit var binding : ActivityDetalleLibroBinding
    private var  tituloLibro = ""
    private var  urlLibro = ""
    private lateinit var  progressDialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDetalleLibroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idLibro= intent.getStringExtra("idLibro")!!

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Por favor espere")
        progressDialog.setCanceledOnTouchOutside(false)
        binding.IbRegresar.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }





        binding.BtnLeerLibro.setOnClickListener{
            val intent = Intent(this@DetalleLibro, LeerLibro::class.java)
            intent.putExtra("idLibro",idLibro)
            startActivity(intent)

        }

        binding.BtnDescargarLibro.setOnClickListener{

            descargarLibro()
        }

        cargarDetalleLibro()

    }

    private fun descargarLibro() {
        progressDialog.setMessage("Descargando Libro")
        progressDialog.show()

        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(urlLibro)
        storageReference.getBytes(Constantes.Maximo_bytes_pdf)
            .addOnSuccessListener {bytes->

                guardarLibroDisp(bytes)

            }
            .addOnFailureListener{e->
                progressDialog.dismiss()
                Toast.makeText(applicationContext,"$${e.message}",Toast.LENGTH_SHORT).show()
            }
    }

    private fun guardarLibroDisp(bytes :  ByteArray) {

     val nombreLibro_extension = "$tituloLibro.pdf"
        try {
            val carpeta = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            carpeta.mkdirs()
            val archivo_ruta = carpeta.path+"/"+nombreLibro_extension
            val out = FileOutputStream(archivo_ruta)
            out.write(bytes)
            out.close()

            Toast.makeText(applicationContext,"Pdf Guardado",Toast.LENGTH_SHORT).show()
            progressDialog.dismiss()

        }catch (e: Exception){



            Toast.makeText(applicationContext,"${e.message}",Toast.LENGTH_SHORT).show()
            progressDialog.dismiss()
        }







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
                    tituloLibro = "${snapshot.child("titulo").value}"
                    urlLibro = "${snapshot.child("url").value}"


                    //Formato del tiempo
                    val fecha = MisFunciones.formatoTiempo(tiempo.toLong())
                    //Cargar categoria del libro
                    MisFunciones.CargarCategoria(categoria, binding.categoriaD)
                    //Cargar la miniatura del libro y el contador de páginas
                    MisFunciones.CargarPdfUrl("$urlLibro","$tituloLibro",binding.VisualizadorPDF,binding.progressBar,binding.paginasD)
                    //Cargar tamaño
                    MisFunciones.CargarTamanioPdf("$urlLibro","$tituloLibro",binding.tamanioD)

                    //Incrustamos la información faltante
                    binding.tituloLibroD.text= tituloLibro
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