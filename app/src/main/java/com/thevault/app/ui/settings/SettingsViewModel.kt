package com.thevault.app.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thevault.app.data.SettingsRepository
import com.thevault.app.notifications.NotificationScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val notificationScheduler: NotificationScheduler
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    init {
        settingsRepository.notificationTime
            .onEach { (hour, minute) ->
                _state.update { it.copy(notificationHour = hour, notificationMinute = minute) }
            }
            .launchIn(viewModelScope)
    }

    fun updateNotificationTime(hour: Int, minute: Int) {
        viewModelScope.launch {
            settingsRepository.setNotificationTime(hour, minute)
            notificationScheduler.scheduleDailyNotification()
        }
    }
}

data class SettingsState(
    val notificationHour: Int = 9,
    val notificationMinute: Int = 0
)
