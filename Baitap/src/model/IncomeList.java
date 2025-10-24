package model;

public class IncomeList {
    private Income[] incomes;
    private int count;

    // Constructor
    public IncomeList(int capacity) {
        incomes = new Income[capacity];
        count = 0;
    }

    // Thêm thu nhập mới
    public void addIncome(Income income) {
        if (count < incomes.length) {
            incomes[count++] = income;
        } else {
            System.out.println("Income list is full!");
        }
    }

    // Hiển thị tất cả thu nhập
    public void displayAllIncomes() {
        if (count == 0) {
            System.out.println("Danh sách thu nhập trống!");
            return;
        }
        for (int i = 0; i < count; i++) {
            incomes[i].displayInfo();
            System.out.println("-------------------");
        }
    }

    // Tìm kiếm thu nhập theo tên
    public Income findIncomeByName(String incomeName) {
        for (int i = 0; i < count; i++) {
            if (incomes[i].getIncomeName().equalsIgnoreCase(incomeName)) {
                return incomes[i];
            }
        }
        return null;
    }

    // Xóa thu nhập theo tên
    public boolean removeIncome(String incomeName) {
        for (int i = 0; i < count; i++) {
            if (incomes[i].getIncomeName().equalsIgnoreCase(incomeName)) {
                for (int j = i; j < count - 1; j++) {
                    incomes[j] = incomes[j + 1];
                }
                incomes[--count] = null;
                return true;
            }
        }
        return false;
    }

    // Cập nhật thu nhập theo tên
    public boolean updateIncome(String oldName, String newName, double newAmount) {
        Income inc = findIncomeByName(oldName);
        if (inc != null) {
            inc.updateIncome(newName, newAmount);
            return true;
        }
        return false;
    }
}
