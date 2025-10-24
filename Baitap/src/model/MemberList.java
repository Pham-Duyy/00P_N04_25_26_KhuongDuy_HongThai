package model;

public class MemberList {
    private Member[] members;
    private int count;

    // Constructor
    public MemberList(int capacity) {
        members = new Member[capacity];
        count = 0;
    }

    // Thêm member mới vào danh sách
    public void addMember(Member member) {
        if (count < members.length) {
            members[count++] = member;
        } else {
            System.out.println("Member list is full!");
        }
    }

    // Hiển thị tất cả các member trong danh sách
    public void displayAllMembers() {
        for (int i = 0; i < count; i++) {
            members[i].displayInfo();
            System.out.println("-------------------");
        }
    }

    // Tìm kiếm member theo tên
    public Member findMemberByName(String name) {
        for (int i = 0; i < count; i++) {
            if (members[i].getName().equalsIgnoreCase(name)) {
                return members[i];
            }
        }
        return null; // Không tìm thấy member
    }
}
