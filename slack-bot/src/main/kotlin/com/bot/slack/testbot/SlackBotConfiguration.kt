package com.bot.slack.testbot

import fr.vsct.tock.bot.connector.slack.addSlackConnector


object SlackBotConfiguration {
    fun registerSlackConnector() {
        addSlackConnector("titanium-slack-bot",
                "/slack",
                "titanium-slack-bot",
                "T77NUJQ4X",
                "B82LCTWP9",
                "lKNZGuKOiJpani1cHFIZ1bP9"
                )
    }
}