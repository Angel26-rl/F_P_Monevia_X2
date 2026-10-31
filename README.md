# Monevia

Aplicación Android de gestión de finanzas personales, desarrollada con Kotlin y Jetpack Compose.

Monevia permite registrar ingresos y gastos, consultar movimientos, revisar la información financiera por mes y visualizar la distribución de los gastos por categoría. Las transacciones se almacenan localmente en el dispositivo.

## Objetivo

Facilitar el control de las finanzas personales mediante el registro organizado de ingresos y gastos, el almacenamiento local y el cálculo automático del balance.

## Funcionalidades

- Registro de ingresos y gastos.
- Clasificación de movimientos por tipo y categoría.
- Registro de monto, descripción y fecha.
- Consulta del historial de movimientos.
- Filtro mensual para consultar movimientos e indicadores del período.
- Cálculo de ingresos, gastos y balance disponible.
- Identificación visual de balances negativos.
- Gráfico de distribución de gastos por categoría.
- Eliminación de movimientos.
- Persistencia local de transacciones mediante Room.
- Registro y edición de la información del perfil, sin inicio de sesión.
- Icono personalizado de Monevia para Android.

## Arquitectura

El repositorio se divide en dos componentes:

### Aplicación Android — `app`

Contiene la aplicación móvil, la interfaz y la persistencia local. Utiliza Kotlin, Jetpack Compose, Material 3, Room, Kotlin Coroutines y Flow.

Responsabilidades:
- Presentar la bienvenida, el registro de perfil y el panel financiero.
- Registrar y consultar movimientos.
- Mostrar indicadores, gráfico por categorías e historial.
- Filtrar la información por mes.
- Guardar las transacciones mediante Room y administrar el perfil mediante preferencias locales.

Consulta [`app/README.md`](app/README.md).

### Biblioteca Kotlin — `monevia-core`

Contiene la lógica de negocio de las transacciones, separada de la interfaz Android. Define el modelo, valida montos y calcula ingresos, gastos y balance. Utiliza Maven.

Consulta [`monevia-core/README.md`](monevia-core/README.md).

## Estructura

```text
Monevia/
├── app/
│   ├── src/main/
│   │   ├── java/com/angel/monevia/
│   │   │   ├── data/local/
│   │   │   ├── data/repository/
│   │   │   ├── ui/theme/
│   │   │   ├── MainActivity.kt
│   │   │   └── PantallaFinanzas.kt
│   │   ├── res/
│   │   └── AndroidManifest.xml
│   ├── build.gradle.kts
│   └── README.md
├── monevia-core/
│   ├── src/main/kotlin/
│   ├── src/test/kotlin/
│   ├── pom.xml
│   └── README.md
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradlew
└── gradlew.bat
```

## Tecnologías

| Tecnología | Uso |
|---|---|
| Kotlin 2.2.10 | Lenguaje |
| Java JDK 11 | Compatibilidad de compilación |
| Android Studio | Desarrollo Android |
| IntelliJ IDEA | Desarrollo de `monevia-core` |
| Jetpack Compose | Interfaces |
| Material 3 | Componentes visuales |
| Room 2.8.4 | Persistencia local |
| Kotlin Coroutines y Flow | Operaciones asíncronas y observación de datos |
| Maven | Construcción de la biblioteca |
| Gradle | Construcción Android |
| Git y GitHub | Control de versiones |

## Requisitos

- Android Studio.
- IntelliJ IDEA para `monevia-core`.
- JDK compatible con JVM 11.
- Android SDK configurado para el proyecto.
- Maven, mediante IntelliJ IDEA o instalación independiente.
- Dispositivo o emulador Android compatible con API 24 o superior.

## Instalación y ejecución

### 1. Clonar

```bash
git clone https://github.com/Angel26-rl/F_P_Monevia_X2.git
```

### 2. Preparar `monevia-core`

Abrir el módulo en IntelliJ IDEA y ejecutar:

```bash
mvn clean test
mvn install
```

Esto instala la biblioteca en el repositorio Maven local para que Android pueda resolver la dependencia.

### 3. Compilar Android

Abrir la carpeta raíz en Android Studio y esperar la sincronización de Gradle. En PowerShell:

```powershell
.\gradlew.bat :app:assembleDebug
```

El APK de depuración se genera normalmente en:

```text
app/build/outputs/apk/debug/app-debug.apk
```

También se puede ejecutar desde Android Studio en un dispositivo físico o emulador.

## Persistencia

- Las transacciones se almacenan localmente mediante Room y SQLite.
- La base de datos se identifica como `monevia_database`.
- La información del perfil se administra mediante `SharedPreferences`.

La aplicación no requiere una cuenta ni un servicio remoto para registrar movimientos.

## Estado del proyecto

Monevia se encuentra en su versión funcional de entrega. La aplicación Android y la biblioteca `monevia-core` están integradas. La aplicación incluye registro y consulta de movimientos, persistencia local, cálculo de balances, consulta mensual, visualización de gastos por categoría e identidad visual propia.

La compilación fue comprobada en Android Studio y la aplicación fue instalada y ejecutada en un dispositivo Motorola Edge 50 Fusion. Las mejoras adicionales se consideran opcionales y no son necesarias para utilizar las funcionalidades actuales.

---

**Desarrollado por:** Angel Ramos

**Repositorio:** [F_P_Monevia_X2](https://github.com/Angel26-rl/F_P_Monevia_X2)