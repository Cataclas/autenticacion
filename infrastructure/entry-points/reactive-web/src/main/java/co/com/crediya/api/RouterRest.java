package co.com.crediya.api;

import co.com.crediya.api.dto.ErrorResponseDTO;
import co.com.crediya.api.dto.LoginRequestDTO;
import co.com.crediya.api.dto.LoginResponseDTO;
import co.com.crediya.api.dto.TokenValidationRequestDTO;
import co.com.crediya.api.dto.TokenValidationResponseDTO;
import co.com.crediya.api.dto.UserInfoRequestDTO;
import co.com.crediya.api.dto.UserInfoResponseDTO;
import co.com.crediya.api.dto.UsuarioRequestDTO;
import co.com.crediya.api.dto.UsuarioResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@Tag(name = "Usuarios", description = "API para gestión de usuarios")
@Tag(name = "Autenticación", description = "API para autenticación de usuarios")
public class RouterRest {
    
    @Bean
    @RouterOperations({
        @RouterOperation(
            path = "/api/v1/usuarios",
            method = RequestMethod.POST,
            operation = @Operation(
                operationId = "registrarUsuario",
                summary = "Registrar nuevo usuario",
                description = "Registra un nuevo usuario en el sistema. REQUIERE TOKEN JWT. Solo usuarios con rol ADMINISTRADOR o ASESOR pueden usar este endpoint.",
                tags = {"Usuarios"},
                security = @SecurityRequirement(name = "bearerAuth"),
                requestBody = @RequestBody(
                    description = "Datos del usuario a registrar",
                    required = true,
                    content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = UsuarioRequestDTO.class),
                        examples = {
                            @ExampleObject(
                                name = "Registro Asesor",
                                summary = "Ejemplo de registro de asesor",
                                value = """
                                {
                                  "nombres": "María Elena",
                                  "apellidos": "González López",
                                  "correo_electronico": "maria.gonzalez@crediya.com",
                                  "salario_base": 3500000.0,
                                  "documento_identidad": "87654321",
                                  "fecha_nacimiento": "1985-03-15",
                                  "direccion": "Carrera 15 #45-67",
                                  "telefono": "3109876543",
                                  "password": "asesor123",
                                  "rol": "ASESOR"
                                }
                                """
                            ),
                            @ExampleObject(
                                name = "Registro Cliente",
                                summary = "Ejemplo de registro de cliente",
                                value = """
                                {
                                  "nombres": "Carlos Andrés",
                                  "apellidos": "Ramírez Torres",
                                  "correo_electronico": "carlos.ramirez@email.com",
                                  "salario_base": 2800000.0,
                                  "documento_identidad": "12345678",
                                  "fecha_nacimiento": "1990-07-22",
                                  "direccion": "Calle 80 #12-34",
                                  "telefono": "3001234567",
                                  "password": "cliente123",
                                  "rol": "SOLICITANTE"
                                }
                                """
                            )
                        }
                    )
                ),
                responses = {
                    @ApiResponse(
                        responseCode = "201",
                        description = "Usuario registrado exitosamente",
                        content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDTO.class)
                        )
                    ),
                    @ApiResponse(
                        responseCode = "400",
                        description = "Datos inválidos o campos requeridos faltantes",
                        content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                        )
                    ),
                    @ApiResponse(
                        responseCode = "401",
                        description = "Token JWT requerido",
                        content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                        )
                    ),
                    @ApiResponse(
                        responseCode = "403",
                        description = "Sin permisos - Solo ADMINISTRADOR y ASESOR pueden registrar usuarios",
                        content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                        )
                    ),
                    @ApiResponse(
                        responseCode = "409",
                        description = "El email ya está registrado",
                        content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                        )
                    ),
                    @ApiResponse(
                        responseCode = "500",
                        description = "Error interno del servidor",
                        content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                        )
                    )
                }
            )
        ),
        @RouterOperation(
            path = "/api/v1/login",
            method = RequestMethod.POST,
            operation = @Operation(
                operationId = "iniciarSesion",
                summary = "Iniciar sesión",
                description = "Autentica un usuario y genera un token JWT",
                tags = {"Autenticación"},
                requestBody = @RequestBody(
                    description = "Credenciales de acceso",
                    required = true,
                    content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = LoginRequestDTO.class),
                        examples = @ExampleObject(
                            name = "Login Admin",
                            summary = "Credenciales de administrador",
                            value = """
                            {
                              "email": "admin@crediya.com",
                              "password": "admin123"
                            }
                            """
                        )
                    )
                ),
                responses = {
                    @ApiResponse(
                        responseCode = "200",
                        description = "Autenticación exitosa",
                        content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponseDTO.class)
                        )
                    ),
                    @ApiResponse(
                        responseCode = "401",
                        description = "Credenciales inválidas",
                        content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                        )
                    ),
                    @ApiResponse(
                        responseCode = "500",
                        description = "Error interno del servidor",
                        content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                        )
                    )
                }
            )
        ),
        @RouterOperation(
            path = "/api/v1/validate-token",
            method = RequestMethod.POST,
            operation = @Operation(
                operationId = "validarToken",
                summary = "Validar token JWT",
                description = "Valida un token JWT y retorna información del usuario. Endpoint para uso de otros microservicios.",
                tags = {"Autenticación"},
                requestBody = @RequestBody(
                    description = "Token a validar",
                    required = true,
                    content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = TokenValidationRequestDTO.class),
                        examples = @ExampleObject(
                            name = "Validar Token",
                            summary = "Ejemplo de validación de token",
                            value = """
                            {
                              "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                            }
                            """
                        )
                    )
                ),
                responses = {
                    @ApiResponse(
                        responseCode = "200",
                        description = "Token validado correctamente",
                        content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TokenValidationResponseDTO.class)
                        )
                    ),
                    @ApiResponse(
                        responseCode = "401",
                        description = "Token inválido",
                        content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TokenValidationResponseDTO.class)
                        )
                    ),
                    @ApiResponse(
                        responseCode = "500",
                        description = "Error interno del servidor",
                        content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                        )
                    )
                }
            )
        ),
        @RouterOperation(
            path = "/api/v1/users/info",
            method = RequestMethod.POST,
            operation = @Operation(
                operationId = "getUserInfo",
                summary = "Obtener información de usuario",
                description = "Obtiene información básica de un usuario por su ID. Endpoint seguro para uso de otros microservicios.",
                tags = {"Usuarios"},
                security = @SecurityRequirement(name = "bearerAuth"),
                requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "ID del usuario a consultar",
                    required = true,
                    content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = UserInfoRequestDTO.class)
                    )
                ),
                responses = {
                    @ApiResponse(
                        responseCode = "200",
                        description = "Información de usuario obtenida exitosamente",
                        content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserInfoResponseDTO.class)
                        )
                    ),
                    @ApiResponse(
                        responseCode = "404",
                        description = "Usuario no encontrado",
                        content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                        )
                    ),
                    @ApiResponse(
                        responseCode = "500",
                        description = "Error interno del servidor",
                        content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                        )
                    )
                }
            )
        )
    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler, AuthHandler authHandler, UserHandler userHandler, ServiceTokenHandler serviceTokenHandler) {
        return route(POST("/api/v1/usuarios").and(accept(MediaType.APPLICATION_JSON)), 
                     handler::registrarUsuario)
                .andRoute(POST("/api/v1/login").and(accept(MediaType.APPLICATION_JSON)),
                         authHandler::iniciarSesion)
                .andRoute(POST("/api/v1/validate-token").and(accept(MediaType.APPLICATION_JSON)),
                         authHandler::validarToken)
                .andRoute(POST("/api/v1/users/info").and(accept(MediaType.APPLICATION_JSON)),
                         userHandler::getUserInfo)
                .andRoute(POST("/api/v1/service-token").and(accept(MediaType.APPLICATION_JSON)),
                         serviceTokenHandler::getServiceToken);
    }
}