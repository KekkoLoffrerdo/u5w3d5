package com.example.u5w3d5.services;

import com.example.u5w3d5.entities.Utente;
import com.example.u5w3d5.exceptions.NotFoundException;
import com.example.u5w3d5.exceptions.UnauthorizedException;
import com.example.u5w3d5.payloads.LoginDTO;
import com.example.u5w3d5.security.TokenTools;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UtentiService utentiService;
    private final TokenTools tokenTools;
    private final PasswordEncoder bcrypt;

    public AuthService(UtentiService utentiService, TokenTools tokenTools, PasswordEncoder bcrypt) {
        this.utentiService = utentiService;
        this.tokenTools = tokenTools;
        this.bcrypt = bcrypt;
    }

    public String checkCredentialsAndGenerateToken(LoginDTO body) {
        try {
            Utente found = this.utentiService.findByEmail(body.email());

            if (this.bcrypt.matches(body.password(), found.getPassword())) {
                return this.tokenTools.generateToken(found.getUtenteId());
            } else {
                throw new UnauthorizedException("Credenziali errate");
            }

        } catch (NotFoundException ex) {
            throw new UnauthorizedException("Credenziali errate");
        }
    }
}