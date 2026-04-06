package DAJ2EE.Controller;

import DAJ2EE.entity.Color;
import DAJ2EE.Service.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/colors")
public class ColorController {

    @Autowired
    private ColorService colorService;

    @GetMapping
    public List<Color> getAll() {
        return colorService.findAll();
    }
}
