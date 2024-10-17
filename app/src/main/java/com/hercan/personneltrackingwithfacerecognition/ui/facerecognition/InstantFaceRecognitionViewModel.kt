package com.hercan.personneltrackingwithfacerecognition.ui.facerecognition

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hercan.personneltrackingwithfacerecognition.backend.ext.Status
import com.hercan.personneltrackingwithfacerecognition.backend.repository.RepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class InstantFaceRecognitionViewModel @Inject constructor(
    private val repository: RepositoryImpl
) : ViewModel() {

    private val _response = MutableLiveData<String?>()
    val response: LiveData<String?> = _response

    private val _isOnLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isOnLoading: LiveData<Boolean> = _isOnLoading

    private val _isOnError: MutableLiveData<String> = MutableLiveData()
    val isOnError: LiveData<String> = _isOnError

    fun uploadImage(file: MultipartBody.Part) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.uploadImage(file).onStart {
                _isOnLoading.postValue(true)
            }.onCompletion {
                _isOnLoading.postValue(false)
            }.catch {
                _isOnError.postValue(it.localizedMessage)
            }.collect {
                if (it.status == Status.SUCCESS) {
                    val response = if (it.data?.peopleNames?.isEmpty() == true) "Unknown"
                    else if (it.data?.peopleNames?.get(0).isNullOrEmpty()) "Unknown"
                    else it.data?.peopleNames?.get(0)
                    _response.postValue(response)
                } else {
                    _isOnError.postValue(it.message ?: "Error")
                }
            }
        }
    }
}