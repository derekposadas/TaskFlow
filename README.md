# TaskFlow — Gestor de Tareas y Proyectos

Aplicación de escritorio para la gestión de proyectos, tareas y equipos de trabajo, desarrollada en **Java + Swing** con conexión a **Oracle Database** y arquitectura **MVC**.

---

## Tecnologías

![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk)
![Oracle](https://img.shields.io/badge/Oracle_DB-XE-red?logo=oracle)
![Swing](https://img.shields.io/badge/UI-Swing-blue)
![Architecture](https://img.shields.io/badge/Arquitectura-MVC-green)
![Pattern](https://img.shields.io/badge/Patrón-DAO_·_Singleton-purple)

---

## Descripción

TaskFlow es una herramienta de productividad pensada para equipos pequeños y medianos. Permite gestionar el ciclo de vida completo de un proyecto: desde la creación del equipo y los usuarios hasta el seguimiento individual de cada tarea.

La aplicación no requiere ningún IDE para ejecutarse — arranca directamente desde un ejecutable `.exe` en Windows.

---

## Funcionalidades

| Módulo | Operaciones |
|---|---|
| **Usuarios** | Crear, editar, eliminar, listar, buscar por nombre |
| **Proyectos** | Crear, editar, eliminar, asignar equipo, filtrar por estado |
| **Tareas** | Crear, editar, cambiar estado (Pendiente / En Progreso / Completada), asignar a usuario, filtrar y buscar |
| **Equipos** | Crear, editar, eliminar, añadir y quitar miembros |

---

## Arquitectura

El proyecto sigue el patrón **MVC** con una capa **DAO** para aislar completamente el acceso a datos:

```
Vista (Swing)        Controlador              DAO (SQL)
─────────────        ───────────              ─────────
UsuariosPanel   →   UsuarioController   →   UsuarioDAO
ProyectosPanel  →   ProyectoController  →   ProyectoDAO  →  Oracle DB
TareasPanel     →   TareaController     →   TareaDAO
EquiposPanel    →   EquipoController    →   EquipoDAO
```

- **Modelo** (`model/`) — POJOs puros, sin lógica de negocio.
- **Vista** (`view/`) — Paneles Swing. No accede a la base de datos directamente.
- **Controlador** (`controller/`) — Recibe eventos de la vista, valida y delega al DAO.
- **DAO** (`dao/`) — Único responsable de ejecutar sentencias SQL contra Oracle.
- **DatabaseConnection** (`database/`) — Singleton JDBC que gestiona la conexión.

---

## Estructura del proyecto

```
TaskFlow/
├── src/
│   ├── main/           → App.java (punto de entrada)
│   ├── model/          → Usuario, Proyecto, Tarea, Equipo
│   ├── view/           → Paneles y formularios Swing
│   ├── controller/     → Lógica de negocio
│   ├── dao/            → Consultas SQL (CRUD)
│   ├── database/       → Conexión JDBC Singleton
│   └── utils/          → Helpers de estilo y UI
├── sql/
│   └── taskflow_oracle.sql   → Script completo: tablas, secuencias, triggers y datos de ejemplo
├── lib/                      → ojdbc8.jar (ver instalación)
├── TaskFlow.exe              → Lanzador
├── build.xml                 → Compilación con Apache Ant
└── nbproject/                → Configuración NetBeans (opcional)
```

---

## Instalación y ejecución

### Requisitos

| Herramienta | Versión |
|---|---|
| Java JDK | 17 o superior — [adoptium.net](https://adoptium.net) |
| Oracle Database | XE / 19c / 21c — [oracle.com](https://www.oracle.com/database/technologies/xe-downloads.html) |

---

### Paso 1 — Clonar el repositorio

```bash
git clone https://github.com/derekposadas/TaskFlow.git
cd TaskFlow
```

---

### Paso 2 — Configurar la base de datos

Abre SQL Developer o SQL*Plus con un usuario administrador y ejecuta:

```sql
CREATE USER TASKFLOW IDENTIFIED BY 1234;
GRANT CONNECT, RESOURCE, DBA TO TASKFLOW;
```

Luego conéctate como `TASKFLOW` y ejecuta el script completo:

```bash
# SQL*Plus
sqlplus TASKFLOW/1234@localhost:1521/XE
@sql/taskflow_oracle.sql
```

O en SQL Developer: abre `sql/taskflow_oracle.sql` y pulsa **F5**.

> El script crea todas las tablas, secuencias, triggers e inserta datos de ejemplo.

---

### Paso 3 — Ejecutar

Abrir el archivo `TaskFlow.exe`.

---

## Problemas frecuentes

**`ClassNotFoundException: oracle.jdbc.driver.OracleDriver`**
El archivo `ojdbc8.jar` no está en `lib/`.

**`ORA-01017: invalid username/password`**
Usuario o contraseña incorrectos en `DatabaseConnection.java`.

**`ORA-12541: TNS no listener`**
Oracle no está arrancado. En Windows, busca el servicio `OracleServiceXE` en el Administrador de servicios y arráncalo.

**Las tablas aparecen vacías**
El script SQL no se ejecutó correctamente. Repite el Paso 3.

---

## Licencia

Proyecto de uso libre para fines educativos y de portfolio. Puedes adaptarlo y extenderlo libremente.
