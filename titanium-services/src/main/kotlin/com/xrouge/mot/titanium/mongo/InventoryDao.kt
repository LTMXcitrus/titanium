package com.xrouge.mot.titanium.mongo

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.xrouge.mot.titanium.model.InventoryElement
import com.xrouge.mot.titanium.util.logError
import com.xrouge.mot.titanium.util.logInfo
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.ext.mongo.BulkOperation
import io.vertx.ext.mongo.MongoClient


private val mongodb_url = System.getProperty("mongodb_url", "mongodb://localhost:27017")
private val mongodb_name = System.getProperty("mongodb_name", "titanium")
private val collection_name = "inventory"

class InventoryDao(vertx: Vertx) {

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

    fun save(element: InventoryElement, handler: () -> Unit) {
        client.save(collection_name, JsonObject(mapper.writeValueAsString(element)), {
            if (it.succeeded()) {
                handler()
            } else {
                logError<InventoryDao> { "insert failed because: ${it.cause().message}" }
            }
        })
    }

    fun updateAll(elements: List<InventoryElement>, handler: () -> Unit) {
        val operations = elements.map {
            BulkOperation.createReplace(JsonObject().put("_id", it._id),
                    JsonObject(mapper.writeValueAsString(it)))
        }
        client.bulkWrite(collection_name, operations, {
            handler()
        })
    }

    fun saveAll(elements: List<InventoryElement>, handler: () -> Unit) {
        val operations = elements.map { BulkOperation.createInsert(JsonObject(mapper.writeValueAsString(it))) }
        client.bulkWrite(collection_name, operations, {
            handler()
        })
    }

    fun findOne(id: String, handler: (InventoryElement) -> Unit) {
        client.findOne(collection_name,
                JsonObject().put("_id", id),
                null,
                {
                    handler(mapper.readValue(it.result().toString(), InventoryElement::class.java))
                })
    }

    fun findAll(handler: (List<InventoryElement>) -> Unit) {
        client.find(collection_name, JsonObject(), {
            handler(it.result().map { jsonObject -> mapper.readValue(jsonObject.toString(), InventoryElement::class.java) })
        })
    }

    fun update(element: InventoryElement, handler: (String, Boolean) -> Unit) {
        if (element._id.isNullOrBlank()) {
            handler("The element \"_id\" must not be null", false)
            throw IllegalArgumentException("The element \"_id\" must not be null")
        } else {
            client.replace(collection_name, JsonObject().put("_id", element._id), JsonObject(mapper.writeValueAsString(element)), {
                logInfo<InventoryDao> { "update ${element.name}" }
                handler("update ${element.name}", true)
            })
        }
    }

    fun remove(element: InventoryElement, handler: (String) -> Unit) {
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