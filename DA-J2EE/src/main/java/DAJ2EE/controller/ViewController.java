package DAJ2EE.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping({"/", "/admin", "/admin/auth", "/payment-result"})
    public String forwardToIndex() {
        return "forward:/index.html";
    }
}
