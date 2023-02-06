package com.hardcoreandroid.fileencryptiondemo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import java.io.File
import java.nio.charset.StandardCharsets

class FileEncryptionDemoViewModel(application: Application): AndroidViewModel(application) {
    private val mainKeyAlias by lazy {
        // Although you can define your own key generation parameter specification, it's
        // recommended that you use the value specified here.
        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        MasterKeys.getOrCreate(keyGenParameterSpec)
    }

    private val sharedPreferences by lazy {
        //The name of the file on disk will be this, plus the ".xml" extension.
        val sharedPrefsFile = "sharedPrefs"

        //Create the EncryptedSharedPremises using the key above
        EncryptedSharedPreferences.create(
            sharedPrefsFile,
            mainKeyAlias,
            getApplication(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun writeToSharedPrefs(value: String) {
        with (sharedPreferences.edit()) {
            putString("test", value)
            apply()
        }
    }

    fun readFromSharedPrefs(): String? {
        return sharedPreferences.getString("test", "")
    }

    private val encryptedFile by lazy {
        //This is the app's internal storage folder
        val baseDir = getApplication<Application>().filesDir

        //The encrypted file within the app's internal storage folder
        val fileToWrite = File(baseDir, "encrypted-file.txt")

        //Create the encrypted file
        EncryptedFile.Builder(
            fileToWrite,
            getApplication(),
            mainKeyAlias,
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()
    }

    fun writeToEncryptedFile(content: String) {
        //Open the file for writing, and write our contents to it.
        //Note how Kotlin's 'use' function correctly closes the resource after we've finished,
        //regardless of whether or not an exception was thrown.
        encryptedFile.openFileOutput().use {
            it.write(content.toByteArray(StandardCharsets.UTF_8))
            it.flush()
        }
    }

    fun readFromEncryptedFile(): String {
        //We will read up to the first 32KB from this file. If your file may be larger, then you
        //can increase this value, or read it in chunks.
        val fileContent = ByteArray(32000)

        //The number of bytes actually read from the file.
        val numBytesRead: Int

        //Open the file for reading, and read all the contents.
        //Note how Kotlin's 'use' function correctly closes the resource after we've finished,
        //regardless of whether or not an exception was thrown.
        try {
            encryptedFile.openFileInput().use {
                numBytesRead = it.read(fileContent)
            }
        }
        catch (e: Exception) {
            //For the sake of this demo, we ignore exceptions, but they should be properly handled
            //in production code!
            return ""
        }

        return String(fileContent, 0, numBytesRead)
    }
}