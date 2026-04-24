package com.example.u5w3d5.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "eventi")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Evento {

    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    @Column(name = "evento_id")
    private UUID eventoId;
    @Column(nullable = false)
    private String titolo;
    @Column(nullable = false, length = 500)
    private String descrizione;
    @Column(nullable = false)
    private LocalDate data;
    @Column(nullable = false)
    private String luogo;
    @Column(name = "posti_disponibili", nullable = false)
    private int postiDisponibili;
    @ManyToOne
    @JoinColumn(name = "organizzatore_id", nullable = false)
    private Utente organizzatore;

    public Evento(String titolo, String descrizione, LocalDate data, String luogo, int postiDisponibili, Utente organizzatore) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.data = data;
        this.luogo = luogo;
        this.postiDisponibili = postiDisponibili;
        this.organizzatore = organizzatore;
    }
}
