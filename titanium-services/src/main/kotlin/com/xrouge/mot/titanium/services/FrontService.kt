package com.xrouge.mot.titanium.services

import com.xrouge.mot.titanium.model.Batch
import com.xrouge.mot.titanium.model.Element
import com.xrouge.mot.titanium.mongo.Dao
import io.vertx.core.Vertx


class FrontService(vertx: Vertx) {

    val dao = Dao(vertx)

    fun getAllElements(handler: (List<Element>) -> Unit) {
        dao.findAll(handler)
    }

    fun save(element: Element, handler: () -> Unit) {
        dao.save(element, handler)
    }

    fun update(element: Element, handler: (String, Boolean) -> Unit) {
        dao.update(element, handler)
    }

    fun remove(element: Element, handler: (String) -> Unit) {
        dao.remove(element, handler)
    }

    fun getElementsFromBatch(batch: Batch, handler: (List<Element>) -> Unit){
        getAllElements({ allElements ->
            handler(allElements.filter { it.batch == batch })
        })
    }

    fun searchElements(query: String, handler: (List<Element>) -> Unit){
        getAllElements({ allElements ->
            handler(allElements.filter { it.contains(query.toLowerCase()) })
        })
    }

    fun findOne(id: String, handler: (Element) -> Unit){
        dao.findOne(id, handler)
    }

    fun getElementSearchText(id: String, handler: (String) -> Unit){
        findOne(id, { element ->
            handler(element.getSearchText())
        })
    }
}