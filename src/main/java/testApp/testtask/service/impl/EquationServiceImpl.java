package testApp.testtask.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.AllArgsConstructor;
import org.mvel2.MVEL;
import org.springframework.stereotype.Service;
import testApp.testtask.entity.Equation;
import testApp.testtask.repository.EquationRepository;
import testApp.testtask.service.EquationService;

@Service
@AllArgsConstructor
public class EquationServiceImpl implements EquationService {

    private final EquationRepository equationRepository;
    private static final Pattern VALID_EQUATION_PATTERN = Pattern.compile("[0-9+\\-*/=xх()\\.\\s]+");

    @Override
    public boolean createEquation(String equationString) {
        equationString = equationString.trim();
        if (!checkEquation(equationString)) return false;
        saveEquation(equationString);
        return true;
    }

    @Override
    public boolean createEquationWithArgument(String equationString, String argument) {
        equationString = equationString.trim();
        if (!checkEquation(equationString)) return false;
        if (checkResultEquals(equationString, argument)){
            saveEquation(equationString,argument);
            return true;
        }
        return false;
    }

    @Override
    public List<Equation> findAll() {
        return equationRepository.findAll();
    }

    @Override
    public List<Equation> findAllWithRoots() {
        return equationRepository.findAllByRootIsNotNull();
    }

    @Override
    public List<Equation> findAllWithOutRoots() {
        return equationRepository.findAllByRootIsNull();
    }

    @Override
    public List<Equation> findAllWithRoot(long root) {
        return equationRepository.findAllByRoot(root);
    }


    public boolean checkResultEquals(String equationString, String argument) {
        equationString = equationString.replaceAll("x", argument);
        String[] parts = equationString.split("=");
        if (parts.length != 2) {
            return false;
        }
        try {
            double leftResult = MVEL.eval(parts[0], Double.class);
            double rightResult = MVEL.eval(parts[1], Double.class);

            double epsilon = 1e-9;
            return Math.abs(leftResult - rightResult) < epsilon;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }


    public boolean checkEquation(String equationString) {
        Matcher matcher = VALID_EQUATION_PATTERN.matcher(equationString);
        if (!matcher.matches()) {
            return false;
        }

        if (!checkParentheses(equationString)) {
            return false;
        }

        if (!checkOperators(equationString)) {
            return false;
        }
        return true;
    }
    private boolean checkParentheses(String equation) {
        List<Character> list = new ArrayList<>();

        for (char c : equation.toCharArray()) {
            if (c == '(') {
                list.add(c);
            } else if (c == ')') {
                if (list.isEmpty() || list.remove(list.size() - 1) != '(') {
                    return false;
                }
            }
        }

        return list.isEmpty();
    }

    private boolean checkOperators(String equation) {
        for (int i = 0; i < equation.length() - 1; i++) {
            char current = equation.charAt(i);
            char next = equation.charAt(i + 1);

            if (isOperator(current) && isOperator(next) && next != '-') {
                return false;
            }
        }

        return true;
    }

    private boolean isNumeric(char c) {
        return Character.isDigit(c) || c == '.';
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    void saveEquation(String equationString, String argument) {
        Equation equation = new Equation();
        equation.setEquation(equationString);
        equation.setRoot(Long.parseLong(argument));
        if (!equationRepository.existsByEquationAndRoot(equationString,Long.parseLong(argument))) equationRepository.save(equation);
    }

    void saveEquation(String equationString){
        Equation equation = new Equation();
        equation.setEquation(equationString);
        if (!equationRepository.existsByEquationAndRootIsNull(equationString)) equationRepository.save(equation);
    }
}
