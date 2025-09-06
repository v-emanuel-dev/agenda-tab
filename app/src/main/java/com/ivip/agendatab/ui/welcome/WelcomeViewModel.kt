package com.ivip.agendatab.ui.welcome

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class WelcomeUiState(
    val isFirstLaunch: Boolean = true,
    val currentStep: Int = 0,
    val isLoading: Boolean = false
)

class WelcomeViewModel(
    private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(WelcomeUiState())
    val uiState: StateFlow<WelcomeUiState> = _uiState.asStateFlow()

    private val sharedPrefs = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_FIRST_LAUNCH = "is_first_launch"
        private const val KEY_WELCOME_COMPLETED = "welcome_completed"
    }

    init {
        checkFirstLaunch()
    }

    private fun checkFirstLaunch() {
        val isFirstLaunch = sharedPrefs.getBoolean(KEY_FIRST_LAUNCH, true)
        val welcomeCompleted = sharedPrefs.getBoolean(KEY_WELCOME_COMPLETED, false)

        _uiState.value = _uiState.value.copy(
            isFirstLaunch = isFirstLaunch && !welcomeCompleted
        )
    }

    fun nextStep() {
        val currentStep = _uiState.value.currentStep
        if (currentStep < 3) {
            _uiState.value = _uiState.value.copy(currentStep = currentStep + 1)
        }
    }

    fun previousStep() {
        val currentStep = _uiState.value.currentStep
        if (currentStep > 0) {
            _uiState.value = _uiState.value.copy(currentStep = currentStep - 1)
        }
    }

    fun setStep(step: Int) {
        if (step in 0..3) {
            _uiState.value = _uiState.value.copy(currentStep = step)
        }
    }

    fun completeWelcome() {
        viewModelScope.launch {
            sharedPrefs.edit()
                .putBoolean(KEY_FIRST_LAUNCH, false)
                .putBoolean(KEY_WELCOME_COMPLETED, true)
                .apply()

            _uiState.value = _uiState.value.copy(
                isFirstLaunch = false
            )
        }
    }

    fun shouldShowWelcome(): Boolean {
        return _uiState.value.isFirstLaunch
    }

    fun resetWelcome() {
        sharedPrefs.edit()
            .putBoolean(KEY_FIRST_LAUNCH, true)
            .putBoolean(KEY_WELCOME_COMPLETED, false)
            .apply()

        _uiState.value = WelcomeUiState(isFirstLaunch = true, currentStep = 0)
    }
}

class WelcomeViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WelcomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WelcomeViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}