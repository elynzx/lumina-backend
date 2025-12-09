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
INSERT INTO tipos_evento (nombre_tipo, descripcion, url_foto)
VALUES ('Boda', 'Ceremonias matrimoniales y recepciones',
        'https://images.unsplash.com/photo-1757589166064-ef528f309c94?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&q=80&w=715'),
       ('Conferencia', 'Charlas empresariales y seminarios',
        'https://images.unsplash.com/photo-1560523160-c4ef2f0c61a9?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&q=80&w=1170'),
       ('Cumpleaños', 'Celebraciones de cumpleaños y fiestas familiares',
        'https://images.unsplash.com/photo-1556125574-d7f27ec36a06?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&q=80&w=1170'),
       ('Concierto', 'Eventos musicales y presentaciones artísticas',
        'https://images.unsplash.com/photo-1576941230616-1df1071ff6c0?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&q=80&w=1074'),
       ('Graduación', 'Ceremonias de graduación',
        'https://images.unsplash.com/photo-1599943821034-8cb5c7526922?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&q=80&w=759'),
       ('Corporativo', 'Eventos empresariales',
        'https://images.unsplash.com/photo-1523580494863-6f3031224c94?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&q=80&w=1170');

-- ===== INSERTAR MOBILIARIOS =====
INSERT INTO mobiliario (nombre, descripcion, stock_total, precio_unitario, url_foto)
VALUES ('Mesa Redonda', 'Mesa redonda modelo Cavari para 10 personas', 50, 25.00,
        'https://images.unsplash.com/photo-1759955903787-08b20dd8a925?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D'),
       ('Mesa Rectangular', 'Mesa rectangular modelo Caela para 10 personas', 40, 30.00,
        'https://images.unsplash.com/photo-1715592029240-81cb714785e1?q=80&w=687&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D'),
       ('Silla Tiffany', 'Silla elegante dorada estilo Tiffany', 200, 8.00,
        'https://images.unsplash.com/photo-1528221297180-b340bcc21812?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NTV8fHdlZGRpbmclMjBmdXJuaXR1cmV8ZW58MHx8MHx8fDA%3D'),
       ('Silla Chiavari', 'Silla blanca estilo Chiavari', 150, 10.00,
        'https://images.unsplash.com/photo-1739796089365-3280112c6329?q=80&w=1169&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D'),
       ('Catering Premium', 'Menú de 3 tiempos, incluye personal y menaje', 100000, 50.00,
        'https://images.unsplash.com/photo-1751163781124-85bd6628de1e?q=80&w=687&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D'),
       ('Servicio de Bar', 'Barra libre 4 horas', 100000, 2000.00,
        'https://plus.unsplash.com/premium_photo-1764196527164-8a3ae326e11d?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D'),
       ('Dj', 'Incluye equipo de sonido', 10000, 1500.00,
        'https://images.unsplash.com/photo-1713450602872-9e7587ea22ae?q=80&w=1760&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D'),
       ('Fotografía y Video', 'Cobertura completa + edición', 10000, 2500.00,
        'https://images.unsplash.com/photo-1541515929569-1771522cbaa9?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D'),
       ('Servicios obligatorios', 'Servicio de limpieza, seguridad y garantía', 10000, 1650.00,
        'https://static.vecteezy.com/system/resources/previews/002/389/448/non_2x/cleaning-service-black-glyph-icon-vector.jpg');

-- ===== INSERTAR LOCALES (✅ CORREGIDO - Con ubicación geográfica) =====
INSERT INTO locales (nombre_local, direccion, id_distrito, aforo_maximo, precio_hora, descripcion, estado, latitud,
                     longitud, url_google_maps)
VALUES ('Salón Dorado', 'Av. Pardo 123, Miraflores', 1, 200, 500.00,
        'Elegante salón con vista al mar, ideal para bodas y eventos especiales', 'AVAILABLE', -12.111627, -77.021151,
        'https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3901.0461505005206!2d-77.02145!3d-12.111627!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x9105c8d4d4d4d4d5%3A0x0!2sMiraflores%2C%20Lima!5e0!3m2!1ses!2spe!4v1234567890'),
       ('Terraza Verde', 'Jr. Olivos 456, San Isidro', 2, 150, 400.00,
        'Hermosa terraza con jardín, perfecta para eventos al aire libre', 'AVAILABLE', -12.091807, -77.031641,
        'https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3901.2168505005206!2d-77.031641!3d-12.091807!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x9105c8a8a8a8a8a9%3A0x0!2sSan%20Isidro%2C%20Lima!5e0!3m2!1ses!2spe!4v1234567890'),
       ('Centro Empresarial', 'Av. Javier Prado 789, Surco', 3, 500, 800.00,
        'Moderno centro de convenciones con tecnología de punta', 'AVAILABLE', -12.055410, -77.025319,
        'https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3901.4875505005206!2d-77.025319!3d-12.055410!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x9105c8d4d4d4d4d5%3A0x0!2sSurco%2C%20Lima!5e0!3m2!1ses!2spe!4v1234567890'),
       ('Casa La Molina', 'Urb. La Molina Vieja, La Molina', 4, 120, 350.00,
        'Ambiente colonial con arquitectura histórica', 'AVAILABLE', -12.072038, -76.984680,
        'https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3901.3168505005206!2d-76.984680!3d-12.072038!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x9105c8d4d4d4d4d5%3A0x0!2sLa%20Molina%2C%20Lima!5e0!3m2!1ses!2spe!4v1234567890'),
       ('Roof Top Barranco', 'Av. Grau 567, Barranco', 5, 100, 450.00,
        'Azotea con vista panorámica del distrito bohemio', 'AVAILABLE', -12.142847, -77.028961,
        'https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3900.8755505005206!2d-77.028961!3d-12.142847!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x9105c8d4d4d4d4d5%3A0x0!2sBarranco%2C%20Lima!5e0!3m2!1ses!2spe!4v1234567890'),
       ('Salón Crystal', 'Av. San Luis 890, San Borja', 6, 180, 480.00,
        'Salón moderno con cristalería y acabados de lujo', 'UNAVAILABLE', -12.095234, -76.998765,
        'https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3901.1968505005206!2d-76.998765!3d-12.095234!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x9105c8d4d4d4d4d5%3A0x0!2sSan%20Borja%2C%20Lima!5e0!3m2!1ses!2spe!4v1234567890'),
       ('Garden Party', 'Jr. Ica 234, Miraflores', 1, 80, 280.00, 'Jardín privado ideal para eventos íntimos',
        'AVAILABLE', -12.118456, -77.024890,
        'https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3900.9968505005206!2d-77.024890!3d-12.118456!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x9105c8d4d4d4d4d5%3A0x0!2sMiraflores%2C%20Lima!5e0!3m2!1ses!2spe!4v1234567890'),
       ('Loft Industrial', 'Av. Brasil 678, San Isidro', 2, 90, 320.00, 'Espacio industrial moderno con estilo urbano',
        'AVAILABLE', -12.087654, -77.034567,
        'https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3901.2568505005206!2d-77.034567!3d-12.087654!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x9105c8d4d4d4d4d5%3A0x0!2sSan%20Isidro%2C%20Lima!5e0!3m2!1ses!2spe!4v1234567890');

-- ===== INSERTAR FOTOS DE LOCALES =====
INSERT INTO fotos_locales (id_local, url_foto, descripcion)
VALUES
-- Salón Dorado
(1, 'https://images.unsplash.com/photo-1519167758481-83f550bb49b3?w=800', 'Vista principal del salón'),
(1, 'https://images.unsplash.com/photo-1464366400600-7168b8af9bc3?w=800', 'Interior elegante'),
(1, 'https://images.unsplash.com/photo-1511795409834-ef04bbd61622?w=800', 'Montaje para boda'),

-- Terraza Verde
(2,
 'https://images.unsplash.com/photo-1734705797879-0c23e9edca21?q=80&w=1169&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
 'Terraza con jardín vertical'),
(2,
 'https://images.unsplash.com/photo-1734705797907-a2ad9e6c71a2?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1yZWxhdGVkfDJ8fHxlbnwwfHx8fHw%3D',
 'Vista superior'),
(2,
 'https://images.unsplash.com/photo-1734705797834-a356d77e4140?q=80&w=1169&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
 'Vista interior'),

-- Centro Empresarial
(3,
 'https://images.unsplash.com/photo-1603430416695-752802223e94?q=80&w=1576&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
 'Auditorio principal'),
(3,
 'https://images.unsplash.com/photo-1603430416807-87dd6f9538d0?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1yZWxhdGVkfDV8fHxlbnwwfHx8fHw%3D',
 'Sala de conferencias'),
(3,
 'https://images.unsplash.com/photo-1629969338712-472f4440929d?q=80&w=1218&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
 'Sala de conferencias'),

-- Casa La Molina
(4,
 'https://images.unsplash.com/photo-1733244738988-70b1f4b96c37?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
 'Patio colonial'),
(4,
 'https://images.unsplash.com/photo-1733244739136-0f22b674a18b?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
 'Patio colonial'),
(4,
 'https://images.unsplash.com/photo-1733244739141-9fd6c0a75a1b?q=80&w=749&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
 'Patio colonial'),

-- Roof Top Barranco
(5,
 'https://images.unsplash.com/photo-1752857015590-ab5254f2523f?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
 'Vista panorámica'),
(5,
 'https://images.unsplash.com/photo-1752857015570-2fcc1c8a6350?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
 'Vista panorámica'),
(5,
 'https://images.unsplash.com/photo-1752857015560-d5c05b3f7ecb?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1yZWxhdGVkfDJ8fHxlbnwwfHx8fHw%3D',
 'Vista panorámica'),

-- Salón Crystal
(6, 'https://images.unsplash.com/photo-1519167758481-83f550bb49b3?w=800', 'Cristalería elegante'),

-- Garden Party
(7,
 'https://images.unsplash.com/photo-1707333513884-34c7881fb4ee?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
 'Jardín íntimo'),
(7,
 'https://images.unsplash.com/photo-1707333514312-39cf7658479c?q=80&w=764&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
 'Jardín íntimo'),
(7,
 'https://images.unsplash.com/photo-1707333514156-d42751dca70d?q=80&w=764&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
 'Jardín íntimo'),

-- Loft Industrial
(8,
 'https://plus.unsplash.com/premium_photo-1664530452596-e1c17e342876?q=80&w=686&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
 'Estilo industrial'),
(8,
 'https://plus.unsplash.com/premium_photo-1664530452485-72ddb634c24c?q=80&w=686&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
 'Estilo industrial'),
(8,
 'https://plus.unsplash.com/premium_photo-1664530452361-9f86e38784b8?q=80&w=686&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
 'Estilo industrial');

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
-- Loft Industrial (boda, conciertos, corporativo)
(8, 1),
(8, 4),
(8, 6);

INSERT INTO metodos_pago (nombre_metodo, descripcion)
VALUES ('Tarjeta', 'Pago con tarjeta de crédito o débito'),
       ('Transferencia', 'Pago mediante transferencia bancaria con comprobante'),
       ('PagoEfectivo', 'Pago en efectivo con código generado');