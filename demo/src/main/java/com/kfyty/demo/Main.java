package com.kfyty.demo;

import com.kfyty.boot.K;
import com.kfyty.core.autoconfig.annotation.BootApplication;
import com.kfyty.database.generator.config.annotation.EnableAutoGenerate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@BootApplication
@EnableAutoGenerate(templatePrefix = "space.en")
public class Main {

    public static void main(String[] args) throws Exception {
        K.run(Main.class, args);
    }
}
