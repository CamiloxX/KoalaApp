package com.example.koalaap.Administrador

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.koalaap.R
import com.example.koalaap.databinding.ActivityAgregarPdfBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import android.net.Uri
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.example.koalaap.Elegir_Rol
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.storage.FirebaseStorage




class Agregar_PDF : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    private lateinit var binding: ActivityAgregarPdfBinding
    private lateinit var categoriaArrayList: ArrayList<ModeloCategoria>
    private var pdfUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarPdfBinding.inflate(layoutInflater)
        setContentView(binding.root)


        firebaseAuth = FirebaseAuth.getInstance()
        CargarCategorias()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.IbRegresar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.AdjuntarPdfIb.setOnClickListener {
            ElegirPdf()
        }

        binding.TvCategoriaLibro.setOnClickListener {
            SeleccionarCat()
        }

        binding.BtnSubirPdf.setOnClickListener {
             ValidarInformacion()
        }
    }
    private var titulo = ""
    private var descripcion =""
    private var categoria = ""
    private fun ValidarInformacion() {
        titulo= binding.EtPdf.text.toString().trim()
        descripcion = binding.EtDescripcionPdf.text.toString().trim()
        categoria = binding.TvCategoriaLibro.text.toString().trim()

        if (titulo.isEmpty()){
Toast.makeText(this, "Ingrese Titulo", Toast.LENGTH_SHORT).show()
        }
        else if(descripcion.isEmpty()){
            Toast.makeText(this,"Ingrese Descripcion", Toast.LENGTH_SHORT).show()
        }
        else if (categoria.isEmpty()){
            Toast.makeText(this ,"Seleccione Categoria", Toast.LENGTH_SHORT).show()

        }
        else if(pdfUri==null){
            Toast.makeText(this ,"Adjunte un PDF", Toast.LENGTH_SHORT).show()
        }
        else{
            SubirPdfStore()
        }
    }

    private fun SubirPdfStore() {
    progressDialog.setMessage("Subiendo PDF :D")
        progressDialog.show()
        val tiempo = System.currentTimeMillis()
        val ruta_pdf = "PDF/$tiempo"
        val storageReference = FirebaseStorage.getInstance().getReference(ruta_pdf)
        storageReference.putFile(pdfUri!!)
            .addOnSuccessListener {tarea->
                val uriTask : Task<Uri> = tarea.storage.downloadUrl
                while (!uriTask.isSuccessful);
                val UrlPdfSubido = "${uriTask.result}"
                SubirPdfBD(UrlPdfSubido,tiempo)
            }
            .addOnFailureListener{ e->
                progressDialog.dismiss()
                Toast.makeText(this ,"Ha fallado la subida del archivo ,debido a ${e.message}", Toast.LENGTH_SHORT).show()

            }

    }

    private fun SubirPdfBD(urlPdfSubido: String, tiempo: Long) {
    progressDialog.setMessage("Subido pdf a la BD")
    val uid = firebaseAuth.uid
        val hashMap : HashMap<String, Any> = HashMap()
        hashMap["uid"] = "$uid"
        hashMap["id"]= "$tiempo"
        hashMap["titulo"]= titulo
        hashMap["descripcion"]= descripcion
        hashMap["categoria"]= id_categoria
        hashMap["url"]= urlPdfSubido
        hashMap["tiempo"] = tiempo
        hashMap["contadorVistas"]= 0
        hashMap["contadorDescargas"] = 0


        val ref = FirebaseDatabase.getInstance().getReference( "Libros")
        ref.child("$tiempo")
            .setValue(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this ,"Libro subido con exito",Toast.LENGTH_SHORT).show()
                binding.EtPdf.setText("")
                binding.EtDescripcionPdf.setText("")
                binding.TvCategoriaLibro.setText("")
                pdfUri = null
            }

            .addOnFailureListener {e->
                 progressDialog.dismiss()
                Toast.makeText(this ,"Ha fallado la subida del archivo ,debido a ${e.message}",Toast.LENGTH_SHORT).show()

            }


    }

    private fun CargarCategorias() {
        categoriaArrayList = ArrayList()
        val ref =
            FirebaseDatabase.getInstance().getReference("Categorías").orderByChild("categoria")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                categoriaArrayList.clear()
                for (ds in snapshot.children) {
                    val modelo = ds.getValue(ModeloCategoria::class.java)
                    categoriaArrayList.add(modelo!!)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private var id_categoria = ""
    private var titulo_categoria = ""
    private fun SeleccionarCat() {
        val categoriasArray = arrayOfNulls<String>(categoriaArrayList.size)
        for (i in categoriasArray.indices) {
            categoriasArray[i] = categoriaArrayList[i].categoria
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Seleccionar categoria")
            .setItems(categoriasArray) { dialog, which ->
                // Actualización correcta de las variables
                id_categoria = categoriaArrayList[which].id
                titulo_categoria = categoriaArrayList[which].categoria
                // Aquí debes actualizar el texto sin llamar a la variable como una función
                binding.TvCategoriaLibro.text = titulo_categoria
            }
            .show()
    }

    private fun ElegirPdf() {
        val intent = Intent()
        intent.type = "application/pdf"
        intent.action = Intent.ACTION_GET_CONTENT
        pdfActivityRL.launch(intent)


    }

    private val pdfActivityRL = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { resultado ->
            if (resultado.resultCode == RESULT_OK) {
                pdfUri = resultado.data!!.data
            } else {
                Toast.makeText(this, "Cancelando", Toast.LENGTH_SHORT).show()
            }
        }

    )
}



