package in.pkg.main.controller;

import java.io.File;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import in.pkg.main.entities.Contact;
import in.pkg.main.entities.User;
import in.pkg.main.helper.Message;
import in.pkg.main.repository.ContactRepository;
import in.pkg.main.repository.UserRepo;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;




@Controller
@RequestMapping("user")
public class UserController
{
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private ContactRepository contactRepository;
	
	//Common Method to add user (Add Common Data)
	
	@ModelAttribute
	public void addCommonData(Model model,Principal principal)
	{
		String username = principal.getName();
		System.out.println("Username is "+username);
		
		User user =  userRepo.getUserByUserName(username);
		System.out.println("User "+user);
		
		//get user using username(Email)
		// Send user to dashboard.html
		
		model.addAttribute("user",user);
	}
	
	
	// Dashboard of Home
	@RequestMapping("/index")
	public String dashboard(Model model,Principal principal) 
	{
		String username = principal.getName();
		System.out.println("Username is "+username);
		
		User user =  userRepo.getUserByUserName(username);
		System.out.println("User "+user);
		
		//get user using username(Email)
		// Send user to dashboard.html
		
		model.addAttribute("user",user);
		model.addAttribute("title","User Dashboard");
	
		return "normal/user_dashboard";
	}
	
	
	// Open ADD CONTACT FORM Handler
	
	@GetMapping("/add-contact")
	public String openAddContactForm(Model model)
	{
		model.addAttribute("title","Add Contact");
		model.addAttribute("contact",new Contact());
		
		return "normal/add_contact_form";
	}
	
	
	// Backend For add contact form usr-add-contact
	
	@PostMapping("/process-contact")
	public String processContact(@Valid @ModelAttribute Contact contact,
			BindingResult bindingResult,
			@RequestParam("c_image") MultipartFile file,
			Principal principal,
			HttpSession session)
	{
		
		try
		{
			//to get user details and 
			//save in database 
			String name = principal.getName();
			User user = this.userRepo.getUserByUserName(name);
			
			/*
			 * if(3>2) { throw new Exception(); }
			 */
			
			
			// Processing and uploading file
			 if(file.isEmpty())
			 {
				 System.out.println("An Image file is required");
				 contact.setC_image("defaultimg.jpg");
				 //return "normal/add_contact_form";
			 }
			 else
			 {
				 // file to folder and save in db of contact
				 contact.setC_image(file.getOriginalFilename());
				 
				 File saveFile =  new ClassPathResource("static/img").getFile();
				 
				Path path =  Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
				 
				 Files.copy(file.getInputStream(), path , StandardCopyOption.REPLACE_EXISTING);
				 
				 System.out.println("Image Uploaded Successfully");
			 }
			
			
			contact.setUser(user); //Contact ko dena tha user
			user.getContacts().add(contact); //user ko dena tha contact
			
			//Final Saving
			this.userRepo.save(user);
			
			System.out.println("\n\nSuccessfully Added in database\n");
			
			// For Check and contact field name and form field name must be same
			//Just For Check
			//System.out.println("\nContact Details Data "+contact); 
			
			//message success
			session.setAttribute("message", new Message("Your Contact is added successfully","success"));

		}
		catch(Exception e)
		{
			e.printStackTrace();
			//Error Message
			session.setAttribute("message", new Message("All Fields are required... Try again","danger"));
		}
		return "normal/add_contact_form";
	}
	
	
	// Show all Contacts handler
	
	@GetMapping("/show-contacts/{page}")
	private String showContacts(@PathVariable("page") Integer page,Model model,Principal principal)
	{
		model.addAttribute("title","Show User Contacts");
		
		// Contacts ki saari list database se bhejni he
		//Only those contacts who is login
		// Per Page we will print only 5 contacts 
		String userName = principal.getName();
		User user = this.userRepo.getUserByUserName(userName);
		
		Pageable pageable = PageRequest.of(page, 5);
		
		Page<Contact> contacts = this.contactRepository.findContactsByUser(user.getId(),pageable);
		
		model.addAttribute("contacts",contacts);
		model.addAttribute("currentPage",page);
		model.addAttribute("totalPages",contacts.getTotalPages());
		
		
		
		return "normal/show_contacts";
	}

	
	// Showing specific contact details 
	
	@GetMapping("/{c_id}/contact")
	public String showContactDetails(@PathVariable("c_id") Integer c_id,Model model,Principal principal)
	{
		System.out.println("\nContact Id "+c_id);
		
		// Fetching Contact Details
		
		String userName = principal.getName();
		User user = this.userRepo.getUserByUserName(userName);
		
		Optional<Contact> contactOptional = this.contactRepository.findById(c_id);
		Contact contact = contactOptional.get();
		
		
		if(user.getId() == contact.getUser().getId())
		{
		
		model.addAttribute("contact",contact);
		model.addAttribute("title",contact.getC_name());
		}
		
		
		return "normal/contact_details";
	}
	
	//Delete Contact Handler
	
	@GetMapping("/delete/{c_id}")
	public String deleteContact(@PathVariable("c_id") Integer c_id,Model model,Principal principal,HttpSession session)
	{
		
		// Fetch Cid First
		
		Optional<Contact> contactOptional = this.contactRepository.findById(c_id);
		Contact contact = contactOptional.get();
		
		// check is also required 
		String userName = principal.getName();
		User user = this.userRepo.getUserByUserName(userName);
		if(user.getId() == contact.getUser().getId())
		{
			
		//contact.setUser(null);	
			
			User currentUser = this.userRepo.getUserByUserName(principal.getName());
			
			currentUser.getContacts().remove(contact);
			
			this.userRepo.save(currentUser);
		
		
		
		session.setAttribute("message", new Message("Contact Deleted Successfully","success"));
		}
		
		return "redirect:/user/show-contacts/0";
	}
	
	
	// Update form handler
	
	@PostMapping("/update-contact/{c_id}")
	private String updateForm(@PathVariable("c_id") Integer c_id,Model model)
	{
		
		model.addAttribute("title","Update Contact");
		
		Contact contact = this.contactRepository.findById(c_id).get();
		model.addAttribute("contact",contact);
		return "normal/update_form";
	}
	
	
	// Updating Contact Handler
	
	@PostMapping("/process-update")
	public String updateHandler(@Valid @ModelAttribute Contact contact,
			BindingResult bindingResult,
			@RequestParam("c_image") MultipartFile file,
			Principal principal,
			Model model,HttpSession session)
	{
		try
		{
			//Old Contact Details
			Contact oldContactDetail =  this.contactRepository.findById(contact.getC_id()).get();
			
			if(file.isEmpty())
			{
				//rewrite
				//1. delete old photo
				File deleteFile =  new ClassPathResource("static/img").getFile();
				File file1 = new File(deleteFile,oldContactDetail.getC_image());
				file1.delete();
				
				//2. update new photo
				
				 File saveFile =  new ClassPathResource("static/img").getFile();
				 
					Path path =  Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
					 
					 Files.copy(file.getInputStream(), path , StandardCopyOption.REPLACE_EXISTING);
					 
					 contact.setC_image(file.getOriginalFilename());
			}
			else
			{	//Agr Empty h image
				contact.setC_image(oldContactDetail.getC_image());
			}
			
			// For Current User
			
			User user = this.userRepo.getUserByUserName(principal.getName());
			contact.setUser(user);
			this.contactRepository.save(contact);
			
			session.setAttribute("message", new Message("Contact Successfully Updated","success"));
			
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("Updating Contact Handler");
		System.out.println("\n\nConatact Name  "+contact.getC_name());
		System.out.println("\n\nConatact Id  "+contact.getC_id());
		return "redirect:/user/"+contact.getC_id()+"/contact";
	}
	
	
	// User profile which is login 
	@GetMapping("/profile")
	public String yourProfile(Model model)
	{
		
		model.addAttribute("title","Profile Page");
		return "normal/profile";
	}
	
}
