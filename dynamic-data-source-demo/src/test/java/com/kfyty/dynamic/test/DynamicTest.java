package com.kfyty.dynamic.test;

import com.kfyty.dynamic.service.DynamicDataSourceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DynamicTest {
    @Autowired
    private DynamicDataSourceService dynamicDataSourceService;

    @Test
    public void test() throws Exception {
        System.out.println(dynamicDataSourceService);
    }
}
