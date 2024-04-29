package com.example.koalaap.Modelos

class Modelopdf {

    var uid : String = ""
    var id : String = ""
    var titulo : String = ""
    var descipcion : String = ""
    var categoria : String = ""
    var url : String = ""
    var tiempo : Long = 0
    var contadorVistas : Long = 0
    var contadorDescargas : Long = 0
    var esFavoritos = false

    constructor()
    constructor(
        uid: String,
        id: String,
        titulo: String,
        descipcion: String,
        categoria: String,
        url: String,
        tiempo: Long,
        contadorVistas: Long,
        contadorDescargas: Long,
        esFavoritos : Boolean
    ) {
        this.uid = uid
        this.id = id
        this.titulo = titulo
        this.descipcion = descipcion
        this.categoria = categoria
        this.url = url
        this.tiempo = tiempo
        this.contadorVistas = contadorVistas
        this.contadorDescargas = contadorDescargas
        this.esFavoritos = esFavoritos
    }


}