# 🏷️ Sistema de Gestión de Productos y Códigos de Barras

## 💻 Trabajo Práctico Integrador — Programación 2

### 🧩 Descripción del Proyecto

Este **Trabajo Práctico Integrador** aplica los fundamentos de **Programación Orientada a Objetos (POO)** y **Persistencia de Datos (JDBC + MySQL)**.  
El sistema gestiona **Productos** y sus respectivos **Códigos de Barras**, implementando una **arquitectura en capas** con todas las operaciones CRUD (Crear, Leer, Actualizar, Eliminar) desde una interfaz de consola.

---

## 🎯 Objetivos Académicos

### 🏗️ Arquitectura en Capas

- Separación en cuatro capas: **Main/UI**, **Service**, **DAO**, **Models**.
- Comunicación desacoplada y validación en cada nivel.

### 🔁 Programación Orientada a Objetos

- Aplicación de principios **SOLID** y **encapsulamiento**.
- Uso de interfaces genéricas (`GenericDAO`, `GenericService`).
- Herencia desde la clase abstracta `Base`.

### 🗄️ Persistencia de Datos

- Conexión mediante **JDBC** con **MySQL**.
- Implementación del patrón **DAO (Data Access Object)**.
- Uso de **PreparedStatements** para evitar inyección SQL.
- Gestión de transacciones con **commit** y **rollback**.
- Consultas con **LEFT JOIN** para la relación Producto ↔ Código.

### ⚙️ Manejo de Excepciones y Recursos

- Uso de `try-with-resources` en todas las operaciones JDBC.
- Cierre automático de conexiones y manejo controlado de errores.

### 🧠 Patrones de Diseño

- **Factory Pattern:** conexión centralizada (`DatabaseConnection`).
- **Service Layer Pattern:** lógica de negocio separada.
- **DAO Pattern:** acceso a datos encapsulado.
- **Soft Delete Pattern:** eliminación lógica sin borrar físicamente.

### ✅ Validaciones

- **Código de barras único.**
- **Campos requeridos:** nombre y precio.
- **Eliminación segura:** evita referencias huérfanas.

---

## 🚀 Funcionalidades Principales

- 🧾 **Gestión de Productos:** crear, listar, actualizar y eliminar productos.
- 🔢 **Gestión de Códigos de Barras:** CRUD independiente o asociado.
- 🔍 **Búsqueda flexible:** por nombre o valor del código.
- 🧩 **Relación 1:1:** cada producto puede tener un solo código.
- 🧱 **Soft Delete:** eliminación lógica en ambas entidades.
- 🔄 **Transacciones seguras:** rollback automático ante fallos.

---

## ⚙️ Requisitos del Sistema

| Componente        | Versión Requerida       |
| ----------------- | ----------------------- |
| ☕ Java JDK       | 17 o superior           |
| 🐬 MySQL          | 8.0 o superior          |
| 🧱 Maven o Gradle | Cualquiera              |
| 💻 SO             | Windows / Linux / macOS |

---

## 🧰 Instalación y Configuración

### 🧩 Script SQL Inicial

```sql
CREATE DATABASE IF NOT EXISTS dbtpi_productos;
USE dbtpi_productos;

CREATE TABLE codigos_barras (
    id INT AUTO_INCREMENT PRIMARY KEY,
    valor VARCHAR(50) NOT NULL UNIQUE,
    eliminado BOOLEAN DEFAULT FALSE
);

CREATE TABLE productos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    precio DECIMAL(10,2),
    codigo_id INT,
    eliminado BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (codigo_id) REFERENCES codigos_barras(id)
);
```

### 🔧 Compilación

```bash
# Linux/macOS
mvn clean compile

# Windows
mvn.cmd clean compile
```

### ⚡ Ejecución

```bash
mvn exec:java -Dexec.mainClass="Main.Main"
```

---

## 🧭 Uso del Sistema

### Menú Principal

```
========= MENU =========
1. Crear producto
2. Listar productos
3. Actualizar producto
4. Eliminar producto
5. Crear código de barras
6. Listar códigos
7. Actualizar código por ID
8. Eliminar código por ID
9. Actualizar código de un producto
10. Eliminar código de un producto
0. Salir
```

### Ejemplo de Alta de Producto

```
Nombre: Yerba Mate
Precio: 1800
¿Desea agregar un código de barras? (s/n): s
Valor del código de barras: 7791234567890
```

---

## 🧱 Arquitectura

```
┌──────────────────────────────────────┐
│   🧑‍💻 Capa Main / UI                │
│   AppMenu, MenuHandler, MenuDisplay  │
│   Interacción con el usuario         │
└───────────┬──────────────────────────┘
            │
┌───────────▼──────────────────────────┐
│   ⚙️ Capa Service                    │
│   ProductoServiceImpl, CodigoService │
│   Reglas de negocio y validación     │
└───────────┬──────────────────────────┘
            │
┌───────────▼──────────────────────────┐
│   💾 Capa DAO                        │
│   ProductoDAO, CodigoBarrasDAO       │
│   Persistencia y consultas JDBC      │
└───────────┬──────────────────────────┘
            │
┌───────────▼──────────────────────────┐
│   📦 Capa Models                     │
│   Producto, CodigoBarras, Base       │
│   Entidades del dominio              │
└──────────────────────────────────────┘
```

---

## 🧩 Modelo de Datos

```
┌─────────────────────┐          ┌────────────────────────┐
│     productos        │          │   codigos_barras       │
├─────────────────────┤          ├────────────────────────┤
│ id (PK)             │          │ id (PK)                │
│ nombre              │          │ valor (UNIQUE)         │
│ precio              │          │ eliminado              │
│ codigo_id (FK) ─────┼──────▶   │                        │
│ eliminado           │          └────────────────────────┘
└─────────────────────┘
```

**Relación:** 1 producto ↔ 1 código de barras (opcional)

---

## 🧮 Patrones y Buenas Prácticas

- ✅ **DAO Pattern:** acceso a datos desacoplado.
- ✅ **Service Layer:** validaciones y coordinación de entidades.
- ✅ **Soft Delete:** `UPDATE ... SET eliminado = TRUE`.
- ✅ **PreparedStatements:** evita inyección SQL.
- ✅ **Validación de unicidad:** código de barras único.
- ✅ **Transacciones seguras:** rollback automático.

---

## 🔒 Reglas de Negocio

1. El valor del código de barras debe ser único.
2. Nombre del producto obligatorio.
3. Precio ≥ 0.
4. Eliminación lógica (soft delete) en ambas entidades.
5. No se puede eliminar un código asociado sin desasociarlo primero.
6. Consultas solo sobre registros activos (`eliminado = FALSE`).

---

## 🧠 Conceptos de Programación 2 Aplicados

| Concepto                 | Implementación                                       |
| ------------------------ | ---------------------------------------------------- |
| **Herencia**             | Clase abstracta `Base`                               |
| **Polimorfismo**         | Interfaces genéricas `GenericDAO`, `GenericService`  |
| **Encapsulamiento**      | Getters/setters en todas las entidades               |
| **Abstracción**          | DAO y Service como contratos                         |
| **JDBC**                 | Persistencia directa con MySQL                       |
| **DAO Pattern**          | `ProductoDAO`, `CodigoBarrasDAO`                     |
| **Service Layer**        | `ProductoServiceImpl` coordina lógica y persistencia |
| **Exception Handling**   | `try-with-resources` y propagación controlada        |
| **Dependency Injection** | En `AppMenu.createProductoService()`                 |

---

## 🧾 Troubleshooting

| Error                                              | Causa                    | Solución                                     |
| -------------------------------------------------- | ------------------------ | -------------------------------------------- |
| `ClassNotFoundException: com.mysql.cj.jdbc.Driver` | Falta el conector JDBC   | Agregar `mysql-connector-j.jar` al classpath |
| `Communications link failure`                      | MySQL no está corriendo  | Iniciar el servicio MySQL                    |
| `Access denied for user 'root'@'localhost'`        | Credenciales incorrectas | Revisar `DatabaseConnection.java`            |
| `Unknown database`                                 | BD no creada             | Ejecutar script SQL inicial                  |
| `Duplicate entry`                                  | Código repetido          | Cambiar valor de `codigos_barras.valor`      |

---

## 🧱 Limitaciones

1. Interfaz solo de consola.
2. Un código por producto (relación 1:1).
3. Sin paginación.
4. Sin pool de conexiones (una por operación).

---

## 📚 Tecnologías

- ☕ **Java 17+**
- 🐬 **MySQL 8.x**
- 🧱 **Maven o Gradle**
- 🔌 **JDBC Driver:** `mysql-connector-j 8.4.0`

---

## 🎓 Contexto Académico

**Materia:** Programación 2  
**Evaluación:** Trabajo Práctico Integrador (TPI)  
**Objetivo:** Aplicar conceptos de POO, JDBC y arquitectura en capas.  
**Año:** 2025  
**Institución:** Universidad de Mendoza

---

**Versión:** 1.0  
**Autor:** [Fran Quarnolo](https://github.com/FranQuarnolo)  
**Java:** 17+  
**MySQL:** 8.x

🧩 _Proyecto educativo — Trabajo Práctico Integrador de Programación 2._
