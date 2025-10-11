package com.example.servingwebcontent.service;

import com.example.servingwebcontent.model.Role;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private List<Role> roles = new ArrayList<>();

    public List<Role> getAllRoles() {
        return roles;
    }

    public Role addRole(Role role) {
        roles.add(role);
        return role;
    }

    public Role updateRole(Long id, Role newRole) {
        for (Role role : roles) {
            if (role.getId().equals(id)) {
                role.setName(newRole.getName());
                role.setDescription(newRole.getDescription());
                return role;
            }
        }
        return null;
    }

    public boolean deleteRole(Long id) {
        return roles.removeIf(r -> r.getId().equals(id));
    }

    public Role findRoleById(Long id) {
        for (Role r : roles) {
            if (r.getId().equals(id)) {
                return r;
            }
        }
        return null;
    }
}
