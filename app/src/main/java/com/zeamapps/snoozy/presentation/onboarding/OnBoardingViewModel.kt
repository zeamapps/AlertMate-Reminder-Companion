package com.zeamapps.snoozy.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zeamapps.snoozy.domain.AppEntryUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class OnBoardingViewModel @Inject constructor( var appEntryUseCases: AppEntryUseCases) : ViewModel() {
    fun saveEntry(){
        viewModelScope.launch {
            appEntryUseCases.saveAppEntry()
        }
    }
}