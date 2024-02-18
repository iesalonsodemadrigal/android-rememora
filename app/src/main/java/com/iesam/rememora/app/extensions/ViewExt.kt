package com.iesam.rememora.app.extensions

import android.graphics.Typeface
import android.view.View
import com.getkeepsafe.taptargetview.TapTarget
import com.iesam.rememora.R

fun View.hide() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.createTarget (title : String , subtitle: String, radius : Int) : TapTarget {

    val target = TapTarget.forView(this, title, subtitle)
        .outerCircleColor(R.color.seed) //Especificar un color para el círculo exterior //md_theme_light_surfaceTint
        .outerCircleAlpha(0.70f) //Especifique la cantidad alfa para el círculo exterior
        .targetCircleColor(R.color.md_theme_light_onPrimary) //Especifique un color para el círculo objetivo
        .titleTextSize(30) //Especificar el tamaño (en sp) del texto del título.
        //.titleTextColor(R.color.md_theme_light_error) // Especificar el color del texto del título.
        .descriptionTextSize(25) // Especifique el tamaño (en sp) del texto de descripción.
        //.descriptionTextColor(R.color.md_theme_light_onError) // Especificar el color del texto de descripción.
        .textColor(R.color.md_theme_light_onPrimary) // Especifique un color tanto para el título como para el texto de descripción.
        .textTypeface(Typeface.SANS_SERIF) // especificar un tipo de letra para el texto
        .dimColor(R.color.md_theme_light_shadow) // Si está configurado, se atenuará detrás de la vista con un 30% de opacidad del color dado.
        .drawShadow(true) // Si dibujar una sombra paralela o no
        .cancelable(false) // Si al tocar fuera del círculo exterior se descarta la vista
        .tintTarget(true) // Si se debe teñir el color de la vista de destino
        .transparentTarget(true) // Especifique si el objetivo es transparente (muestra el contenido debajo)
        .targetRadius(radius) // Especifique el radio objetivo (en dp)

    return target

}