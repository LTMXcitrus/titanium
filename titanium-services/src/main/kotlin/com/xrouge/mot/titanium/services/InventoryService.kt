package com.xrouge.mot.titanium.services

import com.xrouge.mot.titanium.model.Element
import com.xrouge.mot.titanium.model.InventoryElement
import com.xrouge.mot.titanium.mongo.ElementDao
import com.xrouge.mot.titanium.mongo.InventoryDao
import io.vertx.core.Vertx
import org.joda.time.LocalDate


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

    fun endInventory(handler: (List<Element>) -> Unit) {
        inventoryDao.findAll { inventoryElements ->
            if (inventoryElements.any { !it.uptodate }){
                handler(emptyList())
            } else {
                val elements = inventoryElements.map { it.toElement() }
                elementDao.removeAll {
                    elementDao.saveAll(elements, {
                        googleSheetsService.exportToGoogleSheets("Dernier inventaire " + LocalDate.now(), "Inventaire", {
                            handler(elements)
                        })
                    })
                }
            }
        }
    }

}