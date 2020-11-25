package org.novanto.firebaselivenessdetection

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import id.privy.livenessfirebasesdk.LivenessApp
import id.privy.livenessfirebasesdk.entity.LivenessItem
import id.privy.livenessfirebasesdk.listener.PrivyCameraLivenessCallBackListener
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.File.separator
import java.io.FileOutputStream
import java.io.OutputStream

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val livenessApp = LivenessApp.Builder(this)
            .setDebugMode(false)
            .setBlinkInstructions("Kedipkan mata")
            .setMotionInstruction("Lihat ke kiri", "Lihat ke kanan")
            .setSuccessText("Berhasil! Silahkan lihat ke kamera lagi untuk mengambil foto")
            .setInstructions("Lihat ke kamera dan tempatkan wajah pada lingakaran hijau")
            .build()

        buttonStart.setOnClickListener {
            livenessApp.start(object : PrivyCameraLivenessCallBackListener {

                override fun success(livenessItem: LivenessItem?) {
                    if (livenessItem != null) {
                        test_image.setImageBitmap(livenessItem.imageBitmap)
                        saveImage(livenessItem.imageBitmap,"photo1")
                        toast("photo is saved")
                    }
                }

                override fun failed(t: Throwable?) {

                }

            })
        }
    }

    // Method to save an image to gallery and return uri
    private fun saveImage(bitmap: Bitmap, title:String):Uri{

        // Save image to gallery
        val savedImageURL = MediaStore.Images.Media.insertImage(
            contentResolver,
            bitmap,
            title,
            "Image of $title"
        )

        // Parse the gallery image url to uri
        return Uri.parse(savedImageURL)
    }
}

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}