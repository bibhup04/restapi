package com.learning.restapi.service;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learning.restapi.entity.Cycle;
import com.learning.restapi.entity.addNewCycle;
import com.learning.restapi.exception.CycleShopBusinessException;
import com.learning.restapi.repository.CycleRepository;

@Service
public class CycleService {
    @Autowired
    private CycleRepository cycleRepository;

    public void addCycle(addNewCycle addCycle){
        Cycle cycle = new Cycle();
        cycle.setBrand(addCycle.getBrand());
        cycle.setNumBorrowed(0);
        cycle.setStock(addCycle.getStock());
        cycleRepository.save(cycle);
    }

    public List<Cycle> listCycles() {
        var listFromDB = cycleRepository.findAll();
        var cycleList = new ArrayList<Cycle>();
        listFromDB.forEach(cycleList::add);
        return cycleList;
    }

    public List<Cycle> listAvailableCycles() {
        return listCycles()
        .stream()
        .filter(cycle -> cycle.getNumAvailable() > 0)
        .collect(Collectors.toList());
    }

    public Cycle findByIdOrThrow404(long id) {
        var optCycle = cycleRepository.findById(id);
        if (optCycle.isEmpty()) {
            throw new CycleShopBusinessException(
                String.format("Can't find the cycle with id %d in the DB",
                id));
        }
        return optCycle.get();
    }

    public void borrowCycle(long id, int count) {
        var cycle = findByIdOrThrow404(id);
        cycle.setNumBorrowed(cycle.getNumBorrowed() + count);
        cycleRepository.save(cycle);
    }

    public void returnCycle(long id, int count) {
        var cycle = findByIdOrThrow404(id);
        cycle.setNumBorrowed(cycle.getNumBorrowed() - count);
        cycleRepository.save(cycle);
    }

    public void borrowCycle(long id) {
        borrowCycle(id, 1);
    }

    public void returnCycle(long id) {
        returnCycle(id, 1);
    }

    public void restockBy(long id, int count) {
        var cycle = findByIdOrThrow404(id);
        cycle.setStock(cycle.getStock() + count);
        cycleRepository.save(cycle);
    }

}
