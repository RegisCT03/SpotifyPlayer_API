package com.example.application.usecase

class NotFoundException(message: String) : Exception(message)
class ConflictException(message: String) : Exception(message)
class ValidationException(message: String) : Exception(message)