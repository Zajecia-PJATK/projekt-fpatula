package com.business.market.simulator.finance.owner;

import jakarta.transaction.Transactional;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;
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
        return ownerRepository.saveAndFlush(newCompany);
    }

    public Owner createOwner(String name, OwnerType ownerType) {
        Owner owner = new Owner();
        owner.setOwnerName(name);
        owner.setOwnerType(ownerType);
        return ownerRepository.saveAndFlush(owner);
    }

    public List<Owner> saveOwners(Collection<Owner> owners){
        return ownerRepository.saveAllAndFlush(owners);
    }

    @Transactional
    public List<Owner> getAllAssetsOwners(){
        return ownerRepository.findAll();
    }
}
