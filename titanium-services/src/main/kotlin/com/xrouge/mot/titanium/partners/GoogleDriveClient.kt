package com.xrouge.mot.titanium.partners

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.batch.json.JsonBatchCallback
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.googleapis.json.GoogleJsonError
import com.google.api.client.http.HttpHeaders
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.File
import com.google.api.services.drive.model.Permission
import com.xrouge.mot.titanium.util.logError
import com.xrouge.mot.titanium.util.logInfo
import java.io.IOException


fun main(args: Array<String>) {
    println(GoogleDriveClient.listFilesFromFolder("1z7eQfwfacpTNK_BDxBtV7yINsmMaJwRI"))
}



object GoogleDriveClient {

    /** Application name.  */
    private val APPLICATION_NAME = "crf-05-titanium"

    /** Global instance of the JSON factory.  */
    private val JSON_FACTORY = JacksonFactory.getDefaultInstance()

    /** Global instance of the HTTP transport.  */
    private val HTTP_TRANSPORT: HttpTransport = GoogleNetHttpTransport.newTrustedTransport()

    /** Global instance of the scopes required by this quickstart.

     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/drive-java-quickstart
     */
    private val SCOPES = listOf(DriveScopes.DRIVE)

    private val driveService: Drive

    init {
        driveService = createDriveService()
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
                .createScoped(SCOPES)
    }

    /**
     * Build and return an authorized Drive client service.
     * @return an authorized Drive client service
     * *
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun createDriveService(): Drive {
        val credential = authorize()
        return Drive.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build()
    }

    fun updatePermissions() {
        val fileId = "1DZwQXbA3JOeI5zdWiz45sJFRU686pWf8lues9YNqY5Y"
        val callback = object : JsonBatchCallback<Permission>() {
            @Throws(IOException::class)
            override fun onFailure(e: GoogleJsonError,
                                   responseHeaders: HttpHeaders) {
                // Handle error
                logError<GoogleDriveClient> { e.message }
            }

            @Throws(IOException::class)
            override fun onSuccess(permission: Permission,
                                   responseHeaders: HttpHeaders) {
                logInfo<GoogleDriveClient> { "Permission ID: " + permission.id }
            }
        }
        val batch = driveService.batch()
        val userPermission = Permission()
                .setType("user")
                .setRole("owner")
                .setEmailAddress("mattcitron45140@gmail.com")
        driveService.permissions().create(fileId, userPermission)
                .setTransferOwnership(true)
                .setFields("id")
                .queue(batch, callback)
        batch.execute()
    }

    fun createFolder(parentId: String, name: String): String {
        val fileMetadata = File()
        fileMetadata.name = name
        fileMetadata.mimeType = "application/vnd.google-apps.folder"
        fileMetadata.parents = listOf(parentId)

        val file = driveService.files().create(fileMetadata)
                .setFields("id")
                .execute()
        logInfo<GoogleDriveClient> { "created folder '$name'" }
        return file.id
    }

    fun moveFileToFolder(fileId: String, folderId: String) {
        val file = driveService.files().get(fileId)
                .setFields("parents")
                .execute()
        val previousParents = StringBuilder()
        for (parent in file.parents) {
            previousParents.append(parent)
            previousParents.append(',')
        }
        driveService.files().update(fileId, null)
                .setAddParents(folderId)
                .setRemoveParents(previousParents.toString())
                .setFields("id, parents")
                .execute()
        logInfo<GoogleDriveClient> { "moved file '$fileId' to folder '$folderId'" }
    }

    fun listFilesFromFolder(folderId: String): List<File> {
        return driveService.files().list().setQ("'$folderId' in parents").setFields("files(name, id, modifiedTime)").execute().files
    }
}