package com.example.servingwebcontent.service;

import com.example.servingwebcontent.model.Group;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class GroupService {
    private List<Group> groups = new ArrayList<>();

    public List<Group> getAllGroups() {
        return groups;
    }

    public Group addGroup(Group group) {
        groups.add(group);
        return group;
    }

    public Group updateGroup(Long id, Group newGroup) {
        for (Group group : groups) {
            if (group.getId().equals(id)) {
                group.setName(newGroup.getName());
                group.setDescription(newGroup.getDescription());
                group.setMembers(newGroup.getMembers());
                group.setFund(newGroup.getFund());
                return group;
            }
        }
        return null;
    }

    public boolean deleteGroup(Long id) {
        return groups.removeIf(g -> g.getId().equals(id));
    }

    public Group findGroupById(Long id) {
        for (Group g : groups) {
            if (g.getId().equals(id)) {
                return g;
            }
        }
        return null;
    }
}
