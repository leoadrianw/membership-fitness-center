package fitnesscenter.membershipfitnesscenter.service;

import fitnesscenter.membershipfitnesscenter.dto.DtoAddServiceMenuRequest;
import fitnesscenter.membershipfitnesscenter.dto.DtoExercise;
import fitnesscenter.membershipfitnesscenter.model.Exercise;
import fitnesscenter.membershipfitnesscenter.model.ServiceMenu;
import fitnesscenter.membershipfitnesscenter.repository.IServiceMenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceMenuService {
    @Autowired
    private IServiceMenuRepository serviceMenuRepository;

    public List<ServiceMenu> getAllServiceMenus() {
        return serviceMenuRepository.findAll();
    }

    public ServiceMenu addServiceMenu(DtoAddServiceMenuRequest dtoAddServiceMenuRequest) {
        ServiceMenu serviceMenu = new ServiceMenu();
        serviceMenu.setName(dtoAddServiceMenuRequest.getName());
        serviceMenu.setSchedule(dtoAddServiceMenuRequest.getSchedule());
        serviceMenu.setDurationInMinutes(dtoAddServiceMenuRequest.getDurationInMinutes());
        serviceMenu.setPricePerSession(dtoAddServiceMenuRequest.getPricePerSession());
        serviceMenu.setTotalSessions(dtoAddServiceMenuRequest.getTotalSessions());
        List<Exercise> exerciseList = new ArrayList<>();
        dtoAddServiceMenuRequest.getExerciseList().forEach(dtoExercise -> {
            Exercise exercise = new Exercise();
            exercise.setName(dtoExercise.getName());
            exercise.setDescription(dtoExercise.getDescription());
            exercise.setDurationInMinutes(dtoExercise.getDurationInMinutes());
            exercise.setServiceMenu(serviceMenu);
            exerciseList.add(exercise);
        });
        serviceMenu.setExerciseList(exerciseList);
        serviceMenuRepository.save(serviceMenu);

        return serviceMenu;
    }

}
