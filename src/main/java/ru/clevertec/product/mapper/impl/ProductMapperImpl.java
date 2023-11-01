package ru.clevertec.product.mapper.impl;

import ru.clevertec.product.data.InfoProductDto;
import ru.clevertec.product.data.ProductDto;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.mapper.ProductMapper;
import ru.clevertec.product.validators.ValidateProduct;

import javax.print.attribute.standard.Copies;

public class ProductMapperImpl implements ProductMapper {

    @Override
    public Product toProduct(ProductDto productDto) {
        return new Product(null,
                productDto.name(),
                productDto.description(),
                productDto.price(),
                null);
    }

    @Override
    public InfoProductDto toInfoProductDto(Product product) {
        if (new ValidateProduct().isValidProduct(product)) {
            return new InfoProductDto(product.getUuid(),
                    product.getName(),
                    product.getDescription(),
                    product.getPrice());
        }
        throw new IllegalArgumentException("Not valid product - " + product);
    }

    @Override
    public Product merge(Product product, ProductDto productDto) {
        return new Product(product.getUuid(),
                productDto.name(),
                productDto.description(),
                productDto.price(),
                product.getCreated());
    }
}
