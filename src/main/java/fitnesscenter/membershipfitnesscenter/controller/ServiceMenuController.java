package fitnesscenter.membershipfitnesscenter.controller;

import fitnesscenter.membershipfitnesscenter.dto.DtoAddServiceMenuRequest;
import fitnesscenter.membershipfitnesscenter.model.ServiceMenu;
import fitnesscenter.membershipfitnesscenter.service.ServiceMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/service-menu")
public class ServiceMenuController {
    @Autowired
    private ServiceMenuService serviceMenuService;

    @GetMapping("/list")
    public ResponseEntity<List<ServiceMenu>> getAllServiceMenus() {
        List<ServiceMenu> serviceMenuList = serviceMenuService.getAllServiceMenus();
        return ResponseEntity.ok(serviceMenuList);
    }

    // Hanya untuk inject data supaya lebih rapi
    @PostMapping("/add")
    public ResponseEntity<ServiceMenu> addServiceMenu(@RequestBody DtoAddServiceMenuRequest dtoAddServiceMenuRequest) {
        ServiceMenu serviceMenu = serviceMenuService.addServiceMenu(dtoAddServiceMenuRequest);
        return ResponseEntity.ok(serviceMenu);
    }

}
