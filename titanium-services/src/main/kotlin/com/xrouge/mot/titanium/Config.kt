package com.xrouge.mot.titanium

import java.util.*


object Config {
    val props = Properties()
    val titaniumFolderId: String

    init {
        props.load(Config::class.java.getResourceAsStream("/application.properties"))
        titaniumFolderId = props.getProperty("titanium.folder.id")
    }


}