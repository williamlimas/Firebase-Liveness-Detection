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

//    /// @param folderName can be your app's name
//    private fun saveImage(bitmap: Bitmap, context: Context, folderName: String) {
//        if (android.os.Build.VERSION.SDK_INT >= 29) {
//            val values = contentValues()
//            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" + folderName)
//            values.put(MediaStore.Images.Media.IS_PENDING, true)
//            // RELATIVE_PATH and IS_PENDING are introduced in API 29.
//
//            val uri: Uri? = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
//            if (uri != null) {
//                saveImageToStream(bitmap, context.contentResolver.openOutputStream(uri))
//                values.put(MediaStore.Images.Media.IS_PENDING, false)
//                context.contentResolver.update(uri, values, null, null)
//            }
//        } else {
//            val directory = File(Environment.getExternalStorageDirectory().toString() + separator + folderName)
//            // getExternalStorageDirectory is deprecated in API 29
//
//            if (!directory.exists()) {
//                directory.mkdirs()
//            }
//            val fileName = System.currentTimeMillis().toString() + ".png"
//            val file = File(directory, fileName)
//            saveImageToStream(bitmap, FileOutputStream(file))
//            if (file.absolutePath != null) {
//                val values = contentValues()
//                values.put(MediaStore.Images.Media.DATA, file.absolutePath)
//                // .DATA is deprecated in API 29
//                context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
//            }
//        }
//    }
//
//    private fun contentValues() : ContentValues {
//        val values = ContentValues()
//        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
//        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
//        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
//        return values
//    }
//
//    private fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream?) {
//        if (outputStream != null) {
//            try {
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
//                outputStream.close()
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }



