package ru.otus.service;

import ru.otus.dto.ClientDto;

import java.util.List;

public interface ClientService {

    boolean saveClient(ClientDto client);

    ClientDto getClient(long id);

    List<ClientDto> findAll();
}
