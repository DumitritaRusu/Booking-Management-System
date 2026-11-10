-- Minimal PostgreSQL schema for the Java Booking Management application.
-- Create database `proiect_booking` before running this file.
-- This schema intentionally contains no user/customer records.

CREATE TABLE IF NOT EXISTS client (
    id_client SERIAL PRIMARY KEY,
    nume VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL,
    telefon VARCHAR(40) NOT NULL
);

CREATE TABLE IF NOT EXISTS proprietate (
    id_proprietate SERIAL PRIMARY KEY,
    nume VARCHAR(100) NOT NULL,
    adresa VARCHAR(150) NOT NULL,
    tip_proprietate VARCHAR(20) NOT NULL CHECK (tip_proprietate IN ('Apartament', 'Vila', 'Cabana')),
    pret_noapte NUMERIC(10, 2) NOT NULL,
    rating NUMERIC(2, 1),
    nr_camere INTEGER NOT NULL,
    are_bucatarie BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS facilitati (
    id_facilitati SERIAL PRIMARY KEY,
    are_parcare BOOLEAN NOT NULL DEFAULT FALSE,
    pret_parcare NUMERIC(10, 2) NOT NULL DEFAULT 0,
    are_mic_dejun BOOLEAN NOT NULL DEFAULT FALSE,
    pret_mic_dejun NUMERIC(10, 2) NOT NULL DEFAULT 0,
    are_piscina BOOLEAN NOT NULL DEFAULT FALSE,
    pret_piscina NUMERIC(10, 2) NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS proprietate_facilitati (
    id_proprietate INTEGER NOT NULL REFERENCES proprietate(id_proprietate) ON DELETE CASCADE,
    id_facilitati INTEGER NOT NULL REFERENCES facilitati(id_facilitati) ON DELETE CASCADE,
    PRIMARY KEY (id_proprietate, id_facilitati)
);

CREATE TABLE IF NOT EXISTS rezervare (
    id_rezervare SERIAL PRIMARY KEY,
    id_proprietate INTEGER NOT NULL REFERENCES proprietate(id_proprietate),
    id_client INTEGER NOT NULL REFERENCES client(id_client),
    data_check_in DATE NOT NULL,
    data_check_out DATE NOT NULL,
    pret_total NUMERIC(10, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS plata (
    id_plata SERIAL PRIMARY KEY,
    id_rezervare INTEGER NOT NULL REFERENCES rezervare(id_rezervare),
    suma NUMERIC(10, 2) NOT NULL,
    data_plata DATE NOT NULL,
    metoda_plata VARCHAR(50) NOT NULL
);
