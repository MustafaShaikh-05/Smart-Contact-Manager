package in.pkg.main.config;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import in.pkg.main.entities.User;

public class CustomUserDetail implements UserDetails
{
	
	private User user;

	
	public CustomUserDetail(User user) {
		super();
		this.user = user;
	}


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		SimpleGrantedAuthority simpleGrantedAuthority =  new SimpleGrantedAuthority(user.getRole());
		
		return List.of(simpleGrantedAuthority);
	}


	@Override
	public String getPassword() {
		return user.getPassword();
	}


	@Override
	public String getUsername() {
		
		return user.getEmail();
	}

	

}
