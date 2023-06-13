package com.business.market.simulator;

import com.business.market.simulator.finance.FinanceService;
import com.business.market.simulator.finance.instrument.FinancialInstrument;
import com.business.market.simulator.finance.instrument.FinancialInstrumentRepository;
import com.business.market.simulator.finance.instrument.active.ActiveInstrumentRepository;
import com.business.market.simulator.finance.instrument.active.type.Share;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@SpringBootTest
class MarketSimulatorApplicationTests {

    @Autowired
    ActiveInstrumentRepository activeInstrumentRepository;
    @Autowired
    FinancialInstrumentRepository financialInstrumentRepository;
    @Autowired FinanceService financeService;
    @Test
    void contextLoads() {
    }

    @Test
    @Transactional
    void getShareMarketValue() {
        FinancialInstrument financialInstrument = new FinancialInstrument();
        financialInstrumentRepository.save(financialInstrument);
        Share firstShare = new Share();
        firstShare.setAskPrice(BigDecimal.TWO);
        financialInstrument.addActiveInstrument(firstShare);
        Share secondShare = new Share();
        secondShare.setAskPrice(BigDecimal.ONE);
        financialInstrument.addActiveInstrument(secondShare);
        activeInstrumentRepository.save(firstShare);
        activeInstrumentRepository.save(secondShare);
        Assertions.assertEquals(BigDecimal.ONE,financeService.getMarketValue(firstShare));
    }

}
