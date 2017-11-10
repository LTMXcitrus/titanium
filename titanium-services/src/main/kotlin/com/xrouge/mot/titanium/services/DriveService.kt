package com.xrouge.mot.titanium.services

import com.xrouge.mot.titanium.model.DriveFile
import com.xrouge.mot.titanium.partners.GoogleDriveClient
import org.joda.time.DateTime
import org.joda.time.DateTimeZone

val folders = mapOf(Pair("inventaire", "0BxEfiC5NZQNGRWg1Y3Z2OXRZRjA"),
        Pair("titanium", "0B7KGd9STpji8WDR4MW03aEtoVzg"))

class DriveService {

    fun listChildrenOfFolder(folder: String): List<DriveFile> {
        val folderId = folders[folder]
        return if(folderId != null){
            GoogleDriveClient.listFilesFromFolder(folderId).map { DriveFile(it.name, it.id, DateTime(it.modifiedTime.value, DateTimeZone.forID("Europe/Paris"))) }
        } else {
            emptyList()
        }
    }

    fun childrendIds(folderId: String): List<String> {
        return GoogleDriveClient.listFilesFromFolder(folderId).map { it.id }
    }
}