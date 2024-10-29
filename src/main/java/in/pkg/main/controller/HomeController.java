package in.pkg.main.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import in.pkg.main.entities.User;
import in.pkg.main.helper.Message;
import in.pkg.main.repository.UserRepo;
import jakarta.servlet.http.HttpSession;



@Controller
public class HomeController 
{
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	//Dao Object to save data
	@Autowired
	private UserRepo userRepo;
	
	
	// 1st Home Page Chalega
	@GetMapping("/")
	public String home(Model model)
	{
		model.addAttribute("title","Home - Smart Contact Manager");
		return "home";
	}
	
	@GetMapping("/about")
	public String about(Model model)
	{
		model.addAttribute("title","About - Smart Contact Manager");
		return "about";
	}
	
	// 2nd Handler for Sign Up Page
	@GetMapping("/signup")
	public String signup(Model model)
	{
		
		model.addAttribute("title","Register-Smart Contact Manager");
		// Take data from form and store in db
		model.addAttribute("user",new User());
		return "signup";
	}
	
	//3rd handler for registering user that is for formmmmmmm 
	// main data yha par aayega form se
	@PostMapping("/register")										//for check box below
	public String registerUser(@ModelAttribute("user") User user,BindingResult result1,@RequestParam(value = "agreement",defaultValue = "false") boolean agreement , Model model,HttpSession session)
	{
		
		try
		{
			
			if(!agreement)
			{
				System.out.println("You have not accepted the terms and conditions");
				throw new Exception("You have not accepted the terms and conditions");
				
			}
			
			if(result1.hasErrors())
			{
				System.out.println("\n ERROR "+result1.toString());
				model.addAttribute("user",user);
				return "signup";
			}
			
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			
			//For check	
				System.out.println("Agreement "+agreement);
				System.out.println("User"+user);
				
				
				// For save
				User result = this.userRepo.save(user);
				
				
				// Issee yhi data apne form par wapas se show hoga
				model.addAttribute("user",new User());
				
				session.setAttribute("message",new Message("Successfully Registered","alert-success"));
				return "signup";
				
				// we will use dao object to save data
				
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			model.addAttribute("user",user);
			session.setAttribute("message", new Message("Something Went Wrong please try again later","alert-danger"));
			return "signup";
			
		}
		
			
			
	}
	
	
	// Handler for custom Login
	
	@GetMapping("/signin")
	public String customLogin(Model model)
	{
		
		model.addAttribute("title","Login Page");
		return "login";
	}
	
	
}
