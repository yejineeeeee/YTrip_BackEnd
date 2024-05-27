package CodeIt.Ytrip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class YtripApplication {

	public static void main(String[] args) {
		SpringApplication.run(YtripApplication.class, args);
	}

}
