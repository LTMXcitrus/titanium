package com.xrouge.mot.titanium.mongo

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.xrouge.mot.titanium.model.Element
import com.xrouge.mot.titanium.util.logError
import com.xrouge.mot.titanium.util.logInfo
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.ext.mongo.BulkOperation
import io.vertx.ext.mongo.MongoClient


private val mongodb_url = System.getProperty("mongodb_url", "mongodb://localhost:27017")
private val mongodb_name = System.getProperty("mongodb_name", "titanium")
private val collection_name = "stocks"

class ElementDao(vertx: Vertx) {

    val client: MongoClient
    val mapper = jacksonObjectMapper()

    init {
        mapper.findAndRegisterModules()
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        mapper.registerModule(JodaModule())

        val config = JsonObject().put("connection_string", mongodb_url).put("db_name", mongodb_name)
        client = MongoClient.createShared(vertx, config)
    }

    fun save(element: Element, handler: () -> Unit) {
        client.save(collection_name, JsonObject(mapper.writeValueAsString(element)), {
            if (it.succeeded()) {
                handler()
            } else {
                logError<ElementDao> { "insert failed because: ${it.cause().message}" }
            }
        })
    }

    fun saveAll(elements: List<Element>, handler: () -> Unit) {
        val operations = elements.map { BulkOperation.createInsert(JsonObject(mapper.writeValueAsString(it))) }
        client.bulkWrite(collection_name, operations, {
            handler()
        })
    }

    fun findOne(id: String, handler: (Element) -> Unit) {
        client.findOne(collection_name,
                JsonObject().put("_id", id),
                null,
                {
                    handler(mapper.readValue(it.result().toString(), Element::class.java))
                })
    }

    fun findAll(handler: (List<Element>) -> Unit) {
        client.find(collection_name, JsonObject(), {
            handler(it.result().map { jsonObject -> mapper.readValue(jsonObject.toString(), Element::class.java) })
        })
    }

    fun update(element: Element, handler: (String, Boolean) -> Unit) {
        if (element._id.isNullOrBlank()) {
            handler("The element \"_id\" must not be null", false)
            throw IllegalArgumentException("The element \"_id\" must not be null")
        } else {
            client.save(collection_name, JsonObject(mapper.writeValueAsString(element)), {
                logInfo<ElementDao> { "update ${element.name}" }
                handler("L'élement ${element.name} a bien été mis à jour", true)
            })
        }
    }

    fun remove(element: Element, handler: (String) -> Unit) {
        client.remove(collection_name, JsonObject(mapper.writeValueAsString(element)), {
            handler("removed element: ${element.name}")
        })
    }

    fun removeAll(handler: () -> Unit) {
        client.removeDocuments(collection_name, JsonObject(), {
            handler()
        })
    }
}