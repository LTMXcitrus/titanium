package com.xrouge.mot.titanium.util

import org.slf4j.LoggerFactory


inline fun <reified T : Any> logger() = LoggerFactory.getLogger(T::class.java)

inline fun <reified T : Any> logTrace(message: () -> String) {
    val logger = logger<T>()
    if (logger.isTraceEnabled) logger.trace(message())
}

inline fun <reified T : Any> logInfo(message: () -> String) {
    val logger = logger<T>()
    if (logger.isInfoEnabled) logger.info(message())
}

inline fun <reified T : Any> logWarn(message: () -> String) {
    val logger = logger<T>()
    if (logger.isWarnEnabled) logger.warn(message())
}

inline fun <reified T : Any> logWarn(message: () -> String, throwable: Throwable) {
    val logger = logger<T>()
    if (logger.isWarnEnabled) logger.warn(message(), throwable)
}

inline fun <reified T : Any> logWarn(throwable: Throwable) {
    val logger = logger<T>()
    if (logger.isWarnEnabled) logger.warn(throwable.message, throwable)
}

inline fun <reified T : Any> logError(message: () -> String) {
    val logger = logger<T>()
    if (logger.isErrorEnabled) logger.error(message())
}

inline fun <reified T : Any> logError(throwable: Throwable) {
    val logger = logger<T>()
    if (logger.isErrorEnabled) logger.error(throwable.message, throwable)
}
