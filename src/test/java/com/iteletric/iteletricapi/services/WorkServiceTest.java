package com.iteletric.iteletricapi.services;

import com.iteletric.iteletricapi.config.exception.BusinessException;
import com.iteletric.iteletricapi.dtos.work.WorkRequest;
import com.iteletric.iteletricapi.dtos.work.WorkResponse;
import com.iteletric.iteletricapi.enums.material.UnitOfMeasure;
import com.iteletric.iteletricapi.enums.user.RoleName;
import com.iteletric.iteletricapi.models.Material;
import com.iteletric.iteletricapi.models.User;
import com.iteletric.iteletricapi.models.Work;
import com.iteletric.iteletricapi.repositories.BudgetRepository;
import com.iteletric.iteletricapi.repositories.WorkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WorkServiceTest {

    @Mock
    private WorkRepository workRepository;

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private MaterialService materialService;

    @Mock
    private UserService userService;

    @InjectMocks
    private WorkService workService;

    private User user;
    private Work work;
    private Material material;

    @BeforeEach
    void setUp() {
        user = User.builder().name("Diego").email("diego@gmail.com").password("123").role(RoleName.ROLE_OWNER).build();
        user.setId(1L);
        user.setDeleted(0);

        material = Material.builder().name("Fio").price(BigDecimal.valueOf(100)).quantityUnitMeasure(BigDecimal.valueOf(100)).unitMeasure(UnitOfMeasure.METERS).build();
        material.setId(1L);
        material.setOwner(user);

        work = Work.builder().name("Instalação").laborPrice(BigDecimal.valueOf(100.00)).materialList(Collections.singletonList(material)).build();
        work.setId(1L);
    }

    @Test
    void create_ShouldCreateWorkSuccessfully() {
        WorkRequest request = new WorkRequest();
        request.setName("Fio 2");
        request.setLaborPrice(BigDecimal.valueOf(150.00));
        request.setMaterialIdList(List.of(1L));

        when(materialService.getAllMaterialSelectedById(request.getMaterialIdList())).thenReturn(Collections.singletonList(material));

        workService.create(request);

        verify(workRepository).save(any(Work.class));
    }

    @Test
    void create_ShouldCreateWhenNoMaterials() {
        WorkRequest request = new WorkRequest();
        request.setName("Fio");
        request.setLaborPrice(BigDecimal.valueOf(150.00));
        request.setMaterialIdList(Collections.emptyList());

        when(materialService.getAllMaterialSelectedById(request.getMaterialIdList())).thenReturn(Collections.emptyList());
        workService.create(request);
        verify(workRepository).save(any(Work.class));
    }

    @Test
    void update_ShouldUpdateWorkSuccessfully() {
        WorkRequest request = new WorkRequest();
        request.setName("Cabo elétrico");
        request.setLaborPrice(BigDecimal.valueOf(200.00));
        request.setMaterialIdList(List.of(1L));

        when(workRepository.findById(1L)).thenReturn(Optional.of(work));

        when(materialService.getAllMaterialSelectedById(request.getMaterialIdList())).thenReturn(Collections.singletonList(material));

        when(budgetRepository.findByWorkListContaining(work)).thenReturn(Collections.emptyList());

        workService.update(1L, request);
        assertEquals("Cabo elétrico", work.getName());
        assertEquals(BigDecimal.valueOf(200.00), work.getLaborPrice());

        verify(workRepository).save(work);
    }

    @Test
    void delete_ShouldDeleteWork() {
        when(workRepository.findById(1L)).thenReturn(Optional.of(work));

        workService.delete(1L);

        verify(workRepository).delete(work);
    }

    @Test
    void delete_ShouldThrowExceptionWhenWorkNotFound() {
        when(workRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(BusinessException.class, () -> workService.delete(1L));
        assertEquals("Trabalho não encontrado", exception.getMessage());
    }

    @Test
    void getById_ShouldReturnWork() {
        when(workRepository.findById(1L)).thenReturn(Optional.of(work));

        Work result = workService.getById(1L);

        assertNotNull(result);
        assertEquals("Instalação", result.getName());
    }

    @Test
    void getById_ShouldThrowExceptionIfNotFound() {
        when(workRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(BusinessException.class, () -> workService.getById(1L));
        assertEquals("Trabalho não encontrado", exception.getMessage());
    }

    @Test
    void getAllWorkSelectedById_ShouldReturnWorks() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(workRepository.findAllById(ids)).thenReturn(Arrays.asList(work, new Work()));

        List<Work> result = workService.getAllWorkSelectedById(ids);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void getAllWorkSelectedById_ShouldThrowExceptionWhenEmpty() {
        List<Long> ids = List.of(3L);
        when(workRepository.findAllById(ids)).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(BusinessException.class, () -> workService.getAllWorkSelectedById(ids));
        assertEquals("Os serviços selecionados não foram encontrados", exception.getMessage());
    }

    @Test
    void list_ShouldReturnWorkWithNameFilter() {
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "id"));
        User currentUser = user;

        Page<Work> workPage = new PageImpl<>(Collections.singletonList(work));
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(workRepository.findByOwnerAndNameContainingIgnoreCase(currentUser, "Instalação", pageable))
                .thenReturn(workPage);

        Page<WorkResponse> result = workService.list("Instalação", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Instalação", result.getContent().get(0).getName());
        verify(workRepository).findByOwnerAndNameContainingIgnoreCase(currentUser, "Instalação", pageable);
    }

    @Test
    void list_ShouldReturnAllWorkWithoutNameFilter() {
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "id"));
        User currentUser = user;

        Page<Work> workPage = new PageImpl<>(Collections.singletonList(work));
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(workRepository.findByOwner(eq(currentUser), eq(pageable))).thenReturn(workPage);

        Page<WorkResponse> result = workService.list(null, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(workRepository).findByOwner(eq(currentUser), eq(pageable));
    }

    @Test
    void update_ShouldThrowExceptionWhenWorkNotFound() {
        WorkRequest request = new WorkRequest();
        request.setName("Cabo elétrico");
        request.setLaborPrice(BigDecimal.valueOf(200.00));
        request.setMaterialIdList(List.of(1L));

        when(workRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(BusinessException.class, () -> workService.update(1L, request));
        assertEquals("Trabalho não encontrado", exception.getMessage());
    }

    @Test
    void getAllWorkSelectedById_ShouldThrowExceptionForInvalidIds() {
        List<Long> ids = List.of(1L, 2L);
        when(workRepository.findAllById(ids)).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(BusinessException.class, () -> workService.getAllWorkSelectedById(ids));
        assertEquals("Os serviços selecionados não foram encontrados", exception.getMessage());
    }

    @Test
    void update_ShouldUpdatePriceOnly() {
        WorkRequest request = new WorkRequest();
        request.setLaborPrice(BigDecimal.valueOf(250.00));
        request.setMaterialIdList(List.of(1L));

        when(workRepository.findById(1L)).thenReturn(Optional.of(work));
        when(materialService.getAllMaterialSelectedById(request.getMaterialIdList())).thenReturn(Collections.singletonList(material));

        workService.update(1L, request);

        assertEquals(BigDecimal.valueOf(250.00), work.getLaborPrice());
        verify(workRepository).save(work);
    }

    @Test
    void getById_ShouldThrowExceptionForNullId() {
        Exception exception = assertThrows(BusinessException.class, () -> workService.getById(null));
        assertEquals("Trabalho não encontrado", exception.getMessage());
    }
}
