<h1 align="center">KoalaAPP</h1>
<p align="center">
  <img src="https://github.com/CamiloxX/KoalaApp/assets/28693419/59e87dd0-9b7c-4a07-acf9-f349548d23d3" alt="KoalaApp Image">
</p>

## Contenido de la app móvil

- [Login y Registro](#login-y-registro)
- [Fragmentos](#fragmentos)
- [Buscar PDF (Más descargado, Más vistos)](#buscar-pdf-más-descargado-más-vistos)
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
<img src https://github.com/CamiloxX/KoalaApp/assets/28693419/4f8d3320-cd6a-48f6-a41e-0dbd5b0b1e7e>

<p>Nuesta app Cuenta principalmente con 4 Fragmentos , dentros de estos esta  Dashboard, Buscar,Subir,Favoritos y Cuenta 
Como primera pantalla se mostrara los PDFs que se hayan subido a la plataforma</p>


<h1 id="buscar-pdf-más-descargado-más-vistos">Buscar PDF (Más descargado, Más vistos)</h1>
<!-- Contenido para Buscar PDF -->

<h1 id="pdf-leer-descargar-añadir-a-favoritos">PDF (Leer, Descargar, Añadir a Favoritos)</h1>
<!-- Contenido para PDF -->
