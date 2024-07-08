/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigoGenerarTablas;

/**
 *
 * @author capitan
 */
public class CodigoSQL {
    
    /*
    PONGO ESTE CODIGO POR SI ACABO EL ARCHIVO SQL QUE ADJUNTO
    PRESENTA ALGUN ERROR A LA HORA DE ABRIRLO
    
    CREATE DATABASE empresa;
    
    -----------------------
    Creacion tabla empleados
    
    CREATE TABLE empleados (
    id_empleado INT PRIMARY KEY,
    nombre_usuario VARCHAR(50),
    contrasena VARCHAR(50),
    nombre_completo VARCHAR(100),
    telefono_contacto VARCHAR(15),
    id_incidencia INT,
);

    ------------------------
-- Creación de la tabla incidencias
    
CREATE TABLE incidencias (
    id_incidencia INT PRIMARY KEY,
    fecha_hora_generacion DATETIME,
    empleado_origen VARCHAR(50),
    empleado_destino VARCHAR(50),
    detalle_incidencia TEXT,
    tipo_incidencia CHAR(1),
    id_empleado_origen INT,
    id_empleado_destino INT,
    FOREIGN KEY (id_empleado_origen) REFERENCES empleados(id_empleado),
    FOREIGN KEY (id_empleado_destino) REFERENCES empleados(id_empleado)
);

    ---------------------------
    añadir las claves foranea
    
ALTER TABLE empleados
ADD FOREIGN KEY (id_incidencia) REFERENCES incidencias(id_incidencia);

    ---------------------------
    añadir las claves foraneas
    
ALTER TABLE incidencias
ADD FOREIGN KEY (id_empleado_origen) REFERENCES empleados(id_empleado),
ADD FOREIGN KEY (id_empleado_destino) REFERENCES empleados(id_empleado);    
    
    */
    
}
