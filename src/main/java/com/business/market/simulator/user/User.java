package com.business.market.simulator.user;

import com.business.market.simulator.finance.instrument.ActiveInstrument;
import com.business.market.simulator.utils.BigDecimalToStringConverter;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Entity(name="users")
public class User {
    @Id
    private long userId;
    private String username;
    private String passwordHash;
    @Convert(converter = BigDecimalToStringConverter.class)
    private BigDecimal balance;
    @OneToMany
    private List<ActiveInstrument> ownedInstruments;
}
