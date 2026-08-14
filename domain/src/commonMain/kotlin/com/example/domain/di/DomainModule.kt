package com.example.domain.di

import com.example.core.interfaces.*
import com.example.domain.usecases.*
import org.koin.dsl.module

val domainModule = module {
    factory<GetTasksUseCase> { GetTasksUseCaseImpl(get()) }
    factory<CreateTaskUseCase> { CreateTaskUseCaseImpl(get()) }
    factory<ToggleTaskUseCase> { ToggleTaskUseCaseImpl(get()) }
    factory<DeleteTaskUseCase> { DeleteTaskUseCaseImpl(get()) }
}
