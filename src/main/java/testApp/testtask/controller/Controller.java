package testApp.testtask.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import testApp.testtask.entity.Equation;
import testApp.testtask.service.EquationService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Controller implements CommandLineRunner {

    private final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    private final EquationService equationService;


    @Override
    public void run(String... args) {
        System.out.println("Welcome to Equation Checker");
        while (true) {
            menu();
            try {
                String choice = bufferedReader.readLine();
                switch (choice) {
                    case "1" -> this.inputEquation();
                    case "2" -> this.inputEquationWithArgument();
                    case "3" -> this.getEquation();
                    case "0" -> System.exit(0);
                    default -> System.out.println("Incorrect input");
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }



    public void menu() {
        System.out.println();
        System.out.println("1. Input your equation without argument");
        System.out.println("2. Input your equation with argument");
        System.out.println("3. Get equations");
        System.out.println("0. Exit");
        System.out.print("Choose your option: ");
    }

    private void getEquation() throws IOException {
        System.out.println();
        System.out.println("1. Get all equations");
        System.out.println("2. Get all equations with roots");
        System.out.println("3. Get all equations without roots");
        System.out.println("4. Get all equations with your root");
        System.out.print("Choose your option: ");
        String choice = bufferedReader.readLine();
        switch (choice) {
            case "1" -> getAllEquations();
            case "2" -> getAllEquationsWithRoots();
            case "3" -> getAllEquationsWithOutRoots();
            case "4" -> getAllEquationsWithRoot();
            default -> System.out.println("Incorrect input");
        }
    }

    private void getAllEquationsWithRoot() throws IOException {
        System.out.print("Input your root: ");
        String stringRoot = bufferedReader.readLine();
        try {
            long root = Long.parseLong(stringRoot);
            int i = 1;
            List<Equation> equationList = equationService.findAllWithRoot(root);
            if (!equationList.isEmpty()) {
                for (Equation equation : equationList) {
                    System.out.println("Equation " +i + ": " +equation.getEquation() + " root:" + equation.getRoot());
                    i++;
                }
            }
            else System.out.println("No Equations found");
        }catch (NumberFormatException e) {
            System.out.println("Invalid number format");
        }

    }

    private void getAllEquationsWithOutRoots() {
        int i = 1;
        List<Equation> equationList = equationService.findAllWithOutRoots();
        if (!equationList.isEmpty()) {
            for (Equation equation : equationList) {
                System.out.println("Equation " + i + ": " + equation.getEquation());
                i++;
            }
        }else System.out.println("No Equations found");
    }

    private void getAllEquationsWithRoots() {
        int i = 1;
        List<Equation> equationList =equationService.findAllWithRoots();
        if (!equationList.isEmpty()) {
            for (Equation equation : equationList) {
                System.out.println("Equation " + i + ": " + equation.getEquation() + " root:" + equation.getRoot());
                i++;
            }
        }else System.out.println("No Equations found");
    }

    private void getAllEquations() {
        int i = 1;
        List<Equation> equationList = equationService.findAll();
        if (!equationList.isEmpty()) {
            for (Equation equation : equationList) {
                if (equation.getRoot() != null) {
                    System.out.println("Equation " + i + ": " + equation.getEquation() + " root:" + equation.getRoot());
                } else {
                    System.out.println("Equation " + i + ": " + equation.getEquation());
                }
                i++;
            }
        }else System.out.println("No Equations found");
    }

    private void inputEquationWithArgument() throws IOException {
        System.out.print("Input your equation:");
        String equation = bufferedReader.readLine();
        System.out.print("Input your argument:");
        String argument = bufferedReader.readLine();
        if (equationService.createEquationWithArgument(equation, argument)) {
            System.out.println("Equation is valid and saved to database");
        } else {
            System.out.println("Equation or argument is not valid");
        }
    }

    private void inputEquation() throws IOException {
        System.out.print("Input your equation:");
        String equation = bufferedReader.readLine();

        if (equationService.createEquation(equation)) {
            System.out.println("Equation is valid and saved to database");
        } else {
            System.out.println("Equation is not valid");
        }
    }
}
