package ru.otus.dao;

import ru.otus.model.Client;
import ru.otus.service.DbServiceClientImpl;

import java.util.List;

public class ClientDaoImpl implements ClientDao {

    private final DbServiceClientImpl dbServiceClient;


    public ClientDaoImpl(DbServiceClientImpl dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    public Client save(Client client) {
        return dbServiceClient.saveClient(client);
    }

    @Override
    public List<Client> findAll() {
        return dbServiceClient.findAll();
    }
}
