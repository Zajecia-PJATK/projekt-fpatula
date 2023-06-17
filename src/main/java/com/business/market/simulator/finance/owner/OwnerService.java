package com.business.market.simulator.finance.owner;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Setter(onMethod_ = @Autowired)
@Service
public class OwnerService {
    OwnerRepository ownerRepository;

    public Company createCompany(String name, String equityCapital, String income, String losses, String dividendsPerShare, OwnerType ownerType) {
        Company newCompany = new Company(ownerType);
        newCompany.setOwnerName(name);
        newCompany.setEquityCapital(new BigDecimal(equityCapital));
        newCompany.setIncome(new BigDecimal(income));
        newCompany.setLoses(new BigDecimal(losses));
        newCompany.setDividendsPerShare(new BigDecimal(dividendsPerShare));
        return ownerRepository.save(newCompany);
    }

    public Owner createOwner(String name, OwnerType ownerType) {
        Owner owner = new Owner();
        owner.setOwnerName(name);
        owner.setOwnerType(ownerType);
        return ownerRepository.save(owner);
    }

    public List<Owner> getAllAssetsOwners(){
        return ownerRepository.findAll();
    }
}
