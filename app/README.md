# Monevia — Aplicación Android

Este módulo contiene la aplicación móvil de **Monevia**, un sistema de gestión de finanzas personales desarrollado para dispositivos Android mediante Kotlin y Jetpack Compose.

Su propósito es proporcionar una interfaz que permita registrar, consultar y administrar ingresos y gastos, manteniendo actualizada la información financiera del usuario.

## 1. Tecnologías utilizadas

| Tecnología | Versión | Función |
|---|---|---|
| Kotlin | 2.2.10 | Lenguaje de programación |
| Android Gradle Plugin | 9.4.1 | Configuración y construcción Android |
| Gradle | 9.6.0 | Automatización de compilación |
| Jetpack Compose | BOM 2026.02.01 | Desarrollo de interfaces |
| Material 3 | Según catálogo de dependencias | Componentes visuales |
| Room | 2.8.4 | Persistencia local |
| KSP | 2.2.10-2.0.2 | Procesamiento de código |
| Java | JVM 11 | Compatibilidad de compilación |

## 2. Estructura del módulo

```text
app/
│
├── src/
│   └── main/
│       ├── java/com/angel/monevia/
│       │   │
│       │   ├── data/
│       │   │   ├── local/
│       │   │   │   ├── MoneviaDatabase.kt
│       │   │   │   ├── TransaccionDao.kt
│       │   │   │   └── TransaccionEntity.kt
│       │   │   │
│       │   ├── data/repository/
│       │   │   └── TransaccionRepository.kt
│       │   │
│       │   ├── ui/theme/
│       │   │   ├── Color.kt
│       │   │   ├── Theme.kt
│       │   │   └── Type.kt
│       │   │
│       │   ├── MainActivity.kt
│       │   └── PantallaFinanzas.kt
│       │
│       ├── res/
│       └── AndroidManifest.xml
│
├── build.gradle.kts
└── README.md
```

## 3. Componentes principales

### MainActivity.kt

Es el punto de entrada de la aplicación Android.

Se encarga de iniciar la interfaz y administrar el flujo inicial del perfil del usuario.

### PantallaFinanzas.kt

Contiene la interfaz principal de gestión financiera.

Entre sus responsabilidades se encuentran:

- Mostrar los ingresos y gastos.
- Presentar el balance disponible.
- Registrar nuevas transacciones.
- Mostrar el historial de movimientos.
- Permitir eliminar transacciones.
- Facilitar la edición de la información del perfil.
- Actualizar la información visual cuando cambian los datos.

La interfaz está desarrollada mediante Jetpack Compose y componentes de Material 3.

### Capa de datos local

Se encuentra dentro de `data/local/`.

**TransaccionEntity.kt**

Define la entidad que representa una transacción dentro de la base de datos.

**TransaccionDao.kt**

Contiene las operaciones de acceso a los registros, incluyendo inserción, consulta y eliminación.

**MoneviaDatabase.kt**

Define la base de datos local mediante Room.

### TransaccionRepository.kt

Actúa como intermediario entre la interfaz, la base de datos y la biblioteca Kotlin `monevia-core`.

Se encarga de gestionar las operaciones relacionadas con las transacciones y utilizar la lógica de negocio de la biblioteca.

## 4. Persistencia de información

La aplicación utiliza Room como biblioteca de persistencia local, basada en SQLite.

La base de datos se identifica como:

```text
monevia_database
```

Actualmente, las transacciones se almacenan localmente en el dispositivo.

Los datos del perfil se administran mediante SharedPreferences.

## 5. Integración con monevia-core

La aplicación utiliza la biblioteca Kotlin mediante una dependencia Maven:

```kotlin
implementation("com.angel.monevia:monevia-core:1.0-SNAPSHOT")
```

La dependencia se resuelve mediante el repositorio Maven local configurado en el proyecto.

Para actualizar la biblioteca después de realizar modificaciones en su código fuente, es necesario ejecutar Maven desde el módulo `monevia-core`:

```bash
mvn clean test
mvn install
```

Después, Android puede utilizar la versión instalada durante su siguiente compilación.

Para más información, consultar el archivo `../monevia-core/README.md`.

## 6. Configuración de Android

Configuración principal del módulo:

| Propiedad | Valor |
|---|---|
| Namespace | `com.angel.monevia` |
| Application ID | `com.angel.monevia` |
| Compile SDK | 37 |
| Minimum SDK | 24 |
| Target SDK | 37 |
| Version Code | 1 |
| Version Name | 1.0 |

## 7. Compilación y ejecución

### Requisitos

- Android Studio.
- JDK compatible con la configuración del proyecto.
- SDK de Android correspondiente.
- Biblioteca `monevia-core` instalada en Maven Local.
- Dispositivo Android o emulador compatible.

### Compilar la aplicación

Desde la carpeta raíz del repositorio Monevia, ejecutar:

```powershell
.\gradlew.bat :app:assembleDebug
```

El archivo APK de depuración se genera normalmente en:

```text
app/build/outputs/apk/debug/app-debug.apk
```

### Ejecutar en un dispositivo

1. Abrir el proyecto principal en Android Studio.
2. Conectar un dispositivo Android o iniciar un emulador.
3. Seleccionar la configuración de ejecución `app`.
4. Ejecutar mediante el botón Run.

## 8. Estado del módulo

La aplicación cuenta con una implementación funcional de registro de ingresos y gastos, persistencia local de transacciones, consulta de movimientos y cálculo automático del balance.

El desarrollo continuará con mejoras de interfaz, experiencia de usuario, almacenamiento de preferencias, animaciones, pruebas y optimización.

---

**Proyecto:** Monevia

**Módulo:** Aplicación Android

**Desarrollador:** Angel Ramos

[Volver al README principal](../README.md)