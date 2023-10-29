package fitnesscenter.membershipfitnesscenter.repository;

import fitnesscenter.membershipfitnesscenter.model.ServiceMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IServiceMenuRepository extends JpaRepository<ServiceMenu, Long> {
}
