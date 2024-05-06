<h1 align="center">KoalaAPP</h1>
<p align="center">
  <img src="https://github.com/CamiloxX/KoalaApp/assets/28693419/59e87dd0-9b7c-4a07-acf9-f349548d23d3" alt="KoalaApp Image">
</p>

## Contenido de la app móvil

- [Login y Registro](#login-y-registro)
- [Fragmentos](#fragmentos)
- [PDF (Leer, Descargar, Añadir a Favoritos)](#pdf-leer-descargar-añadir-a-favoritos)

<h1 id="login-y-registro">Login y Registro</h1>
<p align="center">
  <img src="https://github.com/CamiloxX/KoalaApp/assets/28693419/7a795c6c-ca9c-400d-810a-2cf3c9f0ea4f" alt="Login y Registro Image">
```
  
    class Login_Admin : AppCompatActivity() {
    private lateinit var binding: ActivityLoginAdminBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
<p>La autenticacion del usuario la realizamos usando FireBase , esta se usa mediante un correo Electronico y Contraseña</p>
La manera en el que autenticamos estos datos de inicio de sesion es mediante el siguiente codigo

     //Autentica si el inicio de sesion es exitoso
    private fun loginAdmin() {
        progressDialog.setMessage("Iniciando Sesion")
        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                progressDialog.dismiss()
                startActivity(Intent(this@Login_Admin, MainActivity::class.java))
                finishAffinity()
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(
                    applicationContext,
                    "No se pudo iniciar sesión debido a ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

      

</p>

<p>El registro pedimos varios datos pero los que vamos autenticar va ser mediante el correo , de igual forma se valida que los campos sea validos y no esten vacios </p>

    private fun ValidarInformacion() {
    nombres = binding.EtNombresAdmin.text.toString().trim()
        apellidos = binding.EtApellidosAdmin.text.toString().trim()
        username = binding.EtUserAdmin.text.toString().trim()
        email = binding.EtEmailAdmin.text.toString().trim()
        password =binding.EtPasswordAdmin.text.toString().trim()
        r_password = binding.EtPasswordAdminR.text.toString().trim()


        if(nombres.isEmpty()){
            binding.EtNombresAdmin.error="Ingrese Nombres"
            binding.EtNombresAdmin.requestFocus()
        }
        else if(apellidos.isEmpty()) {
            binding.EtApellidosAdmin.error = "Ingrese Apellido"
            binding.EtApellidosAdmin.requestFocus()
        }

        else if(username.isEmpty()){
            binding.EtUserAdmin.error="Ingrese Nombre Usuario"
            binding.EtUserAdmin.requestFocus()
        }

<h1 id="fragmentos">Fragmentos</h1>
<!-- Contenido para Fragmentos -->

<p align="center">  <img src="https://github.com/CamiloxX/KoalaApp/assets/28693419/af12b1cd-4868-444e-a271-3e93753a0eed" alt=" Fragmentos">
<p>Nuesta app Cuenta principalmente con 4 Fragmentos , dentros de estos esta  Dashboard, Buscar,Subir,Favoritos y Cuenta 
Como primera pantalla se mostrara los PDFs que se hayan subido a la plataforma</p>


<p align="center">  <img src="https://github.com/CamiloxX/KoalaApp/assets/28693419/6a3d80ad-528b-4099-a18d-7185df65d529" alt=" Fragmentos">

<p>En el fragmento de buscar tendremos la oportunidad de filtrar los PDFs segun su categoria o dando click en el boton de mas vistos o mas descargados, creamos una funcion donde hace lo siguiente</p>


      private fun topDescargados() {
    // Inicializo una lista vacía que va a contener objetos de tipo Modelopdf.
    pdfArrayList = ArrayList()

    // Obtengo una referencia al nodo 'Libros' de mi base de datos en Firebase.
    val ref = FirebaseDatabase.getInstance().getReference("Libros")

    // Solicito los 5 libros con el mayor número de descargas. Para ello, ordeno los libros en el nodo
    // 'Libros' por el campo 'contadorDescargas' y limito los resultados a los últimos 5.
    ref.orderByChild("contadorDescargas").limitToLast(5)
        .addValueEventListener(object : ValueEventListener {
            // Este método se llama cada vez que hay un cambio en los datos de los libros
            // en la base de datos o la primera vez que se ejecuta la consulta.
            override fun onDataChange(snapshot: DataSnapshot) {
                // Limpio la lista para eliminar datos antiguos antes de agregar los nuevos.
                pdfArrayList.clear()

                // Recorro cada uno de los datos obtenidos en la consulta.
                for (ds in snapshot.children) {
                    // Convierto el dato obtenido a un objeto de tipo Modelopdf.
                    val modelo = ds.getValue(Modelopdf::class.java)
                    // Agrego el objeto Modelopdf a la lista.
                    pdfArrayList.add(modelo!!)
                }

                // Creo un adaptador para RecyclerView pasándole el contexto y la lista de PDFs.
                adapatadorPdfAdmin = AdaptadorPdfAdmin(this@TopDescargados, pdfArrayList)
                // Establezco el adaptador al RecyclerView para mostrar los datos.
                binding.RvTopDescargados.adapter = adapatadorPdfAdmin
            }

            // Este método se llama si se cancela la consulta a la base de datos.
            override fun onCancelled(error: DatabaseError) {
                // Aquí podrías manejar el caso de error, como mostrar un mensaje de error.
            }
        })
        }

<p>De igual forma realizamos libros mas vistos donde  realizamos el siguiente codigo </p>

    // Aquí declaro las variables que necesito para conectar mi diseño (layout) con el código,
    // la lista donde voy a almacenar los datos de los libros y el adaptador para el RecyclerView.
    private lateinit var binding: ActivityTopVistosBinding
    private lateinit var pdfArrayList: ArrayList<Modelopdf>
    private lateinit var adapatadorPdfAdmin: AdaptadorPdfAdmin

    // El método onCreate se llama cuando se crea la actividad.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflar el layout para esta actividad usando binding para acceder a los elementos del layout.
        binding = ActivityTopVistosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Llamo al método que inicializa la carga de los libros más vistos.
        topVistos()
    }

    // Método para cargar los datos de los libros más vistos de Firebase.
    private fun topVistos() {
        // Inicializo la lista de PDFs como vacía.
        pdfArrayList = ArrayList()
        // Obtengo una referencia al nodo 'Libros' de Firebase.
        val ref = FirebaseDatabase.getInstance().getReference("Libros")
        // Hago una consulta que ordena los libros por el campo 'contadorVistas' y obtengo los últimos 5.
        ref.orderByChild("contadorVistas").limitToLast(5)
            .addValueEventListener(object : ValueEventListener {
                // Este método maneja lo que sucede cuando se reciben los datos.
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Limpio la lista para asegurarme de que no se duplican datos.
                    pdfArrayList.clear()
                    // Itero sobre cada elemento obtenido en la consulta.
                    for (ds in snapshot.children) {
                        // Convierto cada elemento a un objeto Modelopdf y lo añado a la lista.
                        val modelo = ds.getValue(Modelopdf::class.java)
                        pdfArrayList.add(modelo!!)
                    }
                    // Configuro el adaptador del RecyclerView con el contexto y la lista actualizada.
                    adapatadorPdfAdmin = AdaptadorPdfAdmin(this@TopVistos, pdfArrayList)
                    // Asigno el adaptador al RecyclerView para que se muestren los datos.
                    binding.RvTopVistos.adapter = adapatadorPdfAdmin
                }

                // Si se cancela la consulta, este método maneja el error.
                override fun onCancelled(error: DatabaseError) {
                    // Aquí podría manejar errores, como un problema de acceso a Firebase.
                }
            })
    }

<p>El fragmento de subir PDF podremos subir PDF a la app para que las demas personas lo puedan leer o descargar , asi se veria</p>
<p align="center">  <img src="https://github.com/CamiloxX/KoalaApp/assets/28693419/5dbc4390-0d1c-4789-8ba2-94c120e0eb77">
<p> Recordar que las categorias que salen son las que cargaos previamente a FireBase , si se desea agregar nueva categoria se hace manualmente </p>


<p align="center">  <img src="https://github.com/CamiloxX/KoalaApp/assets/28693419/c3aa517f-edfd-4294-8441-42103595ac66">

En el fragemento mi cuenta hay varia informacion donde traemos desde la base de datos  los nombes,correo y la fecha de creacion de la cuenta , ademas econtraremos actividades ,cerrar sesion y  modificar perfil , en la actividad "modificar perfil" podramos cambiar tanto la imagen de perfil y el nombre de usuario 

<p align="center">  <img src="https://github.com/CamiloxX/KoalaApp/assets/28693419/53f53dde-f73d-4a63-b344-3a7ab61409b1">

Estas imagenes que se suban , quedaran guardadas en el Storage del FireBase

<p align="center">  <img src="https://github.com/CamiloxX/KoalaApp/assets/28693419/9d071c8d-87b4-4f73-b9d7-fbbb6a0dfa43">
<p>Igualmente los PDF se guardan dentro de este apartado de Firebase</p>

<p align="center">  <img src=https://github.com/CamiloxX/KoalaApp/assets/28693419/17c63a63-2bb8-48e5-8ec9-7e043da60365">



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


<p>La informacion la actualizamos con el siguiente codigo</p>



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



<h1 id="pdf-leer-descargar-añadir-a-favoritos">PDF (Leer, Descargar, Añadir a Favoritos)</h1>
<!-- Contenido para PDF -->


<p align="center">  <img src="https://github.com/CamiloxX/KoalaApp/assets/28693419/47ad2ea1-d651-4bad-917e-b51c5509dfa6">

<p>Para cargar los detalles del libro usamos la siguiente funcion</p>

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

<p>La seccion de Libro tendremos varios botones , Como Leer, agregar favoritos y Descargar

<h3>Aviso</h3>
<p>Cuando descargas este  PDF , se descargara automaticamente en tu carpeta local de Downloads de tu celular , puedes acceder a esa carpeta directamente en el gestor de archivos ,dependiendo de tu dispositivo movil
</p>


<p align="center">  <img src="https://github.com/CamiloxX/KoalaApp/assets/28693419/34e4ed82-cfab-429c-91c0-ace22fb10909">


