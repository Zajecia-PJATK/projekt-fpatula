package com.business.market.simulator.finance.instrument;

import com.business.market.simulator.utils.InputScanner;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.business.market.simulator.finance.instrument.FinancialInstrumentListService;

@Setter(onMethod_ = @Autowired)
@Controller
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

        Comparator<? super FinancialInstrumentListService.TreasuryBondTableRow> sortingComparator = FinancialInstrumentListService.compareByOwner;

        do {
            treasuryBondTableRows.sort(sortingComparator);

            System.out.println("Treasury Bonds Table");
            System.out.println("--------------------");
            System.out.println("|Name\t|Symbol\t|Contract Value\t|Interest Rate\t|Term");

            for (FinancialInstrumentListService.TreasuryBondTableRow bond : treasuryBondTableRows) {
                System.out.println(bond.getName() + "\t|" + bond.getSymbol() + "\t|" +
                        bond.contractValue() + "\t|" + bond.interestRate() + "\t" + bond.term());
            }

            System.out.println("--------------------");
            System.out.println("Filtering and Sorting Options");
            System.out.println("Write f for filtering and s for sorting and add number");
            System.out.println("1. by Name");
            System.out.println("2. by Symbol");
            System.out.println("3. by Contract Value");
            System.out.println("4. by Interest");
            System.out.println("5. by Term");
            System.out.println("0. Back to Main Menu");

            choice = scanner.getScanner().nextLine();

            switch (choice) {
/*                case "f1" -> Collections.sort(treasuryBonds, Comparator.comparing(TreasuryBond::getName));
                case "f2" -> Collections.sort(treasuryBonds, Comparator.comparing(TreasuryBond::getSymbol));
                case "f3" -> Collections.sort(treasuryBonds, Comparator.comparing(TreasuryBond::getContractValue));
                case "f4" -> Collections.sort(treasuryBonds, Comparator.comparing(TreasuryBond::getInterestRate));
                case "f5" -> Collections.sort(treasuryBonds, Comparator.comparing(TreasuryBond::getTerm));*/
                case "s1" -> sortingComparator = FinancialInstrumentListService.compareByOwner;
                case "s2" -> sortingComparator = FinancialInstrumentListService.compareBySymbol;
                case "s3" -> sortingComparator = FinancialInstrumentListService.compareByContractValue;
                case "s4" -> sortingComparator = FinancialInstrumentListService.compareByInterestRate;
                case "s5" -> sortingComparator = FinancialInstrumentListService.compareByTerm;
                case "0" -> {
                    run = false;
                    System.out.println("Going back to main menu");
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (run);
    }

    private void displayShareTable() {
        boolean run = true;

        String choice;
        List<FinancialInstrument> shares = financialInstrumentService.getAllByType(InstrumentType.SHARE);
        List<FinancialInstrumentListService.ShareTableRow> shareTableRows = financialInstrumentListService.generateShareTableRows(shares);

        Comparator<? super FinancialInstrumentListService.ShareTableRow> sortingComparator = FinancialInstrumentListService.compareByOwner;

        do {
            shareTableRows.sort(sortingComparator);
            System.out.println("Shares Table");
            System.out.println("------------");
            System.out.println("Name\tSymbol\tSector\tCourse Change");

            for (FinancialInstrumentListService.ShareTableRow share : shareTableRows) {
                System.out.println(share.getName() + "\t" + share.getSymbol() + "\t" +
                        share.sector() + "\t" + share.courseChange());
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
                case "s1" -> sortingComparator = FinancialInstrumentListService.compareByOwner;
                case "s2" -> sortingComparator = FinancialInstrumentListService.compareBySymbol;
                case "s3" -> sortingComparator = FinancialInstrumentListService.compareBySector;
                case "s4" -> sortingComparator = FinancialInstrumentListService.compareByCourseChange;
/*                case "f1" -> Collections.sort(shares, Comparator.comparing(Share::getName));
                case "f2" -> Collections.sort(shares, Comparator.comparing(Share::getSymbol));
                case "f3" -> Collections.sort(shares, Comparator.comparing(Share::getSector));
                case "f4" -> Collections.sort(shares, Comparator.comparing(Share::getCourseChange));*/
                case "0" ->{
                    run = false;
                    System.out.println("Going back to main menu");
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (run);
    }
}
