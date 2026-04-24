package com.example.u5w3d5.repositories;

import com.example.u5w3d5.entities.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EventiRepository extends JpaRepository<Evento, UUID> {
}
