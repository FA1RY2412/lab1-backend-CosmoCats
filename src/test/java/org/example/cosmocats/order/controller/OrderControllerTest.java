package org.example.cosmocats.order.controller;

import org.example.cosmocats.order.dto.OrderCreateUpdateDto;
import org.example.cosmocats.order.dto.OrderDto;
import org.example.cosmocats.order.service.OrderService;
import org.example.cosmocats.common.exception.ResourceNotFoundException;
import org.example.cosmocats.security.SecurityTestConfig;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.context.annotation.Import;


import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@WebMvcTest(controllers = OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Import(SecurityTestConfig.class)
class OrderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrderService orderService;

    @Test
    void list_returnsOk() throws Exception {
        OrderDto dto = new OrderDto(
                1L,
                "ORD-001",
                "Голуб Денис",
                "NEW",
                Instant.now(),
                List.of(1L, 2L)
        );

        Mockito.when(orderService.findAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/orders"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].number").value("ORD-001"));
    }

        @Test
        void getById_notFound_returns404() throws Exception {
        Mockito.when(orderService.findById(42L))
                .thenThrow(new ResourceNotFoundException("Order with id 42 not found"));

        mockMvc.perform(get("/api/v1/orders/42"))
                .andExpect(status().isNotFound());
        }


    @Test
    void create_returns201() throws Exception {
        OrderDto dto = new OrderDto(
                1L,
                "ORD-002",
                "Голуб Денис",
                "NEW",
                Instant.now(),
                List.of(2L)
        );

        Mockito.when(orderService.create(any(OrderCreateUpdateDto.class)))
               .thenReturn(dto);

        String body = """
                {
                  "number": "ORD-002",
                  "customerName": "Голуб Денис",
                  "status": "NEW",
                  "productIds": [2]
                }
                """;

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id").value(1))
               .andExpect(jsonPath("$.number").value("ORD-002"));
    }
}
