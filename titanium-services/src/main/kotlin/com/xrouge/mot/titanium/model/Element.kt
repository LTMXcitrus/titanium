package com.xrouge.mot.titanium.model

import org.joda.time.LocalDate

// todo add lastInventoryDate in order to handle inventories ?
data class Element(val name: String,
                   val more: String,
                   val perishable: Boolean,
                   val minimum: Int,
                   val stock: Int,
                   val expirationDate: LocalDate,
                   val location: ClosetLocation,
                   val tags: List<String>,
                   val batch: Batch,
                   val _id: String? = null){

    fun getSearchText(): String{
        return "$name $more $tags"
    }

    fun contains(value: String): Boolean{
        return getSearchText().contains(value)
    }
}
