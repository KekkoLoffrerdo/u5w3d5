package com.example.u5w3d5.repositories;

import com.example.u5w3d5.entities.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PrenotazioniRepository extends JpaRepository<Prenotazione, UUID> {
    List<Prenotazione> findByEventoEventoId(UUID eventoId);
    boolean existsByUtenteUtenteIdAndEventoEventoId(UUID utenteId, UUID eventoId);
}
