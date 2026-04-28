package ro.alacrity.meetmaster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MeetMasterApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeetMasterApplication.class, args);
    }

}
