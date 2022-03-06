package com.example.google.sheets

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.services.sheets.v4.SheetsScopes
import java.io.FileNotFoundException
import java.io.InputStream

class GoogleCredentialProvider {

    fun getCredentialsFromServiceAccount(): Credential {
        val credentialStream = getGoogleCredentialJsonEnvVariable()
//        val credentialStream = getGoogleCredentialJsonFromFile() //for testing on local server

        return GoogleCredential
            .fromStream(credentialStream)
            .createScoped(listOf(SheetsScopes.SPREADSHEETS))
    }

    private fun getGoogleCredentialJsonEnvVariable(): InputStream {
        val credentials = System.getenv("GOOGLE_SPREADSHEET_CREDENTIALS")
            ?: throw RuntimeException("GOOGLE_SPREADSHEET_CREDENTIALS env variable it not specified")
        return credentials.byteInputStream()
    }

    private fun getGoogleCredentialJsonFromFile(): InputStream {
        val filePath = "/credentials.json"
        return SpreadsheetSender::class.java.getResourceAsStream(filePath)
            ?: throw FileNotFoundException("Resource not found: " + filePath)
    }

}