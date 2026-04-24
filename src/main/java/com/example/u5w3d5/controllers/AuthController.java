package com.example.u5w3d5.controllers;

import com.example.u5w3d5.entities.Utente;
import com.example.u5w3d5.exceptions.ValidationException;
import com.example.u5w3d5.payloads.LoginDTO;
import com.example.u5w3d5.payloads.LoginResponseDTO;
import com.example.u5w3d5.payloads.UtenteDTO;
import com.example.u5w3d5.services.AuthService;
import com.example.u5w3d5.services.UtentiService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UtentiService utentiService;

    public AuthController(AuthService authService, UtentiService utentiService) {
        this.authService = authService;
        this.utentiService = utentiService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Utente register(@RequestBody @Validated UtenteDTO body, BindingResult validationResult) {

        if (validationResult.hasErrors()) {
            List<String> errors = validationResult.getFieldErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();

            throw new ValidationException(errors);
        }

        return this.utentiService.save(body);
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody @Validated LoginDTO body, BindingResult validationResult) {

        if (validationResult.hasErrors()) {
            List<String> errors = validationResult.getFieldErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();

            throw new ValidationException(errors);
        }

        String token = this.authService.checkCredentialsAndGenerateToken(body);

        return new LoginResponseDTO(token);
    }
}
