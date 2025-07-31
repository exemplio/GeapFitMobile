package com.geapfit.utils

fun translate(message: String?): String {
    return when (message?.uppercase()) {
        "SERVICE_IS_NOT_REGISTERED_IN_INVENTORY_PRODUCTS_" ->
            "EL SERVICIO NO ESTÁ REGISTRADO EN EL INVENTARIO DE PRODUCTOS"
        "COLLECT_CHANNEL_NOT_FOUND" ->
            "CANAL NO ENCONTRADO"
        "PROFILE_NOT_FOUND" ->
            "Perfil no encontrado"
        else -> message ?: ""
    }
}