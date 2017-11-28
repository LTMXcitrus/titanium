package com.xrouge.mot.titanium

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.xrouge.mot.titanium.model.*
import com.xrouge.mot.titanium.services.*
import com.xrouge.mot.titanium.util.logInfo
import io.vertx.core.Vertx
import io.vertx.core.http.HttpMethod.*
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.CorsHandler


class Router(val vertx: Vertx) {

    private val frontService = FrontService(vertx)
    private val googleSheetService = GoogleSheetsService(vertx)
    private val inventoryService = InventoryService(vertx, googleSheetService)
    private val driveService = DriveService()

    private val mapper = jacksonObjectMapper()

    init {
        mapper.findAndRegisterModules()
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        mapper.registerModule(JodaModule())
    }

    fun buildRouter(): Router {
        val router = Router.router(vertx)

        router.route().handler(BodyHandler.create())

        router.route(OPTIONS, "/*").handler(corsHandler())
        router.route(GET, "/*").handler(corsHandler())
        router.route(POST, "/*").handler(corsHandler())
        router.route(PUT, "/*").handler(corsHandler())


        router.get("/healthcheck").handler { context ->
            context.response().end("Healthcheck: OK")
        }

        router.get("/rest/elements").handler { context ->
            frontService.getAllElements({ elements ->
                context.response().end(mapper.writeValueAsString(elements))
            })
        }

        router.post("/rest/elements").handler { context ->
            frontService.save(mapper.readValue(context.bodyAsString, Element::class.java), {
                context.response().end("saved")
            })
        }

        router.put("/rest/elements").handler { context ->
            frontService.update(mapper.readValue(context.bodyAsString, Element::class.java), { response, ok ->
                if (ok) context.response().endOk(response)
                else context.response().endNotOk(response)
            })
        }

        router.delete("/rest/elements").handler { context ->
            frontService.remove(mapper.readValue(context.bodyAsString, Element::class.java), {
                context.response().end(it)
            })
        }

        router.get("/rest/elements/batch/:batch").handler { context ->
            val batch = Batch.valueOf(context.request().getParam("batch"))
            frontService.getElementsFromBatch(batch, { elements ->
                context.response().end(mapper.writeValueAsString(elements))
            })
        }

        router.get("/rest/elements/byShelf").handler { context ->
            frontService.getElementsGroupedByShelf {
                context.response().end(mapper.writeValueAsString(it))
            }
        }

        router.get("/rest/elements/toOrder").handler { context ->
            frontService.getElementsToOrder { elementsToOrder ->
                context.response().end(mapper.writeValueAsString(elementsToOrder))
            }
        }

        router.get("/rest/elements/search/:query").handler { context ->
            val query = context.request().getParam("query")
            frontService.searchElements(query, { elements ->
                context.response().end(mapper.writeValueAsString(elements))
            })
        }

        router.get("/rest/element/:id/searchText").handler { context ->
            val id = context.request().getParam("id")
            frontService.getElementSearchText(id, {
                context.response().end(it)
            })
        }

        router.get("/rest/sheets/import/spreadsheet/:spreadsheetId").handler { context ->
            val spreadsheetId = context.request().getParam("spreadsheetId")
            googleSheetService.importFromSpreadsheet(spreadsheetId, {
                context.response().end(it)
            })
        }

        router.get("/rest/sheets/import/folder/:folderId").handler { context ->
            val folderId = context.request().getParam("folderId")
            googleSheetService.importFromFolder(folderId, {
                context.response().end(it)
            })
        }

        router.get("/rest/sheets/export/:exportFolderName/:spreadsheetName").handler { context ->
            logInfo<Router> { "export request" }
            val exportFolderName = context.request().getParam("exportFolderName")
            val spreadsheetName = context.request().getParam("spreadsheetName")
            googleSheetService.exportToGoogleSheets(exportFolderName, spreadsheetName, {
                context.response().end(it)
            })
        }

        router.get("/rest/sheets/save").handler { context ->
            googleSheetService.save {
                context.response().end(it)
            }
        }

        router.get("/rest/inventory/start").handler { context ->
            inventoryService.startInventory({ inventory ->
                context.response().end(mapper.writeValueAsString(inventory))
            })
        }

        router.get("/rest/inventory/byShelf").handler { context ->
            inventoryService.getInventoryByShelf { groupedInventory ->
                context.response().end(mapper.writeValueAsString(groupedInventory))
            }
        }

        router.post("/rest/inventory/partial").handler { context ->
            val partialInventory = mapper.readValue<List<InventoryElement>>(context.bodyAsString, mapper.typeFactory.constructCollectionType(List::class.java, InventoryElement::class.java))
            inventoryService.savePartialInventory(partialInventory, { savedPartialInventory ->
                context.response().end(mapper.writeValueAsString(savedPartialInventory))
            })
        }

        router.post("/rest/inventory/end").handler { context ->
            val inventoryElements: Map<ClosetLocation, List<InventoryElement>> = mapper.readValue(context.bodyAsString, object : TypeReference<Map<ClosetLocation, List<InventoryElement>>>() {})
            inventoryService.endInventory( inventoryElements, { elements ->
                context.response().end(mapper.writeValueAsString(elements))
            })
        }

        router.get("/rest/inventory/end").handler { context ->
            inventoryService.endInventory({ elements ->
                context.response().end(mapper.writeValueAsString(elements))
            })
        }

        router.get("/rest/drive/folder/default/files").handler { context ->
            context.response().end(mapper.writeValueAsString(driveService.listChildrenOfFolder()))
        }

        router.delete("/rest/admin/inventory").handler { context ->
            inventoryService.deleteInventory({
                context.response().end(it)
            })
        }

        return router
    }

    private fun corsHandler(): CorsHandler {
        return CorsHandler.create("*")
                .allowedMethods(setOf(GET, POST, OPTIONS, DELETE, PUT))
                .allowedHeader("Content-Type")
    }

}