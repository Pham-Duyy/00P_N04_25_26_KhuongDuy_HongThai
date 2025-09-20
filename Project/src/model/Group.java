package model;

public class Group {
    private String groupName;
    private String description;

    // Constructor
    public Group(String groupName, String description) {
        this.groupName = groupName;
        this.description = description;
    }

    // Hiển thị thông tin nhóm
    public void displayInfo() {
        System.out.println("Group Name: " + groupName);
        System.out.println("Description: " + description);
    }

    // Getter cho groupName
    public String getGroupName() {
        return groupName;
    }

    // Setter cho groupName
    public void setGroupName(String groupName) {
        this.groupName = groupName;
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
