package com.foodapp.foodapp.forDevelopment;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DatabaseDataFakerMock implements DatabaseDataFakerInterface {
    @Override
    public void initFakeData() {
        log.info("Mocked fake init database");
    }
}
