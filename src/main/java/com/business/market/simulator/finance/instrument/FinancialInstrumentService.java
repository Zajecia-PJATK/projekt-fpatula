package com.business.market.simulator.finance.instrument;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class FinancialInstrumentService {
    private static final Sort sortByInstrumentType = Sort.by(Sort.Direction.ASC,"type");
}
