package com.circule.talent.service;

import com.circule.talent.dto.clients.ClientCreateDTO;
import com.circule.talent.dto.clients.ClientDTO;
import com.circule.talent.dto.clients.ClientUpdateDTO;
import com.circule.talent.exception.ResourceNotFoundException;
import com.circule.talent.mapper.ClientMapper;
import com.circule.talent.repository.ClientRepository;
import com.circule.talent.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    private final ClientMapper clientMapper;

    private final RoleRepository roleRepository;

    public List<ClientDTO> index() {
        return clientMapper.map(clientRepository.findAll());
    }

    public ClientDTO show(Long id) {
        var talent =  clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client with id " + id + " not found"));
        return clientMapper.map(talent);
    }

    public ClientDTO create(ClientCreateDTO clientData) {
        var client = clientMapper.map(clientData);
        client.setRoles(Arrays.asList(roleRepository.findByName("ROLE_CLIENT")));
        clientRepository.save(client);
        return clientMapper.map(client);
    }

    public ClientDTO update(ClientUpdateDTO clientData, Long id) {
        var client =  clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client with id " + id + " not found"));
        clientMapper.update(clientData, client);
        clientRepository.save(client);
        return clientMapper.map(client);
    }

    public void delete(Long id) {
        clientRepository.deleteById(id);
    }
}
