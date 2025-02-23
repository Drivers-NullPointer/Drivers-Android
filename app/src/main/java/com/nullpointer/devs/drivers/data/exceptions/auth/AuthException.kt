package com.nullpointer.devs.drivers.data.exceptions.auth

sealed class AuthException(message: String) : Exception(message){

    sealed class LoginException(message: String) : AuthException(message){
        class UserNotFoundException(message: String) : LoginException(message)
        class InvalidCredentialsException(message: String) : LoginException(message)
        class ServerException(message: String) : LoginException(message)
        class TooManyRequestsException(message: String) : LoginException(message)
    }

    sealed class RegisterException(message: String) : AuthException(message){
        class UserAlreadyExistsException(message: String) : RegisterException(message)
        class ServerException(message: String) : RegisterException(message)
        class TooManyRequestsException(message: String) : RegisterException(message)
    }

    sealed class ForgotException(message: String) : AuthException(message){
        class EmailUserNotVerifiedException(message: String) : ForgotException(message)
        class UserNotFoundException(message: String) : ForgotException(message)
        class ServerException(message: String) : ForgotException(message)
        class TooManyRequestsException(message: String) : ForgotException(message)
    }

    class UnknownException(message: String) : AuthException(message)
}