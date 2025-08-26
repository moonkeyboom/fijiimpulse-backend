package sa3.fijiimpulse;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import sa3.fijiimpulse.entity.User;

import java.util.List;

@SpringBootApplication
public class FijiimpulseApplication {

	public static void main(String[] args) {
		SpringApplication.run(FijiimpulseApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner() {
		return args -> {
//			System.out.println("Welcome to Fiji Impulse");
//			saveData(dao);
//			deleteData(dao);
//			getData(dao);
//			getAllUsers(dao);
//			updateData(dao);
		};
	}
}

