package com.example.servingwebcontent.component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import com.example.servingwebcontent.model.User;

public class WriteToFile {

    public void toFile(ArrayList<User> users) {
        try {
            if (users == null || users.isEmpty()) {
                System.out.println("Không có user nào để ghi vào file!");
                return;
            }

            User user = users.get(users.size() - 1);

            try (FileWriter writer = new FileWriter("./complete/File/Login.txt", true)) {
                writer.write(user.getUsername() + "\n");
                writer.write(user.getPassword() + "\n");
                writer.write(user.getEmail() + "\n");
                writer.write(user.getRole().toString() + "\n");
                writer.write("----------\n"); // Ngăn cách giữa các user
            }

            System.out.println("Ghi file thành công cho user: " + user.getUsername());

        } catch (IOException e) {
            System.out.println("Lỗi khi ghi file!");
            e.printStackTrace();
        }
    }
}
