package app.lusk.client.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.lusk.client.domain.model.Result
import app.lusk.client.domain.model.ServerInfo
import app.lusk.client.domain.model.UserProfile
import app.lusk.client.domain.repository.AuthRepository
import app.lusk.client.domain.security.SecurityManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for authentication screens.
 * Feature: overseerr-android-client
 * Validates: Requirements 1.1, 1.6, 5.4
 */
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val securityManager: SecurityManager
) : ViewModel() {
    
    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
    
    private val _serverValidationState = MutableStateFlow<ServerValidationState>(ServerValidationState.Idle)
    val serverValidationState: StateFlow<ServerValidationState> = _serverValidationState.asStateFlow()
    
    init {
        checkAuthStatus()
    }
    
    /**
     * Check if user is already authenticated.
     */
    private fun checkAuthStatus() {
        viewModelScope.launch {
            authRepository.isAuthenticated().collect { isAuthenticated ->
                if (isAuthenticated) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Unauthenticated
                }
            }
        }
    }
    
    /**
     * Validate server URL.
     * Property 1: URL Validation Correctness
     */
    fun validateServer(url: String) {
        viewModelScope.launch {
            _serverValidationState.value = ServerValidationState.Validating
            
            when (val result = authRepository.validateServerUrl(url)) {
                is Result.Success -> {
                    _serverValidationState.value = ServerValidationState.Valid(result.data)
                }
                is Result.Error -> {
                    _serverValidationState.value = ServerValidationState.Invalid(
                        result.error.message
                    )
                }
                is Result.Loading -> {
                    _serverValidationState.value = ServerValidationState.Validating
                }
            }
        }
    }
    
    /**
     * Initiate Plex authentication.
     * This would typically open a browser or WebView for OAuth.
     */
    fun initiateAuth() {
        _authState.value = AuthState.AuthenticatingWithPlex
    }
    
    /**
     * Handle OAuth callback with Plex token.
     * Property 2: Token Exchange Integrity
     */
    fun handleAuthCallback(plexToken: String) {
        viewModelScope.launch {
            _authState.value = AuthState.ExchangingToken
            
            when (val result = authRepository.authenticateWithPlex(plexToken)) {
                is Result.Success -> {
                    _authState.value = AuthState.Authenticated
                }
                is Result.Error -> {
                    _authState.value = AuthState.Error(result.error.message)
                }
                is Result.Loading -> {
                    _authState.value = AuthState.ExchangingToken
                }
            }
        }
    }
    
    /**
     * Logout user.
     * Property 21: Logout Cleanup
     */
    fun logout() {
        viewModelScope.launch {
            _authState.value = AuthState.LoggingOut
            authRepository.logout()
            _authState.value = AuthState.Unauthenticated
        }
    }
    
    /**
     * Retry authentication after error.
     */
    fun retryAuth() {
        _authState.value = AuthState.Unauthenticated
        _serverValidationState.value = ServerValidationState.Idle
    }
    
    /**
     * Clear server validation state.
     */
    fun clearServerValidation() {
        _serverValidationState.value = ServerValidationState.Idle
    }
}

/**
 * Authentication state.
 */
sealed class AuthState {
    data object Initial : AuthState()
    data object Unauthenticated : AuthState()
    data object AuthenticatingWithPlex : AuthState()
    data object ExchangingToken : AuthState()
    data object Authenticated : AuthState()
    data object LoggingOut : AuthState()
    data class Error(val message: String) : AuthState()
}

/**
 * Server validation state.
 */
sealed class ServerValidationState {
    data object Idle : ServerValidationState()
    data object Validating : ServerValidationState()
    data class Valid(val serverInfo: ServerInfo) : ServerValidationState()
    data class Invalid(val message: String) : ServerValidationState()
}
