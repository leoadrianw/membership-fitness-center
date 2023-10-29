package fitnesscenter.membershipfitnesscenter.controller;

import fitnesscenter.membershipfitnesscenter.model.ServiceMenu;
import fitnesscenter.membershipfitnesscenter.service.ServiceMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/service-menu")
public class ServiceMenuController {
    @Autowired
    private ServiceMenuService serviceMenuService;

    @GetMapping("/list")
    public ResponseEntity<List<ServiceMenu>> getAllServiceMenus() {
        List<ServiceMenu> serviceMenus = serviceMenuService.getAllServiceMenus();
        return ResponseEntity.ok(serviceMenus);
    }

}
