package com.example.koalaap.Modelos

class  ModeloCategoria {

    var id: String=""
    var categoria: String=""
    var tiempo: Long= 0
    var uid: String=""

    constructor()

    constructor(id: String, categoria: String, tiempo: Long, uid: String) {
        this.id = id
        this.categoria = categoria
        this.tiempo = tiempo
        this.uid = uid
    }


}