package web.gradua;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GraduaApplication { // <--- Mudou aqui

    public static void main(String[] args) {
        // <--- E mudou aqui dentro também
        SpringApplication.run(GraduaApplication.class, args);
    }

}