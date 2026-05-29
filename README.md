# ⚡ TaskFlow — Gestor de Tareas

> Aplicación de escritorio para gestión de proyectos, tareas y equipos de trabajo.  
> Desarrollada en **Java + Swing**, conectada a **Oracle Database**, con arquitectura **MVC**.

---

## 📋 ¿Qué hace TaskFlow?

TaskFlow es una herramienta de productividad para equipos pequeños y medianos que permite:

- **Organizar proyectos** — crea y gestiona proyectos con estado (Activo, Pausado, Finalizado) y asígnalos a un equipo.
- **Gestionar tareas** — crea tareas dentro de cada proyecto, asígnalas a usuarios, cambia su estado (Pendiente → En Progreso → Completada) y filtra por prioridad.
- **Administrar usuarios** — registra los miembros del equipo con su rol (Admin, Desarrollador, Diseñador, Tester…).
- **Formar equipos** — agrupa usuarios en equipos y asigna esos equipos a los proyectos.

Todo desde una interfaz limpia y sin necesidad de abrir ningún IDE.

---

## 🖥️ Capturas de pantalla

```
┌──────────────┬───────────────────────────────────────────┐
│              │  📁 Proyectos                              │
│  🏠 Dashboard│ ┌────┬──────────────┬────────┬──────────┐ │
│  👤 Usuarios │ │ ID │ Nombre       │ Estado │  Equipo  │ │
│  📁 Proyectos│ ├────┼──────────────┼────────┼──────────┤ │
│  ✅ Tareas   │ │  1 │ Portal Web   │ Activo │ Frontend │ │
│  👥 Equipos  │ │  2 │ API REST     │ Activo │ Backend  │ │
│              │ └────┴──────────────┴────────┴──────────┘ │
│              │  [+ Nuevo] [✏ Editar] [🗑 Eliminar]       │
└──────────────┴───────────────────────────────────────────┘
```

---

## 🚀 Inicio rápido

### Requisitos previos

| Herramienta | Versión mínima | Enlace |
|---|---|---|
| Java JDK | 11 o superior | [adoptium.net](https://adoptium.net) |
| Oracle Database | XE / 11g / 19c / 21c | [oracle.com](https://www.oracle.com/database/technologies/xe-downloads.html) |
| ojdbc8.jar | Cualquier versión reciente | [mvnrepository.com](https://mvnrepository.com/artifact/com.oracle.database.jdbc/ojdbc8) |

---

### Paso 1 — Clonar el repositorio

```bash
git clone https://github.com/TU_USUARIO/TaskFlow.git
cd TaskFlow
```

---

### Paso 2 — Añadir el driver Oracle

El driver JDBC no está incluido por cuestiones de licencia. Descárgalo y cópialo en la carpeta `lib/`:

```
TaskFlow/
└── lib/
    └── ojdbc8.jar   ← colócalo aquí
```

> **¿Dónde descargarlo?**  
> [https://mvnrepository.com/artifact/com.oracle.database.jdbc/ojdbc8](https://mvnrepository.com/artifact/com.oracle.database.jdbc/ojdbc8)  
> → Haz clic en la versión más reciente → descarga el `.jar`

---

### Paso 3 — Preparar la base de datos Oracle

1. Abre **SQL Developer** o **SQL\*Plus** con un usuario administrador.
2. Crea el usuario de la aplicación:

```sql
CREATE USER taskflow IDENTIFIED BY taskflow123;
GRANT CONNECT, RESOURCE, DBA TO taskflow;
```

3. Conéctate como `taskflow` y ejecuta el script completo:

```
Archivo: sql/taskflow_oracle.sql
```

En SQL Developer: abre el archivo → selecciona todo → **F5** (Ejecutar script).  
En SQL\*Plus:
```bash
sqlplus taskflow/taskflow123@localhost:1521/XE
@ruta/al/archivo/sql/taskflow_oracle.sql
```

> El script crea todas las tablas, secuencias, triggers e inserta datos de ejemplo.

---

### Paso 4 — Configurar la conexión JDBC

Edita el archivo `src/database/DatabaseConnection.java` y ajusta estos valores:

```java
private static final String URL      = "jdbc:oracle:thin:@localhost:1521:XE";
private static final String USUARIO  = "taskflow";
private static final String PASSWORD = "taskflow123";
```

| Campo | Descripción |
|---|---|
| `localhost` | IP o nombre del servidor Oracle |
| `1521` | Puerto por defecto de Oracle |
| `XE` | SID de Oracle Express. Usa `ORCL` si tienes Enterprise |

> Si usas Oracle 21c con Service Name: `jdbc:oracle:thin:@//localhost:1521/XEPDB1`

---

### Paso 5 — Ejecutar la aplicación

#### 🪟 Windows

Haz doble clic en `iniciar.bat`  
o desde la terminal:
```cmd
iniciar.bat
```

#### 🐧 Linux / 🍎 macOS

```bash
chmod +x iniciar.sh
./iniciar.sh
```

> Los scripts compilan automáticamente el proyecto la primera vez. En ejecuciones siguientes arrancan directamente.

---

## 📁 Estructura del proyecto

```
TaskFlow/
├── src/
│   ├── model/          → Clases de datos (Usuario, Proyecto, Tarea, Equipo)
│   ├── view/           → Interfaz gráfica Swing (paneles y formularios)
│   ├── controller/     → Lógica de negocio (conecta Vista con DAO)
│   ├── dao/            → Consultas SQL a Oracle (CRUD)
│   ├── database/       → Conexión JDBC Singleton
│   ├── utils/          → Estilos, colores y helpers de UI
│   └── main/           → App.java — punto de entrada
├── sql/
│   └── taskflow_oracle.sql   → Script completo de base de datos
├── lib/                      → Aquí va ojdbc8.jar (no incluido)
├── iniciar.bat               → Lanzador para Windows
├── iniciar.sh                → Lanzador para Linux/macOS
├── build.xml                 → Configuración Apache Ant
└── nbproject/                → Configuración NetBeans (opcional)
```

---

## 🏗️ Arquitectura MVC

```
  VISTA (Swing)          CONTROLADOR           DAO (SQL)
  ─────────────          ───────────           ─────────
  UsuariosPanel    ───▶  UsuarioController ───▶ UsuarioDAO
  ProyectosPanel   ───▶  ProyectoController───▶ ProyectoDAO    ───▶  Oracle DB
  TareasPanel      ───▶  TareaController   ───▶ TareaDAO
  EquiposPanel     ───▶  EquipoController  ───▶ EquipoDAO
```

- **Modelo** (`model/`) — POJOs: solo datos, sin lógica.
- **Vista** (`view/`) — Interfaz gráfica. No accede a la BD directamente.
- **Controlador** (`controller/`) — Recibe eventos de la vista, valida y delega al DAO.
- **DAO** (`dao/`) — Único responsable de ejecutar SQL contra Oracle.

---

## ✅ Funcionalidades

| Módulo | Operaciones disponibles |
|---|---|
| **Usuarios** | Crear, editar, eliminar, listar, buscar |
| **Proyectos** | Crear, editar, eliminar, listar, buscar, asignar equipo |
| **Tareas** | Crear, editar, eliminar, cambiar estado, filtrar por estado, buscar, asignar a usuario |
| **Equipos** | Crear, editar, eliminar, añadir/quitar miembros |

---

## 🛠️ Tecnologías utilizadas

- **Java SE 11** — lenguaje principal
- **Swing** — interfaz gráfica de escritorio
- **Oracle Database XE** — base de datos relacional
- **JDBC** — conexión Java ↔ Oracle
- **Apache Ant** — compilación y empaquetado
- **Patrón MVC** — arquitectura de la aplicación
- **Patrón DAO** — separación de la lógica de acceso a datos
- **Patrón Singleton** — gestión de la conexión a BD

---

## ❓ Problemas frecuentes

**`ClassNotFoundException: oracle.jdbc.driver.OracleDriver`**  
→ Falta el archivo `lib/ojdbc8.jar`. Sigue el Paso 2.

**`ORA-01017: invalid username/password`**  
→ Revisa usuario y contraseña en `DatabaseConnection.java`.

**`ORA-12541: no listener`**  
→ El servicio de Oracle no está arrancado. Inicia el listener:  
```bash
lsnrctl start   # Linux/macOS
```
En Windows: busca "OracleServiceXE" en Servicios y arráncalo.

**La ventana abre pero las tablas están vacías**  
→ El script SQL no se ejecutó correctamente. Repite el Paso 3.

---

## 📄 Licencia

Proyecto educativo de libre uso. Puedes adaptarlo y extenderlo libremente.

---

*Desarrollado con ☕ Java · Swing · Oracle Database · Arquitectura MVC*
