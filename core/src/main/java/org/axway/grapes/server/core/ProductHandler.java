package org.axway.grapes.server.core;

import org.axway.grapes.server.core.exceptions.GrapesException;
import org.axway.grapes.server.db.RepositoryHandler;
import org.axway.grapes.server.db.datamodel.DbProduct;

import java.util.List;

/**
 * Product Handler
 *
 * <p>Manages all operation regarding Products. It can, get/update Products of the database.</p>
 *
 * @author jdcoffre
 */
public class ProductHandler {


    private final RepositoryHandler repositoryHandler;

    public ProductHandler(final RepositoryHandler repositoryHandler) {
        this.repositoryHandler = repositoryHandler;
    }

    /**
     * Creates a new Product in Grapes database
     *
     * @param dbProduct DbProduct
     */
    public void create(final DbProduct dbProduct) throws GrapesException {
        if(repositoryHandler.getProduct(dbProduct.getName()) != null){
            throw new GrapesException("Product already exist!");
        }

        repositoryHandler.store(dbProduct);
    }

    /**
     * Update a product in Grapes database
     *
     * @param dbProduct DbProduct
     */
    public void update(final DbProduct dbProduct) {
        repositoryHandler.store(dbProduct);
    }

    /**
     * Returns all the product names
     *
     * @return List<String>
     */
    public List<String> getProductNames() {
        return repositoryHandler.getProductNames();
    }

    /**
     * Returns a product regarding its name
     *
     * @param name String
     * @return DbProduct
     */
    public DbProduct getProduct(final String name) throws GrapesException {
        final DbProduct dbProduct = repositoryHandler.getProduct(name);

        if(dbProduct == null){
            throw new GrapesException("Product " + name + " does not exist.");
        }

        return dbProduct;
    }

    /**
     * Deletes a product from the database
     *
     * @param name String
     */
    public void deleteProduct(final String name) throws GrapesException {
        final DbProduct dbProduct = getProduct(name);
        repositoryHandler.deleteProduct(dbProduct.getName());
    }

    /**
     * Patches the product module names
     *
     * @param name String
     * @param moduleNames List<String>
     */
    public void setProductModules(final String name, final List<String> moduleNames) throws GrapesException {
        final DbProduct dbProduct = getProduct(name);
        dbProduct.setModules(moduleNames);
        repositoryHandler.store(dbProduct);
    }
}
