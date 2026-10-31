# Monevia Core

Biblioteca Kotlin que contiene la lógica de negocio para la administración de transacciones financieras de Monevia.

Este módulo está separado de la aplicación Android para mantener los cálculos y las reglas de negocio independientes de la interfaz de usuario.

## Objetivo

Proporcionar las operaciones necesarias para administrar transacciones, validar información financiera y calcular los totales de ingresos, gastos y balance.

## Funcionalidades

- Representación de transacciones mediante modelos Kotlin.
- Administración de ingresos y gastos.
- Validación de montos.
- Cálculo del total de ingresos.
- Cálculo del total de gastos.
- Cálculo del balance financiero.
- Separación de la lógica de negocio respecto a la interfaz Android.

## Estructura del módulo

```text
monevia-core/
├── src/
│   ├── main/
│   │   └── kotlin/
│   │       └── com/angel/monevia/
│   │           ├── GestorTransacciones.kt
│   │           └── Transaccion.kt
│   └── test/
│       └── kotlin/
│           └── com/angel/monevia/
│               └── GestorTransaccionesTest.kt
├── pom.xml
└── README.md
```

## Componentes principales

### `Transaccion.kt`

Define el modelo de datos de una transacción financiera.

Representa la información necesaria para identificar y administrar un movimiento, como su tipo, monto y demás datos asociados.

### `GestorTransacciones.kt`

Contiene las operaciones principales para administrar las transacciones.

Entre sus responsabilidades se encuentran:

- Gestionar los movimientos financieros.
- Validar los montos registrados.
- Calcular los ingresos acumulados.
- Calcular los gastos acumulados.
- Obtener el balance financiero.

## Tecnologías utilizadas

| Tecnología | Uso |
|---|---|
| Kotlin 2.2.10 | Lenguaje de programación |
| Java/JVM 11 | Compatibilidad |
| Maven | Gestión y construcción del proyecto |
| JUnit | Pruebas unitarias |

## Construcción del proyecto

Para compilar y ejecutar las pruebas, utilizar:

```bash
mvn clean test
```

Para generar e instalar la biblioteca en el repositorio Maven local:

```bash
mvn install
```

Una vez instalada, la aplicación Android puede utilizarla mediante la siguiente dependencia:

```kotlin
implementation("com.angel.monevia:monevia-core:1.0-SNAPSHOT")
```

## Pruebas unitarias

El módulo contiene pruebas unitarias en:

```text
src/test/kotlin/com/angel/monevia/GestorTransaccionesTest.kt
```

Estas pruebas permiten verificar el comportamiento de la gestión de transacciones y sus operaciones de cálculo.

Para ejecutarlas:

```bash
mvn test
```

## Integración con Android

`monevia-core` se utiliza desde el módulo `app` de Monevia.

La biblioteca proporciona la lógica de negocio, mientras que la aplicación Android se encarga de la interfaz, la navegación y la persistencia local mediante Room.

Esta separación permite mantener responsabilidades diferenciadas entre ambos módulos.

## Estado del módulo

La biblioteca se encuentra integrada con la aplicación Android de Monevia y proporciona la lógica de negocio necesaria para las operaciones financieras implementadas.

---

**Proyecto:** Monevia

**Desarrollado por:** Angel Ramos

**Repositorio:** [F_P_Monevia_X2](https://github.com/Angel26-rl/F_P_Monevia_X2)