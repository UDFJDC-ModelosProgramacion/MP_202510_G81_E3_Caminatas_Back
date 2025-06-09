package co.edu.udistrital.mdp.caminatas.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Controlador por defecto", description = "Mapeo y estado del servidor")
@RestController
@RequestMapping("/")
public class DefaultController {

    @Operation(summary = "Mostrar el estado del servidor", description = ":)")
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public Map<String, String> welcome() {
        HashMap<String, String> map = new HashMap<>();
        map.put("status", "OK");
        map.put("message", "REST API for caminatas is running");
        return map;
    }
}
