package com.example.u5w3d5.payloads;


import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record EventoDTO(
        @NotBlank(message = "Il titolo è obbligatorio")
        String titolo,

        @NotBlank(message = "La descrizione è obbligatoria")
        @Size(max = 500, message = "La descrizione può avere massimo 500 caratteri")
        String descrizione,

        @Future(message = "La data dell'evento deve essere futura")
        LocalDate data,

        @NotBlank(message = "Il luogo è obbligatorio")
        String luogo,

        @Min(value = 1, message = "I posti disponibili devono essere almeno 1")
        int postiDisponibili
) {
}
