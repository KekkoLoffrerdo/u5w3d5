package com.example.u5w3d5.services;

import com.example.u5w3d5.entities.Evento;
import com.example.u5w3d5.entities.Ruolo;
import com.example.u5w3d5.entities.Utente;
import com.example.u5w3d5.exceptions.BadRequestException;
import com.example.u5w3d5.exceptions.NotFoundException;
import com.example.u5w3d5.payloads.EventoDTO;
import com.example.u5w3d5.repositories.EventiRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.UUID;

@Service
@Slf4j
public class EventiService {

    private final EventiRepository eventiRepository;
    private final UtentiService utentiService;

    public EventiService(EventiRepository eventiRepository, UtentiService utentiService) {
        this.eventiRepository = eventiRepository;
        this.utentiService = utentiService;
    }
    public Evento save(EventoDTO body, UUID organizzatoreId) {
        Utente organizzatore = this.utentiService.findById(organizzatoreId);

        if (!organizzatore.getRuolo().equals(Ruolo.ORGANIZZATORE)) {
            throw new BadRequestException("Solo gli organizzatori possono creare eventi");
        }

        Evento newEvento = new Evento(
                body.titolo(),
                body.descrizione(),
                body.data(),
                body.luogo(),
                body.postiDisponibili(),
                organizzatore
        );

        Evento savedEvento = this.eventiRepository.save(newEvento);

        log.info("L'evento con id " + savedEvento.getEventoId() + " è stato salvato correttamente!");

        return savedEvento;
    }
    public Evento findById(UUID eventoId) {
        return this.eventiRepository.findById(eventoId)
                .orElseThrow(() -> new NotFoundException(eventoId));
    }
    public Evento findByIdAndUpdate(UUID eventoId, EventoDTO body, UUID organizzatoreId) {
        Evento found = this.findById(eventoId);

        if (!found.getOrganizzatore().getUtenteId().equals(organizzatoreId)) {
            throw new BadRequestException("Puoi modificare solo gli eventi creati da te");
        }

        found.setTitolo(body.titolo());
        found.setDescrizione(body.descrizione());
        found.setData(body.data());
        found.setLuogo(body.luogo());
        found.setPostiDisponibili(body.postiDisponibili());

        Evento updatedEvento = this.eventiRepository.save(found);

        log.info("L'evento con id " + updatedEvento.getEventoId() + " è stato modificato correttamente!");

        return updatedEvento;
    }
    public void findByIdAndDelete(UUID eventoId, UUID organizzatoreId) {
        Evento found = this.findById(eventoId);

        if (!found.getOrganizzatore().getUtenteId().equals(organizzatoreId)) {
            throw new BadRequestException("Puoi eliminare solo gli eventi creati da te");
        }

        this.eventiRepository.delete(found);

        log.info("L'evento con id " + eventoId + " è stato eliminato correttamente!");
    }
    public Page<Evento> findAll(int page, int size, String sortBy) {
        if (size > 100 || size < 0) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.eventiRepository.findAll(pageable);
    }
}
