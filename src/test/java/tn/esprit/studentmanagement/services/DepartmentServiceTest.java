package tn.esprit.studentmanagement.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.studentmanagement.entities.Department;
import tn.esprit.studentmanagement.repositories.DepartmentRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentService departmentService;

    @Test
    void getAllDepartmentsReturnsDepartmentsFromRepository() {
        List<Department> departments = List.of(new Department());
        when(departmentRepository.findAll()).thenReturn(departments);

        List<Department> result = departmentService.getAllDepartments();

        assertSame(departments, result);
        verify(departmentRepository).findAll();
    }

    @Test
    void getDepartmentByIdReturnsDepartmentFromRepository() {
        Long departmentId = 1L;
        Department department = new Department();
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));

        Department result = departmentService.getDepartmentById(departmentId);

        assertSame(department, result);
        verify(departmentRepository).findById(departmentId);
    }

    @Test
    void saveDepartmentReturnsSavedDepartment() {
        Department department = new Department();
        when(departmentRepository.save(department)).thenReturn(department);

        Department result = departmentService.saveDepartment(department);

        assertSame(department, result);
        verify(departmentRepository).save(department);
    }

    @Test
    void deleteDepartmentDeletesDepartmentById() {
        Long departmentId = 1L;

        departmentService.deleteDepartment(departmentId);

        verify(departmentRepository).deleteById(departmentId);
    }
}
