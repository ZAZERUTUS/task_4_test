package ru.clevertec.product.validators;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import ru.clevertec.product.data.ProductDTOForTest;
import ru.clevertec.product.entity.Product;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;


class ValidateProductTest {

    ValidateProduct validator;

    @BeforeEach
    void setup() {
        validator = new ValidateProduct();
    }

    @ParameterizedTest(name = "UUID {0} is valid - {1}")
    @CsvSource(value = {"c05fe06e-0ee2-4125-ab8c-b171e00cb2d2,true",
            "31682de0-3c0d-4a73-872f-6201765b9c29,true",
            ",false",})
    void testValidateUUID(UUID forValid, boolean isValid) {
        boolean actual = validator.isValidUUID(forValid);

        Assertions.assertEquals(isValid, actual);
    }

    @ParameterizedTest(name = "Name {0} is valid - {1}")
    @CsvSource(value = {"sss,false",
            "фыва,false",
            "йцукенгшщзх,false",
            "укенг,true",
            "фывапролдж,true",
            "вуувмкк,true",
            "null,false",
            ",false"})
    void testValidateName(String forValid, boolean isValid) {
        boolean actual = validator.isValidName(forValid);

        Assertions.assertEquals(isValid, actual);
    }

    @ParameterizedTest(name = "Description {0} is valid - {1}")
    @CsvSource(value = {"sss,false",
            "фываывакмасв2,false",
            "12345678999,false",
            "фывапролд,false",
            "фывапролдж,true",
            "фывапролджфывапролджфывапролдж,true",
            "фывапролджфывапролджфывапролджц,false",
    })
    void testValidateDescription(String forValid, boolean isValid) {
        boolean actual = validator.isValidDescription(forValid);

        Assertions.assertEquals(isValid, actual);
    }

    @ParameterizedTest(name = "Num {0} is valid - {1}")
    @CsvSource(value = {"1,true",
            "1090,true",
            "1.21,true",
            "0,false",
            "-2,false"
    })
    void testValidateCost(BigDecimal cost, boolean isValid) {
        boolean actual = validator.isValidPrice(cost);

        Assertions.assertEquals(isValid, actual);
    }

    @ParameterizedTest(name = "Date {0} is valid - {1}")
    @CsvSource(value = {",false",
            "2023-11-01T14:05:02.899154,true",
            "-999999999-01-01T00:00,true",
    })
    void testValidateDate(LocalDateTime date, boolean isValid) {
        boolean actual = validator.isValidDate(date);

        Assertions.assertEquals(isValid, actual);
    }

    static Stream<Map.Entry<Product, Boolean>> getProductForValidate() {
        Map<Product, Boolean> map = new HashMap<>();
        map.put(ProductDTOForTest.builder().build().buildProduct(), true);
        map.put(ProductDTOForTest.builder().withDescription("ssdsa").build().buildProduct(), false);
        map.put(ProductDTOForTest.builder().withName("asdfectvd").build().buildProduct(), false);
        map.put(ProductDTOForTest.builder().withUuid(null).build().buildProduct(), false);
        return map.entrySet().stream();
    }

    @ParameterizedTest
    @MethodSource("getProductForValidate")
    void verifyValidateProduct(Map.Entry<Product, Boolean> entry) {
        boolean actual = validator.isValidProduct(entry.getKey());

        Assertions.assertEquals(entry.getValue(), actual);
    }
}