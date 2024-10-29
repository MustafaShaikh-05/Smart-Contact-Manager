package in.pkg.main.entities;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Contact 
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int c_id;
	private String c_name;
	private String c_s_name;
	private String work;
	private String c_email;
	private String phone;
	private String c_image;
	@Column(length = 5000)
	private String description;
	
	@ManyToOne
	private User user;
	
	public Contact(int c_id, String c_name, String c_s_name, String work, String c_email, String phone, String c_image,
			String description, User user) {
		super();
		this.c_id = c_id;
		this.c_name = c_name;
		this.c_s_name = c_s_name;
		this.work = work;
		this.c_email = c_email;
		this.phone = phone;
		this.c_image = c_image;
		this.description = description;
		this.user = user;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public int getC_id() {
		return c_id;
	}
	public void setC_id(int c_id) {
		this.c_id = c_id;
	}
	public String getC_name() {
		return c_name;
	}
	public void setC_name(String c_name) {
		this.c_name = c_name;
	}
	public String getC_s_name() {
		return c_s_name;
	}
	public void setC_s_name(String c_s_name) {
		this.c_s_name = c_s_name;
	}
	public String getWork() {
		return work;
	}
	public void setWork(String work) {
		this.work = work;
	}
	public String getC_email() {
		return c_email;
	}
	public void setC_email(String c_email) {
		this.c_email = c_email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getC_image() {
		return c_image;
	}
	public void setC_image(String c_image) {
		this.c_image = c_image;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	
	public Contact() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return this.c_id==((Contact)obj).getC_id();
	}
	
	
	
	
	
	
	
}
