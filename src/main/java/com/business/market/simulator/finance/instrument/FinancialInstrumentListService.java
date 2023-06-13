package com.business.market.simulator.finance.instrument;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.function.BiPredicate;

@Service
public class FinancialInstrumentListService {
    //TODO implement table view
    private static final Sort sortByInstrumentType = Sort.by(Sort.Direction.ASC, "type");
    private static final Sort sortByOwner = Sort.by(Sort.Direction.ASC, "owningCompany.ownerName");
    private static final Sort sortBySymbol = Sort.by(Sort.Direction.ASC, "symbol");

    private static final Comparator<FinancialInstrument> compareByInstrumentType = Comparator.comparing(FinancialInstrument::getType);
    private static final Comparator<FinancialInstrument> compareByOwner = Comparator.comparing((financialInstrument) -> financialInstrument.getOwningCompany().getOwnerName());
    private static final Comparator<FinancialInstrument> compareBySymbol = Comparator.comparing(FinancialInstrument::getSymbol);

    private static final BiPredicate<FinancialInstrument, String> filterBySymbol = (financialInstrument, symbol) -> financialInstrument.getSymbol().equalsIgnoreCase(symbol);
    private static final BiPredicate<FinancialInstrument, String> filterByOwner = (financialInstrument, name) -> financialInstrument.getOwningCompany().getOwnerName().toUpperCase().matches(".*" + name.toUpperCase() + ".*");
    private static final BiPredicate<FinancialInstrument, String> filterByType = (financialInstrument, type) -> financialInstrument.getType().toString().equalsIgnoreCase(type);


}
