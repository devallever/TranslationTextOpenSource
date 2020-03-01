package com.allever.app.translation.text.util

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object MD5 {

    fun getMD5Str(str: String): String? {
        var messageDigest: MessageDigest? = null

        try {
            messageDigest = MessageDigest.getInstance("MD5")

            messageDigest!!.reset()

            messageDigest.update(str.toByteArray(charset("UTF-8")))
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (messageDigest == null) {
            return null
        }

        val byteArray = messageDigest.digest()

        val md5StrBuff = StringBuffer()

        for (i in byteArray.indices) {
            if (Integer.toHexString(0xFF and byteArray[i].toInt()).length == 1) {
                md5StrBuff.append("0").append(
                    Integer.toHexString(0xFF and byteArray[i].toInt())
                )
            } else {
                md5StrBuff.append(Integer.toHexString(0xFF and byteArray[i].toInt()))
            }
        }

        // return md5StrBuff.substring(8, 24).toString().toUpperCase();
        return md5StrBuff.toString().toUpperCase()
    }

    /**
     * 转换成小写
     *
     * @param str
     * @return
     */
    fun getMD5StrToLowerCase(str: String): String {
        return getMD5Str(str)!!.toLowerCase()
    }
}