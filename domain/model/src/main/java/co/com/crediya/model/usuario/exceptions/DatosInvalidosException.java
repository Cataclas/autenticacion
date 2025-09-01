package co.com.crediya.model.usuario.exceptions;

import java.util.List;

public class DatosInvalidosException extends RuntimeException {
    private final List<String> errores;
    
    public DatosInvalidosException(String mensaje) {
        super(mensaje);
        this.errores = List.of(mensaje);
    }
    
    public DatosInvalidosException(List<String> errores) {
        super(String.join(", ", errores));
        this.errores = errores;
    }
    
    public List<String> getErrores() {
        return errores;
    }
}