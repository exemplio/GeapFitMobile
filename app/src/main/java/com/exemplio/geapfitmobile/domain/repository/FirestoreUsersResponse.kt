package com.exemplio.geapfitmobile.domain.repository

import kotlinx.serialization.Serializable
import org.w3c.dom.Document

@Serializable
data class FirestoreUsersResponse(
    val documents: List<Document>? = null
)
