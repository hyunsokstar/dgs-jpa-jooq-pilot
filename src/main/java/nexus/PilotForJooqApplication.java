//src\main\java\nexus\PilotForJooqApplication.java
package nexus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PilotForJooqApplication {

	public static void main(String[] args) {
		SpringApplication.run(PilotForJooqApplication.class, args);
	}

}
