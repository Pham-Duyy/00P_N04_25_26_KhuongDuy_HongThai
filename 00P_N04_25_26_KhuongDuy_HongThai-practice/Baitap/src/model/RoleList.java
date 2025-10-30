package model;

public class RoleList {
    private Role[] roles;
    private int count;

    // Constructor
    public RoleList(int capacity) {
        roles = new Role[capacity];
        count = 0;
    }

    // Thêm vai trò mới vào danh sách
    public void addRole(Role role) {
        if (count < roles.length) {
            roles[count++] = role;
        } else {
            System.out.println("Role list is full!");
        }
    }

    // Hiển thị tất cả các vai trò trong danh sách
    public void displayAllRoles() {
        for (int i = 0; i < count; i++) {
            roles[i].displayInfo();
            System.out.println("-------------------");
        }
    }

    // Tìm kiếm vai trò theo tên
    public Role findRoleByName(String roleName) {
        for (int i = 0; i < count; i++) {
            if (roles[i].getRoleName().equalsIgnoreCase(roleName)) {
                return roles[i];
            }
        }
        return null; // Không tìm thấy vai trò
    }
}
