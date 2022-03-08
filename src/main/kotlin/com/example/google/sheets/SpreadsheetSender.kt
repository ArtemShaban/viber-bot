package com.example.google.sheets

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.model.ValueRange

class SpreadsheetSender {

    private val applicationName = "Google Sheets API Java Quickstart"
    private val jsonFactory: JsonFactory = GsonFactory.getDefaultInstance()
    private val httpTransport = GoogleNetHttpTransport.newTrustedTransport()

    //https://docs.google.com/spreadsheets/d/1aC6flxcfGVBR9A0f2X5GRK0MfSjYfU7qr15cup_WY5Y/edit#gid=0
    private val spreadsheetId = "1aC6flxcfGVBR9A0f2X5GRK0MfSjYfU7qr15cup_WY5Y"

    private var credentials: Credential = GoogleCredentialProvider().getCredentialsFromServiceAccount()
    private val service = Sheets.Builder(httpTransport, jsonFactory, credentials)
        .setApplicationName(applicationName)
        .build()

    //todo use coroutines to not block thread
    fun append(values: List<String>) {
        val range = "B2"
        val valueRange = ValueRange()
            .setValues(listOf(values))
        service
            .spreadsheets()
            .values()
            .append(spreadsheetId, range, valueRange)
            .setValueInputOption("USER_ENTERED")
            .execute()
    }
}
