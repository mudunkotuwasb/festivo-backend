package bhagya.festivo.festivo.model;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TestUserDTO {

    private String name;
    private String email;
    private String password;
    private String role;
    private String status;
    private List<CustomerDTO> customerDTO;

}
