package net.javaguides.springboot.web;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import net.javaguides.springboot.model.User;
import net.javaguides.springboot.model.VoterPost;
import net.javaguides.springboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import net.javaguides.springboot.service.UserService;
import net.javaguides.springboot.web.dto.UserRegistrationDto;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/")
public class UserRegistrationController {

	private UserService userService;
	@Autowired UserRepository userRepository;

	public UserRegistrationController(UserService userService) {
		super();
		this.userService = userService;
	}

	// Return registration form template
	@RequestMapping(value="/apply", method = RequestMethod.GET)
	public String showApplication(Model model){

		//User user = new User("Adil","Medical", "772", "328974983");

		User user =  userRepository.findByCode("42365");

		List<VoterPost> selectablePosts = Arrays.asList(
				new VoterPost("Chairman"),
				new VoterPost( "Secretary")
		);
		model.addAttribute("user", user);
		model.addAttribute("selectablePosts", selectablePosts);
		return "apply";
	}


	@PostMapping("/apply")
	public String processNomination(@ModelAttribute("user") User user) {
		System.out.println(user.getPosts().toArray());
		userRepository.save(user);
		return "apply";
	}

	@GetMapping("/reg")
	public String index() {
		return "index";
	}

	@PostMapping("/upload-csv-file")
	public String uploadCSVFile(@RequestParam("file") MultipartFile file, Model model) {

		// validate file
		if (file.isEmpty()) {
			model.addAttribute("message", "Please select a CSV file to upload.");
			model.addAttribute("status", false);
		} else {

			// parse CSV file to create a list of `User` objects
			try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

				// create csv bean reader
				CsvToBean<User> csvToBean = new CsvToBeanBuilder(reader)
						.withType(User.class)
						.withIgnoreLeadingWhiteSpace(true)
						.build();

				// convert `CsvToBean` object to list of users
				List<User> users = csvToBean.parse();
				System.out.println(users.size());

				BufferedWriter writer = new BufferedWriter(new FileWriter("invalid.txt"));
				for (User user: users) {
					if(user.getBatch().equalsIgnoreCase("33")) {
						if(user.getName().isEmpty() && user.getCode().isEmpty() && user.getContact().isEmpty())
							continue;
						else if (user.getName() != null && !user.getName().isEmpty() && !user.getCode().isEmpty() && !user.getContact().isEmpty())
							userRepository.save(user);
						else
							writer.write(user.getCode() + " : " + user.getName() + " : " + user.getContact());
						writer.write("\n");
					}
				}
				writer.close();
				// TODO: save users in DB?

				// save users list on model
				model.addAttribute("users", users);
				model.addAttribute("status", true);

			} catch (Exception ex) {
				model.addAttribute("message", "An error occurred while processing the CSV file.");
				model.addAttribute("status", false);
			}
		}

		return "file-upload-status";
	}
	
//	@ModelAttribute("user")
//    public UserRegistrationDto userRegistrationDto() {
//        return new UserRegistrationDto();
//    }
//
	@GetMapping
	public String showRegistrationForm() {
		return "registration";
	}
	
//	@PostMapping
//	public String registerUserAccount(@ModelAttribute("user") UserRegistrationDto registrationDto) {
//		//userService.save(registrationDto);
//		return "redirect:/registration?success";
//	}
}
