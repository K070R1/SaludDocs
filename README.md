# SaludDocs

Aplicación móvil Android de gestión documental para funcionarios del área de Salud de la Corporación Municipal de Quilpué.

Proyecto académico desarrollado para la asignatura **DSY1105 – Desarrollo de Aplicaciones Móviles**.

## 1. Objetivo del proyecto

Desarrollar un prototipo móvil que permita gestionar documentos laborales mediante autenticación de usuarios, consulta de documentos, registro de información, revisión administrativa y almacenamiento local.

La aplicación busca facilitar la organización y el seguimiento de documentos de funcionarios.

## 2. Tecnologías utilizadas

- **Android Studio:** entorno de desarrollo.
- **Kotlin:** lenguaje de programación.
- **Jetpack Compose:** construcción de interfaces gráficas.
- **MVVM:** arquitectura para separar interfaz, lógica de presentación y acceso a datos.
- **Firebase Authentication:** autenticación de usuarios.
- **Cloud Firestore:** almacenamiento de usuarios, roles y metadatos documentales.
- **Room:** persistencia local mediante SQLite.
- **Coroutines y Flow:** operaciones asíncronas y actualización de datos.
- **Git y GitHub:** control de versiones.

## 3. Funcionalidades implementadas

### Autenticación y usuarios

- Inicio de sesión mediante Firebase Authentication.
- Cierre de sesión.
- Identificación de roles: funcionario y administrador.
- Menú principal con opciones según el rol.

### Gestión documental con Firebase

- Selección de archivos PDF mediante el selector nativo de Android.
- Registro de metadatos de documentos en Cloud Firestore.
- Consulta de documentos registrados.
- Búsqueda de documentos.
- Visualización del estado de revisión.
- Aprobación y rechazo de documentos por parte del administrador.
- Registro de información de revisión.

**Limitación actual:** el prototipo permite seleccionar un PDF, pero almacena sus metadatos, no el contenido del archivo. La carga real de archivos queda pendiente para una versión futura.

### Gestión documental local con Room

Se implementó una pantalla independiente llamada **Documentos locales**, que permite realizar las cuatro operaciones CRUD:

- **Create:** registrar documentos.
- **Read:** consultar documentos almacenados.
- **Update:** modificar documentos existentes.
- **Delete:** eliminar documentos.

Los registros se almacenan en SQLite mediante Room y permanecen disponibles al cerrar y volver a abrir la aplicación.

Los documentos locales no se sincronizan automáticamente con Firestore.

### Validaciones

El formulario de documentos locales incluye:

- Validación de nombre obligatorio.
- Validación de tipo obligatorio.
- Validación de longitud mínima de tres caracteres para el nombre.
- Mensajes de error visibles.
- Mensajes de confirmación al guardar, actualizar o eliminar.
- Confirmación antes de eliminar un registro.

### Interfaz y experiencia de usuario

- Pantallas desarrolladas con Jetpack Compose.
- Navegación entre inicio de sesión, menú principal y gestión documental.
- Diseño visual con colores asociados al área de salud.
- Tarjetas reutilizables para las opciones del menú.
- Animación escalonada de entrada mediante `AnimatedVisibility`, `fadeIn`, `slideInVertically` y `tween`.
- Integración con el selector nativo de documentos de Android.

## 4. Arquitectura de la aplicación

El proyecto utiliza el patrón **MVVM (Model–View–ViewModel)**.

La implementación local con Room sigue este flujo:

**Pantalla Compose → ViewModel → Repository → DAO → Room (SQLite)**

Componentes principales:

- **Model / Entity:** representa los datos de un documento.
- **ViewModel:** administra estados, validaciones y operaciones de la interfaz.
- **Repository:** centraliza el acceso a los datos.
- **DAO:** define las consultas y operaciones CRUD.
- **RoomDatabase:** configura la base de datos local.
- **Screens:** muestran formularios, listados y opciones de navegación.

## 5. Organización del código

```text
com.saluddocs.app
├── data
│   ├── local
│   │   ├── DocumentoLocal.kt
│   │   ├── DocumentoLocalDao.kt
│   │   └── SaludDocsDatabase.kt
│   └── repository
│       └── DocumentoLocalRepository.kt
├── navigation
│   └── AppNavigation.kt
├── repository
│   └── UsuarioRepository.kt
├── ui
│   └── screens
│       ├── LoginScreen.kt
│       ├── HomeScreen.kt
│       ├── DocumentosScreen.kt
│       ├── SubirDocumentoScreen.kt
│       ├── RevisarDocumentosScreen.kt
│       └── DocumentosLocalesScreen.kt
└── viewmodel
    └── DocumentoLocalViewModel.kt
```

Esta estructura muestra los principales archivos relacionados con las funcionalidades descritas.

## 6. Ejecución del proyecto

1. Clonar el repositorio desde GitHub.
2. Abrir el proyecto en Android Studio.
3. Configurar el archivo `google-services.json` correspondiente al proyecto Firebase autorizado.
4. Sincronizar las dependencias con Gradle.
5. Seleccionar un emulador o dispositivo Android compatible.
6. Ejecutar la aplicación