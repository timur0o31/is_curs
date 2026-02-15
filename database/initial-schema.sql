CREATE TYPE user_role AS ENUM ('admin', 'doctor', 'patient');
CREATE TYPE diet AS ENUM ('diet_1', 'diet_2', 'diet_3', 'diet_4');
CREATE TYPE status AS ENUM ('pending', 'approved', 'rejected');
CREATE TYPE request_type AS ENUM ('check-in', 'expansion');


CREATE TABLE "User" (
    id SERIAL PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role user_role NOT NULL,
    name VARCHAR(100),
    is_enabled BOOLEAN DEFAULT TRUE,
    is_locked BOOLEAN DEFAULT FALSE
);


CREATE TABLE Admin (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES "User"(id) ON DELETE CASCADE
);


CREATE TABLE Doctor (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES "User"(id) ON DELETE CASCADE,
    specialization VARCHAR(120),
    is_working BOOLEAN DEFAULT FALSE NOT NULL
);


CREATE TABLE Patient (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES "User"(id) ON DELETE CASCADE
);


CREATE TABLE Stay_request (
    id SERIAL PRIMARY KEY,
    patient_id INT NOT NULL REFERENCES Patient(id) ON DELETE CASCADE,
    status status NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    type request_type NOT NULL,
    admission_date DATE NOT NULL,
    discharge_date DATE NOT NULL
);


CREATE TABLE Room (
    id SERIAL PRIMARY KEY,
    room_number INT NOT NULL UNIQUE,
    is_occupied BOOLEAN DEFAULT FALSE
);


CREATE TABLE Stay (
    id SERIAL PRIMARY KEY,
    stay_request_id INT NOT NULL UNIQUE REFERENCES Stay_request(id) ON DELETE CASCADE,
    room_id INT NOT NULL REFERENCES Room(id) ON DELETE RESTRICT,
    doctor_id INT REFERENCES Doctor(id) ON DELETE SET NULL
);


CREATE TABLE Locker (
    id SERIAL PRIMARY KEY,
    locker_number INT NOT NULL UNIQUE,
    patient_id INT UNIQUE REFERENCES Patient(id) ON DELETE SET NULL
);


CREATE TABLE Medical_Card (
    id SERIAL PRIMARY KEY,
    patient_id INT NOT NULL UNIQUE REFERENCES Patient(id) ON DELETE CASCADE,
    diet diet,
    phone_number VARCHAR(12),
    height INT CHECK (height > 0),
    weight INT CHECK (weight > 0)
);


CREATE TABLE Dining_Table (
    id SERIAL PRIMARY KEY,
    table_number INT NOT NULL UNIQUE,
    diet diet NOT NULL
);


CREATE TABLE Seat (
    id SERIAL PRIMARY KEY,
    dining_table_id INT NOT NULL REFERENCES Dining_Table(id) ON DELETE CASCADE,
    seat_number INT NOT NULL UNIQUE,
    patient_id INT REFERENCES Patient(id) ON DELETE SET NULL,
    is_occupied BOOLEAN DEFAULT FALSE
);


CREATE TABLE "Procedure" (
    id SERIAL PRIMARY KEY,
    name VARCHAR(15) NOT NULL,
    description VARCHAR(100),
    default_seats INT CHECK (default_seats > 0),
    duration INTERVAL NOT NULL
);


CREATE TABLE "Session" (
    id SERIAL PRIMARY KEY,
    procedure_id INT REFERENCES "Procedure"(id) ON DELETE CASCADE,
    doctor_id INT NOT NULL REFERENCES Doctor(id) ON DELETE CASCADE,
    time_start TIME NOT NULL
);


CREATE TABLE Registration (
    stay_id INT NOT NULL REFERENCES Stay(id) ON DELETE CASCADE,
    session_id INT NOT NULL REFERENCES "Session"(id) ON DELETE CASCADE,
    is_necessary BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (stay_id, session_id)
);


CREATE TABLE Medicament (
    id SERIAL PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    description VARCHAR(100) NOT NULL,
    intake_time TIME[]
);


CREATE TABLE prescription (
    id SERIAL PRIMARY KEY,
    medicament_id INT NOT NULL REFERENCES Medicament(id) ON DELETE CASCADE,
    medical_card_id INT NOT NULL REFERENCES Medical_Card(id) ON DELETE CASCADE,
    dosage INT CHECK (dosage > 0),
    frequency INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE
);




CREATE TABLE Diary_Entry (
    id SERIAL PRIMARY KEY,
    medical_card_id INT NOT NULL REFERENCES Medical_Card(id) ON DELETE CASCADE,
    comment TEXT
);
