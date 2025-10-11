package com.example.servingwebcontent.repository;

import com.example.servingwebcontent.model.Role;
import java.util.*;
import org.springframework.stereotype.Repository;

@Repository
public class RoleRepository {
    private List<Role> roles = new ArrayList<>();

    public List<Role> findAll() {
        return roles;
    }

    public void save(Role role) {
        roles.add(role);
    }

    public Role findById(Long id) {
        for (Role r : roles) {
            if (r.getId().equals(id)) {
                return r;
            }
        }
        return null;
    }

    public boolean update(Long id, Role newRole) {
        Role role = findById(id);
        if (role != null) {
            role.setName(newRole.getName());
            role.setDescription(newRole.getDescription());
            return true;
        }
        return false;
    }

    public boolean delete(Long id) {
        Role role = findById(id);
        if (role != null) {
            roles.remove(role);
            return true;
        }
        return false;
    }
}
