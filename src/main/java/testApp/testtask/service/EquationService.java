package testApp.testtask.service;

import testApp.testtask.entity.Equation;

import java.util.List;

public interface EquationService {


    boolean createEquation(String equation);

    boolean createEquationWithArgument(String equation, String argument);

    List<Equation> findAll();

    List<Equation> findAllWithRoots();

    List<Equation> findAllWithOutRoots();

    List<Equation> findAllWithRoot(long root);
}
