package sa3.fijiimpulse;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import sa3.fijiimpulse.entity.User;
import sa3.fijiimpulse.repositories.UserDAO;

import java.util.List;

@SpringBootApplication
public class FijiimpulseApplication {

	public static void main(String[] args) {
		SpringApplication.run(FijiimpulseApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(UserDAO dao) {
		return args -> {
//			System.out.println("Welcome to Fiji Impulse");
//			saveData(dao);
//			deleteData(dao);
//			getData(dao);
//			getAllUsers(dao);
//			updateData(dao);
		};
	}

	public void saveData(UserDAO dao){
		User obj1 = new User("hiw", "hiw@gmail.com", "passwd", "customer");
		dao.save(obj1);
		System.out.println("User saved");
	}

	public void deleteData(UserDAO dao){
		int userId = 4;
		dao.delete(userId);
		System.out.println("User deleted");
	}

	public void getData(UserDAO dao){
		int userId = 1;
		User user = dao.getUserById(userId);
		System.out.println(user);
	}

	public void getAllUsers(UserDAO dao) {
		List<User> users = dao.getAllUsers();
		for (User user : users) {
			System.out.println(user);
		}
	}

	public void updateData(UserDAO dao) {
		int userId = 6;
		User user = dao.getUserById(userId);
		System.out.println(user);
		user.setUsername("mai");
		user.setEmail("mai@gmail.com");
		dao.updateUser(user);
		System.out.println("User updated");
		System.out.println(user);
	}
}
