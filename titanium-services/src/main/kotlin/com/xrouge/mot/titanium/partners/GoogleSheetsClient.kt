package com.xrouge.mot.titanium.partners

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.SheetsScopes
import com.xrouge.mot.titanium.model.Element
import com.xrouge.mot.titanium.util.logInfo
import java.io.IOException


fun main(args: Array<String>) {
    println(GoogleSheetsClient.readFromGoogleSheets())
}

object GoogleSheetsClient {
    /** Application name.  */
    private val APPLICATION_NAME = "crf-05-titanium"

    /** Global instance of the JSON factory.  */
    private val JSON_FACTORY = JacksonFactory.getDefaultInstance()

    /** Global instance of the HTTP transport.  */
    private val HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport()


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
    fun getSheetsService(): Sheets {
        val credential = authorize()
        return Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build()
    }

    fun readFromGoogleSheets(): List<Element> {
        val service = getSheetsService()
        var sheet = service.spreadsheets().values().get("1Xu4eHUmxQfp8pnMPfSnOeQO5Lryyj2bgCZDo1dUmcTM", "bot data!A2:I").execute().getValues()
        logInfo<GoogleSheetsClient> { "found ${sheet.size} elements in google sheets" }
        sheet = sheet.filter { it.isNotEmpty() }
        logInfo<GoogleSheetsClient> { "removed empty rows" }
        return sheet.mapNotNull { row ->
            Element.parseFromSheetRow(row)
        }
    }
}