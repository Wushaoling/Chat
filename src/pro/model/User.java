package pro.model;
/**
 * 用户模型
 * @author wushaoling
 *
 */
public class User {
	private String name;
	private String pwd;
	private String email;
	private String age;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	
	@Override
	public String toString() {
		return "User [name=" + name + ", pwd=" + pwd + ", email=" + email
				+ ", age=" + age + "]";
	}
	
	public User() {
		super();
	}
	
	public User(String name) {
		super();
		this.name = name;
	}
	
	public User(String name, String pwd) {
		super();
		this.name = name;
		this.pwd = pwd;
	}
	
	public User(String name, String pwd, String email, String age) {
		super();
		this.name = name;
		this.pwd = pwd;
		this.email = email;
		this.age = age;
	}
}
