package in.pkg.main.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import in.pkg.main.entities.User;
import in.pkg.main.repository.UserRepo;

public class UserDetailsServiceImpl implements UserDetailsService
{
	@Autowired
	private UserRepo userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// Database se user ko lana he or return krna he 
		//that is we fetch user from database // we use dao
		
		User user = userRepo.getUserByUserName(username);
		
		if(user==null)
		{
			throw new UsernameNotFoundException("Could Not Found User");
		}
		
		CustomUserDetail customUserDetail = new CustomUserDetail(user);
		
		return customUserDetail;
	}

}
