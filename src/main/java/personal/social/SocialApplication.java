package personal.social;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import personal.social.enums.Role;
import personal.social.model.User;
import personal.social.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
public class SocialApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SocialApplication.class, args);
	}

	private final UserRepository userRepo;

	@Autowired
	public SocialApplication(UserRepository userRepo) {
		this.userRepo = userRepo;
	}
	@Override
	public void run(String... args) throws Exception {
		// add admin user
		if(userRepo.findByEmail("admin") == null) {
			User adminUser = new User();
			adminUser.setEmail("admin");
			adminUser.setPassword(new BCryptPasswordEncoder().encode("12345"));
			adminUser.setFirst_name("Hồ");
			adminUser.setSurname("Quốc");
			adminUser.setLast_name("Việt");
			adminUser.setPhone("0000000000");
			adminUser.setDob(LocalDate.parse("2000-01-01"));
			adminUser.setCreated_at(LocalDateTime.now());
			adminUser.setRole(Role.ADMIN);
			userRepo.save(adminUser);
		}
	}
}
