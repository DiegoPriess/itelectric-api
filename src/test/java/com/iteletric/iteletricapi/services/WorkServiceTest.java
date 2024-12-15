package com.iteletric.iteletricapi.services;

import com.iteletric.iteletricapi.config.exception.BusinessException;
import com.iteletric.iteletricapi.dtos.work.BulkMaterialRequest;
import com.iteletric.iteletricapi.dtos.work.WorkRequest;
import com.iteletric.iteletricapi.enums.material.UnitOfMeasure;
import com.iteletric.iteletricapi.enums.user.RoleName;
import com.iteletric.iteletricapi.models.BulkMaterial;
import com.iteletric.iteletricapi.models.Material;
import com.iteletric.iteletricapi.models.User;
import com.iteletric.iteletricapi.models.Work;
import com.iteletric.iteletricapi.repositories.WorkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WorkServiceTest {

    @Mock
    private WorkRepository workRepository;

    @Mock
    private MaterialService materialService;

    @InjectMocks
    private WorkService workService;

    private User user;
    private Material material;
    private BulkMaterial bulkMaterial;
    private Work work;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("Diego")
                .email("diego@gmail.com")
                .password("123")
                .role(RoleName.ROLE_OWNER)
                .build();

        user.setId(1L);

        material = Material.builder()
                .name("Fio")
                .price(BigDecimal.valueOf(100))
                .quantityUnitMeasure(100)
                .unitMeasure(UnitOfMeasure.METERS)
                .build();

        material.setId(1L);

        bulkMaterial = BulkMaterial.builder()
                .id(1L)
                .material(material)
                .bulkQuantity(10)
                .build();

        work = Work.builder()
                .id(1L)
                .name("Instalação")
                .laborPrice(BigDecimal.valueOf(100.00))
                .materialList(Collections.singletonList(bulkMaterial))
                .build();
    }

    @Test
    void create_ShouldCreateWorkSuccessfully() {
        WorkRequest request = new WorkRequest();
        request.setName("Instalação");
        request.setLaborPrice(BigDecimal.valueOf(150.00));
        request.setMaterialList(List.of(new BulkMaterialRequest(1L, 10)));

        when(materialService.getById(1L)).thenReturn(material);

        workService.create(request);

        verify(workRepository).save(any(Work.class));
    }

    @Test
    void update_ShouldClearMaterialsIfNoneProvided() {
        WorkRequest request = new WorkRequest();
        request.setName("Sem materiais");
        request.setLaborPrice(BigDecimal.valueOf(200.00));
        request.setMaterialList(new ArrayList<>());

        when(workRepository.findById(1L)).thenReturn(Optional.of(work));

        work.setMaterialList(new ArrayList<>(work.getMaterialList()));

        workService.update(1L, request);

        assertEquals(0, work.getMaterialList().size());
        assertEquals(BigDecimal.ZERO, work.getMaterialPrice());

        verify(workRepository).save(work);
    }

    @Test
    void delete_ShouldDeleteWorkSuccessfully() {
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
    void getById_ShouldReturnWorkSuccessfully() {
        when(workRepository.findById(1L)).thenReturn(Optional.of(work));

        Work result = workService.getById(1L);

        assertNotNull(result);
        assertEquals("Instalação", result.getName());
    }

    @Test
    void getById_ShouldThrowExceptionWhenNotFound() {
        when(workRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(BusinessException.class, () -> workService.getById(1L));
        assertEquals("Trabalho não encontrado", exception.getMessage());
    }

    @Test
    void getAllWorkSelectedById_ShouldReturnWorksSuccessfully() {
        List<Long> ids = List.of(1L, 2L);
        when(workRepository.findAllById(ids)).thenReturn(Arrays.asList(work, new Work()));

        List<Work> result = workService.getAllWorkSelectedById(ids);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void getAllWorkSelectedById_ShouldThrowExceptionForEmptyList() {
        when(workRepository.findAllById(anyList())).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(BusinessException.class, () -> workService.getAllWorkSelectedById(List.of(1L)));
        assertEquals("Os serviços selecionados não foram encontrados", exception.getMessage());
    }
}
