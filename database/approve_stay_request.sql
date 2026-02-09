CREATE OR REPLACE FUNCTION approve_stay_request(
    p_request_id bigint,
    p_room_id    bigint,
    p_doctor_id  bigint DEFAULT NULL
) RETURNS bigint AS $$
DECLARE
v_stay_id       bigint;
    v_room_occupied boolean;
    v_adm           date;
    v_dis           date;
    v_status        text;
BEGIN
SELECT sr.status, sr.admission_date, sr.discharge_date
INTO v_status, v_adm, v_dis
FROM Stay_request sr
WHERE sr.id = p_request_id
    FOR UPDATE;

IF NOT FOUND THEN
        RAISE EXCEPTION 'Stay_request % не найден', p_request_id;
END IF;

    IF v_status <> 'PENDING' THEN
        RAISE EXCEPTION 'Заявка % не в статусе pending', p_request_id;
END IF;

    IF v_adm >= v_dis THEN
                RAISE EXCEPTION 'Некорректные даты: admission_date % >= discharge_date %', v_adm, v_dis;
END IF;

SELECT r.is_occupied
INTO v_room_occupied
FROM Room r
WHERE r.id = p_room_id
    FOR UPDATE;

IF NOT FOUND THEN
        RAISE EXCEPTION 'Room % не найдена', p_room_id;
END IF;

    IF v_room_occupied THEN
        RAISE EXCEPTION 'Room % уже занята', p_room_id;
END IF;

UPDATE Stay_request
SET status = 'APPROVED'
WHERE id = p_request_id;

INSERT INTO Stay (stay_request_id, room_id, doctor_id)
VALUES (p_request_id, p_room_id, p_doctor_id)
    RETURNING id INTO v_stay_id;

RETURN v_stay_id;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION update_room_occupancy()
RETURNS trigger AS $$
BEGIN
  IF EXISTS (
    SELECT 1 FROM room
    WHERE id = NEW.room_id AND is_occupied = TRUE
  ) THEN
    RAISE EXCEPTION 'Комната % уже занята', NEW.room_id;
END IF;

UPDATE room
SET is_occupied = TRUE
WHERE id = NEW.room_id;

RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_stay_room_occupancy ON stay;
CREATE TRIGGER trg_stay_room_occupancy
    BEFORE INSERT ON stay
    FOR EACH ROW
    EXECUTE FUNCTION update_room_occupancy();

CREATE OR REPLACE FUNCTION discharge_stay(p_stay_id bigint)
RETURNS bigint AS $$
DECLARE
v_room_id bigint;
  v_patient_id bigint;
BEGIN
SELECT s.room_id, sr.patient_id
INTO v_room_id, v_patient_id
FROM stay s
         JOIN stay_request sr ON sr.id = s.stay_request_id
WHERE s.id = p_stay_id
    FOR UPDATE;

IF NOT FOUND THEN
    RAISE EXCEPTION 'Stay % не найден', p_stay_id;
END IF;

UPDATE room
SET is_occupied = false
WHERE id = v_room_id;

UPDATE locker
SET patient_id = NULL
WHERE patient_id = v_patient_id;

RETURN v_patient_id;
END;
$$ LANGUAGE plpgsql;



CREATE OR REPLACE FUNCTION discharge_stay_early(p_stay_id bigint, p_discharge_date date)
RETURNS bigint AS $$
DECLARE
v_stay_request_id bigint;
  v_admission_date date;
  v_current_discharge date;
BEGIN
SELECT s.stay_request_id, sr.admission_date, sr.discharge_date
INTO v_stay_request_id, v_admission_date, v_current_discharge
FROM stay s
         JOIN stay_request sr ON sr.id = s.stay_request_id
WHERE s.id = p_stay_id
    FOR UPDATE;

IF NOT FOUND THEN
    RAISE EXCEPTION 'Stay % not found', p_stay_id;
END IF;

  IF p_discharge_date < v_admission_date THEN
    RAISE EXCEPTION 'Discharge date before admission date';
END IF;

  IF p_discharge_date > v_current_discharge THEN
    RAISE EXCEPTION 'Discharge date after current discharge date';
END IF;

UPDATE stay_request
SET discharge_date = p_discharge_date
WHERE id = v_stay_request_id;

RETURN discharge_stay(p_stay_id);
END;
$$ LANGUAGE plpgsql;