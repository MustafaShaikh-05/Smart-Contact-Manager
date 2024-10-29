package in.pkg.main.repository;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.pkg.main.entities.Contact;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
	
	// Fetch all Contacts and also do pagination
	
	@Query("from Contact as c where c.user.id =:userId")
	public Page<Contact> findContactsByUser(@Param("userId")int userId,Pageable pePagable);
	
		
}
