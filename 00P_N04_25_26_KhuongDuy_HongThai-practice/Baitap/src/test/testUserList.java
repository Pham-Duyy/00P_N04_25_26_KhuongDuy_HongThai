package test;

import java.util.ArrayList;
import java.util.Scanner;
import model.UserList;

public class testUserList {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<UserList> users = new ArrayList<>();

        int choice;
        do {
            System.out.println("\n===== MENU QUẢN LÝ USER =====");
            System.out.println("1. Thêm User");
            System.out.println("2. Hiển thị danh sách User");
            System.out.println("3. Cập nhật User theo username");
            System.out.println("4. Xóa User theo username");
            System.out.println("5. Tìm kiếm User theo username");
            System.out.println("0. Thoát");
            System.out.print("Chọn: ");
            choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1:
                    System.out.print("Nhập username: ");
                    String username = sc.nextLine();
                    System.out.print("Nhập email: ");
                    String email = sc.nextLine();
                    System.out.print("Nhập số điện thoại: ");
                    String phone = sc.nextLine();
                    users.add(new UserList(username, email, phone));
                    System.out.println("✅ Thêm user thành công!");
                    break;

                case 2:
                    if (users.isEmpty()) {
                        System.out.println("Danh sách rỗng!");
                    } else {
                        System.out.println("\n--- DANH SÁCH USER ---");
                        for (UserList u : users) {
                            u.displayInfo();
                            System.out.println("-------------------");
                        }
                    }
                    break;

                case 3:
                    System.out.print("Nhập username cần cập nhật: ");
                    String updateName = sc.nextLine();
                    boolean foundUpdate = false;
                    for (UserList u : users) {
                        if (u.getUsername().equals(updateName)) {
                            System.out.print("Nhập username mới: ");
                            u.setUsername(sc.nextLine());
                            System.out.print("Nhập email mới: ");
                            u.setEmail(sc.nextLine());
                            System.out.print("Nhập số điện thoại mới: ");
                            u.setPhone(sc.nextLine());
                            System.out.println("✅ Cập nhật thành công!");
                            foundUpdate = true;
                            break;
                        }
                    }
                    if (!foundUpdate) {
                        System.out.println("❌ Không tìm thấy user!");
                    }
                    break;

                case 4:
                    System.out.print("Nhập username cần xóa: ");
                    String deleteName = sc.nextLine();
                    boolean removed = users.removeIf(u -> u.getUsername().equals(deleteName));
                    if (removed) {
                        System.out.println("✅ Đã xóa user!");
                    } else {
                        System.out.println("❌ Không tìm thấy user!");
                    }
                    break;

                case 5:
                    System.out.print("Nhập username cần tìm: ");
                    String searchName = sc.nextLine();
                    boolean foundSearch = false;
                    for (UserList u : users) {
                        if (u.getUsername().equals(searchName)) {
                            System.out.println("🔍 Thông tin user tìm thấy:");
                            u.displayInfo();
                            foundSearch = true;
                            break;
                        }
                    }
                    if (!foundSearch) {
                        System.out.println("❌ Không tìm thấy user!");
                    }
                    break;

                case 0:
                    System.out.println("Thoát chương trình!");
                    break;

                default:
                    System.out.println("⚠️ Lựa chọn không hợp lệ!");
            }

        } while (choice != 0);

        sc.close();
    }
}