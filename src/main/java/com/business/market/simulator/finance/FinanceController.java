package com.business.market.simulator.finance;

import com.business.market.simulator.finance.instrument.FinanceOperationException;
import com.business.market.simulator.finance.instrument.FinancialInstrumentService;
import com.business.market.simulator.finance.instrument.active.ActiveInstrumentService;
import com.business.market.simulator.finance.instrument.active.type.Share;
import com.business.market.simulator.finance.instrument.active.type.TreasuryBond;
import com.business.market.simulator.user.User;
import com.business.market.simulator.utils.InputScanner;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;

@Setter(onMethod_ = @Autowired)
@Controller
public class FinanceController {

    private InputScanner scanner;
    private FinanceService financeService;
    private ActiveInstrumentService activeInstrumentService;

    public void financeMenu(User user){
        System.out.println("Welcome to the investment menu!");
        System.out.println("1. Buy Shares");
        System.out.println("2. Buy Treasury Bonds");

        System.out.print("Please enter your choice: ");
        int choice = scanner.getScanner().nextInt();

        switch (choice) {
            case 1 -> {
                System.out.print("Enter the symbol of the share you want to buy: ");
                String shareSymbol = scanner.getScanner().next();
                System.out.println("Buying share with symbol: " + shareSymbol);
                Share share = activeInstrumentService.getShareToBuyByUserBySymbol(shareSymbol,user);
                try{
                    financeService.sellInstrument(share,user, BigDecimal.valueOf(1.5));
                } catch (FinanceOperationException e) {
                    System.out.println(e.getMessage());
                }
            }
            case 2 -> {
                System.out.print("Enter the symbol of the treasury bond you want to buy: ");
                String bondSymbol = scanner.getScanner().next();
                System.out.println("Buying treasury bond with symbol: " + bondSymbol);
                TreasuryBond treasuryBond = activeInstrumentService.getTreasuryBondToBuyByUserBySymbol(bondSymbol);
                try {
                    financeService.sellInstrument(treasuryBond,user);
                } catch (FinanceOperationException e) {
                    System.out.println(e.getMessage());
                }
            }
            default -> System.out.println("Invalid choice. Please try again.");
        }
    }
}
