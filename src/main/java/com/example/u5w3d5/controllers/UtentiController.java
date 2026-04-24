package com.example.u5w3d5.controllers;

import com.example.u5w3d5.entities.Prenotazione;
import com.example.u5w3d5.entities.Utente;
import com.example.u5w3d5.services.PrenotazioniService;
import com.example.u5w3d5.services.UtentiService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/utenti")
public class UtentiController {

    private final UtentiService utentiService;
    private final PrenotazioniService prenotazioniService;

    public UtentiController(UtentiService utentiService, PrenotazioniService prenotazioniService) {
        this.utentiService = utentiService;
        this.prenotazioniService = prenotazioniService;
    }
    @GetMapping("/me")
    public Utente getOwnProfile(@AuthenticationPrincipal Utente currentAuthenticatedUser) {
        return currentAuthenticatedUser;
    }
    @GetMapping("/me/prenotazioni")
    public Page<Prenotazione> getOwnPrenotazioni(@AuthenticationPrincipal Utente currentAuthenticatedUser,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size,
                                                 @RequestParam(defaultValue = "dataPrenotazione") String sortBy) {
        return this.utentiService.findPrenotazioniByUtente(currentAuthenticatedUser.getUtenteId(), page, size, sortBy);
    }
    @DeleteMapping("/me/prenotazioni/{prenotazioneId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOwnPrenotazione(@PathVariable UUID prenotazioneId,
                                      @AuthenticationPrincipal Utente currentAuthenticatedUser) {
        this.prenotazioniService.findByIdAndDelete(prenotazioneId, currentAuthenticatedUser.getUtenteId());
    }
}
