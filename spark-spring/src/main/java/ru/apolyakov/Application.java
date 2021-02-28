package ru.apolyakov;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.apolyakov.service.JudgementService;
import ru.apolyakov.service.PopularWordsService;

import java.util.List;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(Application.class, args);
        JudgementService judgementService = applicationContext.getBean(JudgementService.class);
        List<String> top3 = judgementService.topX("beatles", 3);
        System.out.println(top3);
    }
}
