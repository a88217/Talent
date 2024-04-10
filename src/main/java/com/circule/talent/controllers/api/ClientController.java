package com.circule.talent.controllers.api;

import com.circule.talent.dto.clients.ClientCreateDTO;
import com.circule.talent.dto.clients.ClientDTO;
import com.circule.talent.dto.clients.ClientUpdateDTO;
import com.circule.talent.service.ClientService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@AllArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping(path = "")
    public ResponseEntity<List<ClientDTO>> index() {
        var result = clientService.index();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(result.size()))
                .body(result);
    }

    @GetMapping(path = "/{id}")
    public ClientDTO show(@PathVariable long id) {
        return clientService.show(id);
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public ClientDTO create(@Valid @RequestBody ClientCreateDTO clientData) {
        return clientService.create(clientData);
    }

    @PutMapping(path = "/{id}")
    public ClientDTO update(@Valid @RequestBody ClientUpdateDTO clientData, @PathVariable Long id) {
        return clientService.update(clientData, id);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        clientService.delete(id);
    }
}