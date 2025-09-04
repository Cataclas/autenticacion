package co.com.crediya.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ErrorResponseDTO {
    
    @JsonProperty("mensaje")
    private String mensaje;
    
    @JsonProperty("errores")
    private List<String> errores;
    
    @JsonProperty("codigo_error")
    private String codigoError;
    
    @JsonProperty("timestamp")
    private LocalDateTime timestamp;
    
    @JsonProperty("trace_id")
    private String traceId;
    
    public static ErrorResponseDTO of(String mensaje, String codigoError) {
        return ErrorResponseDTO.builder()
                .mensaje(mensaje)
                .codigoError(codigoError)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    public static ErrorResponseDTO of(List<String> errores, String codigoError) {
        return ErrorResponseDTO.builder()
                .mensaje("Se encontraron errores de validación")
                .errores(errores)
                .codigoError(codigoError)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    public static ErrorResponseDTO of(String mensaje, String codigoError, String traceId) {
        return ErrorResponseDTO.builder()
                .mensaje(mensaje)
                .codigoError(codigoError)
                .timestamp(LocalDateTime.now())
                .traceId(traceId)
                .build();
    }
}