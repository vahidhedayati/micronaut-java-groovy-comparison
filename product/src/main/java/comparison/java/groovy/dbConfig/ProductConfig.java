package comparison.java.groovy.dbConfig;

import io.micronaut.context.annotation.ConfigurationProperties;

@ConfigurationProperties("product")
public class ProductConfig {

    private String databaseName = "product";
    private String collectionName = "product";

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }


}
