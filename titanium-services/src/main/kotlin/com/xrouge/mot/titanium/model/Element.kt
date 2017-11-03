package com.xrouge.mot.titanium.model

import com.xrouge.mot.titanium.partners.GoogleSheetsClient
import com.xrouge.mot.titanium.util.logError
import com.xrouge.mot.titanium.util.logInfo
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat

// todo add lastInventoryDate in order to handle inventories ?
data class Element(val name: String,
                   val more: String?,
                   val perishable: Boolean?,
                   val minimum: Int,
                   val stock: Int,
                   val expirationDate: LocalDate?,
                   val toOrder: Int,
                   val location: ClosetLocation,
                   val tags: List<String>,
                   val batch: Batch,
                   val _id: String? = null) {

    fun getSearchText(): String {
        return "$name $more $tags".toLowerCase()
    }

    fun contains(value: String): Boolean {
        return getSearchText().contains(value)
    }

    companion object {
        fun parseFromSheetRow(uncastRow: List<*>): Element? {
            val row = uncastRow.map { it.toString() }
            logInfo<Element> { "reading element from row: $row" }

            val name = row[0]
            val more = row[1]
            val perishable = parsePerishable(row[2])
            val minimum = row[3].toInt()
            val stock = row[4].toInt()
            val expirationDate = if (row[5].isNullOrBlank()) null else DateTimeFormat.forPattern("MM/yyyy").parseLocalDate(row[5] as String)
            val toOrder = row[6].toInt()
            val closetLocation = parseClosetLocation(row[7])
            val tags = parseTagsFromRow(row[8])
            var element: Element? = null
            try {
                element = Element(name, more, perishable, minimum, stock, expirationDate, toOrder, closetLocation, tags, Batch.PHARMACIE)
            } catch(e: Exception) {
                logError<Element> { "Error reading row: $row" }
                logError<Element> (e)
            }
            return element
        }

        fun parseTagsFromRow(value: String): List<String>{
            return value.split(", ")
        }

        fun parsePerishable(value: String): Boolean {
            return if(value.toUpperCase() == "NON"){
                false
            } else !value.isNullOrEmpty()
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
