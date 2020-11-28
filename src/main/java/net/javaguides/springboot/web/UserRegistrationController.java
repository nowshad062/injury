package net.javaguides.springboot.web;

import com.lowagie.text.DocumentException;
import net.javaguides.springboot.model.Injury;
import net.javaguides.springboot.model.Patient;
import net.javaguides.springboot.model.Role;
import net.javaguides.springboot.model.User;
import net.javaguides.springboot.repository.PatientRepository;
import net.javaguides.springboot.repository.PostRepository;
import net.javaguides.springboot.repository.UserRepository;
import net.javaguides.springboot.repository.VoteCountRepository;
import net.javaguides.springboot.service.EmailService;
import net.javaguides.springboot.service.Html2PdfService;
import net.javaguides.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/")
public class UserRegistrationController {

	private UserService userService;
	private EmailService emailService;
	@Autowired UserRepository userRepository;
	@Autowired
	PostRepository postRepository;
	@Autowired
	VoteCountRepository voteCountRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	PatientRepository patientRepository;

	User loggedInUser = null;

	@GetMapping("/import")
	public String generateUser() {
		if(userRepository.count() == 0){

			Role roleAdmin = new Role();
			roleAdmin.setName("Admin");
			User user1 = new User();
			user1.setCode("123456");
			user1.setPassword("1234");
			user1.setRole(roleAdmin);
			userRepository.save(user1);

			Role roleStaff = new Role();
			roleStaff.setName("Staff");
			User user2 = new User();
			user2.setCode("654321");
			user2.setPassword("1234");
			user2.setRole(roleStaff);
			userRepository.save(user2);

			Role roleClient = new Role();
			roleClient.setName("Limited User");
			User user3 = new User();
			user3.setCode("456789");
			user3.setPassword("1234");
			user3.setRole(roleClient);
			userRepository.save(user3);

		}
		return "redirect:/";
	}

	@GetMapping("/")
	public String root(Model model, User user) {

		return login(model,user);
	}

	@GetMapping("/getpassword")
	public String getPassword(Model model, User user) {
		return "getpassword";
	}

	@PostMapping("/sendpassword")
	public String getPassword(Model model, @RequestParam("username") String username) {
		User user = userRepository.findByCode(username);

		System.out.println("send email to " + user.getEmail() + " : " + user.getPassword());

		SimpleMailMessage registrationEmail = new SimpleMailMessage();
		registrationEmail.setTo(user.getEmail());
		registrationEmail.setSubject("Registration Confirmation");
		registrationEmail.setText("Your Password is:" + user.getPassword());
		registrationEmail.setFrom("info@33bcshealth.com");
		emailService.sendEmail(registrationEmail);

		return login(model, user);
	}

	@GetMapping("/register")
	public String register(Model model, User user) {
		return "registration";
	}

	@PostMapping("/register")
	public String processRegister(Model model, User user,  @RequestParam("username") String username,
								  @RequestParam("name") String name,
								  @RequestParam("email") String email,
								  @RequestParam("contact") String contact,
								  @RequestParam("facility") String facility,
								  @RequestParam("password") String password) {

		System.out.println(email);
		if(!name.isEmpty() && !username.isEmpty() && !email.isEmpty() && !password.isEmpty() && !contact.isEmpty()) {

			if(userRepository.findByCode(username) != null){
				model.addAttribute("error", true);
			}else {
				try {
					User newuser = new User();
					newuser.setCode(username);
					newuser.setName(name);
					newuser.setEmail(email);
					newuser.setContact(contact);
					newuser.setFacility(facility);
					newuser.setPassword(password);
					newuser.setBatch("33");
					userRepository.save(newuser);
					model.addAttribute("success", true);
				}catch (Exception e){
					model.addAttribute("exist", true);
					return register(model,user);
				}

			}
		}else{
			model.addAttribute("success", false);
			model.addAttribute("error", false);
			model.addAttribute("wrong", true);
			model.addAttribute("exist", false);
		}

		return register(model,user);
	}

	@GetMapping("/generate")
	public String showCertificateForm(Patient patient, Model model) {

		//patientRepository.incrementDispatchID();
		//long dispatchId = patientRepository.getNextDispatchId();
		Date date = new Date();
		//model.addAttribute("dispatchId", patient.getDispatchId());
		model.addAttribute("date", date.toString());
		return "generate-certificate";
	}

	@PostMapping("add")
	public String addCertificate(Patient patient, BindingResult result, Model model) {
		if (result.hasErrors()) {
			System.out.println(result.getAllErrors().toString());
			return "login";
		}
		List<Injury> injuries = new ArrayList<>();
		for(Injury injury : patient.getInjuries()){

			if(injury.getTypeOfInjury() != null && !injury.getTypeOfInjury().isEmpty() && injury.getTypeOfInjury() !="")
				injuries.add(injury);
		}
		patient.setInjuries(injuries);
		Date date = new Date();
		String generationDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
		patient.setGenerationDate(generationDate);
		patientRepository.save(patient);
		return "redirect:viewcertificate/" + patient.getId();
	}

	@GetMapping("viewcertificate/{id}")
	public String viewCertificate(@PathVariable("id") long id, Model model) throws ParseException {
		Patient patient = patientRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid Certificate Id:" + id));
		model.addAttribute("patient", patient);
		return "certificate";
	}
	@GetMapping("search")
	public String showSearch(Model model) {
		if(loggedInUser != null) {
			model.addAttribute("patients", patientRepository.findByOrderByIdDesc());
			model.addAttribute("user", loggedInUser);
            model.addAttribute("patient", new Patient());
			return "search";
		}
		return "login";
	}

	@GetMapping("edit/{id}")
	public String showUpdateForm(@PathVariable("id") long id, Model model) {
		Patient patient = patientRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid student Id:" + id));

		Date date = new Date();
		model.addAttribute("dispatchId", patient.getId()+"");
		model.addAttribute("date", date.toString());
		model.addAttribute("patient", patient);
		return "update-certificate";
	}

	@PostMapping("update/{id}")
	public String updateStudent(@PathVariable("id") long id, @Valid Patient patient, BindingResult result,
								Model model) {
		if (result.hasErrors()) {
			patient.setId(id);
			return "update-certificate";
		}
		Patient patientToUpdate = patientRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid dispatch Id:" + id));

		List<Injury> injuries = new ArrayList<>();
		for(Injury injury : patient.getInjuries()){

			if(injury.getTypeOfInjury() != null && !injury.getTypeOfInjury().isEmpty() && injury.getTypeOfInjury() !="")
				injuries.add(injury);
		}
		patient.setInjuries(injuries);
		patientToUpdate = patient;
		Date date = new Date();
		String generationDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
		patient.setGenerationDate(generationDate);
		patientRepository.save(patientToUpdate);

		model.addAttribute("patients", patientRepository.findByOrderByIdDesc());
		return "redirect:/search";
	}

	@GetMapping("delete/{id}")
	public String deleteStudent(@PathVariable("id") long id, Model model) {
		Patient patient = patientRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid student Id:" + id));
		if(patient != null)
			patientRepository.delete(patient);
		model.addAttribute("patients", patientRepository.findByOrderByIdDesc());
		return "redirect:/search";
	}

	@GetMapping("/logout")
	public String logout(Model model) {
		loggedInUser = null;
		return "login";
	}

	@GetMapping("/login")
	public String login(Model model, User user) {
		if(user.getCode() == null){
			model.addAttribute("sent", false);
		}else{
			model.addAttribute("sent", true);
		}
		return "login";
	}

	@PostMapping("/login")
	public String processLogin(Model model, @RequestParam("username") String username, @RequestParam("password") String password) {

		User user = userRepository.findByCodeAndPassword(username,password);
		if(user != null){
			loggedInUser = user;
			return "redirect:generate/";
		}
		else {
			System.out.println("Login Failed");
			model.addAttribute("error", true);
			return "login";
		}
	}


	private String parseThymeleafTemplate(Long id) {

		Patient patient = patientRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid patient Id:" + id));

		ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode(TemplateMode.HTML);

		TemplateEngine templateEngine = new TemplateEngine();
		templateEngine.setTemplateResolver(templateResolver);

		Context context = new Context();
		context.setVariable("patient", patient);
		return templateEngine.process("pdfcertificate", context);
	}

	public void generatePdfFromHtml(String html) throws IOException, DocumentException {
		String outputFolder = System.getProperty("user.home") + File.separator + "thymeleaf.pdf";
		OutputStream outputStream = new FileOutputStream(outputFolder);
		ITextRenderer renderer = new ITextRenderer();
		renderer.setDocumentFromString(html);
		renderer.layout();
		renderer.createPDF(outputStream);
		outputStream.close();
	}

	@Autowired
	private Html2PdfService pdfService;

	@GetMapping("/print/{id}")
	public String print(@PathVariable("id") long id) throws IOException, DocumentException {

		Patient patient = patientRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid patient Id:" + id));

		System.out.println(patient.getId());

		Map parameters = new HashMap();
		parameters.put("patient", patient);
		String html = pdfService.template2Html("pdfcertificate", patient);
		generatePdfFromHtml(html);
		return "test";
	}

	@PostMapping("search")
	public String showSearchResult(Model model, Patient patient, @RequestParam("fromAge") String fromAge, @RequestParam("fromAge") String toAge,
								   @RequestParam("fromAge") String fromDateOfAdmission, @RequestParam("fromAge") String toDateOfAdmission) {
		if(loggedInUser != null) {

			String query="select * from patient p where p.id is not null ";
			if(!patient.getDispatchId().isEmpty())
				query += " and p.dispatch_id =" + patient.getDispatchId();
			if(!patient.getName().isEmpty())
				query += " and p.name ='" + patient.getName() + "'";
			if(!patient.getPhoneNo().isEmpty())
				query += " and p.phone_no ='" + patient.getPhoneNo()+ "'";
			if(patient.getSex() != null && !patient.getSex().isEmpty())
				query += " and p.sex ='" + patient.getSex()+ "'";
			if(!patient.getRelation().isEmpty())
				query += " and p.relation ='" + patient.getRelation()+ "'";
			if(!patient.getNid().isEmpty())
				query += " and p.nid ='" + patient.getNid()+ "'";
			if(!patient.getGurdian().isEmpty())
				query += " and p.gurdian ='" + patient.getGurdian()+ "'";
			if(!patient.getVillage().isEmpty())
				query += " and p.village ='" + patient.getVillage()+ "'";
			if(!patient.getPost().isEmpty())
				query += " and p.post ='" + patient.getPost()+ "'";
			if(!patient.getThana().isEmpty())
				query += " and p.thana ='" + patient.getThana()+ "'";
			if(!patient.getDistrict().isEmpty())
				query += " and p.district ='" + patient.getDistrict()+ "'";
			if(!patient.getWard().isEmpty())
				query += " and p.ward ='" + patient.getWard()+ "'";
			if(!patient.getDocket().isEmpty())
				query += " and p.docket ='" + patient.getDocket()+ "'";
			if(!patient.getDocketDate().isEmpty())
				query += " and p.docket_date ='" + patient.getDocketDate()+ "'";
			if(!patient.getRegNo().isEmpty())
				query += " and p.reg_no ='" + patient.getRegNo()+ "'";
			if(!fromAge.isEmpty() && !toAge.isEmpty())
				query += " and p.age ='" + patient.getRegNo()+ "'";

			System.out.println(query);
			List<Patient> patients = entityManager.createNativeQuery(query,Patient.class).getResultList();

			model.addAttribute("patients", patients);
			model.addAttribute("user", loggedInUser);
			model.addAttribute("patient", patient);
			return "search";
		}
		return "login";
	}

}
