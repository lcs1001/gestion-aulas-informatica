DROP TABLE IF EXISTS public."propietario_aula" CASCADE;

CREATE TABLE public."propietario_aula" (
	id_propietario_aula character varying(20),
	nombre_propietario_aula character varying(50) NOT NULL,
	nombre_responsable character varying(20) NOT NULL,
	apellidos_responsable character varying(20) NOT NULL,
	correo_responsable character varying(20) NOT NULL,
	telefono_responsable character varying(20) NOT NULL,
	tipo character varying(15) NOT NULL,
	CONSTRAINT "PK_PopietarioAula" PRIMARY KEY (id_propietario_aula),	
    CONSTRAINT "UNQ_PopietarioAula_NombrePropietarioAula" UNIQUE (nombre_propietario_aula),
	CONSTRAINT "UNQ_PopietarioAula_NombreApellidosResponsable" UNIQUE (nombre_responsable, apellidos_responsable),
	CONSTRAINT "UNQ_PopietarioAula_CorreoResponsable" UNIQUE (correo_responsable),
    CONSTRAINT "UNQ_PopietarioAula_TelefonoResponsable" UNIQUE (telefono_responsable),
	CONSTRAINT "CHK_PopietarioAula_Tipo" CHECK(tipo IN ('Centro', 'Departamento'))
);

DROP TABLE IF EXISTS public."aula" CASCADE;

CREATE TABLE public."aula" (
	nombre_aula character varying(20),
	ubicacion_centro character varying(20) NOT NULL,
    propietario_aula character varying(20) NOT NULL,
    capacidad integer NOT NULL,
    num_ordenadores integer NOT NULL,
    disponible boolean NOT NULL,
    CONSTRAINT "PK_Aula" PRIMARY KEY (nombre_aula, ubicacion_centro),
    CONSTRAINT "FK_Aula_PopietarioAula_UbicacionCentro" FOREIGN KEY (ubicacion_centro)
        REFERENCES public."propietario_aula" (id_propietario_aula) 
        ON UPDATE CASCADE 
        ON DELETE CASCADE,
    CONSTRAINT "FK_Aula_PopietarioAula_Propietario" FOREIGN KEY (propietario_aula)
        REFERENCES public."propietario_aula" (id_propietario_aula)
        ON UPDATE CASCADE 
        ON DELETE CASCADE,
    CONSTRAINT "CHK_Aula_Capacidad" CHECK(capacidad > 0),
    CONSTRAINT "CHK_Aula_NumOrdenadores" CHECK(num_ordenadores > 0)
);

DROP TABLE IF EXISTS public."reserva" CASCADE;

CREATE TABLE public."reserva" (
	id_reserva integer,
    fecha_inicio date NOT NULL,
	fecha_fin date,
    hora_inicio time without time zone NOT NULL,
    hora_fin time without time zone NOT NULL,
    nombre_aula character varying(20) NOT NULL,
    ubicacion_centro character varying(20) NOT NULL,
    dia_semana character varying(10),
    motivo character varying(50) NOT NULL,
    a_cargo_de character varying(50) NOT NULL,
    responsable character varying(20) NOT NULL,
    reserva_rango boolean NOT NULL,
    CONSTRAINT "PK_Reserva" PRIMARY KEY (id_reserva),
    CONSTRAINT "FK_Reserva_Aula_AulaCentro" FOREIGN KEY (nombre_aula, ubicacion_centro)
        REFERENCES public."aula"
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT "FK_Reserva_PropietarioAula_Responsable" FOREIGN KEY (responsable)
        REFERENCES public."propietario_aula" (id_propietario_aula)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT "CHK_Reserva_DiaSemana" CHECK(dia_semana IN ('Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado','Domingo')),
    CONSTRAINT "CHK_Reserva_RangoFechas" CHECK((reserva_rango IS FALSE AND dia_semana IS NULL) OR (reserva_rango IS TRUE AND dia_semana IS NOT NULL))
);

DROP TABLE IF EXISTS public."historico_reservas" CASCADE;

CREATE TABLE public."historico_reservas" (
    id_reserva integer NOT NULL,    
    tipo_operacion character varying(10) NOT NULL,
    fecha_operacion timestamp without time zone NOT NULL,
    responsable_operacion character varying(20) NOT NULL,
    CONSTRAINT "PK_HistoricoReservas" PRIMARY KEY (id_reserva, tipo_operacion),
    CONSTRAINT "FK_HistoricoReservas_Reserva" FOREIGN KEY (id_reserva)
        REFERENCES public."reserva" (id_reserva) 
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT "FK_HistoricoReservas_PropietarioAula_Responsable" FOREIGN KEY (responsable_operacion)
        REFERENCES public."propietario_aula" (id_propietario_aula)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT "UNQ_HistoricoReservas" UNIQUE (id_reserva, tipo_operacion, fecha_operacion, responsable_operacion)    
);

COMMIT;