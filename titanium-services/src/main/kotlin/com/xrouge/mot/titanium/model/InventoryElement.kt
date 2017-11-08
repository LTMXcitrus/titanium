package com.xrouge.mot.titanium.model

import org.joda.time.LocalDate


data class InventoryElement(val name: String,
                            val more: String?,
                            val perishable: Boolean?,
                            val minimum: Int,
                            val stock: Int?,
                            val expirationDate: LocalDate?,
                            val location: ClosetLocation,
                            val tags: List<String>,
                            val batch: Batch,
                            var uptodate: Boolean,
                            val _id: String? = null) {

    fun toElement(): Element {
        return Element(name, more, perishable, minimum, stock!!, expirationDate, location, tags, batch)
    }
}