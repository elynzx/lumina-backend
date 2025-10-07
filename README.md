# Lumina Eventos – Backend

API RESTful desarrollada para el Sistema de alquiler de locales para eventos, encargada de gestionar toda la lógica de negocio, seguridad y conexión con la base de datos.

## Descripción

Proporciona los servicios necesarios para el funcionamiento de la plataforma Lumina Eventos, permitiendo los clientes buscar, reservar y gestionar locales para eventos (matrimonios, cumpleaños, conferencias, etc.), mientras que los administradores pueden gestionar el catálogo de locales, mobiliario, clientes y reservas.
Incluye autenticación mediante JWT (JSON Web Tokens), validación de datos, control de acceso por roles y endpoints estructurados bajo buenas prácticas RESTPlataforma.

## Características Principales

### Para Clientes:
- Registro y autenticación con JWT
- Búsqueda de locales con filtros (distrito, tipo de evento, aforo, precio)
- Reserva de locales con selección de fecha y hora
- Selección de mobiliario adicional (sillas, mesas, luces)
- Proceso de pago (Yape, Plin, MercadoPago)
- Descarga de comprobante en PDF
- Gestión de perfil personal

### Para Administradores:
- CRUD completo de locales
- CRUD completo de mobiliario
- Gestión de clientes
- Visualización de reservas
- Dashboard con estadísticas
- Cambio de estados (locales, reservas)

## Tecnologías Utilizadas

### Backend:
- **Java 17**
- **Spring Boot 3.x**
- **Spring Security** con JWT
- **Spring Data JPA / Hibernate**
- **MySQL 8.0**
- **Maven**
  
### Dependencias principales:
```xml
- spring-boot-starter-web
- spring-boot-starter-data-jpa
- spring-boot-starter-security
- spring-boot-starter-validation
- jjwt (JSON Web Token)
- lombok
- mysql-connector-java
```
### Seguridad

- Autenticación: JWT (JSON Web Token)
- Contraseñas: Encriptadas con BCrypt
- CORS: Configurado para orígenes específicos
- Validaciones: Bean Validation en todos los DTOs
- Roles: Protección de endpoints por rol (CLIENTE, ADMIN)

