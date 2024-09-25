package com.hercan.personneltrackingwithfacerecognition.ui.addnewpersonnel

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.GradientDrawable
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.hercan.personneltrackingwithfacerecognition.R
import com.hercan.personneltrackingwithfacerecognition.binding.viewBinding
import com.hercan.personneltrackingwithfacerecognition.databinding.FragmentAddNewPersonnelBinding
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar

class AddNewPersonnelFragment : Fragment(R.layout.fragment_add_new_personnel) {

    private val binding by viewBinding(FragmentAddNewPersonnelBinding::bind)
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private var selectedPicture: Uri? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private val firstVideoframes = mutableListOf<Bitmap>()
    private val secondVideoframes = mutableListOf<Bitmap>()
    private var controlStr = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindUI()
    }

    private fun registerLauncher() {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val intentFromResult = result.data
                    if (intentFromResult != null) {
                        selectedPicture = intentFromResult.data
                        if (selectedPicture != null) {
                            selectedPicture?.let {
                                binding.ivPersonnelPhoto.setBackgroundResource(R.color.cream)
                                binding.ivPersonnelPhoto.setImageURI(selectedPicture)
                            }
                        }
                    }
                }
            }
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
                if (result) {
                    val intentToGallery =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    activityResultLauncher.launch(intentToGallery)
                } else {
                    Toast.makeText(requireContext(), R.string.izin_gerekli, Toast.LENGTH_LONG)
                        .show()
                }
            }
    }

    private fun selectImage() {
        activity?.let {
            if (ContextCompat.checkSelfPermission(
                    requireActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)

                } else {
                    val intentToGallery =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    activityResultLauncher.launch(intentToGallery)
                }
            }
        }
    }

    private fun bitmapToUri(bitmap: Bitmap): Uri? {
        val file = File(requireContext().cacheDir, "image_${System.currentTimeMillis()}.png")
        try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

        return Uri.fromFile(file)
    }

    private fun addNewPerson() = with(binding) {

        val name = etName.text.toString()
        val surname = etSurname.text.toString()
        val birthday = etBirthday.text.toString()
        val tc = etTc.text.toString()
        val department = etDepartment.text.toString()
        val registrationDate = if (etRegistrationDate.text.toString() == "") {
            val calendar = Calendar.getInstance()
            val formatter = SimpleDateFormat("dd.MM.yyyy")
            formatter.format(calendar.time)
        } else {
            etRegistrationDate.text.toString()
        }

        if (name == "" || surname == "" || tc == "" || firstVideoframes.isEmpty() || secondVideoframes.isEmpty()) {
            if (name == "") etName.error = getString(R.string.bu_alan_bos_birakilamaz)
            if (surname == "") etSurname.error = getString(R.string.bu_alan_bos_birakilamaz)
            if (tc == "") etTc.error = getString(R.string.bu_alan_bos_birakilamaz) else TODO()
            if (firstVideoframes.isEmpty()) {
                Toast.makeText(activity, "Lütfen video ekleyiniz!", Toast.LENGTH_LONG).show()
            } else if (secondVideoframes.isEmpty()) Toast.makeText(
                activity, "Lütfen video ekleyiniz!", Toast.LENGTH_LONG
            ).show() else TODO()
        } else {
            scrollView.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            val postMap = hashMapOf<String, Any>()
            val currentUser = auth.currentUser
            if (selectedPicture != null) {
                val reference = storage.reference
                val imageReference =
                    reference.child("${currentUser?.email.toString()}/personnel/$name.jpg")
                firstVideoframes.forEachIndexed { index, bitmap ->
                    val uri = bitmapToUri(bitmap)
                    uri?.let {
                        reference.child("${currentUser?.email.toString()}/faceData/train/{${name.lowercase()}}/${name + index.toString()}.jpg")
                            .putFile(it)
                    }
                }
                secondVideoframes.forEachIndexed { index, bitmap ->
                    val uri = bitmapToUri(bitmap)
                    uri?.let {
                        reference.child("${currentUser?.email.toString()}/faceData/test/{${name.lowercase()}}/${name + index.toString()}.jpg")
                            .putFile(it)
                    }
                }
                imageReference.putFile(selectedPicture!!).addOnCompleteListener {
                    if (it.isSuccessful) {
                        imageReference.downloadUrl.addOnSuccessListener { uri ->
                            postMap["downloadUrl"] = uri.toString()
                            postMap["name"] = name
                            postMap["surname"] = surname
                            postMap["tc"] = tc
                            postMap["birthday"] = birthday
                            postMap["department"] = department
                            postMap["registrationDate"] = registrationDate
                            val ref = firestore.collection("sirket")
                                .document(currentUser?.email.toString()).collection("personnel")
                                .document(name)
                            ref.set(postMap).addOnSuccessListener {
                                Toast.makeText(
                                    activity, R.string.kayit_basarili, Toast.LENGTH_LONG
                                ).show()
                                requireActivity().supportFragmentManager.popBackStack()
                            }.addOnFailureListener {
                                Toast.makeText(
                                    activity, R.string.kayit_basarisiz, Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                }.addOnFailureListener {
                    Toast.makeText(activity, R.string.kayit_basarisiz, Toast.LENGTH_LONG).show()
                }
            } else {
                postMap["downloadUrl"] = ""
                postMap["name"] = name
                postMap["surname"] = surname
                postMap["tc"] = tc
                postMap["birthday"] = birthday
                postMap["department"] = department
                postMap["registrationDate"] = registrationDate
                val ref = firestore.collection("sirket").document(currentUser?.email.toString())
                    .collection("personnel").document(name)
                ref.set(postMap).addOnSuccessListener {
                    Toast.makeText(activity, R.string.kayit_basarili, Toast.LENGTH_LONG).show()
                    requireActivity().supportFragmentManager.popBackStack()
                }.addOnFailureListener {
                    Toast.makeText(activity, R.string.kayit_basarisiz, Toast.LENGTH_LONG).show()
                    requireActivity().supportFragmentManager.popBackStack()
                }
            }
        }
    }

    private fun bindUI() = with(binding) {
        registerLauncher()
        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        ivPersonnelPhoto.setOnClickListener { selectImage() }
        btnSave.setOnClickListener {
            addNewPerson()
        }

        val gradientDrawable = GradientDrawable(
            GradientDrawable.Orientation.BOTTOM_TOP, intArrayOf(
                ContextCompat.getColor(requireContext(), android.R.color.white),
                ContextCompat.getColor(requireContext(), R.color.green_hornet)
            )
        )
        layoutBackground.background = gradientDrawable

        ivAddFirstVideo.setOnClickListener {
            controlStr = "first"
            addVideo()
        }

        ivAddSecondVideo.setOnClickListener {
            controlStr = "second"
            addVideo()
        }
    }

    private fun addVideo() {
        val videoIntent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        videoIntent.type = "video/*"
        startActivityForResult(videoIntent, 1001)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1001 && resultCode == RESULT_OK && data != null) {
            val videoUri: Uri? = data.data
            videoUri?.let {
                extractFramesFromVideo(it)
            }
        }
    }

    private fun extractFramesFromVideo(videoUri: Uri) {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(context, videoUri)

        val duration =
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong() ?: 0

        val frameCount = 20
        val frameInterval = duration / frameCount

        for (i in 0 until frameCount) {
            val timeUs = i * frameInterval * 1000
            val bitmap = retriever.getFrameAtTime(timeUs, MediaMetadataRetriever.OPTION_CLOSEST)

            bitmap?.let {
                when (controlStr) {
                    "first" -> firstVideoframes.add(it)
                    "second" -> secondVideoframes.add(it)
                    else -> TODO()
                }
            }
        }
        if (firstVideoframes.isNotEmpty()) binding.ivAddFirstVideo.setImageBitmap(firstVideoframes[0])
        if (secondVideoframes.isNotEmpty()) binding.ivAddSecondVideo.setImageBitmap(
            secondVideoframes[0]
        )
        retriever.release()
    }
}
