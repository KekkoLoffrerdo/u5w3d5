package com.example.u5w3d5.repositories;

import com.example.u5w3d5.entities.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface PrenotazioniRepository extends JpaRepository<Prenotazione, UUID> {
    List<Prenotazione> findByEventoEventoId(UUID eventoId);
    boolean existsByUtenteUtenteIdAndEventoEventoId(UUID utenteId, UUID eventoId);
    Page<Prenotazione> findByUtenteUtenteId(UUID utenteId, Pageable pageable);
}
