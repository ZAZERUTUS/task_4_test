package ru.clevertec.product.service.impl;

import lombok.RequiredArgsConstructor;
import ru.clevertec.product.data.InfoProductDto;
import ru.clevertec.product.data.ProductDto;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.exception.ProductNotFoundException;
import ru.clevertec.product.mapper.ProductMapper;
import ru.clevertec.product.repository.ProductRepository;
import ru.clevertec.product.service.ProductService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper mapper;
    private final ProductRepository productRepository;

    @Override
    public InfoProductDto get(UUID uuid) {
        return this.mapper
                .toInfoProductDto(productRepository.findById(uuid).orElseThrow(() -> new ProductNotFoundException(uuid)));
    }

    @Override
    public List<InfoProductDto> getAll() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(mapper::toInfoProductDto).collect(Collectors.toList());
    }

    @Override
    public UUID create(ProductDto productDto) {
        Product product = mapper.toProduct(productDto);
        Product saved = productRepository.save(product);
        return saved.getUuid();
    }

    @Override
    public void update(UUID uuid, ProductDto productDto) {
        Optional<Product> optionalProduct = productRepository.findById(uuid);
        if (optionalProduct.isPresent()) {
            Product pr = optionalProduct.get();
            pr.setName(productDto.name());
            pr.setDescription(productDto.description());
            pr.setPrice(productDto.price());
            productRepository.save(pr);
        } else {
            throw new ProductNotFoundException(uuid);
        }
    }

    @Override
    public void delete(UUID uuid) {
        productRepository.delete(uuid);
    }
}
