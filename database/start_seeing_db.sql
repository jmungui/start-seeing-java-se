-- Crear base de datos
CREATE DATABASE IF NOT EXISTS start_seeing_db;
USE start_seeing_db;

-- Tabla: categoria
CREATE TABLE categoria (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_categoria VARCHAR(100) NOT NULL,
    clasificacion VARCHAR(1) NOT NULL
);

-- Tabla: directores
CREATE TABLE directores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    pais_origen VARCHAR(100) NOT NULL,
    oscar TINYINT(1) NOT NULL DEFAULT 0
);

-- Tabla: pelicula
CREATE TABLE pelicula (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    ano INT NOT NULL,
    estrellas INT NOT NULL,
    categoria_id INT,
    director_id INT,
    FOREIGN KEY (categoria_id) REFERENCES categoria(id)
        ON UPDATE CASCADE
        ON DELETE SET NULL,
    FOREIGN KEY (director_id) REFERENCES directores(id)
        ON UPDATE CASCADE
        ON DELETE SET NULL
);

-- Datos iniciales de ejemplo (opcional)
INSERT INTO categoria (nombre_categoria, clasificacion)
VALUES ('Acción', 'B'), ('Drama', 'A'), ('Comedia', 'A');

INSERT INTO directores (nombre, pais_origen, oscar)
VALUES ('Christopher Nolan', 'Reino Unido', 1),
       ('Quentin Tarantino', 'EE.UU.', 1),
       ('Sofia Coppola', 'EE.UU.', 0);

INSERT INTO pelicula (nombre, ano, estrellas, categoria_id, director_id)
VALUES ('Inception', 2010, 5, 1, 1),
       ('Pulp Fiction', 1994, 5, 2, 2),
       ('Lost in Translation', 2003, 4, 2, 3);

USE start_seeing_db;
-- 2. CREACIÓN DE LA TABLA 'ROLES'
-- Contiene los roles o permisos únicos que puede tener un usuario
DROP TABLE IF EXISTS roles;
CREATE TABLE roles (
    id INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL UNIQUE, -- Ej: 'ADMIN', 'USER', 'EDITOR'
    PRIMARY KEY (id)
) ENGINE=InnoDB;

-- 3. CREACIÓN DE LA TABLA 'USUARIOS'
-- Contiene la información de los usuarios
DROP TABLE IF EXISTS usuarios;
CREATE TABLE usuarios (
    id INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL, -- Recomendado almacenar un hash, no texto plano
    PRIMARY KEY (id)
) ENGINE=InnoDB;

-- 4. CREACIÓN DE LA TABLA DE UNIÓN 'USUARIO_ROL'
-- Tabla de relación Muchos a Muchos (N:M). Es donde se definen las llaves foráneas
-- que conectan un usuario con uno o más roles.
DROP TABLE IF EXISTS usuario_rol;
CREATE TABLE usuario_rol (
    usuario_id INT NOT NULL,
    rol_id INT NOT NULL,

    PRIMARY KEY (usuario_id, rol_id), -- La combinación de ambos ID es la clave primaria

    -- Definición de la Llave Foránea al Usuario
    FOREIGN KEY (usuario_id) REFERENCES usuarios (id) ON DELETE CASCADE,

    -- Definición de la Llave Foránea al Rol
    FOREIGN KEY (rol_id) REFERENCES roles (id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 5. INSERCIÓN DE DATOS INICIALES (ROLES BÁSICOS)
-- Estos roles deben existir antes de que se puedan asignar a cualquier usuario.
INSERT INTO roles (nombre) VALUES ('ADMIN');
INSERT INTO roles (nombre) VALUES ('USER');
INSERT INTO roles (nombre) VALUES ('EDITOR');

-- 6. INSERCIÓN DE UN USUARIO DE PRUEBA (Opcional)
-- Contraseña '1234' (DEBE ser hasheada en un proyecto real)
INSERT INTO usuarios (username, password) VALUES ('admin_test', '1234');
-- Contraseña 'password'
INSERT INTO usuarios (username, password) VALUES ('user_prueba', 'password');

-- 7. ASIGNACIÓN DE ROLES AL USUARIO DE PRUEBA
-- Asignar el rol 'ADMIN' (ID 1) y 'USER' (ID 2) al usuario 'admin_test' (ID 1)
INSERT INTO usuario_rol (usuario_id, rol_id) VALUES (1, 1); -- admin_test es ADMIN
INSERT INTO usuario_rol (usuario_id, rol_id) VALUES (1, 2); -- admin_test también es USER

-- Asignar solo el rol 'USER' (ID 2) al usuario 'user_prueba' (ID 2)
INSERT INTO usuario_rol (usuario_id, rol_id) VALUES (2, 2);

CREATE TABLE bitacora (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario VARCHAR(100),
    accion VARCHAR(255),
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);