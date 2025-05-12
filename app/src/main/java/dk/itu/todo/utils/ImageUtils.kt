package dk.itu.todo.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import java.io.InputStream
import android.net.Uri

fun getRotatedBitmapFromUri(context: Context, uri: Uri): Bitmap {
    val inputStream = context.contentResolver.openInputStream(uri)!!
    val bitmap = BitmapFactory.decodeStream(inputStream)
    inputStream.close()

    val exifStream: InputStream = context.contentResolver.openInputStream(uri)!!
    val exif = ExifInterface(exifStream)
    val orientation = exif.getAttributeInt(
        ExifInterface.TAG_ORIENTATION,
        ExifInterface.ORIENTATION_NORMAL
    )
    exifStream.close()

    val rotationAngle = when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> 90f
        ExifInterface.ORIENTATION_ROTATE_180 -> 180f
        ExifInterface.ORIENTATION_ROTATE_270 -> 270f
        else -> 0f
    }

    return if (rotationAngle != 0f) rotateImage(bitmap, rotationAngle) else bitmap
}

fun rotateImage(source: Bitmap, angle: Float): Bitmap {
    val matrix = Matrix().apply { postRotate(angle) }
    return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
}
