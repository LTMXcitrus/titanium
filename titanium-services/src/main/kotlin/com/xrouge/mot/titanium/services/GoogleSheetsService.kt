package com.xrouge.mot.titanium.services

import com.google.api.services.sheets.v4.model.Request
import com.google.api.services.sheets.v4.model.ValueRange
import com.xrouge.mot.titanium.Config
import com.xrouge.mot.titanium.model.ClosetLocation
import com.xrouge.mot.titanium.model.Element
import com.xrouge.mot.titanium.mongo.ElementDao
import com.xrouge.mot.titanium.partners.GoogleDriveClient
import com.xrouge.mot.titanium.partners.GoogleSheetsClient
import com.xrouge.mot.titanium.util.logError
import com.xrouge.mot.titanium.util.logInfo
import io.vertx.core.Vertx
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat


class GoogleSheetsService(val vertx: Vertx) {

    private val dao = ElementDao(vertx)

    fun importFromSpreadsheet(spreadsheetId: String, handler: (String) -> Unit) {
        dao.removeAll({ logInfo<GoogleSheetsService> { "Cleared the database" } })
        vertx.executeBlocking<Void>({
            val elements = GoogleSheetsClient.readFromGoogleSheets(spreadsheetId, "bot data!A2:I").mapNotNull { row ->
                Element.parseFromSheetRow(row)
            }
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

    fun save(handler:(String) -> Unit) {
        val stringDate = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm").print(LocalDateTime.now())
        val name = "Sauvegarde $stringDate"
        exportToGoogleSheets(name, name, handler)
    }

    fun exportToGoogleSheets(exportFolderName: String, spreadsheetName: String, handler: (String) -> Unit) {
        vertx.executeBlocking<String>({
            logInfo<GoogleSheetsClient> { "starting export" }
            val spreadsheetId = GoogleSheetsClient.createSpreadsheet(spreadsheetName)
            val subFolderId = GoogleDriveClient.createFolder(Config.titaniumFolderId, exportFolderName)
            GoogleDriveClient.moveFileToFolder(spreadsheetId, subFolderId)
            dao.findAll { elements ->
                writeSpreadSheet(spreadsheetId, elements)
                it.complete(spreadsheetId)
            }
        }, {
            if (it.succeeded()) {
                handler(it.result())
            } else {
                logError<GoogleSheetsService> { "export failed, reason: ${it.cause().message}" }
                handler("export failed, reason: ${it.cause().message}")
            }
        })
    }

    private fun writeSpreadSheet(spreadsheetId: String, elements: List<Element>): String {
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
            formatRequests.add(GoogleSheetsClient.darkenRow(botDataSheetId, botSheetValues.size))
            botSheetValues.add(emptyList())
            val sheetId = GoogleSheetsClient.addSheet(spreadsheetId, location.location)
            formatRequests.addAll(GoogleSheetsClient.headerRows(sheetId, listOf(0)))
            formatRequests.addAll(listOf(GoogleSheetsClient.resizeColumns(sheetId, 0,2, 300),
                    GoogleSheetsClient.resizeColumns(sheetId,2, null, 120)))
            formatRequests.add(GoogleSheetsClient.freezeColumnsAndRows(sheetId))
            val shelfBody = ValueRange()
                    .setValues(shelfSheetValues)
            GoogleSheetsClient.appendToSpreadsheet(spreadsheetId, "'${location.location}'!A1:J", shelfBody)
            logInfo<GoogleSheetsClient> { "Sheet '${location.location}' written" }
        }
        val body = ValueRange()
                .setValues(botSheetValues)
        GoogleSheetsClient.appendToSpreadsheet(spreadsheetId,"'bot data'!A1:J", body )
        formatRequests.addAll(GoogleSheetsClient.headerRows(botDataSheetId, listOf(0)))
        formatRequests.addAll(listOf(GoogleSheetsClient.resizeColumns(botDataSheetId, 0,2, 300),
                GoogleSheetsClient.resizeColumns(botDataSheetId,2, null, 120)))
        formatRequests.add(GoogleSheetsClient.freezeColumnsAndRows(botDataSheetId))

        GoogleSheetsClient.execRequests(spreadsheetId, formatRequests)

        logInfo<GoogleSheetsClient> { "Sheet 'bot data' written" }

        return spreadsheetId
    }

    fun importFromFolder(folderId: String, handler: (String) -> Unit) {
        val spreadsheetId = DriveService().childrendIds(folderId).firstOrNull()
        if(spreadsheetId == null){
            handler("no spreadsheet found")
        } else {
            importFromSpreadsheet(spreadsheetId, handler)
        }
    }
}