package ru.clevertec.product.mapper.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.product.data.InfoProductDto;
import ru.clevertec.product.data.ProductDTOForTest;
import ru.clevertec.product.data.ProductDto;
import ru.clevertec.product.entity.Product;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductMapperImplTest {

    @InjectMocks
    ProductMapperImpl mapper;


    @Test
    void toProductConverterWithUUIDAndCreatedNull() { //todo - возможно дописать тесты на ошибку валидации
        //Given
        ProductDto productDTD = ProductDTOForTest.builder().build().buildProductDto();
        Product expected = ProductDTOForTest.builder()
                .withUuid(null)
                .withCreated(null)
                .build().buildProduct();

        //When
        Product actual = mapper.toProduct(productDTD);

        //Then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void toInfoProductDto() {
        //Given
        Product foConvert = ProductDTOForTest.builder().build().buildProduct();
        InfoProductDto expected = ProductDTOForTest.builder().build().buildInfoProductDto();

        //When
        InfoProductDto actual = mapper.toInfoProductDto(foConvert);

        //Then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void toInfoProductDtoWithNotValidProduct() {
        //Given
        Product foConvert = ProductDTOForTest.builder().withUuid(null).build().buildProduct();

        //When
        Exception ex = Assertions.assertThrows(IllegalArgumentException.class,
                () -> mapper.toInfoProductDto(foConvert));

        //Then
        Assertions.assertTrue(ex.getMessage().contains("Not valid product - " + foConvert));
    }


    @Test
    void mergeWithCurrentParameters() {
        //Given
        Product productForMerge = ProductDTOForTest.builder().build().buildProduct();
        ProductDTOForTest example = ProductDTOForTest.builder()
                .withDescription("мержед")
                .withName("НовоеИм")
                .withPrice(BigDecimal.TEN).build();
        ProductDto productDtoForMerge = example.buildProductDto();
        Product expected = example.buildProduct();

        //When
        Product actual = mapper.merge(productForMerge, productDtoForMerge);

        //Then
        Assertions.assertEquals(expected, actual);
    }
}