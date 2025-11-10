package com.example.facturacion_inventario.ui.store

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.facturacion_inventario.domain.usecase.CarritoUseCases

/**
 * Factory personalizado para crear RemoteCartViewModel con dependencias
 */
class RemoteCartViewModelFactory(
    private val application: Application,
    private val carritoUseCases: CarritoUseCases = CarritoUseCases()
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RemoteCartViewModel::class.java)) {
            return RemoteCartViewModel(application, carritoUseCases) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}

