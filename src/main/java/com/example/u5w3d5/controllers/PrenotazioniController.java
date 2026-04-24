package com.example.u5w3d5.controllers;

import com.example.u5w3d5.entities.Prenotazione;
import com.example.u5w3d5.entities.Utente;
import com.example.u5w3d5.exceptions.ValidationException;
import com.example.u5w3d5.payloads.PrenotazioneDTO;
import com.example.u5w3d5.services.PrenotazioniService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/prenotazioni")
public class PrenotazioniController {

    private final PrenotazioniService prenotazioniService;

    public PrenotazioniController(PrenotazioniService prenotazioniService) {
        this.prenotazioniService = prenotazioniService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Prenotazione savePrenotazione(@RequestBody @Validated PrenotazioneDTO body,
                                         BindingResult validationResult,
                                         @AuthenticationPrincipal Utente currentAuthenticatedUser) {
        if (validationResult.hasErrors()) {
            List<String> errors = validationResult.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();
            throw new ValidationException(errors);
        }

        return this.prenotazioniService.save(body, currentAuthenticatedUser.getUtenteId());
    }
}
