package com.dicoding.picodiploma.loginwithanimation.view.addstory

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.dicoding.picodiploma.loginwithanimation.BuildConfig
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val MAXIMAL_SIZE = 1000000
private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(Date())

fun getImageUri(context: Context): Uri {
    var uri: Uri? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "$timeStamp.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/MyCamera/")
        }
        uri = context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
    }
    return uri ?: getImageUriForPreQ(context)
}

private fun getImageUriForPreQ(context: Context): Uri {
    val filesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val imageFile = File(filesDir, "/MyCamera/$timeStamp.jpg")
    if (imageFile.parentFile?.exists() == false) imageFile.parentFile?.mkdir()
    return FileProvider.getUriForFile(
        context,
        "${BuildConfig.APPLICATION_ID}.fileprovider",
        imageFile
    )
}

fun createCustomTempFile(context: Context): File {
    val cacheDir = context.externalCacheDir
    return File.createTempFile(timeStamp, ".jpg", cacheDir)
}


fun uriToFile(imageUri: Uri, context: Context): File {
    val tempFile = createCustomTempFile(context)
    context.contentResolver.openInputStream(imageUri)?.use { inputStream ->
        FileOutputStream(tempFile).use { outputStream ->
            val buffer = ByteArray(1024)
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }
        }
    }
    return tempFile
}

fun File.reduceImageSize(): File {
    val originalFile = this
    val bitmap = BitmapFactory.decodeFile(originalFile.path).adjustRotation(originalFile)
    var quality = 100
    var fileSize: Int

    do {
        val outputStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        val byteArray = outputStream.toByteArray()
        fileSize = byteArray.size
        quality -= 5
    } while (fileSize > MAXIMAL_SIZE)

    FileOutputStream(originalFile).use { fileOutputStream ->
        bitmap?.compress(Bitmap.CompressFormat.JPEG, quality, fileOutputStream)
    }

    return originalFile
}

fun Bitmap.adjustRotation(file: File): Bitmap? {
    val exif = ExifInterface(file.absolutePath)
    val orientation = exif.getAttributeInt(
        ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED
    )

    val rotationMatrix = Matrix()
    when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> rotationMatrix.postRotate(90F)
        ExifInterface.ORIENTATION_ROTATE_180 -> rotationMatrix.postRotate(180F)
        ExifInterface.ORIENTATION_ROTATE_270 -> rotationMatrix.postRotate(270F)
    }

    return if (orientation != ExifInterface.ORIENTATION_NORMAL) {
        Bitmap.createBitmap(this, 0, 0, width, height, rotationMatrix, true)
    } else {
        this
    }
}





