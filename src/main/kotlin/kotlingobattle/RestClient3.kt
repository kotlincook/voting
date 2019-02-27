package kotlingobattle

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.isSuccessful
import com.github.kittinunf.fuel.core.requests.CancellableRequest
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.result.Result


private var externalSystem = mapOf(
        "ASK-Auskunft" to "https://services.dev.ask.cp.creditreform.de:8445/ask-auskunft/v1/info/systemInfo",
        "ASK-Verzeichnis" to "https://services.dev.ask.cp.creditreform.de:8447/ask-verzeichnis/v1/info/systemInfo",
        "ASK-Inkasso" to "https://services.dev.ask.cp.creditreform.de:8443/ask-inkasso/v1/info/systemInfo"
)

data class SystemInfo(
        val serviceName: String,
        val serviceHealth: String,
        val warVersion: String
)

fun show_system_info(name: String, url: String): CancellableRequest {

    return Fuel.get(url).responseObject { request, response, result: Result<SystemInfo, FuelError> ->
        if (!response.isSuccessful) {
            println("The HTTP request failed with error: $response")
        } else {
            println("System info   : $name")
            with(result.get()) {
                println("Service       : $serviceName")
                println("Health status : $serviceHealth")
                println("Version       : $warVersion")
            }
        }
    }
}


fun main() {
    externalSystem.map { (name, url) ->
        show_system_info(name, url)
    }.forEach { it.join() }
}
