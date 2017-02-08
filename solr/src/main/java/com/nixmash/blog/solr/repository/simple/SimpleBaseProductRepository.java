package com.nixmash.blog.solr.repository.simple;

import com.nixmash.blog.solr.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.repository.Facet;
import org.springframework.data.solr.repository.Query;

import java.util.List;

public interface SimpleBaseProductRepository extends CrudRepository<Product, String> {

	List<Product> findByAvailableTrue();

	@Query(value = "*:*")
	@Facet(fields = { "inStock" }, limit = 50)
	FacetPage<Product> findByFacetOnAvailable(Pageable pageable);

	@Query(value = "*:*")
	@Facet(fields = { "cat" }, limit = 50)
	FacetPage<Product> findByFacetOnCategory(Pageable pageable);
}
