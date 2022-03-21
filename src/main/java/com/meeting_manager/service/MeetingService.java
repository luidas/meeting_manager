package com.meeting_manager.service;

import com.meeting_manager.Storage;
import com.meeting_manager.model.Attendee;
import com.meeting_manager.model.Category;
import com.meeting_manager.model.Meeting;
import com.meeting_manager.model.Type;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class MeetingService {

    private final Storage storage;

    public MeetingService() throws IOException, ParseException {
        this.storage = new Storage();
    }

    public String saveMeeting(Meeting meeting) throws IOException,
            ParseException {
        Meeting meetingCreated = (Meeting) storage.saveObject(meeting);
        return "Meeting created: " + meetingCreated.toString();
    }

    public String deleteMeeting(String meetingName, String identity) throws IOException, ParseException {
        Meeting meetingToDelete = findMeetingByName(meetingName);
        if (meetingToDelete == null) {
            return "Meeting not found";
        }
        if (!meetingToDelete.getResponsiblePerson().equals(identity)) {
            return "You are not the responsible person";
        }
        storage.deleteObject(meetingToDelete);
        return "Meeting deleted successfully";
    }

    public String addAttendee(Attendee attendeeToAdd, String meetingName) throws IOException, ParseException {
        Meeting meetingToChange = findMeetingByName(meetingName);
        if (meetingToChange == null) {
            return "Meeting with name: " + meetingName + "does not exist";
        }
        if (meetingToChange.getAttendeeList().stream().anyMatch(
                a -> a.getName().equals(attendeeToAdd.getName()))) {
            return "Attendee " + attendeeToAdd.getName() + "is already in " +
                    "meeting " + meetingName;
        }
        List<Meeting> allMeetings = (List<Meeting>) (List<?>) storage.findAll(
                Meeting.class);
        int numOfOverlappingMeetings =
                (int) allMeetings.stream().map(
                        Meeting::getAttendeeList).flatMap(
                        Collection::stream).filter(
                        a -> a.getName().equals(attendeeToAdd.getName()) &&
                                !LocalDate.parse(a.getAttendingFrom()).isAfter(
                                        LocalDate.parse(
                                                attendeeToAdd.getAttendingTo())) &&
                                !LocalDate.parse(
                                        attendeeToAdd.getAttendingTo()).isBefore(
                                        LocalDate.parse(
                                                a.getAttendingFrom()))).count();
        if (numOfOverlappingMeetings > 0) {
            return "Attendee " + attendeeToAdd.getName() + " has overlapping " +
                    "meetings";
        }
        storage.deleteObject(meetingToChange);
        meetingToChange.getAttendeeList().add(attendeeToAdd);
        storage.saveObject(meetingToChange);
        return "Added attendee: " + attendeeToAdd.toString();
    }


    public void deleteAttendee(String meetingName, String attendeeToDelete) throws IOException, ParseException {
        Meeting meetingToChange = findMeetingByName(meetingName);
        if (meetingToChange != null) {
            List<Attendee> attendeeList = meetingToChange.getAttendeeList();
            for (Iterator<Attendee> it = attendeeList.iterator(); it.hasNext(); ) {
                Attendee attendee = it.next();
                String attendeeName = attendee.getName();
                if (attendeeName.equals(
                        attendeeToDelete) && !attendeeName.equals(
                        meetingToChange.getResponsiblePerson())) {
                    storage.deleteObject(meetingToChange);
                    it.remove();
                    storage.saveObject(meetingToChange);
                }
            }
        }
    }

    public Meeting findMeetingByName(String nameToFind) {
        List<Meeting> allMeetings =
                (List<Meeting>) (List<?>) storage.findAll(Meeting.class);
        Meeting meetingToReturn = null;
        for (Meeting meeting : allMeetings) {
            if (meeting.getName().equals(nameToFind)) {
                meetingToReturn = meeting;
            }
        }
        return meetingToReturn;
    }

    public List<Meeting> meetingQuery(String description,
                                      String responsiblePerson,
                                      Category category, Type type,
                                      String startDate,
                                      String endDate, Integer numOfAttendees) {
        List<Meeting> allMeetings =
                (List<Meeting>) (List<?>) storage.findAll(Meeting.class);
        return
                allMeetings.stream()
                        .filter(m -> {
                            LocalDate meetingStartDate =
                                    LocalDate.parse(m.getStartDate());
                            LocalDate meetingEndDate =
                                    LocalDate.parse(m.getEndDate());
                            return (description == null ||
                                    m.getDescription().toLowerCase().contains(
                                            description.toLowerCase())) &&
                                    (responsiblePerson == null ||
                                            m.getResponsiblePerson().equals(
                                                    responsiblePerson)) &&
                                    (category == null || m.getCategory().equals(
                                            category)) &&
                                    (type == null || m.getType().equals(
                                            type)) && (startDate == null ||
                                    meetingStartDate.isAfter(
                                            LocalDate.parse(
                                                    startDate)) ||
                                    meetingStartDate.isEqual(
                                            LocalDate.parse(
                                                    startDate))) &&
                                    (endDate == null || meetingEndDate.isBefore(
                                            LocalDate.parse(
                                                    endDate)) ||
                                            meetingEndDate.isEqual(
                                                    LocalDate.parse(
                                                            endDate))) &&
                                    (numOfAttendees == null ||
                                            m.getAttendeeList().size() >=
                                                    numOfAttendees);
                        })
                        .collect(Collectors.toList());
    }

    public boolean nameExists(String name) {
        List<Meeting> allMeetings =
                (List<Meeting>) (List<?>) storage.findAll(Meeting.class);
        List<String> allMeetingNames =
                allMeetings.stream().map(Meeting::getName).collect(
                        Collectors.toList());
        return allMeetingNames.contains(name);
    }
}
