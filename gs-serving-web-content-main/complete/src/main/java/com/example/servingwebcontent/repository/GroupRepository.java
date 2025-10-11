package com.example.servingwebcontent.repository;

import com.example.servingwebcontent.model.Group;
import java.util.*;
import org.springframework.stereotype.Repository;

@Repository
public class GroupRepository {
    private List<Group> groups = new ArrayList<>();

    public List<Group> findAll() {
        return groups;
    }

    public void save(Group group) {
        groups.add(group);
    }

    public Group findById(Long id) {
        for (Group g : groups) {
            if (g.getId().equals(id)) {
                return g;
            }
        }
        return null;
    }

    public boolean update(Long id, Group newGroup) {
        Group group = findById(id);
        if (group != null) {
            group.setName(newGroup.getName());
            group.setDescription(newGroup.getDescription());
            group.setMembers(newGroup.getMembers());
            group.setFund(newGroup.getFund());
            return true;
        }
        return false;
    }

    public boolean delete(Long id) {
        Group group = findById(id);
        if (group != null) {
            groups.remove(group);
            return true;
        }
        return false;
    }
}
