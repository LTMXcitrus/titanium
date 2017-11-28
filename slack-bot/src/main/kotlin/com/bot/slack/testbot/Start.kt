package com.bot.slack.testbot

import com.bot.slack.testbot.SlackBotConfiguration.registerSlackConnector
import fr.vsct.tock.bot.importNlpDump
import fr.vsct.tock.bot.registerAndInstallBot

fun main(args: Array<String>) {
    Start.start()
}

object Start {

    fun start() {
        registerSlackConnector()

        registerAndInstallBot(testBot)

        importNlpDump("/crfbot_app.json")
    }
}