package com.example.koalaap.Administrador

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Media
import android.view.Menu
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.koalaap.R
import com.example.koalaap.databinding.ActivityEditarPerfilAdminBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class EditarPerfilAdmin : AppCompatActivity() {


    private  lateinit var  binding : ActivityEditarPerfilAdminBinding
    private  lateinit var  firebaseAuth : FirebaseAuth
    private var imagenUri : Uri?=null

    private  lateinit var  progressDialog   : ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarPerfilAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        cargarInformacion()



        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere , Actualizando ;D")
        progressDialog.setCanceledOnTouchOutside(false)


        binding.IbRegresar.setOnClickListener(){
            onBackPressedDispatcher.onBackPressed()
        }

        binding.FbCambiarImagen.setOnClickListener{
            mostrarOpciones()
        }

        binding.BtnActualizar.setOnClickListener{
            validarInformacion()

        }


    }

    private fun mostrarOpciones() {
        val  popupMenu = PopupMenu(this, binding.imgPerfilAdmin)
        popupMenu.menu.add(Menu.NONE,0,0,"Galeria")
        popupMenu.menu.add(Menu.NONE,1,1,"Camara")
        popupMenu.show()


        popupMenu.setOnMenuItemClickListener{item->
            val id = item.itemId
            if(id == 0){

                //Elegir imagen Galaria

                seleccionarImgGaleria()

            }else if (id==1){
                //Imagen Camara

                tomarFotografia()

            }
            true

        }
    }

    private fun tomarFotografia() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE,"Titulo_temp")
        values.put(MediaStore.Images.Media.DESCRIPTION,"Description_temp")
        imagenUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imagenUri)
        ARLCamara.launch(intent)
    }

    private  val ARLCamara = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult>{resultado->
            if(resultado.resultCode == Activity.RESULT_OK){
                subirImagenStorage()
            }
            else{
                Toast.makeText(applicationContext,"Cancelado", Toast.LENGTH_SHORT).show()

            }

        }
    )

    private fun seleccionarImgGaleria() {

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        ARLGaleria.launch(intent)

    }

    private var  ARLGaleria = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> {resultado->
            if(resultado.resultCode == Activity.RESULT_OK){
                val data  =  resultado.data
                imagenUri = data!!.data
              //  binding.imgPerfilAdmin.setImageURI(imagenUri)

                subirImagenStorage()

            }else{
                    Toast.makeText(applicationContext,"Cancelado", Toast.LENGTH_SHORT).show()
            }
        }
    )

    private var nombres = ""
    private fun validarInformacion() {

        nombres =  binding.EtANombres.text.toString().trim()

        if(nombres.isEmpty()){
            Toast.makeText(applicationContext, "Ingrese un nuevo Nombre",Toast.LENGTH_SHORT).show()
        }else{

            ActualizarInfo()
        }


    }

    private fun subirImagenStorage() {

         progressDialog.setMessage("Subiendo Imagen a Storage")
        progressDialog.show()


        val rutaImagen = "ImagenesPerfilAdministrador/"+firebaseAuth.uid

        val  ref = FirebaseStorage.getInstance().getReference(rutaImagen)
        ref.putFile(imagenUri!!)
            .addOnSuccessListener {taskSnapshot->
                val uriTask : Task<Uri> = taskSnapshot.storage.downloadUrl
                while(!uriTask.isSuccessful);
                val urlImagen = "${uriTask.result}"
                subirImagenDataBase(urlImagen)
            }
            .addOnFailureListener{e->
                progressDialog.dismiss()
                Toast.makeText(applicationContext, "${e.message}",Toast.LENGTH_SHORT).show()

            }


    }

    private fun subirImagenDataBase(urlImagen : String) {

        progressDialog.setMessage("Actualizando Imagen")
        val hashMap : HashMap<String , Any> =  HashMap()
        if(imagenUri != null ){
            hashMap["imagen"] = urlImagen
        }

        val ref = FirebaseDatabase.getInstance().getReference("Usuarios")
        ref.child(firebaseAuth.uid!!)
            .updateChildren(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(applicationContext,"Su ha actualizado Correctamente :D", Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener{e->
                progressDialog.dismiss()
                Toast.makeText(applicationContext,"${e.message}", Toast.LENGTH_SHORT).show()
            }

    }

    private fun ActualizarInfo() {
        progressDialog.setMessage("Actualizando")
        val hashMap : HashMap<String , Any> = HashMap()
        hashMap["nombres"] = "$nombres"
        val ref = FirebaseDatabase.getInstance().getReference("Usuarios")
        ref.child(firebaseAuth.uid!!)
            .updateChildren(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(applicationContext, "Se actualizo la informacion",Toast.LENGTH_SHORT).show()


            }
            .addOnFailureListener{e->
               progressDialog.dismiss()
                Toast.makeText(applicationContext, "No se actualizo , debido a  ${e.message}",Toast.LENGTH_SHORT).show()

            }
    }

    private fun cargarInformacion() {
         val ref  = FirebaseDatabase.getInstance().getReference( "Usuarios")
        ref.child(firebaseAuth.uid!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //Obtener informacion tiempo real

                    val nombre = "${snapshot.child("nombres").value}"
                    val imagen = "${snapshot.child("imagen").value}"

                    //setear

                    binding.EtANombres.setText(nombre)

                    try{

                        Glide.with(applicationContext)
                            .load(imagen)
                            .placeholder(R.drawable.ic_img_perfil)
                            .into(binding.imgPerfilAdmin)
                    }catch (e : Exception){
                        Toast.makeText(applicationContext,"${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }
}