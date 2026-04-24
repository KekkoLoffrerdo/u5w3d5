package com.example.u5w3d5.services;

import com.example.u5w3d5.entities.Prenotazione;
import com.example.u5w3d5.entities.Ruolo;
import com.example.u5w3d5.entities.Utente;
import com.example.u5w3d5.exceptions.BadRequestException;
import com.example.u5w3d5.exceptions.NotFoundException;
import com.example.u5w3d5.payloads.UtenteDTO;
import com.example.u5w3d5.repositories.PrenotazioniRepository;
import com.example.u5w3d5.repositories.UtentiRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class UtentiService {

    private final UtentiRepository utentiRepository;
    private final PasswordEncoder bcrypt;
    private final PrenotazioniRepository prenotazioniRepository;

    public UtentiService(UtentiRepository utentiRepository, PasswordEncoder bcrypt, PrenotazioniRepository prenotazioniRepository) {
        this.utentiRepository = utentiRepository;
        this.bcrypt = bcrypt;
        this.prenotazioniRepository = prenotazioniRepository;
    }

    public Utente save(UtenteDTO body) {
        if (this.utentiRepository.existsByEmail(body.email())) {
            throw new BadRequestException("L'indirizzo email " + body.email() + " è già in uso!");
        }

        Ruolo ruolo;

        if (body.ruolo().equalsIgnoreCase("utente")) {
            ruolo = Ruolo.UTENTE;
        } else if (body.ruolo().equalsIgnoreCase("organizzatore")) {
            ruolo = Ruolo.ORGANIZZATORE;
        } else {
            throw new BadRequestException("Il ruolo può essere UTENTE oppure ORGANIZZATORE");
        }

        Utente newUtente = new Utente(
                body.nome(),
                body.cognome(),
                body.email(),
                this.bcrypt.encode(body.password()),
                body.dataNascita(),
                ruolo
        );

        Utente savedUtente = this.utentiRepository.save(newUtente);
        log.info("L'utente con id " + savedUtente.getUtenteId() + " è stato salvato correttamente!");
        return savedUtente;
    }

    public Utente findById(UUID utenteId) {
        return this.utentiRepository.findById(utenteId)
                .orElseThrow(() -> new NotFoundException(utenteId));
    }

    public Utente findByEmail(String email) {
        return this.utentiRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("L'utente con email " + email + " non è stato trovato!"));
    }

    public Page<Prenotazione> findPrenotazioniByUtente(UUID utenteId, int page, int size, String sortBy) {
        if (size > 100 || size < 0) size = 10;
        if (page < 0) page = 0;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.prenotazioniRepository.findByUtenteUtenteId(utenteId, pageable);
    }
}