package coms309;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
class WelcomeController {

    @GetMapping("/")
    public String welcome() {
        return "Hello and welcome to COMS 309!!!!!! Cutler adjusted this for testing!.?!";
    }

    @GetMapping("/{name}")
    public String welcome(@PathVariable String name) {
        return "Welcome " + name + " I hope you had fun learning";
    }
    
    @GetMapping("/poem")
    public String poem() {
    	return "I am a crab who walks the shore and pinches toes all day."
    			+ " If I were you I'd wear some shoes and not get in my way.";
    }
}
