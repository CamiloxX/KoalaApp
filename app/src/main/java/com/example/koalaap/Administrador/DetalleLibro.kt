package com.example.koalaap.Administrador

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.widget.Toast
import androidx.swiperefreshlayout.widget.CircularProgressDrawable.ProgressDrawableSize
import com.example.koalaap.LeerLibro
import com.example.koalaap.Modelos.ModeloComentario
import com.example.koalaap.R
import com.example.koalaap.databinding.ActivityDetalleLibroBinding
import com.example.koalaap.databinding.DialogAgregarComentarioBinding
import com.google.firebase.auth.FirebaseAuth
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

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var  progressDialog : ProgressDialog

    private var esFavorito = false
    private lateinit var comentarioArrayList: ArrayList<ModeloComentario>
    private lateinit var adaptadorComentario: AdaptadorComentario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDetalleLibroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idLibro= intent.getStringExtra("idLibro")!!


        MisFunciones.incrementarVistas(idLibro)


        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Por favor espere")
        progressDialog.setCanceledOnTouchOutside(false)

        firebaseAuth = FirebaseAuth.getInstance()

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

        binding.BtnAgregarFavorito.setOnClickListener{
        if(esFavorito){
            MisFunciones.eliminarFavoritos(this@DetalleLibro,idLibro)
        }else{
            agregarFavoritos()
        }
        }

        binding.IbAgregarComenatio.setOnClickListener{
            dialogComentar()
        }


        comprobarFavorito()
        cargarDetalleLibro()
        listarComentarios()

    }

    private fun listarComentarios() {
        comentarioArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Libros")
        ref.child(idLibro).child("Comentarios")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    comentarioArrayList.clear()
                    for(ds in snapshot.children){
                        val modelo = ds.getValue(ModeloComentario::class.java)
                        comentarioArrayList.add(modelo!!)
                    }
                    adaptadorComentario = AdaptadorComentario(this@DetalleLibro,comentarioArrayList)
                    binding.RvComentarios.adapter = adaptadorComentario
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    private var comentario = ""
    private fun dialogComentar() {
        val agregar_comentario_binding = DialogAgregarComentarioBinding.inflate(LayoutInflater.from(this))

        val builder = AlertDialog.Builder(this)
        builder.setView(agregar_comentario_binding.root)

        val alertDialog = builder.create()
        alertDialog.show()
        alertDialog.setCanceledOnTouchOutside(false)

        agregar_comentario_binding.IbCerrar.setOnClickListener{
            alertDialog.dismiss()
        }
        agregar_comentario_binding.BtnComentar.setOnClickListener{
            comentario = agregar_comentario_binding.EtAgregarComentario.text.toString().trim()
            if(comentario.isEmpty()){
                Toast.makeText(applicationContext, "Agrege un comentario", Toast.LENGTH_SHORT).show()
            }else{
                alertDialog.dismiss()
                agregarComentario()
            }
        }
    }

    private fun agregarComentario() {
        progressDialog.setMessage("Agregando comentario")
        progressDialog.show()

        val tiempo = "${System.currentTimeMillis()}"

        val hashMap = HashMap<String, Any>()
        hashMap["id"] = "$tiempo"
        hashMap["idLibro"] = "${idLibro}"
        hashMap["tiempo"] = "$tiempo"
        hashMap["comentario"] = "${comentario}"
        hashMap["uid"] = "${firebaseAuth.uid}"

        val ref = FirebaseDatabase.getInstance().getReference("Libros")
        ref.child(idLibro).child("Comentarios").child(tiempo)
            .setValue(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(applicationContext, "Su comentario se ha publicado", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{e->
                progressDialog.dismiss()
                Toast.makeText(applicationContext, "${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun comprobarFavorito() {
        val ref = FirebaseDatabase.getInstance().getReference("Usuarios")
        ref.child(firebaseAuth.uid!!).child("Favoritos").child(idLibro)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    esFavorito = snapshot.exists()
                    if(esFavorito){
                        binding.BtnAgregarFavorito.setCompoundDrawablesRelativeWithIntrinsicBounds(
                           0,
                            R.drawable.ic_agregar_favorito,
                            0,
                            0
                        )
                        binding.BtnAgregarFavorito.text = "Eliminar de favoritos"
                    }else{
                        binding.BtnAgregarFavorito.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            0,
                            R.drawable.ic_no_favorito,
                            0,
                            0
                        )
                        binding.BtnAgregarFavorito.text="Agregar a favoritos"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun agregarFavoritos(){
        val tiempo = System.currentTimeMillis()

        val hasMap = HashMap<String, Any>()
        hasMap["id"] = idLibro
        hasMap["tiempo"] = tiempo

        val ref = FirebaseDatabase.getInstance().getReference("Usuarios")
        ref.child(firebaseAuth.uid!!).child("Favoritos").child(idLibro)
            .setValue(hasMap)
            .addOnSuccessListener {
                Toast.makeText(applicationContext,"Libro añadido a favoritos", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{e->
                Toast.makeText(applicationContext,"No se agrego a favortios debido a ${e.message}", Toast.LENGTH_SHORT).show()
            }
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
            incrementarNumeroDesc()

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

    private fun incrementarNumeroDesc(){
        val ref = FirebaseDatabase.getInstance().getReference("Libros")
        ref.child(idLibro)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    var contadorDescarActual= "${snapshot.child("contadorDescargas").value}"
                    if(contadorDescarActual == "" || contadorDescarActual == "null" ){
                        contadorDescarActual= "0"
                    }
                    val nuevaDes= contadorDescarActual.toLong()+1

                    val hashMap = HashMap<String, Any> ()
                    hashMap["contadorDescargas"] = nuevaDes

                    val BDRef= FirebaseDatabase.getInstance().getReference("Libros")
                    BDRef.child(idLibro)
                        .updateChildren(hashMap)
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

}