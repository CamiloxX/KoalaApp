package com.example.koalaap.Administrador.Fragmentos_Admin

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.koalaap.databinding.FragmentAdminArchivoBinding
import com.example.koalaap.Modelos.ModeloCategoria
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.util.HashMap

class Fragment_admin_archivo : Fragment() {
    private var _binding: FragmentAdminArchivoBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    private var pdfUri: Uri? = null
    private lateinit var categoriaArrayList: ArrayList<ModeloCategoria>
    private var titulo = ""
    private var descripcion = ""
    private var categoria = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        _binding = FragmentAdminArchivoBinding.inflate(inflater, container, false)
        firebaseAuth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(context)
        categoriaArrayList = ArrayList()
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cargarCategorias()

        binding.TvCategoriaLibro.setOnClickListener {
            seleccionarCategoria()
        }

        binding.AdjuntarPdfIb.setOnClickListener {
            elegirPdf()
        }

        binding.BtnSubirPdf.setOnClickListener {
            validarInformacion()
        }
    }

    private fun cargarCategorias() {
        val ref = FirebaseDatabase.getInstance().getReference("Categorías")
        ref.orderByChild("categoria").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                categoriaArrayList.clear()
                for (ds in snapshot.children) {
                    val modelo = ds.getValue(ModeloCategoria::class.java)
                    modelo?.let { categoriaArrayList.add(it) }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error al cargar categorías.", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private var id_categoria = ""
    private var titulo_categoria = ""
    private fun seleccionarCategoria() {
        val categoriasArray = arrayOfNulls<String>(categoriaArrayList.size)
        for (i in categoriasArray.indices) {
            categoriasArray[i] = categoriaArrayList[i].categoria
        }

        context?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Seleccionar categoría")
                .setItems(categoriasArray) { _, which ->
                    id_categoria = categoriaArrayList[which].id
                    titulo_categoria = categoriaArrayList[which].categoria
                    categoria = categoriaArrayList[which].categoria
                    binding.TvCategoriaLibro.text = categoria
                }
                .show()
        }
    }

    private fun elegirPdf() {
        val intent = Intent()
        intent.type = "application/pdf"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PDF_PICK_CODE)
    }

    private fun validarInformacion() {
        titulo = binding.EtPdf.text.toString().trim()
        descripcion = binding.EtDescripcionPdf.text.toString().trim()
        if (titulo.isEmpty()) {
            Toast.makeText(context, "Ingrese Título", Toast.LENGTH_SHORT).show()
        } else if (descripcion.isEmpty()) {
            Toast.makeText(context, "Ingrese Descripción", Toast.LENGTH_SHORT).show()
        } else if (categoria.isEmpty()) {
            Toast.makeText(context, "Seleccione Categoría", Toast.LENGTH_SHORT).show()
        } else if (pdfUri == null) {
            Toast.makeText(context, "Adjunte un PDF", Toast.LENGTH_SHORT).show()
        } else {
            subirPdfStore()
        }
    }

    private fun subirPdfStore() {
        progressDialog.setMessage("Subiendo PDF...")
        progressDialog.show()
        val timestamp = System.currentTimeMillis()
        val filePathAndName = "PDF/$timestamp"
        val storageReference = FirebaseStorage.getInstance().getReference(filePathAndName)
        storageReference.putFile(pdfUri!!)
            .addOnSuccessListener { taskSnapshot ->
                val uriTask: Task<Uri> = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful);
                val uploadedPdfUrl = uriTask.result.toString()
                subirPdfBD(uploadedPdfUrl, timestamp)
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(context, "Ha fallado la subida del archivo debido a ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun subirPdfBD(urlPdfSubido: String, tiempo: Long) {
        progressDialog.setMessage("Subiendo pdf a la BD...")
        val uid = firebaseAuth.uid!!
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["uid"] = uid
        hashMap["id"] = "$tiempo"
        hashMap["titulo"] = titulo
        hashMap["descripcion"] = descripcion
        hashMap["categoria"] = id_categoria
        hashMap["url"] = urlPdfSubido
        hashMap["tiempo"] = tiempo
        hashMap["contadorVistas"] = 0
        hashMap["contadorDescargas"] = 0

        val ref = FirebaseDatabase.getInstance().getReference("Libros")
        ref.child("$tiempo")
            .setValue(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(context, "Libro subido con éxito", Toast.LENGTH_SHORT).show()
                // Reset form
                binding.EtPdf.text = null
                binding.EtDescripcionPdf.text = null
                binding.TvCategoriaLibro.text = ""
                pdfUri = null
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(context, "Ha fallado la subida del archivo debido a ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val PDF_PICK_CODE = 1000
    }

    // Método para obtener el resultado de la selección del archivo PDF
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {
            if (requestCode == PDF_PICK_CODE) {
                pdfUri = data?.data
                Toast.makeText(context, "PDF seleccionado: $pdfUri", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Selección cancelada", Toast.LENGTH_SHORT).show()
        }
    }
}
