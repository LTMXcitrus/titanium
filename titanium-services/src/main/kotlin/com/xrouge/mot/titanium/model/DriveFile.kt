package com.xrouge.mot.titanium.model

import org.joda.time.DateTime

data class DriveFile(val name: String,
                     val fileId: String,
                     val modifiedTime: DateTime)