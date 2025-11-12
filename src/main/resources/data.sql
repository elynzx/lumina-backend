
-- ===== INSERTAR ROLES =====
INSERT INTO roles (nombre_rol, descripcion)
VALUES ('ADMIN', 'Administrador del sistema con acceso completo'),
       ('CLIENTE', 'Cliente que puede hacer reservas y gestionar su perfil');

-- ===== INSERTAR DISTRITOS =====
INSERT INTO distritos (nombre_distrito)
VALUES ('Miraflores'),
       ('San Isidro'),
       ('Surco'),
       ('La Molina'),
       ('Barranco'),
       ('San Borja');

-- ===== INSERTAR TIPOS DE EVENTO =====
INSERT INTO tipos_evento (nombre_tipo, descripcion)
VALUES ('Boda', 'Ceremonias matrimoniales y recepciones'),
       ('Conferencia', 'Charlas empresariales y seminarios'),
       ('Cumpleaños', 'Celebraciones de cumpleaños y fiestas familiares'),
       ('Concierto', 'Eventos musicales y presentaciones artísticas'),
       ('Graduación', 'Ceremonias de graduación'),
       ('Corporativo', 'Eventos empresariales');

-- ===== INSERTAR MOBILIARIOS =====
INSERT INTO mobiliario (nombre, descripcion, stock_total, precio_unitario, url_foto)
VALUES ('Mesa Redonda', 'Mesa redonda para 8 personas', 50, 25.00,
        'https://images.unsplash.com/photo-1586023492125-27b2c045efd7?w=400'),
       ('Mesa Rectangular', 'Mesa rectangular para 10 personas', 40, 30.00,
        'https://images.unsplash.com/photo-1549497538-303791108f95?w=400'),
       ('Silla Tiffany', 'Silla elegante estilo Tiffany', 200, 8.00,
        'https://images.unsplash.com/photo-1506439773649-6e0eb8cfb237?w=400'),
       ('Silla Chiavari', 'Silla dorada estilo Chiavari', 150, 10.00,
        'https://images.unsplash.com/photo-1586023492125-27b2c045efd7?w=400'),
       ('Tarima', 'Tarima para eventos de 3x3 metros', 10, 150.00,
        'https://images.unsplash.com/photo-1598300042247-d088f8ab3a91?w=400'),
       ('Equipo de Sonido', 'Sistema de audio profesional', 8, 200.00,
        'https://images.unsplash.com/photo-1493225457124-a3eb161ffa5f?w=400'),
       ('Proyector', 'Proyector HD con pantalla', 5, 120.00,
        'https://images.unsplash.com/photo-1560472354-b33ff0c44a43?w=400'),
       ('Iluminación LED', 'Sistema de luces LED decorativas', 15, 180.00,
        'https://images.unsplash.com/photo-1478147427282-58e87a9ce616?w=400');

-- ===== INSERTAR LOCALES =====
INSERT INTO locales (nombre_local, direccion, id_distrito, aforo_maximo, precio_hora, descripcion, estado)
VALUES ('Salón Dorado', 'Av. Pardo 123, Miraflores', 1, 200, 500.00,
        'Elegante salón con vista al mar, ideal para bodas y eventos especiales', 'AVAILABLE'),
       ('Terraza Verde', 'Jr. Olivos 456, San Isidro', 2, 150, 400.00,
        'Hermosa terraza con jardín, perfecta para eventos al aire libre', 'AVAILABLE'),
       ('Centro Empresarial', 'Av. Javier Prado 789, Surco', 3, 500, 800.00,
        'Moderno centro de convenciones con tecnología de punta', 'AVAILABLE'),
       ('Casa Colonial', 'Calle San Martín 321, La Molina', 4, 120, 350.00,
        'Ambiente colonial con arquitectura histórica', 'AVAILABLE'),
       ('Roof Top Barranco', 'Av. Grau 567, Barranco', 5, 100, 450.00,
        'Azotea con vista panorámica del distrito bohemio', 'AVAILABLE'),
       ('Salón Crystal', 'Av. San Luis 890, San Borja', 6, 180, 480.00,
        'Salón moderno con cristalería y acabados de lujo', 'UNAVAILABLE'),
       ('Garden Party', 'Jr. Ica 234, Miraflores', 1, 80, 280.00, 'Jardín privado ideal para eventos íntimos',
        'AVAILABLE'),
       ('Loft Industrial', 'Av. Brasil 678, San Isidro', 2, 90, 320.00, 'Espacio industrial moderno con estilo urbano',
        'AVAILABLE');

-- ===== INSERTAR FOTOS DE LOCALES =====
INSERT INTO fotos_locales (id_local, url_foto, descripcion)
VALUES
-- Salón Dorado
(1, 'https://images.unsplash.com/photo-1519167758481-83f550bb49b3?w=800', 'Vista principal del salón'),
(1, 'https://images.unsplash.com/photo-1464366400600-7168b8af9bc3?w=800', 'Interior elegante'),
(1, 'https://images.unsplash.com/photo-1511795409834-ef04bbd61622?w=800', 'Montaje para boda'),

-- Terraza Verde
(2, 'https://images.unsplash.com/photo-1530103862676-de8c9debad1d?w=800', 'Terraza con jardín'),
(2, 'https://images.unsplash.com/photo-1505236858219-8359eb29e329?w=800', 'Vista nocturna'),

-- Centro Empresarial
(3, 'https://images.unsplash.com/photo-1475721027785-f74eccf877e2?w=800', 'Auditorio principal'),
(3, 'https://images.unsplash.com/photo-1542744095-291d1f67b221?w=800', 'Sala de conferencias'),

-- Casa Colonial
(4, 'https://images.unsplash.com/photo-1571896349842-33c89424de2d?w=800', 'Patio colonial'),

-- Roof Top Barranco
(5, 'https://images.unsplash.com/photo-1517457373958-b7bdd4587205?w=800', 'Vista panorámica'),

-- Salón Crystal
(6, 'https://images.unsplash.com/photo-1519167758481-83f550bb49b3?w=800', 'Cristalería elegante'),

-- Garden Party
(7, 'https://images.unsplash.com/photo-1530103862676-de8c9debad1d?w=800', 'Jardín íntimo'),

-- Loft Industrial
(8, 'https://images.unsplash.com/photo-1556909114-f6e7ad7d3136?w=800', 'Estilo industrial');

-- ===== RELACIONES LOCAL-TIPO EVENTO =====
INSERT INTO local_tipo_evento (id_local, id_tipo_evento)
VALUES
-- Salón Dorado (bodas, graduaciones)
(1, 1),
(1, 5),
-- Terraza Verde (cumpleaños, corporativo)
(2, 3),
(2, 6),
-- Centro Empresarial (conferencias, corporativo)
(3, 2),
(3, 6),
-- Casa Colonial (bodas, graduaciones)
(4, 1),
(4, 5),
-- Roof Top Barranco (cumpleaños, conciertos)
(5, 3),
(5, 4),
-- Salón Crystal (bodas, corporativo)
(6, 1),
(6, 6),
-- Garden Party (cumpleaños)
(7, 3),
-- Loft Industrial (conciertos, corporativo)
(8, 4),
(8, 6);