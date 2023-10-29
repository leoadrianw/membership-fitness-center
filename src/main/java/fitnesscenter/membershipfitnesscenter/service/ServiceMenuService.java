package fitnesscenter.membershipfitnesscenter.service;

import fitnesscenter.membershipfitnesscenter.model.ServiceMenu;
import fitnesscenter.membershipfitnesscenter.repository.IServiceMenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceMenuService {
    @Autowired
    private IServiceMenuRepository serviceMenuRepository;

    public List<ServiceMenu> getAllServiceMenus() {
        return serviceMenuRepository.findAll();
    }

}
