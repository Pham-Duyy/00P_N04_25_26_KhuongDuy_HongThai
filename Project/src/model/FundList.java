package model;

public class FundList {
    private Fund[] funds;
    private int count;

    public FundList(int capacity) {
        funds = new Fund[capacity];
        count = 0;
    }

    // Thêm quỹ mới
    public void addFund(Fund fund) {
        if (count < funds.length) {
            funds[count++] = fund;
        } else {
            System.out.println("Danh sách quỹ đã đầy!");
        }
    }

    // Hiển thị tất cả quỹ
    public void displayAllFunds() {
        if (count == 0) {
            System.out.println("Danh sách quỹ trống!");
            return;
        }
        for (int i = 0; i < count; i++) {
            funds[i].displayInfo();
            System.out.println("-------------------");
        }
    }

    // Tìm quỹ theo tên
    public Fund findFundByName(String fundName) {
        for (int i = 0; i < count; i++) {
            if (funds[i].getFundName().equalsIgnoreCase(fundName)) {
                return funds[i];
            }
        }
        return null;
    }

    // Xóa quỹ
    public boolean removeFund(String fundName) {
        for (int i = 0; i < count; i++) {
            if (funds[i].getFundName().equalsIgnoreCase(fundName)) {
                for (int j = i; j < count - 1; j++) {
                    funds[j] = funds[j + 1];
                }
                funds[--count] = null;
                return true;
            }
        }
        return false;
    }

    // Cập nhật quỹ
    public boolean updateFund(String oldName, String newName, double newTotalAmount) {
        Fund f = findFundByName(oldName);
        if (f != null) {
            f.updateFund(newName, newTotalAmount);
            return true;
        }
        return false;
    }
}
