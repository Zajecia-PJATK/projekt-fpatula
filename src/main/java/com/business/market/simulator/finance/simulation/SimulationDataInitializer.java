package com.business.market.simulator.finance.simulation;

import com.business.market.simulator.finance.instrument.FinancialInstrument;
import com.business.market.simulator.finance.instrument.FinancialInstrumentService;
import com.business.market.simulator.finance.instrument.InstrumentType;
import com.business.market.simulator.finance.instrument.Sector;
import com.business.market.simulator.finance.instrument.active.ActiveInstrumentException;
import com.business.market.simulator.finance.instrument.active.ActiveInstrumentService;
import com.business.market.simulator.finance.owner.Company;
import com.business.market.simulator.finance.owner.Owner;
import com.business.market.simulator.finance.owner.OwnerService;
import com.business.market.simulator.finance.owner.OwnerType;
import com.business.market.simulator.user.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Setter(onMethod_ = @Autowired)
@Component
public class SimulationDataInitializer {

    OwnerService ownerService;
    FinancialInstrumentService financialInstrumentService;
    ActiveInstrumentService activeInstrumentService;
    UserService userService;

    @Transactional
    @PostConstruct
    public void initializeSimulationData() throws ActiveInstrumentException {
        Company techCoreSolutions = ownerService.createCompany("TechCore Solutions", "50000000", "10000000", "2000000", "1.20", OwnerType.PLC);
        Company globalOilEnergyCorp = ownerService.createCompany("Global Oil Energy Corp", "80000000", "15000000", "1500000", "0.80", OwnerType.PLC);
        Company interstellarPharmaceuticals = ownerService.createCompany("Interstellar Pharmaceuticals", "30000000", "5000000", "500000", "0.50", OwnerType.PLC);
        Company quantumaniaIndustries = ownerService.createCompany("Quantumania Industries", "100000000", "20000000", "3000000", "1.50", OwnerType.PLC);
        Company brightMoonTechnologies = ownerService.createCompany("BrightMoon Technologies", "60000000", "12000000", "1000000", "1.00", OwnerType.PLC);
        FinancialInstrument tcs = financialInstrumentService.createFinancialInstrument(techCoreSolutions, InstrumentType.SHARE, "TCS", Sector.TECHNOLOGY);
        FinancialInstrument goec = financialInstrumentService.createFinancialInstrument(globalOilEnergyCorp, InstrumentType.SHARE, "GOEC", Sector.ENERGY);
        FinancialInstrument ipl = financialInstrumentService.createFinancialInstrument(interstellarPharmaceuticals, InstrumentType.SHARE, "IPL", Sector.HEALTHCARE);
        FinancialInstrument qi = financialInstrumentService.createFinancialInstrument(quantumaniaIndustries, InstrumentType.SHARE, "QI", Sector.TECHNOLOGY);
        FinancialInstrument bmt = financialInstrumentService.createFinancialInstrument(brightMoonTechnologies, InstrumentType.SHARE, "BMT", Sector.TECHNOLOGY);
        activeInstrumentService.createShares(tcs, new BigDecimal("20"),15000);
        activeInstrumentService.createShares(goec, new BigDecimal("10"),10000);
        activeInstrumentService.createShares(ipl, new BigDecimal("5"),10000);
        activeInstrumentService.createShares(qi, new BigDecimal("15"),5000);
        activeInstrumentService.createShares(bmt, new BigDecimal("8"),10000);
        Owner statesTreasury = ownerService.createOwner("United States Department of the Treasury", OwnerType.TREASURY);
        Owner bankOfEngland = ownerService.createOwner("Bank of England", OwnerType.TREASURY);
        Owner ministryJapan = ownerService.createOwner("Ministry of Finance (Japan)", OwnerType.TREASURY);
        Owner germanyBank = ownerService.createOwner("Bundesfinanzagentur (Germany)", OwnerType.TREASURY);
        Owner NBP = ownerService.createOwner("Narodowy Bank Polski", OwnerType.TREASURY);
        FinancialInstrument tbond = financialInstrumentService.createFinancialInstrument(statesTreasury, InstrumentType.TREASURY_BOND, "T-BOND23", Sector.FINANCE);
        FinancialInstrument uksov = financialInstrumentService.createFinancialInstrument(bankOfEngland, InstrumentType.TREASURY_BOND, "UK-SOV23", Sector.FINANCE);
        FinancialInstrument jap = financialInstrumentService.createFinancialInstrument(ministryJapan, InstrumentType.TREASURY_BOND, "JAP-GOV23", Sector.FINANCE);
        FinancialInstrument ger = financialInstrumentService.createFinancialInstrument(germanyBank, InstrumentType.TREASURY_BOND, "GER-FED23", Sector.FINANCE);
        FinancialInstrument nbpinf = financialInstrumentService.createFinancialInstrument(NBP, InstrumentType.TREASURY_BOND, "INF-GOV23", Sector.FINANCE);
        activeInstrumentService.createTreasuryBonds(tbond, BigDecimal.valueOf(10000),2.5,24,10000);
        activeInstrumentService.createTreasuryBonds(uksov, BigDecimal.valueOf(5000),1.8,12,10000);
        activeInstrumentService.createTreasuryBonds(jap, BigDecimal.valueOf(8500),1.2,6,10000);
        activeInstrumentService.createTreasuryBonds(ger, BigDecimal.valueOf(7000),1.5,6,10000);
        activeInstrumentService.createTreasuryBonds(nbpinf, BigDecimal.valueOf(500),16.0,48,10000);
        userService.createUser("SIM1us","zaq1@WSX",new BigDecimal("100000000000"));
        userService.createUser("SIM2us","zaq1@WSX",new BigDecimal("100000000000"));
        userService.createUser("SIM3us","zaq1@WSX",new BigDecimal("100000000000"));
    }
}
