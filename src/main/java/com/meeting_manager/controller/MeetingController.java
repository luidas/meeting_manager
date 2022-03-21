package com.meeting_manager.controller;

import com.meeting_manager.model.Attendee;
import com.meeting_manager.model.Category;
import com.meeting_manager.model.Meeting;
import com.meeting_manager.model.Type;
import com.meeting_manager.service.MeetingService;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/meetings")
public class MeetingController {

    private final MeetingService meetingService;

    public MeetingController() throws IOException, ParseException {
        this.meetingService = new MeetingService();
    }


    // create a new meeting
    @PostMapping()
    public ResponseEntity<String> saveMeeting(
            @RequestBody Meeting meeting) throws IOException, ParseException {
        if (meeting.hasEmptyFields()) {
            return new ResponseEntity<>("Meeting has empty fields",
                    HttpStatus.NOT_FOUND);
        }
        if (meetingService.nameExists(meeting.getName())) {
            return new ResponseEntity<>("Name already taken",
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(meetingService.saveMeeting(meeting),
                HttpStatus.CREATED);
    }

    // delete a meeting
    @DeleteMapping("/{meetingName}/{identity}")
    public ResponseEntity<String> deleteMeeting(
            @PathVariable("meetingName") String meetingName,
            @PathVariable("identity") String identity) throws IOException,
            ParseException {

        String response = meetingService.deleteMeeting(meetingName, identity);
        HttpStatus responseStatus;

        if (response.equals("Meeting not found")) {
            responseStatus = HttpStatus.NOT_FOUND;
        } else if (response.equals("You are not the responsible person")) {
            responseStatus = HttpStatus.FORBIDDEN;
        } else {
            responseStatus = HttpStatus.OK;
        }

        return new ResponseEntity<>(response, responseStatus);
    }

    @PostMapping("/{meetingName}/attendees")
    public ResponseEntity<String> addAttendee(
            @RequestBody Attendee attendeeToAdd,
            @PathVariable("meetingName") String meetingName) throws IOException, ParseException {
        return new ResponseEntity<>(meetingService.addAttendee(attendeeToAdd,
                meetingName),
                HttpStatus.CREATED);
    }


    @DeleteMapping("/{meetingName}/attendees/{attendeeName}")
    public ResponseEntity<String> deleteAttendee(
            @PathVariable("meetingName") String meetingName,
            @PathVariable("attendeeName") String attendeeName) throws IOException, ParseException {
        meetingService.deleteAttendee(meetingName, attendeeName);
        return new ResponseEntity<>("Attendee deleted", HttpStatus.OK);
    }

    @GetMapping("/query")
    public List<Meeting> getMeetingsQuery(
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String responsiblePerson,
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) Type type,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Integer numOfAttendees) {
        return meetingService.meetingQuery(description,
                responsiblePerson,
                category, type, startDate, endDate, numOfAttendees);

    }

}
