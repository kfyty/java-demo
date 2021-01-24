package com.kfyty.table.struct.export;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class TableStructExportMain {
    private static TableStructExportMain self;

    @Autowired
    public void setSelf(TableStructExportMain self) {
        TableStructExportMain.self = self;
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(TableStructExportMain.class, args);
    }
}
