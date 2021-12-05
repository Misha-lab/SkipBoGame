package game.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ClientList implements Serializable {
    private ArrayList<ClientThread> currentClients;
    public ClientList() {
        currentClients = new ArrayList<>();
    }

    public synchronized boolean isOnline(String name) {
        for(ClientThread clientThread: currentClients) {
            if(clientThread.getUsername().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public synchronized ClientThread get(String name) {
        for(ClientThread client : currentClients) {
            if(client.getUsername().equals(name)) {
                return client;
            }
        }
        return null;
    }

    public int size() { return currentClients.size(); }

    public synchronized void add(ClientThread client) {
        currentClients.add(client);
    }

    public synchronized void remove(ClientThread client) {
        currentClients.remove(client);
    }

    public ArrayList<ClientThread> getList() {
        return currentClients;
    }
}

