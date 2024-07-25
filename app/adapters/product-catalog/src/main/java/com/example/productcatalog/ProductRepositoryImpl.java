package com.example.productcatalog;

import org.springframework.data.jpa.repository.JpaRepository;

interface ProductRepositoryImpl extends ProductRepository, JpaRepository<Product, Long> {

}