package com.example.koalaap.Modelos

class ModeloComentario {

    var id =""
    var idLibro = ""
    var comentario =""
    var tiempo = ""
    var uid =""

    constructor()
    constructor(id: String, idLibro: String, comentario: String, tiempo: String, uid: String) {
        this.id = id
        this.idLibro = idLibro
        this.comentario = comentario
        this.tiempo = tiempo
        this.uid = uid
    }
}