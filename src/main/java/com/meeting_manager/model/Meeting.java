package com.meeting_manager.model;

import java.util.List;


public class Meeting {

    private String name;
    private String responsiblePerson;
    private String description;
    private Category category;
    private Type type;
    private String startDate;
    private String endDate;
    private List<Attendee> attendeeList;

    public Meeting(String name, String responsiblePerson, String description,
                   Category category, Type type, String startDate,
                   String endDate, List<Attendee> attendeeList) {
        this.name = name;
        this.responsiblePerson = responsiblePerson;
        this.description = description;
        this.category = category;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.attendeeList = attendeeList;
    }

    public boolean hasEmptyFields() {
        return this.name == null || this.responsiblePerson == null ||
                this.description == null || this.category == null ||
                this.type == null || this.startDate == null ||
                this.endDate == null || this.attendeeList == null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResponsiblePerson() {
        return responsiblePerson;
    }

    public void setResponsiblePerson(String responsiblePerson) {
        this.responsiblePerson = responsiblePerson;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public List<Attendee> getAttendeeList() {
        return attendeeList;
    }

    public void setAttendeeList(List<Attendee> attendeeList) {
        this.attendeeList = attendeeList;
    }

    @Override
    public String toString() {
        return "Meeting{" +
                "name='" + name + '\'' +
                ", responsiblePerson='" + responsiblePerson + '\'' +
                ", description='" + description + '\'' +
                ", category=" + category +
                ", type=" + type +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", attendeeList=" + attendeeList +
                '}';
    }
}
