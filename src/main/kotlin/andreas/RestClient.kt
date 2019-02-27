package andreas

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.fuel.json.FuelJson
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result


var externalSystem = mapOf(
        "ASK-Auskunft" to "https://services.dev.ask.cp.creditreform.de:8445/ask-auskunft/v1/info/systemInfo",
        "ASK-Verzeichnis" to "https://services.dev.ask.cp.creditreform.de:8447/ask-verzeichnis/v1/info/systemInfo",
        "ASK-Inkasse" to "https://services.dev.ask.cp.creditreform.de:8443/ask-inkasso/v1/info/systemInfo"
)


fun show_system_info(name: String, url: String) {

//    url.httpGet().responseString { _, response, result ->
//        when (result) {
//            is Result.Failure -> {
//                println(result)
//            }
//            is Result.Success -> {
//                val data = result.get()
//            }
//        }
//    }




    Fuel.get(url).responseJson { request: Request, response: Response, result: Result<FuelJson, FuelError> ->
        val json = result.get().obj()
        println(name)
        println(json["serviceName"])
        println(json["ServiceHealth"])
        println(json["WarVersion"])
    }



//    Fuel.get(url).responseObject<String> {request: Request, response: Response, result: Result<T, FuelError> -> println("asdfj") }


}


fun main(args: Array<String>) {
    show_system_info("Hallo", externalSystem["ASK-Auskunft"] ?: throw IllegalArgumentException())
    Thread.sleep(100000)

}