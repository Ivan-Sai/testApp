package testApp.testtask.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import testApp.testtask.entity.Equation;

import java.util.List;

public interface EquationRepository extends JpaRepository<Equation, Long> {


    boolean existsByEquationAndRoot(String equation, long root);

    boolean existsByEquationAndRootIsNull(String equationString);

    List<Equation> findAllByRootIsNotNull();

    List<Equation> findAllByRootIsNull();

    List<Equation> findAllByRoot(long root);
}
