-- ============================================================
-- TaskFlow - Script SQL para Oracle Database
-- Ejecutar en orden en SQL*Plus o SQL Developer
-- ============================================================

-- ============================================================
-- 1. SECUENCIAS (auto-incremento en Oracle)
-- ============================================================

CREATE SEQUENCE seq_usuarios
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

CREATE SEQUENCE seq_proyectos
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

CREATE SEQUENCE seq_tareas
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

CREATE SEQUENCE seq_equipos
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

-- ============================================================
-- 2. TABLAS
-- ============================================================

-- Tabla: USUARIOS
CREATE TABLE usuarios (
    id          NUMBER PRIMARY KEY,
    nombre      VARCHAR2(100) NOT NULL,
    email       VARCHAR2(150) UNIQUE NOT NULL,
    rol         VARCHAR2(50)  DEFAULT 'Miembro' NOT NULL,
    fecha_alta  DATE          DEFAULT SYSDATE NOT NULL
);

-- Tabla: EQUIPOS
CREATE TABLE equipos (
    id          NUMBER PRIMARY KEY,
    nombre      VARCHAR2(100) NOT NULL,
    descripcion VARCHAR2(300),
    fecha_alta  DATE DEFAULT SYSDATE NOT NULL
);

-- Tabla: PROYECTOS
CREATE TABLE proyectos (
    id           NUMBER PRIMARY KEY,
    nombre       VARCHAR2(150) NOT NULL,
    descripcion  VARCHAR2(500),
    estado       VARCHAR2(50)  DEFAULT 'Activo' NOT NULL,
    fecha_inicio DATE          DEFAULT SYSDATE NOT NULL,
    fecha_fin    DATE,
    id_equipo    NUMBER,
    CONSTRAINT fk_proyecto_equipo FOREIGN KEY (id_equipo) REFERENCES equipos(id)
);

-- Tabla: TAREAS
CREATE TABLE tareas (
    id           NUMBER PRIMARY KEY,
    titulo       VARCHAR2(200) NOT NULL,
    descripcion  VARCHAR2(500),
    estado       VARCHAR2(50)  DEFAULT 'Pendiente' NOT NULL,
    prioridad    VARCHAR2(50)  DEFAULT 'Media' NOT NULL,
    fecha_inicio DATE          DEFAULT SYSDATE NOT NULL,
    fecha_limite DATE,
    id_proyecto  NUMBER        NOT NULL,
    id_usuario   NUMBER,
    CONSTRAINT fk_tarea_proyecto FOREIGN KEY (id_proyecto) REFERENCES proyectos(id),
    CONSTRAINT fk_tarea_usuario  FOREIGN KEY (id_usuario)  REFERENCES usuarios(id),
    CONSTRAINT chk_estado_tarea  CHECK (estado IN ('Pendiente','En Progreso','Completada')),
    CONSTRAINT chk_prioridad     CHECK (prioridad IN ('Baja','Media','Alta'))
);

-- Tabla: EQUIPO_USUARIOS (relacion muchos a muchos)
CREATE TABLE equipo_usuarios (
    id_equipo   NUMBER NOT NULL,
    id_usuario  NUMBER NOT NULL,
    CONSTRAINT pk_equipo_usuarios PRIMARY KEY (id_equipo, id_usuario),
    CONSTRAINT fk_eu_equipo  FOREIGN KEY (id_equipo)  REFERENCES equipos(id),
    CONSTRAINT fk_eu_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
);

-- ============================================================
-- 3. TRIGGERS para auto-incremento con secuencias
-- ============================================================

CREATE OR REPLACE TRIGGER trg_usuarios_id
BEFORE INSERT ON usuarios
FOR EACH ROW
BEGIN
    IF :NEW.id IS NULL THEN
        :NEW.id := seq_usuarios.NEXTVAL;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER trg_equipos_id
BEFORE INSERT ON equipos
FOR EACH ROW
BEGIN
    IF :NEW.id IS NULL THEN
        :NEW.id := seq_equipos.NEXTVAL;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER trg_proyectos_id
BEFORE INSERT ON proyectos
FOR EACH ROW
BEGIN
    IF :NEW.id IS NULL THEN
        :NEW.id := seq_proyectos.NEXTVAL;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER trg_tareas_id
BEFORE INSERT ON tareas
FOR EACH ROW
BEGIN
    IF :NEW.id IS NULL THEN
        :NEW.id := seq_tareas.NEXTVAL;
    END IF;
END;
/

-- ============================================================
-- 4. DATOS DE EJEMPLO
-- ============================================================

-- Usuarios
INSERT INTO usuarios (nombre, email, rol) VALUES ('Ana García',    'ana.garcia@taskflow.com',    'Admin');
INSERT INTO usuarios (nombre, email, rol) VALUES ('Carlos López',  'carlos.lopez@taskflow.com',  'Desarrollador');
INSERT INTO usuarios (nombre, email, rol) VALUES ('María Martínez','maria.martinez@taskflow.com','Diseñador');
INSERT INTO usuarios (nombre, email, rol) VALUES ('Pedro Sánchez', 'pedro.sanchez@taskflow.com', 'Tester');
INSERT INTO usuarios (nombre, email, rol) VALUES ('Laura Pérez',   'laura.perez@taskflow.com',   'Miembro');

-- Equipos
INSERT INTO equipos (nombre, descripcion) VALUES ('Equipo Frontend', 'Responsables de la interfaz de usuario');
INSERT INTO equipos (nombre, descripcion) VALUES ('Equipo Backend',  'Responsables de la lógica de negocio');
INSERT INTO equipos (nombre, descripcion) VALUES ('Equipo QA',       'Control de calidad y pruebas');

-- Asignar usuarios a equipos
INSERT INTO equipo_usuarios (id_equipo, id_usuario) VALUES (1, 3); -- María -> Frontend
INSERT INTO equipo_usuarios (id_equipo, id_usuario) VALUES (1, 1); -- Ana -> Frontend
INSERT INTO equipo_usuarios (id_equipo, id_usuario) VALUES (2, 2); -- Carlos -> Backend
INSERT INTO equipo_usuarios (id_equipo, id_usuario) VALUES (2, 5); -- Laura -> Backend
INSERT INTO equipo_usuarios (id_equipo, id_usuario) VALUES (3, 4); -- Pedro -> QA

-- Proyectos
INSERT INTO proyectos (nombre, descripcion, estado, id_equipo)
    VALUES ('Portal Web Corporativo', 'Rediseño completo del portal web', 'Activo', 1);
INSERT INTO proyectos (nombre, descripcion, estado, id_equipo)
    VALUES ('API REST de Ventas',     'Servicio backend para módulo de ventas', 'Activo', 2);
INSERT INTO proyectos (nombre, descripcion, estado, id_equipo)
    VALUES ('App Móvil v2.0',         'Nueva versión de la aplicación móvil', 'Pausado', 1);

-- Tareas
INSERT INTO tareas (titulo, descripcion, estado, prioridad, id_proyecto, id_usuario)
    VALUES ('Diseñar pantalla de login',     'Crear mockup y CSS',      'Completada', 'Alta',  1, 3);
INSERT INTO tareas (titulo, descripcion, estado, prioridad, id_proyecto, id_usuario)
    VALUES ('Implementar menú de navegación','Navbar responsive',       'En Progreso','Media', 1, 3);
INSERT INTO tareas (titulo, descripcion, estado, prioridad, id_proyecto, id_usuario)
    VALUES ('Endpoint POST /ventas',         'Crear endpoint REST',     'Pendiente',  'Alta',  2, 2);
INSERT INTO tareas (titulo, descripcion, estado, prioridad, id_proyecto, id_usuario)
    VALUES ('Conectar BD ventas',            'Integrar Oracle con JPA', 'Pendiente',  'Alta',  2, 2);
INSERT INTO tareas (titulo, descripcion, estado, prioridad, id_proyecto, id_usuario)
    VALUES ('Testing módulo login',          'Pruebas funcionales',     'Pendiente',  'Media', 1, 4);
INSERT INTO tareas (titulo, descripcion, estado, prioridad, id_proyecto, id_usuario)
    VALUES ('Wireframes app móvil',          'Diseño inicial pantallas','En Progreso','Baja',  3, 3);

COMMIT;

-- ============================================================
-- 5. VERIFICACIÓN (consultas de comprobación)
-- ============================================================
-- SELECT * FROM usuarios;
-- SELECT * FROM equipos;
-- SELECT * FROM proyectos;
-- SELECT * FROM tareas;
-- SELECT * FROM equipo_usuarios;
