package com.company.enroller.controllers;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashSet;

@RestController
@RequestMapping("/meetings")
public class MeetingRestController {

    @Autowired
    MeetingService meetingService;

    @Autowired
    ParticipantService participantService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetings() {
        Collection<Meeting> meetings = meetingService.getAll();
        return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
    }

    @RequestMapping(value = "/{meetingId}", method = RequestMethod.GET)
    public ResponseEntity<?> getMeeting(@PathVariable("meetingId") long meetingId) {
        Meeting meeting = meetingService.findById(meetingId);
        if (meeting == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> registerMeeting(@RequestBody Meeting meeting) {
        Meeting foundMeeting = meetingService.findById(meeting.getId());
        if (foundMeeting != null) {
            return new ResponseEntity<String>("Unable to create. A meeting with id: " + meeting.getId() + " already exist.", HttpStatus.CONFLICT);
        }
        meetingService.add(meeting);
        return new ResponseEntity<Meeting>(meeting, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{meetingId}/participants", method = RequestMethod.POST)
    public ResponseEntity<?> registerParticipantToMeeting(@PathVariable("meetingId") long meetingId, @RequestBody Participant participant) {
        Meeting meeting = meetingService.findById(meetingId);
        if (meeting == null) {
            return new ResponseEntity<String>("A meeting with id: " + meetingId + " doesn't exist.", HttpStatus.NOT_FOUND);
        }

        Participant existingParticipant = participantService.findByLogin(participant.getLogin());
        if (existingParticipant == null) {
            return new ResponseEntity<String>("A participant doesn't exist.", HttpStatus.NOT_FOUND);
        }

        if (meeting.getParticipants().contains(participant)) {
            return new ResponseEntity<String>("Participant is already added to meeting with id" + meeting.getId(), HttpStatus.CONFLICT);
        }

        meeting.addParticipant(participant);
        meetingService.updateMeeting(meeting);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{meetingId}/participants", method = RequestMethod.GET)
    public ResponseEntity<?> getParticipants(@PathVariable("meetingId") long meetingId) {
        Meeting meeting = meetingService.findById(meetingId);
        if (meeting == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Collection<Participant> participants = meeting.getParticipants();
        return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
    }







}

