package com.example.deneme1.data

import com.example.deneme1.common.ParseDelegate
import com.parse.ParseClassName
import com.parse.ParseObject

@ParseClassName("Notifications")
class Notifications : ParseObject() {
    var tableNumber by ParseDelegate<String?>()
    var customerOrder by ParseDelegate<String?>()
    var waiterID by ParseDelegate<String?>()
    var isCancelled by ParseDelegate<Boolean?>()
    var isFinished by ParseDelegate<Boolean?>()


}