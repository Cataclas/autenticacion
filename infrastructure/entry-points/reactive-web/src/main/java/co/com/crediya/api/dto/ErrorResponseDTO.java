package co.com.crediya.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ErrorResponseDTO {
    
    @JsonProperty("mensaje")
    private String mensaje;
    
    @JsonProperty("errores")
    private List<String> errores;
    
    @JsonProperty("codigo_error")
    private String codigoError;
    
    @JsonProperty("timestamp")
    private LocalDateTime timestamp;
    
    public static ErrorResponseDTO of(String mensaje, String codigoError) {
        ErrorResponseDTO error = new ErrorResponseDTO();
        error.setMensaje(mensaje);
        error.setCodigoError(codigoError);
        error.setTimestamp(LocalDateTime.now());
        return error;
    }
    
    public static ErrorResponseDTO of(List<String> errores, String codigoError) {
        ErrorResponseDTO error = new ErrorResponseDTO();
        error.setMensaje("Se encontraron errores de validación");
        error.setErrores(errores);
        error.setCodigoError(codigoError);
        error.setTimestamp(LocalDateTime.now());
        return error;
    }
}