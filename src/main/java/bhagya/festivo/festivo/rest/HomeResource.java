package bhagya.festivo.festivo.rest;

import bhagya.festivo.festivo.model.ReviewDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import bhagya.festivo.festivo.model.TestUserDTO;

import java.util.UUID;


@RestController
@RequestMapping(value = "/api")
public class HomeResource {

    @GetMapping("/")
    public String index() {
        return "\"Hello World!\"";
    }

    @GetMapping("/home")
    public ResponseEntity<TestUserDTO> getReview() {
        TestUserDTO testUser = new TestUserDTO();
        // Set properties of testUser as needed
        return ResponseEntity.ok(testUser);
    }

}
