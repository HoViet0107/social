package personal.social;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import personal.social.enums.RoleEnum;
import personal.social.enums.Status;
import personal.social.model.*;
import personal.social.repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
public class SocialApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SocialApplication.class, args);
	}

	private final UserRepository userRepo;
	private final RoleRepository roleRepos;
	private final PostRepository postRepos;
	private final PostContentRepository postContentRepos;
	private final CommentRepository commentRepos;
	private final CommentContentRepository cmtContentRepos;

	@Autowired
	public SocialApplication(
            UserRepository userRepo,
            RoleRepository roleRepos,
			PostRepository postRepos,
			PostContentRepository postContentRepos,
			CommentRepository commentRepos,
			CommentContentRepository cmtContentRepos) {
		this.userRepo = userRepo;
		this.roleRepos = roleRepos;
        this.postRepos = postRepos;
        this.postContentRepos = postContentRepos;
        this.commentRepos = commentRepos;
        this.cmtContentRepos = cmtContentRepos;
    }
	@Override
	public void run(String... args) throws Exception {
        LocalDateTime current = LocalDateTime.now();
        // add admin user
        User existedAd = null;
        if (userRepo.findByEmail("admin") == null) {
            User adminUser = new User();
            adminUser.setEmail("admin");
            adminUser.setPassword(new BCryptPasswordEncoder().encode("12345"));
            adminUser.setFirstName("Hồ");
            adminUser.setSurname("Quốc");
            adminUser.setLastName("Việt");
            adminUser.setPhone("0000000000");
            adminUser.setDob(LocalDate.parse("2000-01-01"));
            adminUser.setCreatedAt(LocalDateTime.now());

            userRepo.save(adminUser);

            existedAd = userRepo.findByEmail("admin");
            Roles adminRole = new Roles();
            adminRole.setRole(RoleEnum.ADMIN);
            adminRole.setUser(existedAd);
            roleRepos.save(adminRole);
        }
        /*
			// add post 1
			Post post = new Post(current,current,Status.ACTIVE,existedAd);
			postRepos.save(post);
			PostContent postContent = new PostContent("Post 1 content", current, post);
			postContentRepos.save(postContent);

			existedAd = userRepo.findByEmail("admin");
			// create comment 1
			Comment comment = new Comment(current,current,Status.ACTIVE, 0L,0L,existedAd,post,null);
			commentRepos.save(comment);
			CommentContent commentContent = new CommentContent("comment 1 content", current, comment);
			cmtContentRepos.save(commentContent);

			// create comment 2
			Comment comment1 = new Comment(current,current,Status.ACTIVE, 0L,0L,existedAd,post,comment);
			commentRepos.save(comment1);
			CommentContent comment1Content = new CommentContent("comment 2 content", current, comment1);
			cmtContentRepos.save(comment1Content);
        */
    }
}
