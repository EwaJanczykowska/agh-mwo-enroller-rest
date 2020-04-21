package com.company.enroller.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.company.enroller.model.Participant;
import com.company.enroller.persistence.ParticipantService;

@RestController
@RequestMapping("/participants")
public class ParticipantRestController {

    @Autowired
    ParticipantService participantService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getParticipants() {
        Collection<Participant> participants = participantService.getAll();
        return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public ResponseEntity<?> getParticipant(@PathVariable("userId") String userLogin) {
        Participant participant = participantService.findByLogin(userLogin);
        if (participant == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Participant>(participant, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> registerParticipant(@RequestBody Participant participant) {
        Participant foundParticipant = participantService.findByLogin(participant.getLogin());
        if (foundParticipant != null) {
            return new ResponseEntity<String>("Unable to create. A participant with login " + participant.getLogin() + " already exist.", HttpStatus.CONFLICT);
        }
        participantService.add(participant);
        return new ResponseEntity<Participant>(participant, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteParticipant(@PathVariable("userId") String login) {
        Participant participant = participantService.findByLogin(login);
        if (participant == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        participantService.delete(participant);
        return new ResponseEntity<Participant>(participant, HttpStatus.OK);
        //return new ResponseEntity<Participant>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    public ResponseEntity<?> updatePassword(@RequestBody Participant participant) {
        Participant existingParticipant = participantService.findByLogin(participant.getLogin());
        if (existingParticipant == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        existingParticipant.setPassword(participant.getPassword());
        participantService.update(existingParticipant);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
