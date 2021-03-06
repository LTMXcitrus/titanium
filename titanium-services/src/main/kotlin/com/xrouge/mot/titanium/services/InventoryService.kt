package com.xrouge.mot.titanium.services

import com.xrouge.mot.titanium.model.ClosetLocation
import com.xrouge.mot.titanium.model.Element
import com.xrouge.mot.titanium.model.InventoryElement
import com.xrouge.mot.titanium.mongo.ElementDao
import com.xrouge.mot.titanium.mongo.InventoryDao
import com.xrouge.mot.titanium.util.logInfo
import io.vertx.core.Vertx


class InventoryService(vertx: Vertx, val googleSheetsService: GoogleSheetsService) {

    private val inventoryDao = InventoryDao(vertx)
    private val elementDao = ElementDao(vertx)

    fun startInventory(handler: (List<InventoryElement>) -> Unit) {
        inventoryDao.findAll { inventoryElements ->
            if (inventoryElements.isEmpty()) {
                elementDao.findAll { elements ->
                    val newInventory = elements.map { it.newInventoryElement() }
                    inventoryDao.saveAll(newInventory, {
                        inventoryDao.findAll { newInventoryElements ->
                            handler(newInventoryElements)
                        }
                    })
                }
            } else {
                handler(inventoryElements)
            }
        }
    }

    fun endInventory(inventoryElementsAsMap: Map<ClosetLocation, List<InventoryElement>>, handler: (List<Element>) -> Unit) {
        val inventoryElements = inventoryElementsAsMap.flatMap { it.value }
        savePartialInventory(inventoryElements, {
            endInventory(handler)
        })
    }

    fun endInventory(handler: (List<Element>) -> Unit) {
        inventoryDao.findAll { inventoryElements ->
            if (inventoryElements.any { !it.uptodate }){
                logInfo<InventoryService> { "Inventory is not over yet" }
                handler(emptyList())
            } else {
                val elements = inventoryElements.map { it.toElement() }
                elementDao.removeAll {
                    elementDao.saveAll(elements, {
                        googleSheetsService.save {
                            handler(elements)
                        }
                    })
                }
            }
        }
    }

    fun getInventoryByShelf(handler: (Map<ClosetLocation,List<InventoryElement>>) -> Unit) {
        startInventory { inventoryElements ->
            handler(inventoryElements.groupBy { it.location })
        }
    }

    fun savePartialInventory(partialInventory: List<InventoryElement>, handler: (List<InventoryElement>) -> Unit) {
        partialInventory.forEach { element ->
            element.uptodate = true
        }
        inventoryDao.updateAll(partialInventory, {
            handler(partialInventory)
        })

    }

    fun  deleteInventory(handler: (String)-> Unit) {
        inventoryDao.removeAll {
            handler("Inventaire supprimé")
        }
    }
}