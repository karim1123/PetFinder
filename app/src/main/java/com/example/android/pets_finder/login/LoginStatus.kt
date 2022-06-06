package com.example.android.pets_finder.login

/**
 * Provides the status of a login request.
 */
enum class LoginStatus {
    EmptyEmail,
    EmptyPassword,
    IsNotAuthorized,
    Error,
    Loading,
    Success
}
