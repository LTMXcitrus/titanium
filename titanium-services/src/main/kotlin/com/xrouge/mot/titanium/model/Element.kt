package com.xrouge.mot.titanium.model

import com.xrouge.mot.titanium.util.logError
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat

data class Element(val name: String,
                   val more: String?,
                   val perishable: Boolean?,
                   val minimum: Int,
                   val stock: Int,
                   val expirationDate: LocalDate?,
                   val location: ClosetLocation,
                   val tags: List<String>,
                   val batch: Batch,
                   val _id: String? = null) {

    fun newInventoryElement(): InventoryElement {
        return InventoryElement(name, more, perishable, minimum, null, null, location, tags, batch, false)
    }

    fun getToOrder(): Int {
        return if(minimum - stock < 0) {
            0
        } else {
            minimum - stock
        }
    }

    fun getSearchText(): String {
        return "$name $more $tags".toLowerCase()
    }

    fun contains(value: String): Boolean {
        return getSearchText().contains(value)
    }

    fun toRow(): List<String?> =
            listOf(name,
                    more,
                    perishable.toString(),
                    minimum.toString(),
                    stock.toString(),
                    expirationDate?.toString()?:"",
                    getToOrder().toString(),
                    location.location,
                    tags.joinToString())


    companion object {
        fun parseFromSheetRow(uncastRow: List<*>): Element? {
            val row = uncastRow.map { it.toString() }

            val name = row[0]
            val more = row[1]
            val perishable = parsePerishable(row[2])
            val minimum = row[3].toInt()
            val stock = row[4].toInt()
            val expirationDate = if (row[5].isNullOrBlank()) null else DateTimeFormat.forPattern("yyyy-MM-dd").parseLocalDate(row[5])
            val closetLocation = parseClosetLocation(row[7])
            val tags = parseTagsFromRow(row[8])
            var element: Element? = null
            try {
                element = Element(name, more, perishable, minimum, stock, expirationDate, closetLocation, tags, Batch.PHARMACIE)
            } catch(e: Exception) {
                logError<Element> { "Error reading row: $row" }
                logError<Element>(e)
            }
            return element
        }

        fun parseTagsFromRow(value: String): List<String> {
            return value.split(", ")
        }

        fun parsePerishable(value: String): Boolean {
            return if (value.toUpperCase() == "NON") {
                false
            } else !value.isEmpty()
        }

        fun parseClosetLocation(value: String): ClosetLocation {
            var parseResult: ClosetLocation? = null
            enumValues<ClosetLocation>().iterator().forEachRemaining { const ->
                if (const.location == value) {
                    parseResult = const
                }
            }
            return parseResult ?: ClosetLocation.UNKNOWN
        }
    }


}
