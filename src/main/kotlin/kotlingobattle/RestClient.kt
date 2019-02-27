package kotlingobattle

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.isSuccessful
import com.github.kittinunf.fuel.json.responseJson
import kotlinx.coroutines.runBlocking


var externalSystem = mapOf(
        "ASK-Auskunft" to "https://services.dev.ask.cp.creditreform.de:8445/ask-auskunft/v1/info/systemInfo",
        "ASK-Verzeichnis" to "https://services.dev.ask.cp.creditreform.de:8447/ask-verzeichnis/v1/info/systemInfo",
        "ASK-Inkasso" to "https://services.dev.ask.cp.creditreform.de:8443/ask-inkasso/v1/info/systemInfo"
)


fun show_system_info(name: String, url: String) {

    Fuel.get(url).responseJson { request, response, result ->
        if (!response.isSuccessful) {
            println("The HTTP request failed with error: $response")
        } else {
            val json = result.get().obj()

            println("System info   : $name")
            println("Service       : " + json["serviceName"])
            println("Health status : " + json["serviceHealth"])
            println("Version       : " + json["warVersion"])
        }
    }
}


fun main(args: Array<String>) = runBlocking {
    externalSystem.forEach {
        show_system_info(it.key, it.value)
    }
}