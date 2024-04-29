package com.example.koalaap.Administrador

import android.widget.Filter
import com.example.koalaap.Modelos.Modelopdf

class FiltroPdfAdmin : Filter{

    var filtroList : ArrayList<Modelopdf>
    var adaptadorPdfAdmin : AdaptadorPdfAdmin

    constructor(filtroList: ArrayList<Modelopdf>, adaptadorPdfAdmin: AdaptadorPdfAdmin) {
        this.filtroList = filtroList
        this.adaptadorPdfAdmin = adaptadorPdfAdmin
    }

    override fun performFiltering(libro: CharSequence?): FilterResults {
        var libro : CharSequence?= libro
        val resultados = FilterResults()
        if (libro!=null && libro.isNotEmpty()){
            libro = libro.toString().lowercase()
            val modeloFiltrado : ArrayList<Modelopdf> = ArrayList()
            for (i in filtroList.indices){
                if (filtroList[i].titulo.lowercase().contains(libro)) {
                    modeloFiltrado.add(filtroList[i])
                }
            }
            resultados.count = modeloFiltrado.size
            resultados.values = modeloFiltrado
        }
        else{
            resultados.count = filtroList.size
            resultados.values = filtroList
        }
        return resultados
    }

    override fun publishResults(p0: CharSequence?, resultados: FilterResults) {
        adaptadorPdfAdmin.pdfArrayList = resultados.values as ArrayList<Modelopdf>
        adaptadorPdfAdmin.notifyDataSetChanged()
    }
}