-- ============================================================
-- 1. LIMPIEZA (Opcional: Descomentar si quieres borrar todo antes)
-- ============================================================
-- TRUNCATE TABLE bet, weekly_bet_ticket, user_match_day_score, 
-- user_season_score, coalition_match_day_score, coalition_season_score,
-- match, match_day, division, team, season, coalition, address, usuario CASCADE;

-- ============================================================
-- 2. USUARIOS Y DIRECCIONES (10 Usuarios)
-- ============================================================
INSERT INTO usuario (id, name, surname, nickname, age, phone_number, email, password, is_active) VALUES
(1, 'Juan', 'Perez', 'juancho', 25, '555-0101', 'juan@test.com', 'pass123', true),
(2, 'Maria', 'Gomez', 'mary', 28, '555-0102', 'maria@test.com', 'pass123', true),
(3, 'Carlos', 'Ruiz', 'charly', 30, '555-0103', 'carlos@test.com', 'pass123', true),
(4, 'Laura', 'Diaz', 'lau', 22, '555-0104', 'laura@test.com', 'pass123', true),
(5, 'Pedro', 'Mora', 'peter', 35, '555-0105', 'pedro@test.com', 'pass123', true),
(6, 'Ana', 'Sola', 'anita', 27, '555-0106', 'ana@test.com', 'pass123', true),
(7, 'Luis', 'Vega', 'lucho', 32, '555-0107', 'luis@test.com', 'pass123', true),
(8, 'Sofia', 'Luna', 'sofi', 24, '555-0108', 'sofia@test.com', 'pass123', true),
(9, 'Diego', 'Mar', 'diego10', 29, '555-0109', 'diego@test.com', 'pass123', true),
(10, 'Elena', 'Rios', 'ele', 31, '555-0110', 'elena@test.com', 'pass123', true);

-- Direcciones (Para usuarios y estadios)
INSERT INTO address (id, street, city, postal_code, description, user_id) VALUES
(1, 'Calle Falsa 123', 'Madrid', '28001', 'Casa Juan', 1),
(2, 'Av. Siempre Viva', 'Madrid', '28002', 'Casa Maria', 2),
(3, 'Plaza Mayor 1', 'Barcelona', '08001', 'Estadio Central', null), -- Para partidos
(4, 'Calle Rugby 10', 'Valencia', '46001', 'Campo Norte', null),   -- Para partidos
(5, 'Ruta 66', 'Sevilla', '41001', 'Campo Sur', null);

-- ============================================================
-- 3. EQUIPOS (10 Equipos)
-- ============================================================
INSERT INTO team (id, name, url) VALUES
(1, 'Leonas RC', 'http://img.com/leonas'),
(2, 'Gazellas XV', 'http://img.com/gazellas'),
(3, 'Tigres Rugby', 'http://img.com/tigres'),
(4, 'Osos Polares', 'http://img.com/osos'),
(5, 'Aguilas Imperiales', 'http://img.com/aguilas'),
(6, 'Sharks Valencia', 'http://img.com/sharks'),
(7, 'Lobos Madrid', 'http://img.com/lobos'),
(8, 'Toros Andalucia', 'http://img.com/toros'),
(9, 'Dragones BCN', 'http://img.com/dragones'),
(10, 'Vikingos Norte', 'http://img.com/vikingos');

-- ============================================================
-- 4. ESTRUCTURA DE COMPETICIÓN (Season, Division, Coalitions)
-- ============================================================
INSERT INTO season (id, name, start_season, end_season) VALUES
(1, 'Temporada 2024-2025', '2024-09-01', '2025-06-30');

INSERT INTO division (id, name, category, season_id) VALUES
(1, 'División de Honor', 'A', 1),
(2, 'División de Plata', 'B', 1);

INSERT INTO coalition (id, name) VALUES
(1, 'Ultras Sur'),
(2, 'Frente Atletico');

-- ============================================================
-- 5. INSCRIPCIONES (Scores iniciales)
-- ============================================================
-- Usuarios 1-5 en Coalición 1, Usuarios 6-10 en Coalición 2
INSERT INTO user_season_score (id, total_points, season_id, user_id, coalition_id) VALUES
(1, 50, 1, 1, 1), (2, 40, 1, 2, 1), (3, 60, 1, 3, 1), (4, 10, 1, 4, 1), (5, 0, 1, 5, 1),
(6, 80, 1, 6, 2), (7, 20, 1, 7, 2), (8, 30, 1, 8, 2), (9, 90, 1, 9, 2), (10, 5, 1, 10, 2);

INSERT INTO coalition_season_score (id, total_points, season_id, coalition_id) VALUES
(1, 150, 1, 1), -- Suma dummy de usuarios top
(2, 200, 1, 2);

-- ============================================================
-- 6. JORNADAS Y PARTIDOS (20 Partidos x 2 Divisiones)
-- ============================================================
-- Jornadas División 1
INSERT INTO match_day (id, name, date_begin, date_end, division_id) VALUES
(1, 'Jornada 1', '2024-09-07', '2024-09-08', 1),
(2, 'Jornada 2', '2024-09-14', '2024-09-15', 1),
(3, 'Jornada 3', '2024-09-21', '2024-09-22', 1),
(4, 'Jornada 4', '2024-09-28', '2024-09-29', 1);

-- Jornadas División 2
INSERT INTO match_day (id, name, date_begin, date_end, division_id) VALUES
(5, 'Jornada 1 - B', '2024-09-07', '2024-09-08', 2),
(6, 'Jornada 2 - B', '2024-09-14', '2024-09-15', 2),
(7, 'Jornada 3 - B', '2024-09-21', '2024-09-22', 2),
(8, 'Jornada 4 - B', '2024-09-28', '2024-09-29', 2);

-- Partidos División 1 (Equipos 1-5 mezclados)
INSERT INTO match (id, name, time_date, local_result, away_result, match_day_id, local_team_id, away_team_id, address_id) VALUES
(1, 'Leonas vs Gazellas', '2024-09-07 16:00:00', 20, 10, 1, 1, 2, 3),
(2, 'Tigres vs Osos', '2024-09-07 18:00:00', 15, 15, 1, 3, 4, 3),
(3, 'Aguilas vs Leonas', '2024-09-14 16:00:00', NULL, NULL, 2, 5, 1, 3), -- No jugado
(4, 'Gazellas vs Tigres', '2024-09-14 18:00:00', NULL, NULL, 2, 2, 3, 3),
(5, 'Osos vs Aguilas', '2024-09-21 16:00:00', NULL, NULL, 3, 4, 5, 3),
(6, 'Leonas vs Tigres', '2024-09-21 18:00:00', NULL, NULL, 3, 1, 3, 3),
(7, 'Gazellas vs Osos', '2024-09-28 16:00:00', NULL, NULL, 4, 2, 4, 3),
(8, 'Tigres vs Aguilas', '2024-09-28 18:00:00', NULL, NULL, 4, 3, 5, 3);
-- (Agrega más filas aquí si necesitas llegar a exactamente 20)

-- Partidos División 2 (Equipos 6-10 mezclados)
INSERT INTO match (id, name, time_date, local_result, away_result, match_day_id, local_team_id, away_team_id, address_id) VALUES
(21, 'Sharks vs Lobos', '2024-09-07 16:00:00', 30, 5, 5, 6, 7, 4),
(22, 'Toros vs Dragones', '2024-09-07 18:00:00', 12, 14, 5, 8, 9, 4),
(23, 'Vikingos vs Sharks', '2024-09-14 16:00:00', NULL, NULL, 6, 10, 6, 4),
(24, 'Lobos vs Toros', '2024-09-14 18:00:00', NULL, NULL, 6, 7, 8, 4),
(25, 'Dragones vs Vikingos', '2024-09-21 16:00:00', NULL, NULL, 7, 9, 10, 4),
(26, 'Sharks vs Toros', '2024-09-21 18:00:00', NULL, NULL, 7, 6, 8, 4),
(27, 'Lobos vs Dragones', '2024-09-28 16:00:00', NULL, NULL, 8, 7, 9, 4),
(28, 'Toros vs Vikingos', '2024-09-28 18:00:00', NULL, NULL, 8, 8, 10, 4);

-- ============================================================
-- 7. APUESTAS (Ejemplo para probar ciclos DTO)
-- ============================================================
-- Usuario 1 apuesta en la Jornada 1
INSERT INTO weekly_bet_ticket (id, creation_date, user_season_score_id) VALUES
(1, '2024-09-06 10:00:00', 1);

INSERT INTO bet (id, points_awarded, weekly_bet_ticket_id, match_id, team_id) VALUES
(1, 5, 1, 1, 1), -- Apostó a Leonas (Match 1)
(2, 0, 1, 2, 4); -- Apostó a Osos (Match 2)

-- ============================================================
-- 8. REINICIAR SECUENCIAS (Importante para que no fallen nuevos inserts)
-- ============================================================
-- Ajusta los nombres de las secuencias si Supabase los generó diferente
SELECT setval(pg_get_serial_sequence('usuario', 'id'), (SELECT MAX(id) FROM usuario));
SELECT setval(pg_get_serial_sequence('address', 'id'), (SELECT MAX(id) FROM address));
SELECT setval(pg_get_serial_sequence('team', 'id'), (SELECT MAX(id) FROM team));
SELECT setval(pg_get_serial_sequence('season', 'id'), (SELECT MAX(id) FROM season));
SELECT setval(pg_get_serial_sequence('division', 'id'), (SELECT MAX(id) FROM division));
SELECT setval(pg_get_serial_sequence('coalition', 'id'), (SELECT MAX(id) FROM coalition));
SELECT setval(pg_get_serial_sequence('user_season_score', 'id'), (SELECT MAX(id) FROM user_season_score));
SELECT setval(pg_get_serial_sequence('coalition_season_score', 'id'), (SELECT MAX(id) FROM coalition_season_score));
SELECT setval(pg_get_serial_sequence('match_day', 'id'), (SELECT MAX(id) FROM match_day));
SELECT setval(pg_get_serial_sequence('match', 'id'), (SELECT MAX(id) FROM match));
SELECT setval(pg_get_serial_sequence('weekly_bet_ticket', 'id'), (SELECT MAX(id) FROM weekly_bet_ticket));
SELECT setval(pg_get_serial_sequence('bet', 'id'), (SELECT MAX(id) FROM bet));