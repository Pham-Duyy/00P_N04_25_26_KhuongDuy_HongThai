package com.example.servingwebcontent.model;

import java.util.List;

public class Group {
    private Long id;
    private String name;
    private String description;
    private List<Member> members;
    private Fund fund;

    public Group() {}

    public Group(Long id, String name, String description, List<Member> members, Fund fund) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.members = members;
        this.fund = fund;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public Fund getFund() {
        return fund;
    }

    public void setFund(Fund fund) {
        this.fund = fund;
    }
}
