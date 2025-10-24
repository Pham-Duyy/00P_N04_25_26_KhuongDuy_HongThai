package model;

public class Role {
    private String roleName;
    private String description;

    // Constructor
    public Role(String roleName, String description) {
        this.roleName = roleName;
        this.description = description;
    }

    // Hiển thị thông tin vai trò
    public void displayInfo() {
        System.out.println("Role Name: " + roleName);
        System.out.println("Description: " + description);
    }

    // Getter cho roleName
    public String getRoleName() {
        return roleName;
    }

    // Setter cho roleName
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    // Getter cho description
    public String getDescription() {
        return description;
    }

    // Setter cho description
    public void setDescription(String description) {
        this.description = description;
    }
}
