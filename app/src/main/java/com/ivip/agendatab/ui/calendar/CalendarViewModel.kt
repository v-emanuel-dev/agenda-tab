package com.ivip.agendatab.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivip.agendatab.domain.model.DailyEntry
import com.ivip.agendatab.domain.usecase.DeleteDailyEntryUseCase
import com.ivip.agendatab.domain.usecase.GetDailyEntriesUseCase
import com.ivip.agendatab.domain.usecase.SaveDailyEntryUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

data class CalendarUiState(
    val currentMonth: YearMonth = YearMonth.now(),
    val dailyEntries: Map<LocalDate, DailyEntry> = emptyMap(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val selectedDate: LocalDate? = null,
    val showEditModal: Boolean = false
)

class CalendarViewModel(
    private val getDailyEntriesUseCase: GetDailyEntriesUseCase,
    private val saveDailyEntryUseCase: SaveDailyEntryUseCase,
    private val deleteDailyEntryUseCase: DeleteDailyEntryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    init {
        loadEntriesForCurrentMonth()
    }

    fun onDaySelected(date: LocalDate) {
        _uiState.update {
            it.copy(
                selectedDate = date,
                showEditModal = true
            )
        }
    }

    fun onEditModalDismissed() {
        _uiState.update {
            it.copy(
                selectedDate = null,
                showEditModal = false
            )
        }
    }

    fun onMonthChanged(newMonth: YearMonth) {
        _uiState.update { it.copy(currentMonth = newMonth) }
        loadEntriesForMonth(newMonth)
    }

    fun onEntryUpdated(entry: DailyEntry) {
        viewModelScope.launch {
            try {
                saveDailyEntryUseCase(entry)
                onEditModalDismissed()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = "Failed to save entry: ${e.message}")
                }
            }
        }
    }

    fun onEntryDeleted(date: LocalDate) {
        viewModelScope.launch {
            try {
                deleteDailyEntryUseCase(date)
                onEditModalDismissed()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = "Failed to delete entry: ${e.message}")
                }
            }
        }
    }

    fun onErrorDismissed() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    private fun loadEntriesForCurrentMonth() {
        loadEntriesForMonth(_uiState.value.currentMonth)
    }

    private fun loadEntriesForMonth(month: YearMonth) {
        val startDate = month.atDay(1)
        val endDate = month.atEndOfMonth()

        _uiState.update { it.copy(isLoading = true) }

        getDailyEntriesUseCase(startDate, endDate)
            .onEach { entries ->
                _uiState.update { currentState ->
                    currentState.copy(
                        dailyEntries = entries.associateBy { it.date },
                        isLoading = false
                    )
                }
            }
            .catch { exception ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Failed to load entries: ${exception.message}"
                    )
                }
            }
            .launchIn(viewModelScope)
    }
}