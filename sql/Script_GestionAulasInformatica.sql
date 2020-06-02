DROP TABLE IF EXISTS PropietarioAula CASCADE CONSTRAINTS;

CREATE TABLE PropietarioAula {
	idPropietarioAula character varying(20),
	nombrePropietarioAula character varying(50) NOT NULL,
	nombreResponsable character varying(20) NOT NULL,
	apellidosResponsable character varying(20) NOT NULL,
	correoResponsable character varying(20) NOT NULL,
	telefonoResponsable character varying(20) NOT NULL,
	tipo character varying(15) NOT NULL,
	CONSTRAINT PK_PropietarioAula PRIMARY KEY (idPropietarioAula),	
    CONSTRAINT UNQ_PropietarioAula_NombrePropietarioAula UNIQUE (nombrePropietarioAula),
	CONSTRAINT UNQ_PropietarioAula_NombreApellidosResponsable UNIQUE (nombreResponsable, apellidosResponsable),
	CONSTRAINT UNQ_PropietarioAula_CorreoResponsable UNIQUE (correoResponsable),
    CONSTRAINT UNQ_PropietarioAula_TelefonoResponsable UNIQUE (telefonoResponsable),
	CONSTRAINT CHK_PropietarioAula_Tipo CHECK(tipo IN ('Centro', 'Departamento'))
}

DROP TABLE IF EXISTS Aula CASCADE CONSTRAINTS;

CREATE TABLE Aula {
	nombreAula character varying(20),
	ubicacionCentro character varying(20) NOT NULL,
    propietarioAula character varying(20) NOT NULL,
    CONSTRAINT PK_Aula PRIMARY KEY (nombreAula, ubicacionCentro),
    CONSTRAINT FK_Aula_PropietarioAula_UbicacionCentro FOREIGN KEY (ubicacionCentro)
        REFERENCES PropietarioAula (idPropietarioAula) 
        ON UPDATE CASCADE 
        ON DELETE CASCADE,
    CONSTRAINT FK_Aula_PropietarioAula_Propietario FOREIGN KEY (propietarioAula)
        REFERENCES PropietarioAula (idPropietarioAula)
        ON UPDATE CASCADE 
        ON DELETE CASCADE
    
}

DROP TABLE IF EXISTS Reserva CASCADE CONSTRAINTS;

CREATE TABLE Reserva {
	idReserva integer,
    fechaInicio date NOT NULL,
	fechaFin date,
    horaInicio time without time zone NOT NULL,
    horaFin time without time zone NOT NULL,
    nombreAula character varying(20) NOT NULL,
    ubicacionCentro character varying(20) NOT NULL,
    diaSemana character varying(10),
    motivo character varying(50) NOT NULL,
    aCargoDe character varying(50) NOT NULL,
    responsable character varying(20) NOT NULL,
    reservaRango boolean NOT NULL,
    CONSTRAINT PK_Reserva PRIMARY KEY (idReserva),
    CONSTRAINT FK_Reserva_Aula_AulaCentro FOREIGN KEY (nombreAula, ubicacionCentro)
        REFERENCES Aula
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT FK_Reserva_PropietarioAula_Responsable FOREIGN KEY (responsable)
        REFERENCES PropietarioAula (idPropietarioAula) 
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT CHK_Reserva_DiaSemana CHECK(diaSemana IN ('Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado','Domingo')),
    CONSTRAINT CHK_Reserva_RangoFechas CHECK((reservaRango IS FALSE AND diaSemana IS NULL) OR (reservaRango IS TRUE AND diaSemana IS NOT NULL))
}

DROP TABLE IF EXISTS HistoricoReservas CASCADE CONSTRAINTS;

CREATE TABLE HistoricoReservas {
    idReserva integer NOT NULL,    
    tipoOperacion character varying(10) NOT NULL,
    fechaOperacion timestamp without time zone NOT NULL,
    responsableOperacion character varying(20) NOT NULL,
    CONSTRAINT PK_HistoricoReservas PRIMARY KEY (idReserva, tipoOperacion),
    CONSTRAINT FK_HistoricoReservas_Reserva FOREIGN KEY (idReserva)
        REFERENCES Reserva (idReserva) 
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT FK_HistoricoReservas_PropietarioAula_Responsable FOREIGN KEY (responsableOperacion)
        REFERENCES PropietarioAula (idPropietarioAula) 
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT UNQ_HistoricoReservas UNIQUE (idReserva, tipoOperacion, fechaOperacion, responsableOperacion)    
}