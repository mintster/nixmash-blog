package com.nixmash.blog.solr.repository.simple;

import com.nixmash.blog.solr.enums.SolrDocType;
import com.nixmash.blog.solr.model.IProduct;
import com.nixmash.blog.solr.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.repository.support.SimpleSolrRepository;

import java.util.List;

@NoRepositoryBean
public class SimpleProductRepositoryImpl extends SimpleSolrRepository<Product, String> implements SimpleBaseProductRepository {

	@Override
	public List<Product> findByAvailableTrue() {
		Query query = new SimpleQuery(new Criteria(new SimpleField(Criteria.WILDCARD)).expression(Criteria.WILDCARD));
		query.addFilterQuery(new SimpleQuery(new Criteria(IProduct.DOCTYPE_FIELD).is(SolrDocType.PRODUCT)));
		query.setRows(1000);
		Page<Product> results = getSolrOperations().queryForPage(query, Product.class);
		return results.getContent();
	}

	@Override
	public FacetPage<Product> findByFacetOnAvailable(Pageable pageable) {
		FacetQuery query = new SimpleFacetQuery(new
						Criteria(IProduct.DOCTYPE_FIELD).is(SolrDocType.PRODUCT));
		query.setFacetOptions(new FacetOptions(Product.AVAILABLE_FIELD).setFacetLimit(5));
		query.setPageRequest(new PageRequest(0, 100));
		return getSolrOperations().queryForFacetPage(query, Product.class);

	}

	@Override
	public FacetPage<Product> findByFacetOnCategory(Pageable pageable) {

		FacetQuery query = new
				SimpleFacetQuery(new Criteria(IProduct.DOCTYPE_FIELD)
						.is(SolrDocType.PRODUCT));

		query.setFacetOptions(new FacetOptions(Product.CATEGORY_FIELD)
				.setPageable(new PageRequest(0,20)));

		return getSolrOperations().queryForFacetPage(query, Product.class);
	}

}
