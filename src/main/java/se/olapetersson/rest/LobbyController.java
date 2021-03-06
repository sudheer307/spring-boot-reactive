package se.olapetersson.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import se.olapetersson.entities.Lobby;
import se.olapetersson.repositories.LobbyRepository;

import java.util.List;

/**
 * Created by ola on 2015-11-11.
 */
@RestController
@RequestMapping(value = "/lobbies")
public class LobbyController {
    @Autowired
    LobbyRepository repository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @RequestMapping(method = RequestMethod.GET)
    public List<Lobby> getLobbies() {
        return repository.findAll();
    }

    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    public void createLobby(@PathVariable String name) {
        repository.save(new Lobby(name));
        messagingTemplate.convertAndSend("/topic/lobbies", repository.findAll());
    }

    @RequestMapping(value = "/{name}/clear", method = RequestMethod.GET)
    public void deleteLobby(@PathVariable String name) {
        repository.deleteByName(name);
        messagingTemplate.convertAndSend("/topic/lobbies", repository.findAll());
    }
}
