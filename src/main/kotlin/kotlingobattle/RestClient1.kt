package kotlingobattle

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.core.isSuccessful
import com.github.kittinunf.fuel.core.requests.CancellableRequest
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result


private var externalSystem = mapOf(
        "ASK-Auskunft" to "https://services.dev.ask.cp.creditreform.de:8445/ask-auskunft/v1/info/systemInfo",
        "ASK-Verzeichnis" to "https://services.dev.ask.cp.creditreform.de:8447/ask-verzeichnis/v1/info/systemInfo",
        "ASK-Inkasso" to "https://services.dev.ask.cp.creditreform.de:8443/ask-inkasso/v1/info/systemInfo"
)


fun show_system_info1(name: String, url: String): CancellableRequest {

    return Fuel.get(url).responseJson { _, response, result ->
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


fun main() {
    externalSystem.map { (name, url) ->
        show_system_info1(name, url)
    }.forEach { it.join() }
}