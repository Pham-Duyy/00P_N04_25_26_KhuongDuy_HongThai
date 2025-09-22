package model;

public class GroupList {
    private Group[] groups;
    private int count;

    // Constructor
    public GroupList(int capacity) {
        groups = new Group[capacity];
        count = 0;
    }

    // Thêm nhóm mới vào danh sách
    public void addGroup(Group group) {
        if (count < groups.length) {
            groups[count++] = group;
        } else {
            System.out.println("Group list is full!");
        }
    }

    // Hiển thị tất cả các nhóm trong danh sách
    public void displayAllGroups() {
        for (int i = 0; i < count; i++) {
            groups[i].displayInfo();
            System.out.println("-------------------");
        }
    }

    // Tìm kiếm nhóm theo tên
    public Group findGroupByName(String groupName) {
        for (int i = 0; i < count; i++) {
            if (groups[i].getGroupName().equalsIgnoreCase(groupName)) {
                return groups[i];
            }
        }
        return null; // Không tìm thấy nhóm
    }
}
