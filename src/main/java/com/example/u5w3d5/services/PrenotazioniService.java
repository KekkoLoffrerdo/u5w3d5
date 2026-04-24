package com.example.u5w3d5.services;

import com.example.u5w3d5.entities.Evento;
import com.example.u5w3d5.entities.Prenotazione;
import com.example.u5w3d5.entities.Utente;
import com.example.u5w3d5.exceptions.BadRequestException;
import com.example.u5w3d5.exceptions.NotFoundException;
import com.example.u5w3d5.payloads.PrenotazioneDTO;
import com.example.u5w3d5.repositories.PrenotazioniRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@Slf4j
public class PrenotazioniService {

    private final PrenotazioniRepository prenotazioniRepository;
    private final EventiService eventiService;
    private final UtentiService utentiService;

    public PrenotazioniService(PrenotazioniRepository prenotazioniRepository, EventiService eventiService, UtentiService utentiService) {
        this.prenotazioniRepository = prenotazioniRepository;
        this.eventiService = eventiService;
        this.utentiService = utentiService;
    }
    public Prenotazione save(PrenotazioneDTO body, UUID utenteId) {
        Utente utente = this.utentiService.findById(utenteId);
        Evento evento = this.eventiService.findById(body.eventoId());

        if (evento.getOrganizzatore().getUtenteId().equals(utente.getUtenteId())) {
            throw new BadRequestException("Non puoi prenotare un evento creato da te");
        }

        if (this.prenotazioniRepository.existsByUtenteUtenteIdAndEventoEventoId(utenteId, evento.getEventoId())) {
            throw new BadRequestException("Hai già prenotato questo evento");
        }

        int numeroPrenotazioni = this.prenotazioniRepository.findByEventoEventoId(evento.getEventoId()).size();

        if (numeroPrenotazioni >= evento.getPostiDisponibili()) {
            throw new BadRequestException("Non ci sono più posti disponibili per questo evento");
        }

        Prenotazione newPrenotazione = new Prenotazione(LocalDate.now(), utente, evento);

        Prenotazione savedPrenotazione = this.prenotazioniRepository.save(newPrenotazione);

        log.info("La prenotazione con id " + savedPrenotazione.getPrenotazioneId() + " è stata salvata correttamente!");

        return savedPrenotazione;
    }
    public Prenotazione findById(UUID prenotazioneId) {
        return this.prenotazioniRepository.findById(prenotazioneId)
                .orElseThrow(() -> new NotFoundException(prenotazioneId));
    }
    public void findByIdAndDelete(UUID prenotazioneId, UUID utenteId) {
        Prenotazione found = this.findById(prenotazioneId);

        if (!found.getUtente().getUtenteId().equals(utenteId)) {
            throw new BadRequestException("Puoi eliminare solo le tue prenotazioni");
        }

        this.prenotazioniRepository.delete(found);

        log.info("La prenotazione con id " + prenotazioneId + " è stata eliminata correttamente!");
    }
}
