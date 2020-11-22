package com.cjh;

import com.cjh.mail.MailComponent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EpidemicMoniterApplicationTests {

    @Autowired
    private MailComponent mailComponent;

    @Test
    void contextLoads() {
        mailComponent.send();
    }

}
