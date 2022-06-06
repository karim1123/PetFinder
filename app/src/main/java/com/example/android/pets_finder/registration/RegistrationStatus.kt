package com.example.android.pets_finder.registration

/**
 * Provides the status of a registration request.
 */
enum class RegistrationStatus {
    Initialization,
    EmptyEmail,
    EmptyUserName,
    EmptyPhoneNumber,
    EmptyPassword,
    EmptyConfirmPassword,
    PassAndConfDoNotMatch,
    Error,
    Loading,
    Success
}
