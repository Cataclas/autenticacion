package co.com.crediya.usecase.usuario;

import co.com.crediya.model.auth.gateways.PasswordEncoder;
import co.com.crediya.model.rol.gateways.RolRepository;
import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.exceptions.DatosInvalidosException;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class RegistrarUsuarioUseCase {
    
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Double SALARIO_MINIMO = 0.0;
    private static final Double SALARIO_MAXIMO = 15000000.0;
    private static final String[] ROLES_VALIDOS = {"ADMINISTRADOR", "ASESOR", "SOLICITANTE"};

    public Mono<Usuario> registrar(Usuario usuario) {
        return validarDatos(usuario)
                .then(validarEmailUnico(usuario.getEmail()))
                .then(guardarUsuario(usuario));
    }

    private Mono<Void> validarDatos(Usuario usuario) {
        List<String> errores = new ArrayList<>();
        
        if (usuario == null) {
            errores.add("Los datos del usuario son requeridos");
            return Mono.error(new DatosInvalidosException(errores));
        }
        
        // Validar campos obligatorios
        if (esNuloOVacio(usuario.getNombre())) {
            errores.add("El nombre es obligatorio");
        }
        if (esNuloOVacio(usuario.getApellido())) {
            errores.add("El apellido es obligatorio");
        }
        if (esNuloOVacio(usuario.getEmail())) {
            errores.add("El email es obligatorio");
        }
        if (esNuloOVacio(usuario.getDocumentoIdentidad())) {
            errores.add("El documento de identidad es obligatorio");
        }
        if (usuario.getSalarioBase() == null) {
            errores.add("El salario base es obligatorio");
        }
        if (esNuloOVacio(usuario.getPassword())) {
            errores.add("La contraseña es obligatoria");
        }
        if (esNuloOVacio(usuario.getIdRol())) {
            errores.add("El rol es obligatorio");
        } else if (!esRolValido(usuario.getIdRol())) {
            errores.add("El rol debe ser: ADMINISTRADOR, ASESOR o SOLICITANTE");
        }
        
        // Validar formatos solo si los campos no están vacíos
        if (!esNuloOVacio(usuario.getEmail()) && !EMAIL_PATTERN.matcher(usuario.getEmail()).matches()) {
            errores.add("El formato del email no es válido");
        }
        
        if (usuario.getSalarioBase() != null && 
            (usuario.getSalarioBase() < SALARIO_MINIMO || usuario.getSalarioBase() > SALARIO_MAXIMO)) {
            errores.add("El salario debe estar entre 0 y 15.000.000");
        }
        
        if (!errores.isEmpty()) {
            return Mono.error(new DatosInvalidosException(errores));
        }
        
        return Mono.empty();
    }

    private Mono<Void> validarEmailUnico(String email) {
        return usuarioRepository.existePorEmail(email)
                .flatMap(existe -> existe ? 
                    Mono.error(new DatosInvalidosException("El email ya está registrado")) : 
                    Mono.empty());
    }

    private Mono<Usuario> guardarUsuario(Usuario usuario) {
        LocalDateTime now = LocalDateTime.now();
        String hashedPassword = passwordEncoder.encode(usuario.getPassword());
        
        return rolRepository.findByNombre(usuario.getIdRol())
                .switchIfEmpty(Mono.error(new DatosInvalidosException("Rol " + usuario.getIdRol() + " no encontrado")))
                .flatMap(rol -> {
                    Usuario usuarioConId = usuario.toBuilder()
                            .idUsuario(UUID.randomUUID().toString())
                            .idRol(rol.getIdRol())
                            .password(hashedPassword)
                            .createdAt(now)
                            .updatedAt(now)
                            .createdBy("SYSTEM")
                            .updatedBy("SYSTEM")
                            .active(true)
                            .build();
                    return usuarioRepository.guardar(usuarioConId);
                });
    }

    private boolean esNuloOVacio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }
    
    private boolean esRolValido(String rol) {
        for (String rolValido : ROLES_VALIDOS) {
            if (rolValido.equals(rol)) {
                return true;
            }
        }
        return false;
    }
}