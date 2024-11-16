package com.iteletric.iteletricapi.services;

import com.iteletric.iteletricapi.repositories.BudgetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class BudgetServiceTest {

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private WorkService workService;

    @Mock
    private UserService userService;

    @InjectMocks
    private BudgetService budgetService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
}
