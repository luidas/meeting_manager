package com.meeting_manager.controller;

import com.meeting_manager.model.Attendee;
import com.meeting_manager.model.Meeting;
import com.meeting_manager.service.MeetingService;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/meetings")
public class MeetingController {

    private final MeetingService meetingService;

    public MeetingController() throws IOException, ParseException {
        this.meetingService = new MeetingService();
    }


    // create a new meeting
    @PostMapping()
    public ResponseEntity<Meeting> saveMeeting(
            @RequestBody Meeting meeting) throws IOException, ParseException {
        return new ResponseEntity<>(meetingService.saveMeeting(meeting),
                HttpStatus.CREATED);
    }

    // delete a meeting
    @DeleteMapping("/{meetingName}/{identity}")
    public ResponseEntity<String> deleteMeeting(
            @PathVariable("meetingName") String meetingName,
            @PathVariable("identity") String identity) throws IOException,
            ParseException, ClassNotFoundException {

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
    public ResponseEntity<Attendee> addAttendee(
            @RequestBody Attendee attendeeToAdd,
            @PathVariable("meetingName") String meetingName) throws ClassNotFoundException, IOException, ParseException {
        return new ResponseEntity<>(meetingService.addAttendee(attendeeToAdd,
                meetingName),
                HttpStatus.CREATED);
    }

    // add a person to meeting

    // remove person from meeting

    // list all meetings
//    @GetMapping()
//    public List<Meeting> getAllMeetings() {
//        List meetingList = new ArrayList();
//        for (Object meetingJSON : storage.getJson()) {
//            meetingList.add(new Gson().fromJson(
//                    ((JSONObject) meetingJSON).toJSONString(), Meeting
//                    .class));
//        }
//        return meetingList;
//    }

//    @GetMapping("/query")
//    public List<Meeting> getMeetingsQuery(
//            @RequestParam(required = false) String description,
//            @RequestParam(required = false) String responsiblePerson,
//            @RequestParam(required = false) String category,
//            @RequestParam(required = false) String type,
//            @RequestParam(required = false) String startDate,
//            @RequestParam(required = false) String endDate,
//            @RequestParam(required = false) String numOfAtendees) {
//        return meetingService.meetingQuery();
//    }

}
