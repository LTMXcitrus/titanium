package com.xrouge.mot.titanium

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.xrouge.mot.titanium.model.Batch
import com.xrouge.mot.titanium.model.Element
import com.xrouge.mot.titanium.mongo.Dao
import com.xrouge.mot.titanium.services.FrontService
import com.xrouge.mot.titanium.services.GoogleSheetsService
import com.xrouge.mot.titanium.util.endNotOk
import com.xrouge.mot.titanium.util.endOk
import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler


class Router(val vertx: Vertx) {

    val frontService = FrontService(vertx)
    val googleSheetService = GoogleSheetsService(vertx)

    val mapper = jacksonObjectMapper()

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

        router.get("/rest/sheets/import").handler { context ->
            googleSheetService.importFromGoogleSheets {
                context.response().end(it)
            }
        }

        return router
    }

}