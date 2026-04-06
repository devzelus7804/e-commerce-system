package DAJ2EE.Controller;

import DAJ2EE.Service.SizeService;
import DAJ2EE.entity.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sizes")
public class SizeController {

    @Autowired
    private SizeService sizeService;

    @GetMapping
    public List<Size> getAll() {
        return sizeService.findAll();
    }
}
