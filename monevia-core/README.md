# Monevia Core

**Biblioteca Kotlin para la lógica de negocio de Monevia.**

`monevia-core` es el componente encargado de administrar la lógica relacionada con las transacciones financieras de Monevia. Su objetivo es mantener separadas las operaciones de negocio de la interfaz gráfica de la aplicación Android.

La biblioteca está desarrollada en Kotlin y utiliza Maven para gestionar su construcción, dependencias y pruebas.

## 1. Tecnologías utilizadas

| Tecnología | Versión | Función |
|---|---|---|
| Kotlin | 2.2.10 | Lenguaje de programación |
| Java JDK | 11 | Entorno de ejecución y compilación |
| Maven | Configurado mediante IntelliJ IDEA | Construcción y gestión del proyecto |
| JUnit Jupiter | 5.10.0 | Pruebas unitarias |
| Kotlin Test | 2.2.10 | Herramientas de pruebas |

## 2. Estructura del módulo

```text
monevia-core/
│
├── src/
│   ├── main/
│   │   └── kotlin/
│   │       ├── Main.kt
│   │       └── com/angel/monevia/core/
│   │           ├── GestorTransacciones.kt
│   │           └── Transaccion.kt
│   │
│   └── test/
│       └── kotlin/
│           └── GestorTransaccionesTest.kt
│
├── target/
├── pom.xml
└── README.md
```

## 3. Componentes principales

### Transaccion.kt

Define los elementos fundamentales de una transacción financiera.

Contiene:

**TipoTransaccion**

Enumeración que identifica las dos clases de movimientos:

- `INGRESO`
- `GASTO`

**Transaccion**

Clase de datos que representa un movimiento financiero mediante los siguientes atributos:

| Atributo | Tipo | Descripción |
|---|---|---|
| `id` | Long | Identificador de la transacción |
| `tipo` | TipoTransaccion | Tipo de movimiento |
| `monto` | BigDecimal | Cantidad monetaria |
| `categoria` | String | Categoría del movimiento |
| `descripcion` | String | Descripción del movimiento |
| `fecha` | LocalDate | Fecha de la transacción |

### GestorTransacciones.kt

Es la clase responsable de administrar las operaciones financieras.

Sus métodos principales son:

| Método | Responsabilidad |
|---|---|
| `registrar()` | Registra una transacción después de validar su monto |
| `obtenerTransacciones()` | Devuelve la lista de transacciones |
| `calcularIngresos()` | Calcula la suma de los ingresos |
| `calcularGastos()` | Calcula la suma de los gastos |
| `calcularBalance()` | Obtiene la diferencia entre ingresos y gastos |

### Validación de transacciones

El método `registrar()` comprueba que el monto sea mayor que cero.

Si el monto no cumple esta condición, genera una excepción mediante `require`.

Para las operaciones monetarias se utiliza `BigDecimal`, evitando depender de cálculos con números de punto flotante.

## 4. Pruebas unitarias

El módulo incluye pruebas para verificar el comportamiento de la lógica de negocio.

Archivo principal:

```text
src/test/kotlin/GestorTransaccionesTest.kt
```

Las pruebas se ejecutan mediante Maven y utilizan JUnit 5 y Kotlin Test.

### Ejecutar las pruebas

Desde IntelliJ IDEA:

1. Abrir el proyecto `monevia-core`.
2. Abrir el panel Maven.
3. Desplegar Lifecycle.
4. Ejecutar `clean`.
5. Ejecutar `test`.

También se pueden ejecutar mediante el comando:

```bash
mvn clean test
```

## 5. Construcción e instalación

Para generar e instalar la biblioteca en el repositorio Maven local:

```bash
mvn clean install
```

Este procedimiento:

1. Limpia los archivos de compilaciones anteriores.
2. Compila el código Kotlin.
3. Ejecuta las pruebas unitarias.
4. Genera el archivo JAR.
5. Instala la biblioteca en el repositorio Maven local.

## 6. Configuración Maven

El proyecto utiliza el archivo `pom.xml` para definir sus propiedades, dependencias y complementos.

Identificación del artefacto:

| Propiedad | Valor |
|---|---|
| Group ID | `com.angel.monevia` |
| Artifact ID | `monevia-core` |
| Version | `1.0-SNAPSHOT` |
| JVM Target | 11 |
| Kotlin | 2.2.10 |

## 7. Integración con Android

La aplicación Android utiliza esta biblioteca mediante la siguiente dependencia:

```kotlin
implementation("com.angel.monevia:monevia-core:1.0-SNAPSHOT")
```

Para que Android pueda utilizar los cambios realizados en la biblioteca, se debe ejecutar primero:

```bash
mvn clean install
```

La dependencia se obtiene desde el repositorio Maven local configurado en el proyecto principal.

## 8. Estado del módulo

La biblioteca cuenta con un modelo de transacciones, un gestor para registrar movimientos financieros, validación de montos y métodos para calcular ingresos, gastos y balance.

Su desarrollo continuará mediante mejoras en la lógica de negocio, ampliación de las pruebas unitarias y optimización de la integración con la aplicación Android.

---

**Proyecto:** Monevia

**Módulo:** Biblioteca Kotlin

**Desarrollador:** Angel Ramos

[Volver al README principal](../README.md)