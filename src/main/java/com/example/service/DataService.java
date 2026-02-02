package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.repository.DataRepository;

@Service
public class DataService {

    private final DataRepository dataRepository;

    @Autowired
    public DataService(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    // Add methods to interact with the DataRepository here
    // For example:
    // public List<Data> getAllData() {
    //     return dataRepository.findAll();
    // }

    // public Data saveData(Data data) {
    //     return dataRepository.save(data);
    // }

    // public Optional<Data> getDataById(Long id) {
    //     return dataRepository.findById(id);
    // }

    // public void deleteData(Long id) {
    //     dataRepository.deleteById(id);
    // }
}