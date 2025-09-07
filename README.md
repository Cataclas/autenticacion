# Microservicio de Autenticación - CrediYa

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.java.net/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg)](https://www.postgresql.org/)
[![Clean Architecture](https://img.shields.io/badge/Architecture-Clean-green.svg)](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)

## 📋 Descripción

Microservicio responsable de la gestión de usuarios, autenticación y autorización del sistema CrediYa. Implementa Clean Architecture con Spring WebFlux para programación reactiva y JWT para autenticación stateless.

## 🏗️ Arquitectura

### Clean Architecture
```
├── domain/
│   ├── model/          # Entidades de negocio
│   └── usecase/        # Casos de uso
├── infrastructure/
│   ├── driven-adapters/    # Adaptadores de salida (BD, APIs externas)
│   ├── entry-points/       # Adaptadores de entrada (REST, eventos)
│   └── helpers/            # Utilidades y configuraciones
└── applications/
    └── app-service/        # Aplicación principal
```

### Responsabilidades
- **Registro de Usuarios**: Solo ADMIN/ASESOR pueden registrar usuarios
- **Autenticación JWT**: Generación de tokens con ID de usuario y rol
- **Validación de Tokens**: Servicio para otros microservicios
- **Tokens de Servicio**: Comunicación segura entre microservicios
- **Información de Usuario**: Endpoint para obtener datos de usuario

## 🚀 Stack Tecnológico

- **Java 21** - Lenguaje de programación
- **Spring Boot 3.5.4** - Framework principal
- **Spring WebFlux** - Programación reactiva
- **Spring R2DBC** - Acceso reactivo a base de datos
- **Spring Security** - Seguridad y autenticación
- **JWT** - Tokens de autenticación
- **PostgreSQL 15** - Base de datos
- **Lombok** - Reducción de boilerplate
- **Gradle 8.14.3** - Gestión de dependencias

## 📊 Base de Datos

### Esquema: crediya_auth

#### Entidades Principales
- **usuario**: Información de usuarios del sistema
- **rol**: Roles y permisos de acceso

#### Roles del Sistema
- **ADMINISTRADOR**: Acceso completo al sistema
- **ASESOR**: Gestión de solicitudes y evaluación
- **CLIENTE**: Creación de solicitudes

## 🔐 Seguridad y JWT

### Configuración JWT
- **Algoritmo**: HS256
- **Expiración usuarios**: 2 horas
- **Expiración servicios**: 24 horas
- **Header**: Authorization: Bearer {token}

### Estructura del Token
```json
{
  "sub": "uuid-user",
  "role": "uuid-role",
  "iat": 1757208977,
  "exp": 1757216177
}
```

### Autorización
- **Públicos**: `/api/v1/login`, `/api/v1/validate-token`, `/api/v1/service-token`, `/swagger*`, `/v3/api-docs*`, `/webjars/swagger-ui*`, `/actuator*`
- **Protegidos**: `/api/v1/usuarios` (requiere JWT válido)
- **Especiales**: `/api/v1/users/info` (solo tokens de servicio o ADMIN)

## 📡 API Endpoints

### Principales Servicios

#### Autenticación
- `POST /api/v1/login` - Autenticación de usuarios
- `POST /api/v1/validate-token` - Validación de tokens (servicio a servicio)
- `POST /api/v1/service-token` - Generar token de servicio

#### Gestión de Usuarios
- `POST /api/v1/usuarios` - Registrar nuevo usuario
- `POST /api/v1/users/info` - Obtener información de usuario

### Documentación Completa
- **Swagger UI**: `/webjars/swagger-ui/index.html`

## 🛠️ Configuración

### Variables de Entorno

#### Base de Datos
```env
DB_HOST=<database-host>
DB_PORT=<database-port>
DB_NAME=<database-name>
DB_USERNAME=<database-user>
DB_PASSWORD=<database-password>
```

#### JWT y Seguridad
```env
JWT_SECRET=<jwt-secret-key-256-bits-minimum>
JWT_EXPIRATION_HOURS=<token-expiration-hours>
JWT_SERVICE_SECRET=<service-secret-key-256-bits-minimum>
JWT_SERVICE_EXPIRATION=<service-token-expiration-hours>
```

#### Aplicación
```env
SERVER_PORT=<application-port>
LOG_LEVEL=<log-level>
DB_POOL_INITIAL=<initial-pool-size>
DB_POOL_MAX=<max-pool-size>
DB_POOL_IDLE=<idle-timeout>
ADMIN_ROLE=<admin-role-name>
ASESOR_ROLE=<asesor-role-name>
SOLICITANTE_ROLE=<solicitante-role-name>
```

## 🚀 Instalación y Ejecución

### 1. Configurar Base de Datos
```bash
# Desde el directorio raíz del proyecto
cd ../database
docker-compose up -d crediya-auth-db
```

### 2. Compilar y Ejecutar

#### Desarrollo Local
```bash
# Compilar
./gradlew build

# Ejecutar
./gradlew bootRun
```