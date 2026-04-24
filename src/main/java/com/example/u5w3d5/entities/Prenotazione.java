package com.example.u5w3d5.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "prenotazioni")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Prenotazione {

    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    @Column(name = "prenotazione_id")
    private UUID prenotazioneId;
    @Column(name = "data_prenotazione", nullable = false)
    private LocalDate dataPrenotazione;
    @ManyToOne
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente utente;
    @ManyToOne
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    public Prenotazione(LocalDate dataPrenotazione, Utente utente, Evento evento) {
        this.dataPrenotazione = dataPrenotazione;
        this.utente = utente;
        this.evento = evento;
    }
}
