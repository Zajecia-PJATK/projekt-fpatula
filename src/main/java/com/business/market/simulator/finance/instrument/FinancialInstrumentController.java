package com.business.market.simulator.finance.instrument;

import com.business.market.simulator.finance.instrument.active.ActiveInstrument;
import com.business.market.simulator.utils.InputScanner;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Setter(onMethod_ = @Autowired)
public class FinancialInstrumentController {

    private InputScanner scanner;
    private FinancialInstrumentService financialInstrumentService;
    private FinancialInstrumentListService financialInstrumentListService;
    public void showFinancialInstrumentTableMenu() {
        boolean show = true;
        String choice;

        while (show){
            System.out.println("Please choose a table to view: ");
            System.out.println("1. Treasury Bonds");
            System.out.println("2. Shares");
            System.out.println("0. Exit");

            choice = scanner.getScanner().nextLine();

            switch (choice) {
                case "1" -> displayTreasuryBondTable();
                case "2" -> displayShareTable();
                case "0" -> {
                    show = false;
                    System.out.println("Exiting.");
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }

    }

    private void displayTreasuryBondTable() {
        boolean run = true;
        String choice;
        List<FinancialInstrument> treasuryBonds = financialInstrumentService.getAllByType(InstrumentType.TREASURY_BOND);
        List<FinancialInstrumentListService.TreasuryBondTableRow> treasuryBondTableRows = financialInstrumentListService.generateTreasuryBondTableRows(treasuryBonds);
        do {
            System.out.println("Treasury Bonds Table");
            System.out.println("--------------------");
            System.out.println("|Name\t|Symbol\t|Contract Value\t|Interest Rate\t|Term");

            // Display treasury bonds
/*            for (FinancialInstrumentListService.TreasuryBondTableRow bond : treasuryBonds) {
                System.out.println(bond.getName() + "\t|" + bond.getSymbol() + "\t|" +
                        bond.getContractValue() + "\t|" + bond.getInterestRate() + "\t" + bond.getTerm());
            }*/

/*            System.out.println("--------------------");
            System.out.println("Filtering and Sorting Options");
            System.out.println("Write f for filtering and s for sorting and add number");
            System.out.println("1. by Name");
            System.out.println("2. by Symbol");
            System.out.println("3. by Contract Value");
            System.out.println("4. by Interest Rate");
            System.out.println("5. by Term");
            System.out.println("0. Back to Main Menu");*/

            choice = scanner.getScanner().nextLine();

            switch (choice) {
/*                case "f1" -> Collections.sort(treasuryBonds, Comparator.comparing(TreasuryBond::getName));
                case "f2" -> Collections.sort(treasuryBonds, Comparator.comparing(TreasuryBond::getSymbol));
                case "f3" -> Collections.sort(treasuryBonds, Comparator.comparing(TreasuryBond::getContractValue));
                case "f4" -> Collections.sort(treasuryBonds, Comparator.comparing(TreasuryBond::getInterestRate));
                case "f5" -> Collections.sort(treasuryBonds, Comparator.comparing(TreasuryBond::getTerm));
                case "s1" -> Collections.sort(treasuryBonds, Comparator.comparing(TreasuryBond::getName));
                case "s2" -> Collections.sort(treasuryBonds, Comparator.comparing(TreasuryBond::getSymbol));
                case "s3" -> Collections.sort(treasuryBonds, Comparator.comparing(TreasuryBond::getContractValue));
                case "s4" -> Collections.sort(treasuryBonds, Comparator.comparing(TreasuryBond::getInterestRate));
                case "s5" -> Collections.sort(treasuryBonds, Comparator.comparing(TreasuryBond::getTerm));*/
                case "0" -> {
                    run = false;
                    System.out.println("Going back to Main Menu...");
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (run);
    }

    private void displayShareTable() {
        boolean run = true;

        String choice;
        List<FinancialInstrument> shares = financialInstrumentService.getAllByType(InstrumentType.SHARE);

        do {
            System.out.println("Shares Table");
            System.out.println("------------");
            System.out.println("Name\tSymbol\tSector\tCourse Change");

            // Display shares
/*            for (FinancialInstrumentListService.ShareTableRow share : shares) {
                System.out.println(share.getName() + "\t" + share.getSymbol() + "\t" +
                        share.getSector() + "\t" + share.getCourseChange());
            }

            System.out.println("------------");
            System.out.println("Filtering and Sorting Options");
            System.out.println("Write f for filtering and s for sorting and add number");
            System.out.println("1. Sort by Name");
            System.out.println("2. Sort by Symbol");
            System.out.println("3. Sort by Sector");
            System.out.println("4. Sort by Course Change");
            System.out.println("0. Back to Main Menu");

            choice = scanner.getScanner().nextLine();

            switch (choice) {
                case "s1" -> Collections.sort(shares, Comparator.comparing(Share::getName));
                case "s2" -> Collections.sort(shares, Comparator.comparing(Share::getSymbol));
                case "s3" -> Collections.sort(shares, Comparator.comparing(Share::getSector));
                case "s4" -> Collections.sort(shares, Comparator.comparing(Share::getCourseChange));
                case "f1" -> Collections.sort(shares, Comparator.comparing(Share::getName));
                case "f2" -> Collections.sort(shares, Comparator.comparing(Share::getSymbol));
                case "f3" -> Collections.sort(shares, Comparator.comparing(Share::getSector));
                case "f4" -> Collections.sort(shares, Comparator.comparing(Share::getCourseChange));
                case "0" -> System.out.println("Going back to Main Menu...");
                default -> System.out.println("Invalid choice. Please try again.");
            }*/
        } while (run);
    }
}
