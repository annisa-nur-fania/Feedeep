package com.dicoding.picodiploma.feedeepaplikasi

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.dicoding.picodiploma.feedeepaplikasi.Detail.DetailActivity
import com.dicoding.picodiploma.feedeepaplikasi.databinding.ActivityDetailBinding
import com.dicoding.picodiploma.feedeepaplikasi.databinding.ActivityRecommendBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class ActivityRecommend : AppCompatActivity() {

    companion object {
        private val TAG = ActivityRecommend::class.java.simpleName
    }
    private lateinit var binding : ActivityRecommendBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecommendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getRecommend()


    }
        fun getRecommend() {

            val client = AsyncHttpClient()
            client.addHeader("User-Agent", "request")
            client.addHeader("Authorization", "token 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")

            val url ="https://test-gateway-pkghig2.de.gateway.dev/data_kebutuhan_hewan?id_hewan=kebutuhan_sapi"
            client.get(url, object : AsyncHttpResponseHandler() {
                override fun onSuccess(
                    statusCode: Int,

                    headers: Array<Header>,
                    responseBody: ByteArray
                ) {
                    // Jika koneksi berhasil
                    binding.progressBar.visibility = View.INVISIBLE

                    val result = String(responseBody)
                    val d = Log.d(TAG, result)
                    try {
                        val responseObject = JSONObject(result)
                        val nutrientkalsium = responseObject.getString("kalsium")
                        val nutrientvitamin_A = responseObject.getString("vitamin_A")
                        val nutrientkaroten = responseObject.getString("karoten")
                        val nutrientfosfor = responseObject.getString("fosfor")
                        val nutrientprotein = responseObject.getString("protein")
                        binding.txtKalsium.text = nutrientkalsium
                        binding.txtVitaminA.text = nutrientvitamin_A
                        binding.txtKaroten.text = nutrientkaroten
                        binding.txtFosfor.text = nutrientfosfor
                        binding.txtProtein.text = nutrientprotein
                    }
                    catch (e: Exception) {
                        Toast.makeText(this@ActivityRecommend, e.message, Toast.LENGTH_SHORT).show()
                        e.printStackTrace()
                    }

                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<Header>,
                    responseBody: ByteArray,
                    error: Throwable
                ) {
                    // Jika koneksi gagal
                    binding.progressBar.visibility = View.INVISIBLE
                    val errorMessage = when (statusCode) {
                        401 -> "$statusCode : Bad Request"
                        403 -> "$statusCode : Forbidden"
                        404 -> "$statusCode : Not Found"
                        else -> "$statusCode : ${error.message}"
                    }
                    Toast.makeText(this@ActivityRecommend, errorMessage, Toast.LENGTH_SHORT).show()
                }
            })



    }
}