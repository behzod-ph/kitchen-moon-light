package uz.isdaha.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.isdaha.entity.HelpTable;
import uz.isdaha.service.DeveloperService;

import java.util.List;


@Repository

public interface HelperRepository extends JpaRepository<HelpTable, Long> {

    @Query(value = "select ip , count(ip)from help_table\n" +
        "group by ip;\n", nativeQuery = true)
    List<DeveloperService.CountIp> getCountIp();

    List<HelpTable> findByIp(String ip);


}
