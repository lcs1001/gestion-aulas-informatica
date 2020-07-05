DROP TABLE IF EXISTS public."usuario" CASCADE;

CREATE TABLE public."usuario" (
	id_usuario SERIAL,
	correo character varying(255) NOT NULL,
	contrasena character varying(255) NOT NULL,
	nombre character varying(50) NOT NULL,
	apellidos character varying(50) NOT NULL,
	rol character varying(20) NOT NULL,
	bloqueado boolean,
	telefono character varying(9) NOT NULL,
	CONSTRAINT "PK_Usuario" PRIMARY KEY (id_usuario),	
    CONSTRAINT "UNQ_Usuario_Correo" UNIQUE (correo),
    CONSTRAINT "UNQ_Usuario_NombreApellidos" UNIQUE (nombre, apellidos)
);

DROP TABLE IF EXISTS public."propietario_aula" CASCADE;

CREATE TABLE public."propietario_aula" (
	id_propietario_aula character varying(30),
	nombre_propietario_aula character varying(100) NOT NULL,
	id_responsable integer NOT NULL,
	tipo character varying(15) NOT NULL,
	CONSTRAINT "PK_PopietarioAula" PRIMARY KEY (id_propietario_aula),	
    CONSTRAINT "UNQ_PopietarioAula_NombrePropietarioAula" UNIQUE (nombre_propietario_aula),
    CONSTRAINT "FK_PropietarioAula_Usuario" FOREIGN KEY (id_responsable)
        REFERENCES public."usuario" (id_usuario)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
	CONSTRAINT "CHK_PopietarioAula_Tipo" CHECK(tipo IN ('Centro', 'Departamento'))
);

DROP TABLE IF EXISTS public."aula" CASCADE;

CREATE TABLE public."aula" (
	id_aula SERIAL,
	nombre_aula character varying(20),
	ubicacion_centro character varying(30) NOT NULL,
    propietario_aula character varying(30) NOT NULL,
    capacidad integer NOT NULL DEFAULT 0,
    num_ordenadores integer NOT NULL DEFAULT 0,
    CONSTRAINT "PK_Aula" PRIMARY KEY (id_aula),
    CONSTRAINT "FK_Aula_PopietarioAula_UbicacionCentro" FOREIGN KEY (ubicacion_centro)
        REFERENCES public."propietario_aula" (id_propietario_aula) 
        ON UPDATE CASCADE 
        ON DELETE CASCADE,
    CONSTRAINT "FK_Aula_PopietarioAula_Propietario" FOREIGN KEY (propietario_aula)
        REFERENCES public."propietario_aula" (id_propietario_aula)
        ON UPDATE CASCADE 
        ON DELETE CASCADE,
    CONSTRAINT "UNQ_Aula_NombreAula_UbicacionCentro" UNIQUE (nombre_aula, ubicacion_centro),
    CONSTRAINT "CHK_Aula_Capacidad" CHECK(capacidad >= 0),
    CONSTRAINT "CHK_Aula_NumOrdenadores" CHECK(num_ordenadores >= 0)
);

DROP TABLE IF EXISTS public."reserva" CASCADE;

CREATE TABLE public."reserva" (
	id_reserva SERIAL,
    fecha date NOT NULL,
    hora_inicio time without time zone NOT NULL,
    hora_fin time without time zone NOT NULL,
    id_aula integer NOT NULL,
    dia_semana character varying(10) NOT NULL,
    motivo character varying(50) NOT NULL,
    a_cargo_de character varying(50) NOT NULL,
    responsable character varying(30) NOT NULL,
    CONSTRAINT "PK_Reserva" PRIMARY KEY (id_reserva),
    CONSTRAINT "FK_Reserva_Aula_Aula" FOREIGN KEY (id_aula)
        REFERENCES public."aula" (id_aula)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT "FK_Reserva_PropietarioAula_Responsable" FOREIGN KEY (responsable)
        REFERENCES public."propietario_aula" (id_propietario_aula)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

DROP TABLE IF EXISTS public."historico_reservas" CASCADE;

CREATE TABLE public."historico_reservas" (
	id_operacion SERIAL,
    fecha_operacion timestamp without time zone NOT NULL,
	tipo_operacion character varying(15) NOT NULL,
	motivo_reserva character varying(50) NOT NULL,
    fecha_reserva date NOT NULL,
    hora_inicio_reserva time without time zone NOT NULL,
    hora_fin_reserva time without time zone NOT NULL,
    lugar_reserva character varying(100) NOT NULL,
    a_cargo_de_reserva character varying(50) NOT NULL,
    responsable_operacion character varying(30) NOT NULL,
    CONSTRAINT "PK_HistoricoReservas" PRIMARY KEY (id_operacion),
    CONSTRAINT "UNQ_HistoricoReservas" UNIQUE (id_operacion, fecha_operacion, tipo_operacion, fecha_reserva, hora_inicio_reserva, hora_fin_reserva, lugar_reserva, a_cargo_de_reserva, responsable_operacion)    
);