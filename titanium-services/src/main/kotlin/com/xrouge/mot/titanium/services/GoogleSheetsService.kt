package com.xrouge.mot.titanium.services

import com.google.api.services.sheets.v4.model.*
import com.xrouge.mot.titanium.model.Element
import com.xrouge.mot.titanium.mongo.Dao
import com.xrouge.mot.titanium.partners.GoogleDriveClient
import com.xrouge.mot.titanium.partners.GoogleSheetsClient
import com.xrouge.mot.titanium.util.logError
import com.xrouge.mot.titanium.util.logInfo
import io.vertx.core.Vertx


class GoogleSheetsService(val vertx: Vertx) {

    val dao = Dao(vertx)

    fun importFromGoogleSheets(handler: (String) -> Unit) {
        dao.removeAll({ logInfo<GoogleSheetsService> { "Cleared the database" } })
        vertx.executeBlocking<Void>({
            val elements = GoogleSheetsClient.readFromGoogleSheets()
            logInfo<GoogleSheetsService> { "saving ${elements.size} in database" }
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

    fun exportToGoogleSheets(exportFolderName: String, spreadsheetName: String, handler: (String) -> Unit) {
        vertx.executeBlocking<Void>({
            logInfo<GoogleSheetsClient> { "starting export" }
            val spreadsheetId = GoogleSheetsClient.createSpreadsheet(spreadsheetName)
            val folderId = GoogleDriveClient.createFolder(exportFolderName)
            GoogleDriveClient.moveFileToFolder(spreadsheetId, folderId)
            dao.findAll { elements ->
                GoogleSheetsClient.writeSpreadSheet(spreadsheetId, elements)
                it.complete()
            }
        }, {
            if(it.succeeded()) {
                handler("export successful")
            } else {
                logError<GoogleSheetsService> { "export failed, reason: ${it.cause().message}"  }
                val stackTrace = it.cause().stackTrace.map { "${it.className}.${it.methodName} at ${it.lineNumber} "}.joinToString("/n/t")
                logError<GoogleSheetsService> { "stackTrace: $stackTrace" }
                handler("export failed, reason: ${it.cause().message}")
            }
        })
    }
}