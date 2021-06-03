package com.dicoding.picodiploma.feedeepaplikasi.Home

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.dicoding.picodiploma.feedeepaplikasi.Detail.DetailActivity
import com.dicoding.picodiploma.feedeepaplikasi.R
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.io.IOException
import java.util.ArrayList

class MainAccessCamera : AppCompatActivity() {

    private lateinit var btn: Button
    private lateinit var uploadButton: Button
    private var imageview: ImageView? = null
    private val GALLERY = 1
    private val CAMERA = 2
    private lateinit var restApi:RestApi
    private lateinit var progressBar: ProgressBar
    private lateinit var listview: ListView
    private  var base64Image: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_access_camera)

        restApi = RestApi(this)
        btn = findViewById<View>(R.id.btn) as Button
        imageview = findViewById<View>(R.id.iv) as ImageView
        progressBar = findViewById(R.id.progres)
        listview = findViewById(R.id.list_item)
        uploadButton = findViewById(R.id.btn_upload)
        btn.setOnClickListener { showPictureDialog() }
        uploadButton.setOnClickListener{ uploadImage()}

    }

    private fun uploadImage() {
        base64Image?.let {
            progressBar.visibility = View.VISIBLE
            restApi.uploadImage(it, object : AsyncHttpResponseHandler() {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?
                ) {
                    progressBar.visibility = View.GONE
                    Log.e("TAG", "onSuccess: $statusCode",)
                    responseBody?.let { byt ->
                        val result = String(byt)
                        Log.d("TAG", result)
                        try {
                            val responseObject = JSONObject(result)
                            val image = responseObject.getString("image_base64")
                            val items = responseObject.getJSONArray("terdeteksi")
                            val data = ArrayList<String>()
                            for (i in 0 until items.length()){
                                data.add(items.getString(i))
                            }
                            setResultUpload(image, data)
                            Toast.makeText(this@MainAccessCamera, items.toString(), Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            Toast.makeText(this@MainAccessCamera, e.message, Toast.LENGTH_SHORT).show()
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?,
                    error: Throwable?
                ) {
//                    progressBar.visibility = View.GONE
//                    Toast.makeText(this@MainActivity, error?.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
    private fun setResultUpload(image: String, data: ArrayList<String>) {
        val adapter = ArrayAdapter<String>(this@MainAccessCamera, android.R.layout.simple_list_item_1, android.R.id.text1, data)
        listview.adapter = adapter
        listview.setOnItemClickListener { parent, view, position, id ->

            val intent = Intent(this@MainAccessCamera, DetailActivity::class.java)
            intent.putExtra("image", image)
            intent.putExtra("nama", data[position])
            startActivity(intent)
        }

    }

    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(
            pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> choosePhotoFromGallary()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }

    @Suppress("DEPRECATION")
    fun choosePhotoFromGallary() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        startActivityForResult(galleryIntent, GALLERY)
    }

    @Suppress("DEPRECATION")
    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY) {
            if (data != null) {
                val contentURI = data.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    Toast.makeText(this@MainAccessCamera, "Image Saved!", Toast.LENGTH_SHORT).show()
                    base64Image=bitmap.toBase64String()
                    imageview!!.setImageBitmap(bitmap)


                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this@MainAccessCamera, "Failed!", Toast.LENGTH_SHORT).show()
                }

            }

        } else if (requestCode == CAMERA) {
            val thumbnail = data!!.extras!!.get("data") as Bitmap
            base64Image=thumbnail.toBase64String()
            imageview!!.setImageBitmap(thumbnail)
            Toast.makeText(this@MainAccessCamera, "Image Saved!", Toast.LENGTH_SHORT).show()
        }
    }
}