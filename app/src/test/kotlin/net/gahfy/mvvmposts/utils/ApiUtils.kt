package net.gahfy.mvvmposts.utils

import com.google.gson.reflect.TypeToken
import com.squareup.moshi.Moshi
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader

lateinit var POST_MOCK_PATH: String

class ApiUtils {
    companion object {
        inline fun <reified T : Any> getUrl(jsonPath: String): T? {
            val buf = StringBuilder()

            val kotlinBuildClassesFolder = ApiUtils::class.java.protectionDomain.codeSource.location.path
            val assetsPath = kotlinBuildClassesFolder.replace("/build/tmp/kotlin-classes/debugUnitTest/", "/src/test/assets/api_mocks/$jsonPath")

            val inputStream = FileInputStream(assetsPath)
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))

            var str: String? = bufferedReader.readLine()
            while (str != null) {
                buf.append(str)
                str = bufferedReader.readLine()
            }
            inputStream.close()
            bufferedReader.close()

            val moshi = Moshi.Builder().build()
            val jsonAdapter = moshi.adapter<T>(object : TypeToken<T>() {}.type)

            return jsonAdapter.fromJson(buf.toString())
        }
    }
}