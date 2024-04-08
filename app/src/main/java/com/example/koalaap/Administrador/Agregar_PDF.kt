package com.example.koalaap.Administrador

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.koalaap.R
import com.example.koalaap.databinding.ActivityAgregarPdfBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class Agregar_PDF : AppCompatActivity() {

    private lateinit var binding: ActivityAgregarPdfBinding
    private  lateinit var categoriaArrayList: ArrayList<ModeloCategoria>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarPdfBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CargarCategorias()

        binding.IbRegresar.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }

        binding.TvCategoriaLibro.setOnClickListener{
            SeleccionarCat()
        }
        }
        private fun CargarCategorias(){
        categoriaArrayList = ArrayList()
            val ref= FirebaseDatabase.getInstance().getReference("Categorías").orderByChild("categoria")
            ref.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    categoriaArrayList.clear()
                    for(ds in snapshot.children){
                        val modelo = ds.getValue(ModeloCategoria::class.java)
                        categoriaArrayList.add(modelo!!)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }

    private  var id_categoria = ""
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
}