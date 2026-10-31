# Monevia — Aplicación Android

Este módulo contiene la aplicación móvil de Monevia, desarrollada en Kotlin con Jetpack Compose. Su propósito es ofrecer una interfaz para administrar las finanzas personales, registrar movimientos y consultar el estado financiero del usuario.

## Funcionalidades

- Pantalla de bienvenida y registro del perfil.
- Edición de la información del perfil.
- Registro de ingresos y gastos.
- Clasificación de transacciones por categoría.
- Consulta del historial de movimientos.
- Filtro mensual para consultar los movimientos de un período.
- Cálculo de ingresos, gastos y balance disponible.
- Indicador visual cuando el balance es negativo.
- Gráfico circular para visualizar los gastos por categoría.
- Eliminación de transacciones.
- Almacenamiento local de los movimientos mediante Room.

## Tecnologías utilizadas

- Kotlin 2.2.10.
- Jetpack Compose.
- Material 3.
- Room 2.8.4.
- Kotlin Coroutines.
- Kotlin Flow.
- Gradle.
- Android SDK.

## Arquitectura del módulo

La aplicación Android se encarga de la interfaz de usuario, la navegación y la persistencia local de la información.

### Interfaz de usuario

La interfaz está desarrollada con Jetpack Compose y Material 3.

Entre sus componentes principales se encuentran:

- `MainActivity.kt`: punto de entrada de la aplicación y gestión del flujo inicial de bienvenida y perfil.
- `PantallaFinanzas.kt`: pantalla principal de finanzas, donde se muestran los indicadores, el historial, el filtro mensual y el gráfico de gastos por categoría.

### Persistencia local

La aplicación utiliza Room sobre SQLite para almacenar las transacciones en el dispositivo.

La base de datos se identifica como:

```text
monevia_database
```

La información del perfil se administra mediante `SharedPreferences`.

### Lógica de negocio

La lógica de administración y cálculo de transacciones se encuentra en el módulo independiente `monevia-core`.

La aplicación utiliza la biblioteca mediante la dependencia:

```kotlin
implementation("com.angel.monevia:monevia-core:1.0-SNAPSHOT")
```

Para que Gradle pueda resolver esta dependencia, primero debe instalarse `monevia-core` en el repositorio Maven local.

## Recursos visuales

El módulo contiene los recursos gráficos de la aplicación, incluidos los recursos del icono personalizado de Monevia.

Los recursos del lanzador Android se encuentran organizados dentro de:

```text
src/main/res/
```

El manifiesto utiliza los recursos correspondientes:

```xml
android:icon="@mipmap/ic_launcher"
android:roundIcon="@mipmap/ic_launcher_round"
```

## Configuración

El proyecto utiliza las siguientes versiones principales:

| Componente | Versión |
|---|---|
| Kotlin | 2.2.10 |
| Android Gradle Plugin | 9.4.1 |
| Gradle | 9.6.0 |
| Compose BOM | 2026.02.01 |
| Room | 2.8.4 |
| KSP | 2.2.10-2.0.2 |
| Java/JVM | 11 |
| Compile SDK | 37 |
| Target SDK | 37 |
| Minimum SDK | 24 |

## Compilación

Desde la terminal de Android Studio o PowerShell, ejecutar:

```powershell
.\gradlew.bat :app:assembleDebug
```

Para compilar el módulo:

```powershell
.\gradlew.bat :app:compileDebugSources
```

El APK de depuración se genera normalmente en:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## Ejecución

1. Abrir el proyecto en Android Studio.
2. Verificar que `monevia-core` esté instalado en el repositorio Maven local.
3. Esperar la sincronización de Gradle.
4. Seleccionar un dispositivo físico o emulador.
5. Ejecutar la aplicación.

## Estado del módulo

El módulo Android se encuentra integrado con `monevia-core` y cuenta con las funcionalidades principales de Monevia. Incluye registro de movimientos, almacenamiento local, consulta mensual, indicadores financieros, historial y gráfico de distribución de gastos.

La aplicación fue compilada y ejecutada en un dispositivo Motorola Edge 50 Fusion.

---

**Desarrollado por:** Angel Ramos