package personal.social;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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

	}
}
