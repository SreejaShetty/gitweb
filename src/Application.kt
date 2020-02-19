package com.suryadigital.automatedturk

import com.google.gson.Gson
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import com.suryadigital.automatedturk.github.gitHub
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.client.HttpClient
import io.ktor.client.features.UserAgent
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging
import io.ktor.features.*
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.request.receive
import io.ktor.request.receiveText
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.server.engine.ShutDownUrl
import org.slf4j.event.Level
import java.awt.List
import java.text.DateFormat
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

internal val httpClient: HttpClient = HttpClient {
    install(Logging) {
        level = LogLevel.ALL
    }
    install(UserAgent) { agent = "Surya Digital Automated Turk" }
}

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(@Suppress("UNUSED_PARAMETER") testing: Boolean = false) {
    install(CallLogging) {
        level = Level.INFO
    }

    install(ContentNegotiation){
        gson{
            setPrettyPrinting()
            serializeSpecialFloatingPointValues()
            enableComplexMapKeySerialization()
            setDateFormat(DateFormat.LONG)
            setPrettyPrinting()
        }
    }
    install(DefaultHeaders) {
        header("X-Engine", "Ktor")
    }

    install(ForwardedHeaderSupport)
    install(XForwardedHeaderSupport)

    install(ShutDownUrl.ApplicationCallFeature) {
        shutDownUrl = "/ktor/application/shutdown"
        // A function that will be executed to get the exit code of the process
        exitCodeSupplier = { 0 }
    }

    routing {
        get("/") {
            call.respondText("HEALTHY", contentType = ContentType.Text.Plain)
        }
        route("/github") {
            gitHub()
        }
        post("/"){
            val json = call.receive<String>()
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            print(json)
            print(moshi)
            call.respondText(json)
            // val msg=obj.commits
            //val obj=gson.parse
        }
    }
}






