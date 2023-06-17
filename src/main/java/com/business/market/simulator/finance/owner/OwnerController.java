package com.business.market.simulator.finance.owner;

import com.business.market.simulator.user.User;
import com.business.market.simulator.utils.InputScanner;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Setter(onMethod_ = @Autowired)
@Controller
public class OwnerController {
    private InputScanner scanner;
    private OwnerService ownerService;
    public void showOwnersMenu() {
        boolean show = true;

        while (show) {
            System.out.println("Please select an option:");
            System.out.println("1. Show owners and companies data");
            System.out.println("0. Exit");

            int option = scanner.getScanner().nextInt();

            switch (option) {
                case 1 -> generateOwnersDataView(ownerService.getAllAssetsOwners());
                case 0 -> {
                    System.out.println("Returning to main.");
                    show = false;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
            System.out.println();
        }
    }
    private void generateOwnersDataView(List<Owner> owners) {
        System.out.println("Name         |Income       |Profit      |Book Value    |Dividend Per Share");
        System.out.println("--------------------------------------------------------------");
        List<OwnerDataRow> dataRows = generateOwnerDataRows(owners);
        for (OwnerDataRow dataRow : dataRows) {
            System.out.printf("%-15s%-15s%-15s%-15s%-15s%n",
                    dataRow.name, dataRow.income,
                    dataRow.profit, dataRow.bookValue,
                    dataRow.dividendPerShare);
        }
    }

    private List<OwnerDataRow> generateOwnerDataRows(List<Owner> owners){
        return owners.parallelStream().map(OwnerDataRow::new).toList();
    }
    private static class OwnerDataRow{
        private String name;
        private String income = "";
        private String profit = "";
        private String bookValue = "";
        private String dividendPerShare = "";
        public OwnerDataRow(Owner owner){
            this.name = owner.getOwnerName();
            if (owner instanceof Company company){
                this.income = company.getIncome().setScale(0, RoundingMode.HALF_DOWN).toString();
                this.profit = company.getCompanyProfit().setScale(0, RoundingMode.HALF_DOWN).toString();
                this.bookValue = company.getBookValue().setScale(2,RoundingMode.HALF_DOWN).toString();
                this.dividendPerShare = company.getDividendsPerShare().toString();
            }
        }
    }
}
