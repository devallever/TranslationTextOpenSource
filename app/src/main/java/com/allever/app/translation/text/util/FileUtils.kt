package com.allever.app.translation.text.util

import java.io.File
import java.io.FileOutputStream

object FileUtils {
    fun saveByteArray2File(byteArray: ByteArray?, path: String): Boolean {
        val file = File(path)
        return saveByteArray2File(byteArray, file.parent, file.name)
    }


    fun saveByteArray2File(byteArray: ByteArray?, dir: String, fileName: String): Boolean {
        val file = File(dir, fileName)

        createDir(dir)

        if (file.exists()) {
            file.delete()
        } else {
            file.createNewFile()
        }

        val fos = FileOutputStream(file)
        try {
            fos.write(byteArray)
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        } finally {
            fos.close()
        }
        return true
    }

    private fun createDir(dir: String): Boolean {
        val dirFile = File(dir)
        return if (dirFile.exists()) {
            true
        } else {
            dirFile.mkdirs()
        }
    }
}