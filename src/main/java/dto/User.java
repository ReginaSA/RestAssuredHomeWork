package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@Data
@Builder
@AllArgsConstructor
@JsonSerialize
public class User{
	private String firstName;
	private String lastName;
	private String password;
	private int userStatus;
	private String phone;
	private int id;
	private String email;
	private String username;
}
