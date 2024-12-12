package com.example.capstoneemissioncarbon.ui.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {

    // LiveData untuk teks yang akan ditampilkan
    private val _text = MutableLiveData<String>().apply {
        value = "This is Profile Fragment"
    }
    val text: LiveData<String> = _text

    // LiveData untuk gambar profil
    private val _profileImageUri = MutableLiveData<String>()
    val profileImageUri: LiveData<String> get() = _profileImageUri

    // Fungsi untuk memperbarui gambar profil
    fun updateProfileImage(uri: String) {
        _profileImageUri.value = uri
    }

    // Fungsi untuk memperbarui teks jika diperlukan
    fun updateText(newText: String) {
        _text.value = newText
    }
}
