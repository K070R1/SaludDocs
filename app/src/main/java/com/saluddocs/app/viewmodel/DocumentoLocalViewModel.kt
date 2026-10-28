package com.saluddocs.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.saluddocs.app.data.local.DocumentoLocal
import com.saluddocs.app.data.local.SaludDocsDatabase
import com.saluddocs.app.data.repository.DocumentoLocalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DocumentoLocalViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = DocumentoLocalRepository(
        SaludDocsDatabase
            .obtenerBaseDeDatos(application)
            .documentoLocalDao()
    )

    private val _documentos =
        MutableStateFlow<List<DocumentoLocal>>(emptyList())

    val documentos: StateFlow<List<DocumentoLocal>> =
        _documentos.asStateFlow()

    private val _nombre = MutableStateFlow("")
    val nombre: StateFlow<String> = _nombre.asStateFlow()

    private val _tipo = MutableStateFlow("")
    val tipo: StateFlow<String> = _tipo.asStateFlow()

    private val _descripcion = MutableStateFlow("")
    val descripcion: StateFlow<String> = _descripcion.asStateFlow()

    private val _mensaje = MutableStateFlow("")
    val mensaje: StateFlow<String> = _mensaje.asStateFlow()

    private val _documentoEnEdicion =
        MutableStateFlow<DocumentoLocal?>(null)

    val documentoEnEdicion: StateFlow<DocumentoLocal?> =
        _documentoEnEdicion.asStateFlow()

    init {
        observarDocumentos()
    }

    private fun observarDocumentos() {
        viewModelScope.launch {
            repository.obtenerTodos().collect { lista ->
                _documentos.value = lista
            }
        }
    }

    fun actualizarNombre(valor: String) {
        _nombre.value = valor
        _mensaje.value = ""
    }

    fun actualizarTipo(valor: String) {
        _tipo.value = valor
        _mensaje.value = ""
    }

    fun actualizarDescripcion(valor: String) {
        _descripcion.value = valor
        _mensaje.value = ""
    }

    fun guardarDocumento() {
        val nombreLimpio = _nombre.value.trim()
        val tipoLimpio = _tipo.value.trim()
        val descripcionLimpia = _descripcion.value.trim()

        // Validación de campo obligatorio
        if (nombreLimpio.isEmpty()) {
            _mensaje.value = "El nombre del documento es obligatorio."
            return
        }

        if (tipoLimpio.isEmpty()) {
            _mensaje.value = "El tipo de documento es obligatorio."
            return
        }

        // Segunda validación: longitud mínima
        if (nombreLimpio.length < 3) {
            _mensaje.value =
                "El nombre debe tener al menos 3 caracteres."
            return
        }

        viewModelScope.launch {
            try {
                val documentoActual = _documentoEnEdicion.value

                if (documentoActual == null) {
                    repository.insertar(
                        DocumentoLocal(
                            nombre = nombreLimpio,
                            tipo = tipoLimpio,
                            descripcion = descripcionLimpia
                        )
                    )

                    limpiarFormulario()
                    _mensaje.value = "Documento guardado correctamente."
                } else {
                    repository.actualizar(
                        documentoActual.copy(
                            nombre = nombreLimpio,
                            tipo = tipoLimpio,
                            descripcion = descripcionLimpia
                        )
                    )

                    limpiarFormulario()
                    _mensaje.value = "Documento actualizado correctamente."
                }
            } catch (e: Exception) {
                _mensaje.value =
                    "No se pudo guardar el documento. Inténtalo nuevamente."
            }
        }
    }

    fun editarDocumento(documento: DocumentoLocal) {
        _documentoEnEdicion.value = documento
        _nombre.value = documento.nombre
        _tipo.value = documento.tipo
        _descripcion.value = documento.descripcion
        _mensaje.value = ""
    }

    fun eliminarDocumento(documento: DocumentoLocal) {
        viewModelScope.launch {
            try {
                repository.eliminar(documento)

                if (_documentoEnEdicion.value?.id == documento.id) {
                    limpiarFormulario()
                }

                _mensaje.value = "Documento eliminado correctamente."
            } catch (e: Exception) {
                _mensaje.value =
                    "No se pudo eliminar el documento."
            }
        }
    }

    fun cancelarEdicion() {
        limpiarFormulario()
        _mensaje.value = ""
    }

    private fun limpiarFormulario() {
        _nombre.value = ""
        _tipo.value = ""
        _descripcion.value = ""
        _documentoEnEdicion.value = null
    }
}
