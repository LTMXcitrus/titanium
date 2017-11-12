package com.xrouge.mot.titanium.services

import com.xrouge.mot.titanium.Config
import com.xrouge.mot.titanium.model.DriveFile
import com.xrouge.mot.titanium.partners.GoogleDriveClient
import org.joda.time.DateTime
import org.joda.time.DateTimeZone

class DriveService {

    fun listChildrenOfFolder(): List<DriveFile> {
        return GoogleDriveClient.listFilesFromFolder(Config.titaniumFolderId).map { DriveFile(it.name, it.id, DateTime(it.modifiedTime.value, DateTimeZone.forID("Europe/Paris"))) }

    }

    fun childrendIds(folderId: String): List<String> {
        return GoogleDriveClient.listFilesFromFolder(folderId).map { it.id }
    }
}