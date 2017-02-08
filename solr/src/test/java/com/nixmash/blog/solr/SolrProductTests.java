package com.nixmash.blog.solr;

import com.nixmash.blog.solr.enums.SolrProductField;
import com.nixmash.blog.solr.exceptions.GeoLocationException;
import com.nixmash.blog.solr.model.Product;
import com.nixmash.blog.solr.repository.custom.CustomProductRepository;
import com.nixmash.blog.solr.service.ProductService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.UncategorizedSolrException;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SimpleStringCriteria;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightEntry.Highlight;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
public class SolrProductTests extends SolrContext {

	private static final Logger logger = LoggerFactory.getLogger(SolrProductTests.class);


	private static final String SOLR_STRING = "solr";
	private static final int PRODUCT_ID = 1000;
	private static final int INITIAL_RECORD_COUNT = 56;
	private static final int INITIAL_CATEGORY_COUNT = 6;
	private static final int TEST_RECORD_COUNT = 10;

	@Autowired
	SolrOperations solrOperations;

	@After
	public void tearDown() {
		Query query = new SimpleQuery(new SimpleStringCriteria("cat:test*"));
		solrOperations.delete(query);
		solrOperations.commit();
	}

	@Resource
	CustomProductRepository repo;

	@Autowired
	private ProductService productService;

	@Test
	public void badSimpleQueryThrowsUncategorizedSolrException() {
		int i = 0;
		try {
			productService.getProductsWithUserQuery("bad:field");
		} catch (Exception ex) {
			i++;
			Assert.assertTrue(ex instanceof UncategorizedSolrException);
		}
		try {
			productService.getProductsWithUserQuery("bad::format");
		} catch (Exception ex) {
			i++;
			Assert.assertTrue(ex instanceof UncategorizedSolrException);
		}
		try {
			productService.getProductsWithUserQuery("name:goodQuery");
		} catch (UncategorizedSolrException ex) {
			i++;
		}
		Assert.assertEquals(2, i);
	}

	@Test
	public void badLatLngThrowsGeoLocationException() {
		try {
			productService.getProductsByLocation("12345");
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof GeoLocationException);
		}
	}

	@Test
	public void testCustomQueries() {

		// Named Query from named-queries.properties
		List<Product> products = repo.findByNameOrCategory(SOLR_STRING, sortByIdDesc());
		Assert.assertEquals(1, products.size());

		// Method Name Query test for findByPopularityGreaterThanEqual()
		Product product = SolrTestUtils.createProduct(PRODUCT_ID);
		repo.save(product);

		Page<Product> popularProducts = repo.findByPopularityGreaterThanEqual(10000, new PageRequest(0, 10));
		Assert.assertEquals(1, popularProducts.getTotalElements());
		Assert.assertEquals(Integer.toString(PRODUCT_ID), popularProducts.getContent().get(0).getId());

	}

	@Test
	public void testFacetQuery() {

		FacetPage<Product> facetPage = repo.findProductCategoryFacets(new PageRequest(0, 100));
		Assert.assertEquals(repo.findAllProducts().size(), facetPage.getNumberOfElements());

		Page<FacetFieldEntry> page = facetPage.getFacetResultPage(SolrProductField.CATEGORY);
		Assert.assertEquals(INITIAL_CATEGORY_COUNT, page.getNumberOfElements());

		for (FacetFieldEntry entry : page) {
			Assert.assertEquals(SolrProductField.CATEGORY.getName(), entry.getField().getName());
			Assert.assertEquals(repo.findByCategory(entry.getValue()).size(), entry.getValueCount());
		}
	}

	@Test
	public void simpleQueryTest() {
		List<Product> baseList = SolrTestUtils.createProductList(10);
		repo.save(baseList);
		Assert.assertEquals(baseList.size(), TEST_RECORD_COUNT);

		List<Product> productsByCategory = repo.findProductsBySimpleQuery("cat:test");
		Assert.assertEquals(TEST_RECORD_COUNT, productsByCategory.size());
	}

	@Test
	public void testProductCRUD() {

		long recordCount = repo.count();
		// create local product object
		Product product = SolrTestUtils.createProduct(PRODUCT_ID);

		// save product to Solr Index and confirm index count increased by 1
		repo.save(product);
		Assert.assertEquals(recordCount + 1, repo.count());

		// find single product from Solr
		Product loaded = repo.findOne(Integer.toString(PRODUCT_ID));
		Assert.assertEquals(product.getName(), loaded.getName());

		// update product name in Solr and confirm index count not changed
		loaded.setName("changed named");
		repo.save(loaded);
		Assert.assertEquals(recordCount + 1, repo.count());

		// retrieve product from Solr and confirm name change
		loaded = repo.findOne(Integer.toString(PRODUCT_ID));
		Assert.assertEquals("changed named", loaded.getName());

		// delete the test product in Solr and confirm index count equal to initial count
		repo.delete(loaded);
		Assert.assertEquals(recordCount, repo.count());

	}

	@Test
	public void highlightedResultsShouldContainTermsInBold() {
		List<Product> baseList = SolrTestUtils.createProductList(10);
		repo.save(baseList);

		HighlightPage<Product> highlightProductPage = 
				productService.findByHighlightedName("product", new PageRequest(0, 20));
		assertTrue(containsSnipplet(highlightProductPage, "<b>product</b>"));
	}

	private boolean containsSnipplet(HighlightPage<Product> productsHighlightPage, 
			String snippletToCheck) {
		for (HighlightEntry<Product> he : productsHighlightPage.getHighlighted()) {
			for (Highlight highlight : he.getHighlights()) {
				for (String snipplet : highlight.getSnipplets()) {
					if (snipplet.contains(snippletToCheck)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private Sort sortByIdDesc() {
		return new Sort(Sort.Direction.DESC, Product.ID_FIELD);
	}

}
