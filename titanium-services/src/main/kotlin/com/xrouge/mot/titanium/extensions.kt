package com.xrouge.mot.titanium

import io.vertx.core.http.HttpServerResponse

fun HttpServerResponse.endOk(response: String) {
    this.statusCode = 200
    this.end(response)
}

fun HttpServerResponse.endNotOk(response: String) {
    this.statusCode = 500
    this.end(response)
}
