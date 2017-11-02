package com.xrouge.mot.titanium.mongo

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.xrouge.mot.titanium.model.Element
import com.xrouge.mot.titanium.util.logInfo
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.ext.mongo.MongoClient


private val mongodb_url = System.getProperty("mongodb_url", "mongodb://localhost:27017")
private val mongodb_name = System.getProperty("mongodb_name", "titanium")
private val collection_name = "stocks"

class Dao(vertx: Vertx) {

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
            logInfo<Dao> { "saved element: ${element.name}" }
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
            client.replace(collection_name, JsonObject().put("_id", element._id), JsonObject(mapper.writeValueAsString(element)), {
                logInfo<Dao> { "update ${element.name}" }
                handler("update ${element.name}", true)
            })
        }
    }

    fun remove(element: Element, handler: (String) -> Unit) {
        client.remove(collection_name, JsonObject(mapper.writeValueAsString(element)), {
            handler("removed element: ${element.name}")
        })
    }
}