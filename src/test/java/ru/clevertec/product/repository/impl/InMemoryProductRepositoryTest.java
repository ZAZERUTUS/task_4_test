package ru.clevertec.product.repository.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import ru.clevertec.product.data.ProductDTOForTest;
import ru.clevertec.product.data.ProductDto;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.exception.ProductNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static ru.clevertec.product.repository.impl.InMemoryProductRepository.initWithBaseValues;


class InMemoryProductRepositoryTest {

    private InMemoryProductRepository productRepository;

    private Product getTestProduct() {
        return new Product(UUID.randomUUID(),
                "Продукт1",
                "Описание1",
                BigDecimal.valueOf(1.23),
                LocalDateTime.MIN);
    }

    @BeforeEach
    void setup() {
        productRepository = new InMemoryProductRepository();
    }

    @Test
    void findByIdWithCurrentUUID() {
        //Given
        UUID uuid = InMemoryProductRepository.UUID1_TEST;
        Product expectedProd = InMemoryProductRepository.initWithBaseValues().get(uuid);

        //When
        Product actualProd = productRepository.findById(uuid).orElseThrow();

        //Then
        Assertions.assertEquals(expectedProd, actualProd);
    }

    @Test
    void findByIdWithNotExistUUID() {
        //Given
        UUID uuid = UUID.randomUUID();
        Optional<Product> expectedProd = Optional.empty();

        //When
        Optional<Product> actualProd = productRepository.findById(uuid);

        //Then
        Assertions.assertEquals(expectedProd, actualProd);
    }

    @Test
    void findAllWhenRecordsIsExist() {
        //Given
        List<Product> expected = new ArrayList<Product>(InMemoryProductRepository.initWithBaseValues().values());

        //When
        List<Product> actual = productRepository.findAll();

        //Then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void saveWithCorrectFullData() {
        //Given
        Product expected = getTestProduct();

        //When
        Product actual = productRepository.save(expected);

        //Then
        assertThat(expected)
                .hasFieldOrPropertyWithValue(Product.Fields.name, actual.getName())
                .hasFieldOrPropertyWithValue(Product.Fields.description, actual.getDescription())
                .hasFieldOrPropertyWithValue(Product.Fields.price, actual.getPrice());
    }

    @Test
    void saveWithNewUUID() {
        //Given
        Product expected = getTestProduct();

        //When
        Product actual = productRepository.save(expected);

        //Then
        Assertions.assertNotEquals(expected.getUuid(), actual.getUuid());
    }

    @Test
    void saveWithNewDate() {
        //Given
        Product expected = getTestProduct();

        //When
        Product actual = productRepository.save(expected);

        //Then
        Assertions.assertNotEquals(expected.getCreated(), actual.getCreated());
    }

    @Test
    void saveWithNullUUID() {
        //Given
        Product expected = getTestProduct();
        expected.setUuid(null);

        //When
        Product actual = productRepository.save(expected);

        //Then
        Assertions.assertNotNull(actual.getUuid());
    }
    @Test
    void deleteWithExistUUID() {
        //Given
        UUID uuid = InMemoryProductRepository.UUID1_TEST;

        //When
        productRepository.delete(uuid);

        //Then
        Assertions.assertNull(productRepository.testImpl.getOrDefault(uuid, null));
    }

    @Test
    void deleteWithNotExistUUIDMastThrowException() {
        //Given
        UUID uuid = UUID.randomUUID();

        //When
        Exception ex = Assertions.assertThrows(IllegalArgumentException.class,
                () -> productRepository.delete(uuid));

        //Then
        Assertions.assertTrue(ex.getMessage().contains(uuid.toString()));
    }
}