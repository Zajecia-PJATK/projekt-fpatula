package com.business.market.simulator.finance.owner;

import com.business.market.simulator.finance.transaction.entity.LegalEntity;
import com.business.market.simulator.utils.BigDecimalToStringConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
public class Company extends Owner implements LegalEntity {
    public Company(OwnerType ownerType) {
        super();
        if (!(ownerType.equals(OwnerType.JSC) || ownerType.equals(OwnerType.PLC))) {
            throw new IllegalArgumentException();
        }
        setOwnerType(ownerType);
    }

    @Convert(converter = BigDecimalToStringConverter.class)
    BigDecimal equityCapital;
    @Convert(converter = BigDecimalToStringConverter.class)
    BigDecimal income;
    @Convert(converter = BigDecimalToStringConverter.class)
    BigDecimal loses;
    @Convert(converter = BigDecimalToStringConverter.class)
    BigDecimal dividendsPerShare;

    public BigDecimal getCompanyProfit() {
        return new CompanyHelper().calculateProfit();
    }

    public BigDecimal getBookValue() {
        return new CompanyHelper().getBookValue();
    }

    @Override
    public Long getEntityId() {
        return getOwnerId();
    }

    private class CompanyHelper {
        public BigDecimal calculateProfit() {
            return income.subtract(loses);
        }

        public BigDecimal getBookValue() {
            return equityCapital.divide(BigDecimal.valueOf(getOwnerFinancialInstruments().size()), RoundingMode.HALF_UP);
        }
    }
}
