package com.meeting_manager.model;

public class Attendee {
    private String name;
    private String attendingFrom;
    private String attendingTo;

    public Attendee(String name, String attendingFrom, String attendingTo) {
        this.name = name;
        this.attendingFrom = attendingFrom;
        this.attendingTo = attendingTo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttendingFrom() {
        return attendingFrom;
    }

    public void setAttendingFrom(String attendingFrom) {
        this.attendingFrom = attendingFrom;
    }

    public String getAttendingTo() {
        return attendingTo;
    }

    public void setAttendingTo(String attendingTo) {
        this.attendingTo = attendingTo;
    }
}
