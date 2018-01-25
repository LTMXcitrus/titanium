package com.xrouge.mot.titanium.bot

import fr.vsct.tock.bot.connector.slack.addSlackConnector


object SlackBotConfiguration {
    fun registerSlackConnector() {
        addSlackConnector("titanium-slack-bot",
                "/slack",
                "titanium-slack-bot",
                "",
                "",
                ""
                )
    }
}