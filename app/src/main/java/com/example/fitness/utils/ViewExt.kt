package com.example.fitness.utils

import android.content.Context
import android.widget.Toast

fun Context.showShortToast(text:String){
    Toast.makeText(this,text, Toast.LENGTH_SHORT).show()
}

fun Context.showLongToast(text:String){
    Toast.makeText(this,text, Toast.LENGTH_LONG).show()
}

fun Context.showShortToast(text:Int){
    Toast.makeText(this,resources.getString(text), Toast.LENGTH_SHORT).show()
}

fun Context.showLongToast(text:Int){
    Toast.makeText(this,resources.getString(text), Toast.LENGTH_LONG).show()
}

//showLongToast("текст")
//showLongToast(R.string.app_name)