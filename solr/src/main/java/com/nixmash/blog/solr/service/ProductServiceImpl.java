package com.nixmash.blog.solr.service;

import com.nixmash.blog.solr.enums.SolrDocType;
import com.nixmash.blog.solr.exceptions.GeoLocationException;
import com.nixmash.blog.solr.model.Product;
import com.nixmash.blog.solr.repository.custom.CustomProductRepository;
import com.nixmash.blog.solr.repository.simple.SimpleProductRepositoryImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.solr.core.geo.GeoConverters;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.core.query.result.SolrResultPage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class ProductServiceImpl implements ProductService {

	private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
	private static final Pattern IGNORED_CHARS_PATTERN = Pattern.compile("\\p{Punct}");

	@Resource
	SimpleProductRepositoryImpl simpleProductRepository;

	@Resource
	CustomProductRepository customProductRepository;

	@Override
	public List<Product> getAvailableProducts() {
		logger.debug("Retrieving all available products where inStock:true");
		List<Product> products = customProductRepository.findByAvailableTrueAndDoctype(SolrDocType.PRODUCT);
		return products;
	}

	@Override
	public List<Product> getProductsByLocation(String LatLng) throws GeoLocationException {
		List<Product> found;
		try {
			Point point = GeoConverters.StringToPointConverter.INSTANCE.convert(LatLng);
			found = 
					customProductRepository.findByLocationNear(new Point(point.getX(), point.getY()), new Distance(30));
		} catch (Exception e) {
			logger.debug("No location found with coordinates: {}", LatLng);
			throw new GeoLocationException("Error in mapping latLng: " + LatLng);
		}
		return found;
	}

	@Override
	public HighlightPage<Product> findByHighlightedName(String searchTerm, Pageable pageable) {
		return customProductRepository.findByNameIn(splitSearchTermAndRemoveIgnoredCharacters(searchTerm), pageable);
	}

	@Override
	public HighlightPage<Product> findByHighlightedNameCriteria(String searchTerm) {
		return customProductRepository.searchProductsWithHighlights(searchTerm);
	}

	@Override
	public FacetPage<Product> getFacetedProductsAvailable() {
		logger.debug("Retrieving faceted products by available");
		return simpleProductRepository.findByFacetOnAvailable(new PageRequest(0,100));
	}

	@Override
	public FacetPage<Product> getFacetedProductsCategory() {
		logger.debug("Retrieving faceted products by category");
		return customProductRepository.findProductCategoryFacets(new PageRequest(0, 20));
	}

	@Override
	public List<Product> getProductsByCategory(String category) {
		logger.debug("Retrieving products by category: {}", category);
		return customProductRepository.findByCategory(category);
	}

	@Override
	public Iterable<Product> getAllRecords() {
		logger.debug("Retrieving all records in index");
		return simpleProductRepository.findAll();
	}

	@Override
	public Page<Product> getTestRecords() {
		return customProductRepository.findTestCategoryRecords();
	}

//	@Override
//	public List<Product> getProductsByFilter() {
//		logger.debug("Retrieving all records and filtering out by 'doctype:product'");
//		List<Product> products = Lists.newArrayList(customProductRepository.findAll());
//		return products.stream().filter(p -> p.getDoctype().equals(SolrDocType.PRODUCT)).collect(Collectors.toList());
//	}

	@Override
	public List<Product> getProducts() {
		logger.debug("Retrieving all products by solr @Query");
		return customProductRepository.findAllProducts();
	}

	@Override
	public Page<Product> getProductsPaged(Pageable page) {
		logger.debug("Retrieving all products by solr @Query");
		return customProductRepository.findAllProductsPaged(page);
	}

	@Override
	public List<Product> getProductsByStartOfName(String nameStart) {
		logger.debug("Named Method Query -  findByNameStartingWith()");
		return customProductRepository.findByNameStartingWith(nameStart);
	}

	@Override
	public List<Product> getProductsWithUserQuery(String userQuery) {
		logger.debug("SimpleQuery from user search string -  findProductsBySimpleQuery()");
		return customProductRepository.findProductsBySimpleQuery(userQuery);
	}

	@Override
	public Iterable<Product> getProductsByNameOrCategory(String searchTerm) {
		logger.debug("Using 'Product.findByNameOrCategory' named query - ('name:*?0* OR cat:*?0*')");
		return customProductRepository.findByNameOrCategory(searchTerm, sortByIdDesc());
	}

	@Override
	public List<Product> getProductsByNameOrCategoryAnnotatedQuery(String searchTerm) {
		logger.debug("Using annotated @query  - ('(name:*?0* OR cat:*?0*) AND doctype:product'");
		return customProductRepository.findByAnnotatedQuery(searchTerm, sortByIdDesc());
	}

	@Override
	public Page<Product> getProductsByPopularity(int popularity) {
		logger.debug("Using JPA Method Name Query - findByPopularityGreaterThanEqual()");
		return customProductRepository.findByPopularityGreaterThanEqual(popularity, new PageRequest(0, 10));
	}

	@Override
	public Product getProduct(String Id) {
		return customProductRepository.findOne(Id);
	}

	@Override
	public void updateProductName(Product product) {
		customProductRepository.updateProductName(product);
	}

	@Override
	public List<Product> searchWithCriteria(String searchTerm) {
		return customProductRepository.searchWithCriteria(searchTerm);
	}

	public Sort sortByIdDesc() {
		return new Sort(Sort.Direction.DESC, Product.ID_FIELD);
	}

	@Override
	public FacetPage<Product> autocompleteNameFragment(String fragment, Pageable pageable) {
		if (StringUtils.isBlank(fragment)) {
			return new SolrResultPage<Product>(Collections.<Product> emptyList());
		}
		return customProductRepository.findByNameStartingWith(splitSearchTermAndRemoveIgnoredCharacters(fragment), pageable);
	}

	private Collection<String> splitSearchTermAndRemoveIgnoredCharacters(String searchTerm) {
		String[] searchTerms = StringUtils.split(searchTerm, " ");
		List<String> result = new ArrayList<String>(searchTerms.length);
		for (String term : searchTerms) {
			if (StringUtils.isNotEmpty(term)) {
				result.add(IGNORED_CHARS_PATTERN.matcher(term).replaceAll(" "));
			}
		}
		return result;
	}

}
