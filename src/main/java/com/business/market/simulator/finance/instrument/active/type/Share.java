package com.business.market.simulator.finance.instrument.active.type;

import com.business.market.simulator.finance.instrument.InstrumentType;
import com.business.market.simulator.finance.instrument.active.ActiveInstrument;
import com.business.market.simulator.finance.instrument.active.ActiveInstrumentRepository;
import com.business.market.simulator.finance.transaction.MarketTransaction;
import com.business.market.simulator.utils.BigDecimalToStringConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
public class Share extends ActiveInstrument {
    @Convert(converter = BigDecimalToStringConverter.class)
    private BigDecimal askPrice;

    public Share() {
        super();
        this.setType(InstrumentType.SHARE);
    }

    @Override
    public MarketTransaction buyInstrument() {
        return null;
    }

    @Override
    public MarketTransaction sellInstrument() {
        return null;
    }

    @Override
    public BigDecimal getCurrentMarketValue() {
        ShareHelper shareHelper = new ShareHelper();
        return shareHelper.getLowestAskPrice();
    }

    private class ShareHelper{
        @Autowired
        private ActiveInstrumentRepository activeInstrumentRepository;

        public BigDecimal getLowestAskPrice(){
            return new BigDecimal(activeInstrumentRepository
                    .getMaxAskPriceByFinancialInstrumentId(getFinancialInstrument().getInstrumentId()));
        }
    }
}
