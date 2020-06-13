DROP TABLE IF EXISTS public."propietario_aula" CASCADE;

CREATE TABLE public."propietario_aula" (
	id_propietario_aula character varying(30),
	nombre_propietario_aula character varying(100) NOT NULL,
	nombre_responsable character varying(20) NOT NULL,
	apellidos_responsable character varying(50) NOT NULL,
	correo_responsable character varying(50) NOT NULL,
	telefono_responsable character varying(20) NOT NULL,
	tipo character varying(15) NOT NULL,
	CONSTRAINT "PK_PopietarioAula" PRIMARY KEY (id_propietario_aula),	
    CONSTRAINT "UNQ_PopietarioAula_NombrePropietarioAula" UNIQUE (nombre_propietario_aula),
	CONSTRAINT "CHK_PopietarioAula_Tipo" CHECK(tipo IN ('Centro', 'Departamento'))
);

DROP TABLE IF EXISTS public."aula" CASCADE;

CREATE TABLE public."aula" (
	id_aula integer,
	nombre_aula character varying(20),
	ubicacion_centro character varying(20) NOT NULL,
    propietario_aula character varying(20) NOT NULL,
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
	id_reserva integer,
    fecha_inicio date NOT NULL,
	fecha_fin date,
    hora_inicio time without time zone NOT NULL,
    hora_fin time without time zone NOT NULL,
    id_aula integer NOT NULL,
    dia_semana character varying(10),
    motivo character varying(50) NOT NULL,
    a_cargo_de character varying(50) NOT NULL,
    responsable character varying(20) NOT NULL,
    reserva_rango boolean NOT NULL DEFAULT false,
    CONSTRAINT "PK_Reserva" PRIMARY KEY (id_reserva),
    CONSTRAINT "FK_Reserva_Aula_Aula" FOREIGN KEY (id_aula)
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

-- Inserciones en las tablas

INSERT INTO public."propietario_aula" VALUES('Ciencias', 'Facultad de Ciencias', 'Responsable', 'Ciencias', 'responsableCiencias@gmail.com', '123456789', 'Centro');
INSERT INTO public."propietario_aula" VALUES('Salud', 'Facultad de Ciencias de la Salud', 'Responsable', 'Salud', 'responsableSalud@gmail.com', '154236785', 'Centro');
INSERT INTO public."propietario_aula" VALUES('Economicas-Empresariales', 'Facultad de Ciencias Economicas y Empresariales', 'Responsable', 'Economicas Empresariales', 'responsableEconomicasEmpresariales@gmail.com', '478663425', 'Centro');
INSERT INTO public."propietario_aula" VALUES('Derecho', 'Facultad de Derecho', 'Responsable', 'Derecho', 'responsableDerecho@gmail.com', '785496584', 'Centro');
INSERT INTO public."propietario_aula" VALUES('Educacion', 'Facultad de Educacion', 'Responsable', 'Educacion', 'responsableEducacion@gmail.com', '478512648', 'Centro');
INSERT INTO public."propietario_aula" VALUES('Humanidades-Comunicacion', 'Facultad de Humanidades y Comunicacion', 'Responsable', 'Humanidades Comunicacion', 'responsableHumanidadesComunicacion@gmail.com', '458726485', 'Centro');
INSERT INTO public."propietario_aula" VALUES('EPS', 'Escuela Politecnica Superior', 'Responsable', 'EPS', 'responsableEPS@gmail.com', '458756324', 'Centro');

INSERT INTO public."propietario_aula" VALUES('DPTO-Educacion', 'Departamento de Ciencias de la Educacion', 'Responsable', 'Dpto Educacion', 'responsableDptoEducacion@gmail.com', '548759547', 'Departamento');
INSERT INTO public."propietario_aula" VALUES('DPTO-Informatica', 'Departamento de Ingenieria Informatica', 'Responsable', 'Dpto Informatica', 'responsableDptoInformatica@gmail.com', '745621456', 'Departamento');

INSERT INTO public."aula" VALUES (1, '36-A1', 'EPS', 'DPTO-Informatica', '50');
INSERT INTO public."aula" VALUES (2, 'Sala de Juntas', 'EPS', 'EPS', '100');

-- Simulación reserva
INSERT INTO public."reserva" VALUES ('0', '2020-06-22', NULL , '12:30', '13:30', '2' , NULL,'Reunion cuatrimestral', 'Sara Peña', 'EPS');

COMMIT;