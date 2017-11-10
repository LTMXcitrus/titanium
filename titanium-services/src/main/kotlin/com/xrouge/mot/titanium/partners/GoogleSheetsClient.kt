package com.xrouge.mot.titanium.partners

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.SheetsScopes
import com.google.api.services.sheets.v4.model.*
import com.xrouge.mot.titanium.util.logInfo
import java.io.IOException


fun main(args: Array<String>) {
    logInfo<GoogleSheetsClient> { "sheet ID: " + GoogleSheetsClient.addSheet("1Xu4eHUmxQfp8pnMPfSnOeQO5Lryyj2bgCZDo1dUmcTM", "test sheet") }
}

object GoogleSheetsClient {
    private val service: Sheets
    /** Application name.  */
    private val APPLICATION_NAME = "crf-05-titanium"

    /** Global instance of the JSON factory.  */
    private val JSON_FACTORY = JacksonFactory.getDefaultInstance()

    /** Global instance of the HTTP transport.  */
    private val HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport()

    init {
        service = getSheetsService()
    }

    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * *
     * @throws IOException
     */
    /** Authorizes the installed application to access user's protected data.  */
    @Throws(Exception::class)
    private fun authorize(): Credential {
        return GoogleCredential.getApplicationDefault()
                .createScoped(listOf(SheetsScopes.SPREADSHEETS))
    }

    /**
     * Build and return an authorized Sheets API client service.
     * @return an authorized Sheets API client service
     * *
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun getSheetsService(): Sheets {
        val credential = authorize()
        return Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build()
    }

    fun readFromGoogleSheets(spreadsheetId: String, range: String): List<List<Any>> {
        var sheet = service.spreadsheets().values().get(spreadsheetId, range).execute().getValues()
        logInfo<GoogleSheetsClient> { "found ${sheet.size} elements in google sheets" }
        sheet = sheet.filter { it.isNotEmpty() }
        logInfo<GoogleSheetsClient> { "removed empty rows" }
        return sheet
    }

    fun createSpreadsheet(spreadsheetName: String): String {
        val spreadsheet = Spreadsheet()
        spreadsheet.properties = SpreadsheetProperties()
        spreadsheet.properties.title = spreadsheetName
        val created = service.spreadsheets().create(spreadsheet).execute()
        logInfo<GoogleSheetsClient> { "created spreadsheet '$spreadsheetName'" }
        return created.spreadsheetId
    }

    fun addSheet(spreadsheetId: String, title: String): Int {
        val request = AddSheetRequest()
        request.properties = SheetProperties().setTitle(title)
        val batchUpdate = BatchUpdateSpreadsheetRequest()
        batchUpdate.requests = listOf(Request().setAddSheet(request))
        val response = service.spreadsheets().batchUpdate(spreadsheetId, batchUpdate).execute()
        val addSheetResponse = response.replies[0]["addSheet"] as AddSheetResponse
        logInfo<GoogleSheetsClient> { "Sheet '$title' added to spreadsheet '$spreadsheetId'" }
        return addSheetResponse.properties.sheetId
    }

    fun getSheetId(spreadsheetId: String, sheetTitle: String): Int {
        val spreadsheet = service.spreadsheets().get(spreadsheetId).execute()
        val sheets = spreadsheet.sheets
        return sheets.first { it.properties.title == sheetTitle }.properties.sheetId
    }

    fun removeSheet(spreadsheetId: String, sheetId: Int) {
        val request = DeleteSheetRequest()
        request.sheetId = sheetId
        val batchUpdate = BatchUpdateSpreadsheetRequest()
        batchUpdate.requests = listOf(Request().setDeleteSheet(request))
        service.spreadsheets().batchUpdate(spreadsheetId, batchUpdate).execute()
        logInfo<GoogleSheetsClient> { "sheet '$sheetId' removed" }
    }

    fun execRequests(spreadsheetId: String, requests: List<Request>) {
        val batchUpdate = BatchUpdateSpreadsheetRequest().setRequests(requests)
        service.spreadsheets().batchUpdate(spreadsheetId, batchUpdate).execute()
    }

    fun darkenRow(sheetId: Int, row: Int): Request {
        val blackCellFormat = CellFormat().setBackgroundColor(Color().setBlue(0.5F).setGreen(0.5F).setRed(0.5F))
        val cellData = CellData().setUserEnteredFormat(blackCellFormat)
        val request = RepeatCellRequest()
        request.range = GridRange().setStartRowIndex(row).setEndRowIndex(row + 1).setSheetId(sheetId)
        request.cell = cellData
        request.fields = "userEnteredFormat(backgroundColor)"
        return Request().setRepeatCell(request)
    }

    fun headerRows(sheetId: Int, rows: List<Int>, bold: Boolean = true, fontSize: Int = 12): List<Request> {
        val headerCellFormat = CellFormat().setTextFormat(TextFormat().setBold(bold).setFontSize(fontSize))
        val cellData = CellData().setUserEnteredFormat(headerCellFormat)
        val requests = mutableListOf<Request>()
        rows.forEach { row ->
            val request = RepeatCellRequest()
            request.range = GridRange().setStartRowIndex(row).setEndRowIndex(row + 1).setSheetId(sheetId)
            request.cell = cellData
            request.fields = "userEnteredFormat(textFormat)"
            requests.add(Request().setRepeatCell(request))
        }
        return requests
    }

    fun resizeColumns(sheetId: Int, startIndex: Int, endIndex: Int?, pixelSize: Int): Request {
        val updateDimensions = UpdateDimensionPropertiesRequest()
        updateDimensions.range = DimensionRange().setSheetId(sheetId).setDimension("COLUMNS").setStartIndex(startIndex)
        if (endIndex != null) {
            updateDimensions.range.endIndex = endIndex
        }
        updateDimensions.properties = DimensionProperties().setPixelSize(pixelSize)
        updateDimensions.fields = "pixelSize"
        return Request().setUpdateDimensionProperties(updateDimensions)
    }

    fun freezeColumnsAndRows(sheetId: Int, frozenColumns: Int = 2, frozenRows: Int = 1): Request {
        val request = UpdateSheetPropertiesRequest()
        request.properties = SheetProperties()
        request.properties.gridProperties = GridProperties().setFrozenColumnCount(frozenColumns).setFrozenRowCount(frozenRows)
        request.properties.sheetId = sheetId
        request.fields = "gridProperties.frozenRowCount, gridProperties.frozenColumnCount"

        return Request().setUpdateSheetProperties(request)
    }

    fun appendToSpreadsheet(spreadsheetId: String, range: String, values: ValueRange) {
        service.spreadsheets().values().append(spreadsheetId, range, values)
                .setValueInputOption("RAW")
                .execute()
    }
}