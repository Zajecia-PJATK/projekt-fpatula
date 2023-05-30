package com.business.market.simulator.finance.owner;

import com.business.market.simulator.utils.BigDecimalToStringConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
public class Company extends Owner {
    public Company(OwnerType ownerType) {
        super();
        if (!(ownerType.equals(OwnerType.JSC) || ownerType.equals(OwnerType.PLC))) {
            throw new IllegalArgumentException();
        }
        setOwnerType(ownerType);
    }

    //TODO add calculate methods
    @Convert(converter = BigDecimalToStringConverter.class)
    BigDecimal equityCapital;
    @Convert(converter = BigDecimalToStringConverter.class)
    BigDecimal income;
    @Convert(converter = BigDecimalToStringConverter.class)
    BigDecimal loses;
    @Convert(converter = BigDecimalToStringConverter.class)
    BigDecimal dividendsPerShare;
}
