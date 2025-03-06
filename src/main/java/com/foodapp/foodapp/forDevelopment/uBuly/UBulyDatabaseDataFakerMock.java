package com.foodapp.foodapp.forDevelopment.uBuly;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UBulyDatabaseDataFakerMock implements UBulyDatabaseDataFakerInterface {
    @Override
    public void initData() {
        log.info("Mocked fake init database ubuly");

    }
}
