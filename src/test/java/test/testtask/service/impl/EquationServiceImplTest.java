package test.testtask.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import test.testtask.entity.Equation;
import test.testtask.repository.EquationRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class EquationServiceImplTest {

    @Mock
    private EquationRepository equationRepository;

    @InjectMocks
    private EquationServiceImpl equationService;


    @Test
    void testSaveEquationWithArgument() {
        when(equationRepository.existsByEquationAndRoot(anyString(), anyLong())).thenReturn(false);
        equationService.saveEquation("2*x+5=17", "3");
        verify(equationRepository, times(1)).save(Mockito.any());
    }

    @Test
    void testSaveEquationWithoutArgument() {
        when(equationRepository.existsByEquationAndRootIsNull(anyString())).thenReturn(false);
        equationService.saveEquation("2*x+5=17");
        verify(equationRepository, times(1)).save(Mockito.any());
    }

    @Test
    void checkResultEquals() {
        EquationServiceImpl equationService = new EquationServiceImpl(null);
        assertTrue(equationService.checkResultEquals("2*x+5=17", "6"));
        assertFalse(equationService.checkResultEquals("2*x+5=17", "4"));
        assertFalse(equationService.checkResultEquals("invalid_equation", "3"));
    }

    @Test
    void checkEquation() {
        EquationServiceImpl equationService = new EquationServiceImpl(null);
        assertTrue(equationService.checkEquation("2*(x+5+х)+5=10"));
        assertFalse(equationService.checkEquation("invalid_equation"));
        assertFalse(equationService.checkEquation("2*(x+5+х))+5=10"));
        assertFalse(equationService.checkEquation("2*(x+5+х)++5=10"));
    }

    @Test
    void findAll() {
        List<Equation> expectedEquations = Arrays.asList(
                Equation.builder().id(1L).equation("3*x-8=2").build(),
                Equation.builder().id(2L).equation("3*x+8=11").root(1L).build()
        );
        when(equationRepository.findAll()).thenReturn(expectedEquations);
        List<Equation> actualEquations = equationService.findAll();

        assertEquals(expectedEquations.size(), actualEquations.size());
        for (int i = 0; i < expectedEquations.size(); i++) {
            assertEquals(expectedEquations.get(i), actualEquations.get(i));
        }

        verify(equationRepository, times(1)).findAll();
    }

    @Test
    void findAllWithRoots() {
        List<Equation> expectedEquations = Arrays.asList(
                Equation.builder().id(1L).equation("5*x-8=2").root(2L).build(),
                Equation.builder().id(2L).equation("3*x+8=11").root(1L).build()
        );
        when(equationRepository.findAllByRootIsNotNull()).thenReturn(expectedEquations);
        List<Equation> actualEquations = equationService.findAllWithRoots();

        assertEquals(expectedEquations.size(), actualEquations.size());
        for (int i = 0; i < expectedEquations.size(); i++) {
            assertEquals(expectedEquations.get(i), actualEquations.get(i));
        }

        verify(equationRepository, times(1)).findAllByRootIsNotNull();
    }

    @Test
    void findAllWithOutRoots() {
        List<Equation> expectedEquations = Arrays.asList(
                Equation.builder().id(1L).equation("5*x-8=2").build(),
                Equation.builder().id(2L).equation("3*x+8=11").build()
        );
        when(equationRepository.findAllByRootIsNull()).thenReturn(expectedEquations);
        List<Equation> actualEquations = equationService.findAllWithOutRoots();

        assertEquals(expectedEquations.size(), actualEquations.size());
        for (int i = 0; i < expectedEquations.size(); i++) {
            assertEquals(expectedEquations.get(i), actualEquations.get(i));
        }

        verify(equationRepository, times(1)).findAllByRootIsNull();
    }

    @Test
    void findAllWithRoot() {
        List<Equation> expectedEquations = Arrays.asList(
                Equation.builder().id(1L).equation("2*x+5=11").root(3L).build(),
                Equation.builder().id(2L).equation("3*x-4=5").root(3L).build()
        );

        when(equationRepository.findAllByRoot(3)).thenReturn(expectedEquations);

        List<Equation> actualEquations = equationService.findAllWithRoot(3);

        assertEquals(expectedEquations.size(), actualEquations.size());
        for (int i = 0; i < expectedEquations.size(); i++) {
            assertEquals(expectedEquations.get(i), actualEquations.get(i));
        }

        verify(equationRepository, times(1)).findAllByRoot(3);

    }
}