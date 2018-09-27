package com.xrouge.mot.titanium.bot

import fr.vsct.tock.bot.connector.slack.addSlackConnector


object SlackBotConfiguration {
    private val token1 = ""
    private val token2 = ""
    private val token3 = ""


    fun registerSlackConnector() {
        testBot.addSlackConnector(token1, token2, token3)
    }
}