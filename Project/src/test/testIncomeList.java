package test;

import model.Income;
import model.IncomeList;

public class testIncomeList {
    public static void main(String[] args) {
        IncomeList incomeList = new IncomeList(5);

        // Thêm thu nhập
        incomeList.addIncome(new Income("Lương", 15000));
        incomeList.addIncome(new Income("Thưởng dự án", 5000));
        incomeList.addIncome(new Income("Bán hàng online", 3000));

        System.out.println("📌 Danh sách thu nhập ban đầu:");
        incomeList.displayAllIncomes();

        // Tìm kiếm
        System.out.println("📌 Tìm kiếm thu nhập 'Lương':");
        Income found = incomeList.findIncomeByName("Lương");
        if (found != null) found.displayInfo();

        // Cập nhật
        System.out.println("📌 Cập nhật 'Bán hàng online' thành 'Bán hàng Shopee':");
        if (incomeList.updateIncome("Bán hàng online", "Bán hàng Shopee", 4000)) {
            System.out.println("Cập nhật thành công!");
        } else {
            System.out.println("Không tìm thấy thu nhập cần cập nhật!");
        }

        incomeList.displayAllIncomes();

        // Xóa
        System.out.println("📌 Xóa thu nhập 'Thưởng dự án':");
        if (incomeList.removeIncome("Thưởng dự án")) {
            System.out.println("Xóa thành công!");
        } else {
            System.out.println("Không tìm thấy thu nhập cần xóa!");
        }

        incomeList.displayAllIncomes();
    }
}
