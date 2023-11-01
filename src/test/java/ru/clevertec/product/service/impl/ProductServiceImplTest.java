package ru.clevertec.product.service.impl;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.product.data.InfoProductDto;
import ru.clevertec.product.data.ProductDTOForTest;
import ru.clevertec.product.data.ProductDto;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.exception.ProductNotFoundException;
import ru.clevertec.product.mapper.ProductMapper;
import ru.clevertec.product.repository.impl.InMemoryProductRepository;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductMapper mockMapper;
    @Mock
    private InMemoryProductRepository mockRep;
    @InjectMocks
    private ProductServiceImpl productService;

    @Captor
    private ArgumentCaptor<Product> productCaptor;

    @Test
    void getWithCorrectAndExistUUID() {
        //Given
        InfoProductDto expected = ProductDTOForTest.builder().build().buildInfoProductDto();
        Optional<Product> productFromRep = Optional.of(ProductDTOForTest.builder().build().buildProduct());

        Mockito.when(mockRep.findById(expected.uuid())).thenReturn(productFromRep);
        Mockito.when(mockMapper.toInfoProductDto(productFromRep.get())).thenReturn(expected);
        //When
        InfoProductDto infoProductDto = productService.get(expected.uuid());

        //Then
        Assertions.assertEquals(expected, infoProductDto);
    }

    @Test
    void getWithCorrectAndNotExistUUID() {
        //Given
        InfoProductDto expected = ProductDTOForTest.builder().build().buildInfoProductDto();
        Optional<Product> productFromRep = Optional.empty();

        Mockito.when(mockRep.findById(expected.uuid())).thenReturn(productFromRep);

        //When
        Exception exception = Assertions.assertThrows(ProductNotFoundException.class,
                () ->productService.get(expected.uuid()));

        //Then
        Assertions.assertTrue(exception.getMessage().contains(expected.uuid().toString()));
    }

    @Test
    void getAllShouldBeInvokeRepositoryFindAll() {
        //When
        ProductDTOForTest expected = ProductDTOForTest.builder().build();

        //Given
        productService.getAll();

        //Then
        Mockito.verify(mockRep).findAll();
    }

    @Test
    void getAllShouldReturnListInfoProductDto() {
        //When
        Product product = ProductDTOForTest.builder().build().buildProduct();
        InfoProductDto expected = ProductDTOForTest.builder().build().buildInfoProductDto();

        Mockito.when(mockRep.findAll()).thenReturn(Collections.singletonList(product));
        Mockito.when(mockMapper.toInfoProductDto(product)).thenReturn(expected);
        //Given
        List<InfoProductDto> actualList = productService.getAll();

        //Then
        Assertions.assertEquals(1, actualList.size());
        Assertions.assertEquals(expected,actualList.get(0));
    }

    @Test
    void createShouldInvokeRepository_withoutUUID() {
        Product productToSave = ProductDTOForTest.builder()
                .withUuid(null)
                .build().buildProduct();
        Product expected = ProductDTOForTest.builder().build().buildProduct();
        ProductDto productDto = ProductDTOForTest.builder().build().buildProductDto();

        Mockito.doReturn(expected)
                .when(mockRep).save(productToSave);
        Mockito.when(mockMapper.toProduct(productDto))
                .thenReturn(productToSave);

        productService.create(productDto);

        Mockito.verify(mockRep).save(productCaptor.capture());
        assertThat(productCaptor.getValue())
                .hasFieldOrPropertyWithValue(Product.Fields.uuid, productToSave.getUuid());
    }

    @Test
    void createShouldBeReturnUUIDNotNull() {
        Product productToSave = ProductDTOForTest.builder()
                .withUuid(null)
                .build().buildProduct();
        Product expected = ProductDTOForTest.builder().build().buildProduct();
        ProductDto productDto = ProductDTOForTest.builder().build().buildProductDto();

        Mockito.doReturn(productToSave)
                .when(mockMapper).toProduct(productDto);
        Mockito
                .when(mockRep.save(productToSave))
                .thenReturn(expected);


        UUID actualUUID = productService.create(productDto);

        Assertions.assertEquals(actualUUID, expected.getUuid());
    }

    @Test
    void updateMustBeCorrectUpdate() {
        //Given
        Product productMutable = ProductDTOForTest.builder().build().buildProduct();
        ProductDTOForTest example = ProductDTOForTest.builder().
                withDescription("Updated")
                .build();
        ProductDto forSave = example.buildProductDto();
        Product expected = example.buildProduct();

        Mockito.when(mockRep.findById(productMutable.getUuid()))
                .thenReturn(Optional.of(productMutable));
        //When
        productService.update(productMutable.getUuid(), forSave);

        //Then
        Mockito.verify(mockRep).findById(productMutable.getUuid());
        Mockito.verify(mockRep).save(productCaptor.capture());
        Assertions.assertEquals(expected, productCaptor.getValue());
    }

    @Test
    void updateMustThrowCorrectExceptionWhenNotFountByUUID() {
        //Given
        Product product = ProductDTOForTest.builder().build().buildProduct();
        ProductDto forSave = ProductDTOForTest.builder().
                withDescription("Updated")
                .build().buildProductDto();
        //When
        Exception ex = Assertions.assertThrows(ProductNotFoundException.class,
                () -> productService.update(product.getUuid(), forSave));

        //Then
        Assertions.assertTrue(ex.getMessage().contains(product.getUuid().toString()));
    }

    @Test
    void deleteVerifyCallMethodTo () {
        //When
        ProductDTOForTest expected = ProductDTOForTest.builder().build();

        //Given
        productService.delete(expected.getUuid());

        //Then
        Mockito.verify(mockRep).delete(expected.getUuid());
    }

    @Test
    void deleteVerifyException () {
        //When
        UUID uuid = UUID.randomUUID();

        Mockito.doThrow(new IllegalArgumentException("UUID is not exist - " + uuid)).when(mockRep).delete(uuid);
        //Given
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> productService.delete(uuid));
        System.out.println("_______" + exception.getMessage());
        //Then
        Assertions.assertTrue(exception.getMessage().contains(uuid.toString()));
    }
}