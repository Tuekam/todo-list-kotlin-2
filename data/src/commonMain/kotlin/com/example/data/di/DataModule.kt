package com.example.data.di

import com.example.core.interfaces.*
import com.example.data.api.ApiService
import com.example.data.repositories.*
import org.koin.dsl.module

val dataModule = module {
    single { ApiService.createClient() }
    
    single<GetTasksRepository> { GetTasksRepositoryImpl(get()) }
    single<CreateTaskRepository> { CreateTaskRepositoryImpl(get()) }
    single<UpdateTaskRepository> { UpdateTaskRepositoryImpl(get()) }
    single<DeleteTaskRepository> { DeleteTaskRepositoryImpl(get()) }
}
