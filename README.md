# Monevia

**Aplicación de gestión de finanzas personales para Android.**

Monevia es un proyecto de software desarrollado para facilitar el registro y control de las finanzas personales. Permite administrar ingresos y gastos, consultar el historial de transacciones y visualizar el balance económico a partir de los movimientos registrados.

El proyecto está desarrollado con Kotlin y Android, utilizando una arquitectura que separa la lógica de negocio de la aplicación móvil.

---

## Objetivo del proyecto

Desarrollar una aplicación móvil que permita al usuario llevar un control organizado de sus finanzas personales mediante el registro de ingresos y gastos, el almacenamiento local de información y el cálculo automático del balance.

## Funcionalidades actuales

- Registro de ingresos y gastos.
- Clasificación de transacciones por tipo y categoría.
- Registro de monto, descripción y fecha.
- Consulta del historial de transacciones.
- Cálculo de ingresos y gastos acumulados.
- Actualización automática del balance.
- Identificación visual de balances negativos.
- Eliminación de transacciones.
- Almacenamiento local de transacciones mediante Room.
- Registro y edición de información del perfil sin sistema de inicio de sesión.

## Arquitectura del proyecto

Monevia está organizado en dos componentes principales:

### 1. Aplicación Android — `app`

Contiene la aplicación móvil y su interfaz gráfica.

Tecnologías principales:

- Kotlin
- Android Studio
- Jetpack Compose
- Material 3
- Room
- Kotlin Coroutines y Flow

### 2. Biblioteca Kotlin — `monevia-core`

Contiene la lógica de negocio relacionada con las transacciones financieras.

Responsabilidades principales:

- Definición del modelo de transacción.
- Administración de transacciones.
- Validación de montos.
- Cálculo de ingresos.
- Cálculo de gastos.
- Cálculo del balance.

La biblioteca utiliza Maven para su construcción y administración de dependencias.

---

## Estructura del repositorio

```text
Monevia/
│
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/angel/monevia/
│   │       │   ├── data/
│   │       │   │   ├── local/
│   │       │   │   └── repository/
│   │       │   ├── ui/
│   │       │   │   └── theme/
│   │       │   ├── MainActivity.kt
│   │       │   └── PantallaFinanzas.kt
│   │       └── res/
│   ├── build.gradle.kts
│   └── README.md
│
├── monevia-core/
│   ├── src/
│   │   ├── main/kotlin/
│   │   │   └── com/angel/monevia/core/
│   │   │       ├── GestorTransacciones.kt
│   │   │       └── Transaccion.kt
│   │   └── test/kotlin/
│   │       └── GestorTransaccionesTest.kt
│   ├── pom.xml
│   └── README.md
│
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradlew
├── gradlew.bat
└── README.md
```

## Tecnologías y herramientas

| Tecnología | Uso |
|---|---|
| Kotlin 2.2.10 | Lenguaje de programación |
| Java JDK 11 | Entorno de desarrollo y compatibilidad |
| Android Studio | Desarrollo de la aplicación móvil |
| IntelliJ IDEA | Desarrollo de la biblioteca Kotlin |
| Jetpack Compose | Construcción de interfaces |
| Material 3 | Componentes visuales |
| Room 2.8.4 | Persistencia local de transacciones |
| Maven | Construcción e instalación de la biblioteca |
| Gradle 9.6.0 | Construcción de la aplicación Android |
| Git y GitHub | Control de versiones y alojamiento del código |

## Requisitos de desarrollo

- Android Studio.
- IntelliJ IDEA.
- JDK 11.
- Gradle Wrapper incluido en el repositorio.
- Maven, disponible mediante IntelliJ IDEA o una instalación independiente.
- Dispositivo Android o emulador compatible con Android API 24 o superior.

## Instalación y ejecución

### 1. Clonar el repositorio

```bash
git clone https://github.com/Angel26-rl/F_P_Monevia_X2.git
```

### 2. Preparar la biblioteca Kotlin

Abrir el proyecto `monevia-core` en IntelliJ IDEA.

Ejecutar las siguientes tareas Maven:

```bash
mvn clean test
mvn install
```

La tarea `install` publica la biblioteca en el repositorio Maven local.

### 3. Ejecutar la aplicación Android

Abrir la carpeta principal `Monevia` en Android Studio y esperar a que finalice la sincronización de Gradle.

Compilar la aplicación mediante:

```powershell
.\gradlew.bat :app:assembleDebug
```

También es posible ejecutar la aplicación directamente desde Android Studio mediante un dispositivo físico o emulador compatible.

## Persistencia de datos

Las transacciones se almacenan localmente mediante Room, utilizando SQLite como motor de almacenamiento.

La información del perfil se administra actualmente mediante almacenamiento local de preferencias.

## Control de versiones

El proyecto utiliza un esquema de versionamiento definido para organizar la evolución del software:

| Versión | Tipo de cambio |
|---|---|
| `0.0.1` | Cambios pequeños o menores. |
| `0.1.0` | Cambios intermedios o refactorizaciones importantes. |
| `1.0.0` | Cambios mayores, nuevas funcionalidades y versiones robustas. |

Las versiones se actualizarán conforme evolucione el proyecto.

## Estado del proyecto

Monevia cuenta con una primera implementación funcional de registro de transacciones, almacenamiento local, cálculo de balances e integración entre la aplicación Android y la biblioteca Kotlin.

El proyecto continuará evolucionando mediante mejoras en la interfaz, experiencia de usuario, persistencia de información, pruebas y optimización.

---

**Desarrollado por:** Ángel Ramos

**Repositorio:** [F_P_Monevia_X2](https://github.com/Angel26-rl/F_P_Monevia_X2)