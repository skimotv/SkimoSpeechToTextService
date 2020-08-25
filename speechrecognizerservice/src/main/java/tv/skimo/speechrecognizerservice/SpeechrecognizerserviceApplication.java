package tv.skimo.speechrecognizerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@ComponentScan("tv.skimo")
@EnableScheduling
public class SpeechrecognizerserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpeechrecognizerserviceApplication.class, args);
	}

}
