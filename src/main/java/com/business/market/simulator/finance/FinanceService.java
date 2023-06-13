package com.business.market.simulator.finance;

import com.business.market.simulator.finance.instrument.active.ActiveInstrument;
import com.business.market.simulator.finance.instrument.active.ActiveInstrumentRepository;
import com.business.market.simulator.finance.instrument.active.type.Share;
import com.business.market.simulator.finance.instrument.active.type.TreasuryBond;
import com.business.market.simulator.finance.instrument.aspect.Tradeable;
import com.business.market.simulator.finance.instrument.derivative.Derivative;
import com.business.market.simulator.finance.instrument.derivative.DerivativeRepository;
import com.business.market.simulator.finance.instrument.derivative.type.Index;
import com.business.market.simulator.finance.simulation.MarketSimulationService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@Setter(onMethod_ = {@Autowired})
public class FinanceService {

    private ShareHelper shareHelper;
    private TreasuryBondHelper treasuryBondHelper;
    private IndexHelper indexHelper;

    public BigDecimal getMarketValue(Tradeable tradeable) {
        if (tradeable instanceof ActiveInstrument) {
            switch (((ActiveInstrument) tradeable).getType()) {
                case SHARE -> {
                    return shareHelper.getLowestAskPrice((Share) tradeable);
                }
                case TREASURY_BOND -> {
                    return treasuryBondHelper.getTreasuryBondValue((TreasuryBond) tradeable);
                }
                default -> {
                    return BigDecimal.ZERO;
                }
            }
        } else if (tradeable instanceof Derivative) {
            switch (((Derivative) tradeable).getDerivativeType()) {
                case INDEX -> {
                    return indexHelper.getIndexValue((Index) tradeable);
                }
                default -> {
                    return BigDecimal.ZERO;
                }
            }
        } else {
            return BigDecimal.ZERO;
        }
    }

    @Component
    static class TreasuryBondHelper {
        public BigDecimal getTreasuryBondValue(TreasuryBond treasuryBond) {
            BigDecimal treasuryBondValue = treasuryBond.getInitialContractValue();
            if (getContractPassedPeriodInMonths(treasuryBond) >= treasuryBond.getTermInMonths()) {
                treasuryBondValue = treasuryBondValue.add(
                        treasuryBondValue
                                .divide(BigDecimal.valueOf(100), RoundingMode.UNNECESSARY)
                                .multiply(BigDecimal.valueOf(treasuryBond.getInterest()))
                );
            }
            return treasuryBondValue;
        }

        private long getContractPassedPeriodInMonths(TreasuryBond treasuryBond) {
            LocalDateTime dateSold = treasuryBond.getDateSold().toLocalDateTime();
            LocalDateTime now = MarketSimulationService.currentSimulationTimestamp.toLocalDateTime();
            return ChronoUnit.MONTHS.between(now, dateSold);
        }
    }

    @Component
    static class IndexHelper {
        private final DerivativeRepository derivativeRepository;

        @Autowired
        public IndexHelper(DerivativeRepository derivativeRepository) {
            this.derivativeRepository = derivativeRepository;
        }

        //TODO implement this
        public BigDecimal getIndexValue(Index index) {
            return BigDecimal.ZERO;
        }
    }

    @Component
    static class ShareHelper {
        private final ActiveInstrumentRepository activeInstrumentRepository;

        @Autowired
        public ShareHelper(ActiveInstrumentRepository activeInstrumentRepository) {
            this.activeInstrumentRepository = activeInstrumentRepository;
        }

        public BigDecimal getLowestAskPrice(Share share) {
            return activeInstrumentRepository.getMinAskPriceByFinancialInstrumentId(share.getFinancialInstrument().getInstrumentId());
        }
    }

    @Component
    static class FundHelper {

    }
}
