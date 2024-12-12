package com.example.capstoneemissioncarbon.ui.profile

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.capstoneemissioncarbon.R
import com.example.capstoneemissioncarbon.databinding.FragmentProfileBinding
import com.yalantis.ucrop.UCrop
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var profileViewModel: ProfileViewModel

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 100
        private const val CAMERA_REQUEST_CODE = 101
        private const val GALLERY_REQUEST_CODE = 102
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupProfileData()
        setupListeners()
    }

    private fun setupProfileData() {
        val sharedPreferences = requireActivity().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)

        binding.etUsername.setText(sharedPreferences.getString("username", ""))
        binding.email.setText(sharedPreferences.getString("email", ""))
        binding.phone.setText(sharedPreferences.getString("phone", ""))

        when (sharedPreferences.getString("gender", "")) {
            "Male" -> binding.rbMale.isChecked = true
            "Female" -> binding.rbFemale.isChecked = true
        }

        sharedPreferences.getString("birth_date", "")?.split(" ")?.let {
            binding.tvDay.text = it.getOrNull(0) ?: ""
            binding.tvMonth.text = it.getOrNull(1) ?: ""
            binding.tvYear.text = it.getOrNull(2) ?: ""
        }

        val profileImageUri = sharedPreferences.getString("profile_image_uri", null)
        if (profileImageUri.isNullOrEmpty()) {
            binding.profileImage.setImageResource(R.drawable.baseline_person_24)
        } else {
            binding.profileImage.setImageURI(Uri.parse(profileImageUri))
        }
    }

    private fun setupListeners() {
        binding.tvDay.setOnClickListener { showDatePicker() }
        binding.tvMonth.setOnClickListener { showDatePicker() }
        binding.tvYear.setOnClickListener { showDatePicker() }

        binding.btnSave.setOnClickListener { saveProfileData() }
        binding.btnUploadPicture.setOnClickListener { showImagePickerDialog() }
        binding.takePictureButton.setOnClickListener { checkAndRequestCameraPermission() }

        profileViewModel.profileImageUri.observe(viewLifecycleOwner) { imageUri ->
            binding.profileImage.setImageURI(Uri.parse(imageUri))
        }
    }

    private fun saveProfileData() {
        val sharedPreferences = requireActivity().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Menyimpan data profil ke SharedPreferences
        editor.putString("username", binding.etUsername.text.toString())
        editor.putString("email", binding.email.text.toString())
        editor.putString("phone", binding.phone.text.toString())
        editor.putString(
            "gender",
            view?.findViewById<RadioButton>(binding.groupGender.checkedRadioButtonId)?.text.toString()
        )
        editor.putString(
            "birth_date",
            "${binding.tvDay.text} ${binding.tvMonth.text} ${binding.tvYear.text}"
        )

        // Memastikan bahwa URI gambar profil juga disimpan
        val profileImageUri = sharedPreferences.getString("profile_image_uri", null)
        if (profileImageUri.isNullOrEmpty()) {
            editor.putString("profile_image_uri", "") // Default kosong jika belum ada gambar
        }

        editor.apply()

        // Mengirim broadcast untuk memberi tahu perubahan profil
        val intent = Intent("com.example.UPDATE_PROFILE")
        requireContext().sendBroadcast(intent)

        Toast.makeText(requireContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show()
    }


    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                binding.tvDay.text = day.toString()
                binding.tvMonth.text = SimpleDateFormat("MMM", Locale.getDefault())
                    .format(Calendar.getInstance().apply { set(Calendar.MONTH, month) }.time)
                binding.tvYear.text = year.toString()
            },
            calendar[Calendar.YEAR], calendar[Calendar.MONTH], calendar[Calendar.DAY_OF_MONTH]
        ).show()
    }

    private fun showImagePickerDialog() {
        val options = arrayOf("Gallery", "Take Picture", "Remove Picture")
        AlertDialog.Builder(requireContext())
            .setItems(options) { _, which ->
                when (which) {
                    0 -> openGallery()
                    1 -> checkAndRequestCameraPermission()
                    2 -> removeProfilePicture()
                }
            }.show()
    }

    private fun removeProfilePicture() {
        requireActivity().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
            .edit()
            .remove("profile_image_uri")
            .apply()

        binding.profileImage.setImageResource(R.drawable.baseline_person_24)
        Toast.makeText(requireContext(), "Profile picture removed", Toast.LENGTH_SHORT).show()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            type = "image/*"
        }
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    private fun checkAndRequestCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {
            openCamera()
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivityForResult(intent, CAMERA_REQUEST_CODE)
        } else {
            Toast.makeText(requireContext(), "Camera not available", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                GALLERY_REQUEST_CODE -> {
                    data?.data?.let { uri ->
                        val destinationUri = Uri.fromFile(File(requireContext().cacheDir, "cropped_image.jpg"))
                        UCrop.of(uri, destinationUri)
                            .withAspectRatio(1f, 1f) // Rasio 1:1 untuk lingkaran
                            .withMaxResultSize(1080, 1080) // Ukuran maksimum
                            .start(requireContext(), this)
                    }
                }
                UCrop.REQUEST_CROP -> {
                    data?.let {
                        val resultUri = UCrop.getOutput(it)
                        if (resultUri != null) {
                            val bitmap = loadImageFromUri(resultUri)
                            bitmap?.let { croppedBitmap ->
                                binding.profileImage.setImageBitmap(croppedBitmap) // Tampilkan gambar yang dicrop
                                val savedUri = saveImageToFile(croppedBitmap).toString() // Simpan ke file
                                saveImageUriToPreferences(savedUri) // Simpan URI ke SharedPreferences
                            } ?: Toast.makeText(context, "Failed to load cropped image", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            data?.let {
                val cropError = UCrop.getError(it)
                Toast.makeText(context, "Crop error: ${cropError?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun saveAndSetProfileImage(bitmap: Bitmap, uri: Uri) {
        val croppedBitmap = cropToCircle(bitmap)
        binding.profileImage.setImageBitmap(croppedBitmap)
        saveImageUriToPreferences(saveImageToFile(croppedBitmap).toString())
        requireContext().sendBroadcast(Intent("com.example.UPDATE_PROFILE"))
    }

    private fun cropToCircle(bitmap: Bitmap): Bitmap {
        val size = minOf(bitmap.width, bitmap.height)
        return Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888).apply {
            Canvas(this).apply {
                val paint = Paint().apply { isAntiAlias = true }
                drawOval(RectF(0f, 0f, size.toFloat(), size.toFloat()), paint)
                paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
                drawBitmap(bitmap, null, RectF(0f, 0f, size.toFloat(), size.toFloat()), paint)
            }
        }
    }

    private fun loadImageFromUri(uri: Uri): Bitmap? {
        return requireContext().contentResolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it) }
    }

    private fun saveImageUriToPreferences(uri: String) {
        requireActivity().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
            .edit()
            .putString("profile_image_uri", uri)
            .apply()
    }

    private fun saveImageToFile(bitmap: Bitmap): Uri {
        val file = File(requireContext().cacheDir, "profile_image.jpg").apply {
            FileOutputStream(this).use { bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it) }
        }
        return Uri.fromFile(file)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
