package ru.clevertec.product.repository.impl;

import ru.clevertec.product.entity.Product;
import ru.clevertec.product.repository.ProductRepository;

import javax.naming.NameNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryProductRepository implements ProductRepository {
    public static UUID UUID1_TEST = UUID.fromString("5f4b9633-a0a4-4788-8a6a-2cb5900a7ce1");
    public static UUID UUID2_TEST = UUID.fromString("5f4b9633-a0a4-7777-8a6a-2cb5900a7ce1");

    protected final Map<UUID, Product> testImpl = initWithBaseValues();

    public static Map<UUID, Product> initWithBaseValues() {
        Map<UUID, Product> productMap = new HashMap<>();
        productMap.put(UUID1_TEST, new Product(UUID1_TEST, "Prod1", "Description1", BigDecimal.valueOf(1.23), LocalDateTime.MIN));
        productMap.put(UUID2_TEST, new Product(UUID2_TEST, "Prod2", "Description2", BigDecimal.valueOf(1.24), LocalDateTime.MAX));
        return productMap;
    }

    @Override
    public Optional<Product> findById(UUID uuid) {
        return Optional.ofNullable(testImpl.getOrDefault(uuid, null));
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(testImpl.values());
    }

    @Override
    public Product save(Product product) {
        return new Product(UUID.randomUUID(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                LocalDateTime.now());
    }

    @Override
    public void delete(UUID uuid) {
        if (!testImpl.containsKey(uuid)) {
            throw new IllegalArgumentException("UUID is not exist - " + uuid);
        }else {
            testImpl.remove(uuid);
        }
    }
}
