package com.hercan.personneltrackingwithfacerecognition.ui.facerecognition

import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.hercan.personneltrackingwithfacerecognition.R
import com.hercan.personneltrackingwithfacerecognition.binding.viewBinding
import com.hercan.personneltrackingwithfacerecognition.databinding.FragmentInstantFaceRecognitionBinding
import com.hercan.personneltrackingwithfacerecognition.ui.getpersonneldata.Personnel
import com.hercan.personneltrackingwithfacerecognition.ui.getpersonneldata.toPersonnel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@Suppress("DEPRECATION")
@AndroidEntryPoint
class InstantFaceRecognitionFragment : Fragment(R.layout.fragment_instant_face_recognition) {

    private val binding by viewBinding(FragmentInstantFaceRecognitionBinding::bind)
    private lateinit var auth: FirebaseAuth
    private var adminMail: String = ""
    private val viewModel: InstantFaceRecognitionViewModel by viewModels()
    private var response: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth

        binding.btnCamera.setOnClickListener {
            dispatchTakePictureIntent()
        }
        binding.btnGallery.setOnClickListener {
            dispatchPickFromGalleryIntent()
        }
        binding.ivNotFolder.visibility = View.GONE
        binding.cl.visibility = View.GONE

        val gradientDrawable = GradientDrawable(
            GradientDrawable.Orientation.BOTTOM_TOP, intArrayOf(
                ContextCompat.getColor(requireContext(), android.R.color.white),
                ContextCompat.getColor(requireContext(), R.color.green_hornet)
            )
        )
        binding.layoutBackground.background = gradientDrawable


        arguments.let {
            adminMail =
                it?.let { it1 -> InstantFaceRecognitionFragmentArgs.fromBundle(it1).adminMail }
                    .toString()
        }
    }

    private fun observeViewModelData() {
        viewModel.response.observe(viewLifecycleOwner) {
            response = it
            binding.cl.visibility = View.VISIBLE
            getPersonnel()
        }

        viewModel.isOnLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
                binding.cl.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        viewModel.isOnError.observe(viewLifecycleOwner) {

            if (it == getString(R.string.no_connection_for_backend_tr)) {
                binding.tvNoConnection.visibility = View.VISIBLE
                binding.cl.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
            } else {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                binding.progressBar.visibility = View.GONE
                binding.ivNotFolder.visibility = View.VISIBLE
                binding.cl.visibility = View.GONE
            }
        }
    }

    private fun postViewModel(file: MultipartBody.Part) {
        viewModel.uploadImage(file)
        observeViewModelData()
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, 1)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                requireContext(),
                getString(R.string.failed_to_camera_initialize_tr),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun dispatchPickFromGalleryIntent() {
        val pickPhotoIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        try {
            startActivityForResult(pickPhotoIntent, 2)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                requireContext(),
                getString(R.string.failed_to_gallery_initialize_tr),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) = runBlocking {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                1 -> {
                    val imageBitmap = data?.extras?.get("data") as? Bitmap
                    imageBitmap?.let {
                        processAndUploadImage(it)
                    }
                }

                2 -> {
                    val imageUri = data?.data
                    imageUri?.let {
                        val imageBitmap =
                            MediaStore.Images.Media.getBitmap(requireContext().contentResolver, it)
                        processAndUploadImage(imageBitmap)
                    }
                }
            }
        }
    }

    private fun processAndUploadImage(imageBitmap: Bitmap) {
        val file = File(requireContext().cacheDir, "image.jpg")
        saveBitmapAsJpeg(imageBitmap, file)
        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val body =
            MultipartBody.Part.createFormData(getString(R.string.file_tr), file.name, requestFile)
        postViewModel(body)
    }

    private fun getPersonnel() {
        if (response != null) {
            if (response != getString(R.string.unknown)) {
                val db = FirebaseFirestore.getInstance()
                db.collection(getString(R.string.company_tr)).document(adminMail)
                    .collection(getString(R.string.personnel)).addSnapshotListener { value, _ ->
                        if (value != null) {
                            if (!value.isEmpty) {
                                val doc = value.documents
                                for (item in doc) {
                                    if (response == item.id) {
                                        val personnel = item.toPersonnel()
                                        bindPersonnel(personnel)
                                    }
                                }
                            }
                        }
                    }
            } else {
                binding.progressBar.visibility = View.GONE
            }
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun bindPersonnel(personnel: Personnel) = with(binding) {
        tvNameAndSurname.text = personnel.name
        tvBirthday.text = personnel.birthday
        tvDepartment.text = personnel.department
        if (personnel.photo != "") {
            ivPersonnelPhoto.setBackgroundResource(0)
            Picasso.get().load(personnel.photo).into(ivPersonnelPhoto)
        }
    }

    private fun saveBitmapAsJpeg(bitmap: Bitmap, file: File) {
        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                out?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}