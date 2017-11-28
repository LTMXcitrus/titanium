package com.bot.slack.testbot

import fr.vsct.tock.bot.engine.BotBus

fun BotBus.oneOf(texts: List<String>): String {
    val randomIndex = (Math.random() * texts.size).toInt()
    return texts[randomIndex]
}

fun BotBus.oneOf(vararg texts: String): String {
    val randomIndex = (Math.random() * texts.size).toInt()
    return texts.toList()[randomIndex]
}