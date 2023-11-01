package ru.clevertec.product.validators;

import ru.clevertec.product.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class ValidateProduct {
//    protected void validateProductDTO(ProductDto productDto) {
//        if (productDto.name())
//    }

    public boolean isValidUUID(UUID uuid) {
        return uuid != null && uuid.version() == 4 && uuid.toString()
                .matches("^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$");
    }
    public boolean isValidProduct(Product product) {
        return isValidName(product.getName()) &&
                isValidUUID(product.getUuid()) &&
                isValidDescription(product.getDescription()) &&
                isValidDate(product.getCreated()) &&
                isValidPrice(product.getPrice());
    }
    public boolean isValidName(String name) {
        return (name != null &&
                !name.isEmpty() &&
                name.matches("[а-яА-я\s]{5,10}"));
    }

    public boolean isValidDescription(String description) {
        return description.matches("[а-яА-я\s]{10,30}");
    }

    public boolean isValidPrice(BigDecimal price) {
        return (price != null && price.compareTo(BigDecimal.ZERO) > 0);
    }

    public boolean isValidDate(LocalDateTime date) {
        return date != null;
    }
}
