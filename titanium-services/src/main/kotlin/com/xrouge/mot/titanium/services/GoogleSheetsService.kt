package com.xrouge.mot.titanium.services

import com.xrouge.mot.titanium.mongo.Dao
import com.xrouge.mot.titanium.partners.GoogleSheetsClient
import com.xrouge.mot.titanium.util.logInfo
import io.vertx.core.Vertx


class GoogleSheetsService(val vertx: Vertx) {

    val dao = Dao(vertx)

    fun importFromGoogleSheets(handler: (String) -> Unit) {
        dao.removeAll({ logInfo<GoogleSheetsService> { "Cleared the database" } })
        vertx.executeBlocking<Void>({
            val elements = GoogleSheetsClient.readFromGoogleSheets()
            logInfo<GoogleSheetsService> { "saving ${elements.size} in database" }
//            elements.forEach { element ->
//                logInfo<GoogleSheetsService> { "saving ${element.name}" }
//                dao.save(element, {logInfo<GoogleSheetsService> { "saved ${element.name}" }})
//            }
            dao.saveAll(elements, {
                logInfo<GoogleSheetsService>{ "all elements saved"}
                it.complete()
            })

        }, {
            res ->
            if(res.succeeded()){
                handler("import is successful")
            } else {
                handler("import failed because ${res.cause().message}")
            }
        })
    }

    fun exportToGoogleSheets() {}
}