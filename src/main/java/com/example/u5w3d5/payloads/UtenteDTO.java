package com.example.u5w3d5.payloads;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record UtenteDTO(
        @NotBlank(message = "Il nome è obbligatorio")
        @Size(min = 2, max = 30, message = "Il nome deve essere tra 2 e 30 caratteri")
        String nome,

        @NotBlank(message = "Il cognome è obbligatorio")
        @Size(min = 2, max = 30, message = "Il cognome deve essere tra 2 e 30 caratteri")
        String cognome,

        @NotBlank(message = "L'email è obbligatoria")
        @Email(message = "L'email non è valida")
        String email,

        @NotBlank(message = "La password è obbligatoria")
        @Size(min = 4, message = "La password deve avere almeno 4 caratteri")
        String password,

        @Past(message = "La data di nascita deve essere nel passato")
        LocalDate dataNascita,

        @NotBlank(message = "Il ruolo è obbligatorio")
        String ruolo
) {
}
