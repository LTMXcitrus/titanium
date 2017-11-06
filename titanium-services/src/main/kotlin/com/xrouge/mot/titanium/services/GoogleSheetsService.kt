package com.xrouge.mot.titanium.services

import com.google.api.services.sheets.v4.model.*
import com.xrouge.mot.titanium.model.ClosetLocation
import com.xrouge.mot.titanium.model.Element
import com.xrouge.mot.titanium.mongo.ElementDao
import com.xrouge.mot.titanium.partners.GoogleDriveClient
import com.xrouge.mot.titanium.partners.GoogleSheetsClient
import com.xrouge.mot.titanium.util.logError
import com.xrouge.mot.titanium.util.logInfo
import io.vertx.core.Vertx


class GoogleSheetsService(val vertx: Vertx) {

    val dao = ElementDao(vertx)

    fun importFromGoogleSheets(handler: (String) -> Unit) {
        dao.removeAll({ logInfo<GoogleSheetsService> { "Cleared the database" } })
        vertx.executeBlocking<Void>({
            val elements = GoogleSheetsClient.readFromGoogleSheets()
            logInfo<GoogleSheetsService> { "saving ${elements.size} in database" }
            dao.saveAll(elements, {
                logInfo<GoogleSheetsService> { "all elements saved" }
                it.complete()
            })

        }, {
            res ->
            if (res.succeeded()) {
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
                writeSpreadSheet(spreadsheetId, elements)
                it.complete()
            }
        }, {
            if (it.succeeded()) {
                handler("export successful")
            } else {
                logError<GoogleSheetsService> { "export failed, reason: ${it.cause().message}" }
                handler("export failed, reason: ${it.cause().message}")
            }
        })
    }

    fun writeSpreadSheet(spreadsheetId: String, elements: List<Element>) {
        val botDataSheetId = GoogleSheetsClient.addSheet(spreadsheetId, "bot data")
        GoogleSheetsClient.removeSheet(spreadsheetId, GoogleSheetsClient.getSheetId(spreadsheetId, "Sheet1"))

        val formatRequests = mutableListOf<Request>()
        val titles = listOf("Nom", "Infos supplémentaires", "perissable", "minimum", "stock", "expiration date", "to order", "Etagere", "Tags")
        val botSheetValues = mutableListOf<List<Any?>>(titles)
        val shelves = elements.groupBy { it.location }
        enumValues<ClosetLocation>().iterator().forEach { location ->
            val shelfSheetValues = mutableListOf<List<Any?>>(titles)
            shelves[location]?.forEach {
                botSheetValues.add(it.toRow())
                shelfSheetValues.add(it.toRow())
            }
            formatRequests.add(GoogleSheetsClient.darkenRows(botDataSheetId, botSheetValues.size))
            botSheetValues.add(emptyList())
            val sheetId = GoogleSheetsClient.addSheet(spreadsheetId, location.location)
            formatRequests.addAll(GoogleSheetsClient.headerRows(sheetId, listOf(0)))
            formatRequests.addAll(GoogleSheetsClient.resizeColums(sheetId))
            formatRequests.add(GoogleSheetsClient.freezeColumnsAndRows(sheetId))
            val shelfBody = ValueRange()
                    .setValues(shelfSheetValues)
            GoogleSheetsClient.service.spreadsheets().values().append(spreadsheetId, "'${location.location}'!A1:J", shelfBody)
                    .setValueInputOption("RAW")
                    .execute()
            logInfo<GoogleSheetsClient> { "Sheet '${location.location}' written" }
        }
        val body = ValueRange()
                .setValues(botSheetValues)
        GoogleSheetsClient.service.spreadsheets().values().append(spreadsheetId, "'bot data'!A1:J", body)
                .setValueInputOption("RAW")
                .execute()
        formatRequests.addAll(GoogleSheetsClient.headerRows(botDataSheetId, listOf(0)))
        formatRequests.addAll(GoogleSheetsClient.resizeColums(botDataSheetId))
        formatRequests.add(GoogleSheetsClient.freezeColumnsAndRows(botDataSheetId))

        GoogleSheetsClient.execRequests(spreadsheetId, formatRequests)

        logInfo<GoogleSheetsClient> { "Sheet 'bot data' written" }

    }
}