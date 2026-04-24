package com.example.u5w3d5.controllers;

import com.example.u5w3d5.entities.Evento;
import com.example.u5w3d5.entities.Utente;
import com.example.u5w3d5.exceptions.ValidationException;
import com.example.u5w3d5.payloads.EventoDTO;
import com.example.u5w3d5.services.EventiService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/eventi")
public class EventiController {

    private final EventiService eventiService;

    public EventiController(EventiService eventiService) {
        this.eventiService = eventiService;
    }

    @GetMapping
    public Page<Evento> getEventi(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(defaultValue = "data") String sortBy) {
        return this.eventiService.findAll(page, size, sortBy);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ORGANIZZATORE')")
    public Evento saveEvento(@RequestBody @Validated EventoDTO body,
                             BindingResult validationResult,
                             @AuthenticationPrincipal Utente currentAuthenticatedUser) {
        if (validationResult.hasErrors()) {
            List<String> errors = validationResult.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();
            throw new ValidationException(errors);
        }

        return this.eventiService.save(body, currentAuthenticatedUser.getUtenteId());
    }

    @PutMapping("/{eventoId}")
    @PreAuthorize("hasAuthority('ORGANIZZATORE')")
    public Evento updateEvento(@PathVariable UUID eventoId,
                               @RequestBody @Validated EventoDTO body,
                               BindingResult validationResult,
                               @AuthenticationPrincipal Utente currentAuthenticatedUser) {
        if (validationResult.hasErrors()) {
            List<String> errors = validationResult.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();
            throw new ValidationException(errors);
        }

        return this.eventiService.findByIdAndUpdate(eventoId, body, currentAuthenticatedUser.getUtenteId());
    }

    @DeleteMapping("/{eventoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ORGANIZZATORE')")
    public void deleteEvento(@PathVariable UUID eventoId,
                             @AuthenticationPrincipal Utente currentAuthenticatedUser) {
        this.eventiService.findByIdAndDelete(eventoId, currentAuthenticatedUser.getUtenteId());
    }
}
