package com.business.market.simulator.finance.instrument;

import com.business.market.simulator.finance.instrument.active.type.Share;
import com.business.market.simulator.finance.instrument.active.type.TreasuryBond;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiPredicate;

@Setter(onMethod_ = @Autowired)
@Service
public class FinancialInstrumentListService {
    private FinancialInstrumentService financialInstrumentService;
    interface ActiveInstrumentRow {
        String getName();
        String getSymbol();
    }
    record ShareTableRow(String name, String symbol, String sector, double courseChange) implements ActiveInstrumentRow{
        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getSymbol() {
            return symbol;
        }
    }
    record TreasuryBondTableRow(String name, String symbol, BigDecimal contractValue, double interestRate, int term) implements ActiveInstrumentRow{
        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getSymbol() {
            return symbol;
        }
    }
    public static final Comparator<ActiveInstrumentRow> compareByOwner = Comparator.comparing(ActiveInstrumentRow::getName);
    public static final Comparator<ActiveInstrumentRow> compareBySymbol = Comparator.comparing(ActiveInstrumentRow::getSymbol);
    public static final Comparator<ShareTableRow> compareByCourseChange = Comparator.comparing(ShareTableRow::courseChange);
    public static final Comparator<ShareTableRow> compareBySector = Comparator.comparing(ShareTableRow::symbol);
    public static final Comparator<TreasuryBondTableRow> compareByContractValue = Comparator.comparing(TreasuryBondTableRow::contractValue);
    public static final Comparator<TreasuryBondTableRow> compareByInterestRate = Comparator.comparing(TreasuryBondTableRow::interestRate);
    public static final Comparator<TreasuryBondTableRow> compareByTerm = Comparator.comparing(TreasuryBondTableRow::term);
    public static final BiPredicate<ActiveInstrumentRow, String> filterByOwner = (activeInstrumentRow, name) -> activeInstrumentRow.getName().toUpperCase().matches(".*" + name.toUpperCase() + ".*");
    public static final BiPredicate<ActiveInstrumentRow, String> filterBySymbol = (activeInstrumentRow, symbol) -> activeInstrumentRow.getSymbol().equalsIgnoreCase(symbol);
    public static final BiPredicate<ShareTableRow, Double> filterByCourseChange = (shareTableRow, change) -> shareTableRow.courseChange() >= change ;
    public static final BiPredicate<ShareTableRow, String> filterBySector = (shareTableRow, sector) -> shareTableRow.sector().equalsIgnoreCase(sector);
    public static final BiPredicate<TreasuryBondTableRow,BigDecimal> filterByContractValue = (treasuryBondTableRow, contractValue) -> treasuryBondTableRow.contractValue().compareTo(contractValue) >= 0 ;
    public static final BiPredicate<TreasuryBondTableRow,Double> filterByInterestRate = (treasuryBondTableRow, interestRate) -> treasuryBondTableRow.interestRate() >= interestRate;
    public static final BiPredicate<TreasuryBondTableRow,Integer> filterByTerm = (treasuryBondTableRow, term) -> treasuryBondTableRow.term() >= term;
    List<ShareTableRow> generateShareTableRows(Collection<FinancialInstrument> shares){
        List<ShareTableRow> shareTableRows = new ArrayList<>();
        for (FinancialInstrument share:shares) {
            shareTableRows.add(new ShareTableRow(share.getOwningCompany().getOwnerName(),share.getSymbol(),share.getSector().toString(), financialInstrumentService.calculateShareChange(share,1)));
        }
        return shareTableRows;
    }

    List<TreasuryBondTableRow> generateTreasuryBondTableRows(Collection<FinancialInstrument> treasuryBonds){
        List<TreasuryBondTableRow> treasuryBondTableRows = new ArrayList<>();
        for (FinancialInstrument treasuryBond:treasuryBonds) {
            TreasuryBond exampleBond = (TreasuryBond)treasuryBond.getMarketActiveInstruments().get(0);
            treasuryBondTableRows.add(new TreasuryBondTableRow(treasuryBond.getOwningCompany().getOwnerName(),treasuryBond.getSymbol(),exampleBond.getInitialContractValue(), exampleBond.getOverallInterest(),exampleBond.getTermInMonths()));
        }
        return treasuryBondTableRows;
    }

}
