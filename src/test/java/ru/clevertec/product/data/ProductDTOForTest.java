package ru.clevertec.product.data;

import lombok.Builder;
import lombok.Getter;
import ru.clevertec.product.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Builder(setterPrefix = "with")
@Getter
public class ProductDTOForTest {

    @Builder.Default
    private UUID uuid = UUID.fromString("5f4b9633-a0a4-4788-8a6a-2cb5900a7ce1");;

    @Builder.Default
    private String name = "Продукт";

    @Builder.Default
    private String description = "Описание понятное";

    @Builder.Default
    private BigDecimal price = BigDecimal.valueOf(1.23);

    @Builder.Default
    private LocalDateTime created = LocalDateTime.MIN;

    public Product buildProduct() {
        return new Product(uuid, name, description, price, created);
    }

    public ProductDto buildProductDto() {
        return new ProductDto(name, description, price);
    }

    public InfoProductDto buildInfoProductDto() {
        return new InfoProductDto(uuid, name, description, price);
    }
}
