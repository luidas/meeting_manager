package com.meeting_manager.service;

import com.meeting_manager.Storage;
import com.meeting_manager.model.Attendee;
import com.meeting_manager.model.Meeting;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MeetingService {

    private final Storage storage;

    public MeetingService() throws IOException, ParseException {
        this.storage = new Storage();
    }

    public Meeting saveMeeting(Meeting meeting) throws IOException,
            ParseException {
        return (Meeting) storage.saveObject(meeting);
    }

    public String deleteMeeting(String meetingName, String identity) throws ClassNotFoundException, IOException, ParseException {
        Meeting meetingToDelete = (Meeting) storage.findByField("name",
                meetingName,
                Meeting.class);
        if (meetingToDelete == null) {
            return "Meeting not found";
        }
        if (!meetingToDelete.getResponsiblePerson().equals(identity)) {
            return "You are not the responsible person";
        }
        storage.deleteObject(meetingToDelete);
        return "Meeting deleted successfully";
    }

    public Attendee addAttendee(Attendee attendeeToAdd, String meetingName) throws ClassNotFoundException, IOException, ParseException {
        Meeting meetingTochange = (Meeting) storage.findByField("name",
                meetingName, Meeting.class);
        if (meetingTochange == null) {
            List<Attendee> listWithAttendee =
                    new ArrayList<>(Arrays.asList(attendeeToAdd));
            meetingTochange = new Meeting(meetingName,
                    attendeeToAdd.getName(), null,
                    null, null
                    , null, null, listWithAttendee);
        } else {
            storage.deleteObject(meetingTochange);
            meetingTochange.getAttendeeList().add(attendeeToAdd);
        }
        storage.saveObject(meetingTochange);
        return attendeeToAdd;
    }
}
