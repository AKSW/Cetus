/**
 * The MIT License
 * Copyright (c) 2015 Agile Knowledge Engineering and Semantic Web (AKSW) (usbeck@informatik.uni-leipzig.de)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.aksw.simba.cetus.preprocessing;

import org.aksw.jena_sparql_api.cache.core.QueryExecutionFactoryCacheEx;
import org.aksw.jena_sparql_api.cache.extra.CacheCoreEx;
import org.aksw.jena_sparql_api.cache.extra.CacheCoreH2;
import org.aksw.jena_sparql_api.cache.extra.CacheEx;
import org.aksw.jena_sparql_api.cache.extra.CacheExImpl;
import org.aksw.jena_sparql_api.core.QueryExecutionFactory;
import org.aksw.jena_sparql_api.delay.core.QueryExecutionFactoryDelay;
import org.aksw.jena_sparql_api.http.QueryExecutionFactoryHttp;
import org.aksw.jena_sparql_api.pagination.core.QueryExecutionFactoryPaginated;
import org.aksw.simba.cetus.datatypes.DescribedEntity;
import org.aksw.simba.cetus.datatypes.DescribedEntityType;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.AbstractDocumentSupplier;
import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.aksw.simba.topicmodeling.utils.doc.DocumentText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.vocabulary.RDFS;

public class CetusSPARQLDocumentSupplier extends AbstractDocumentSupplier {

    private static final Logger LOGGER = LoggerFactory.getLogger(CetusSPARQLDocumentSupplier.class);

    private static final String ABSTRACT = "abstract";
    private static final String ENTITY = "entity";
    private static final String ENTITY_LABEL = "elabel";
    private static final String TYPE = "type";
    private static final String TYPE_LABEL = "tlabel";
    private static final String QUERY = "SELECT * { ?" + ENTITY + " a ?" + TYPE + " . ?" + TYPE + " <"
            + RDFS.label.getURI() + "> ?" + TYPE_LABEL + ". FILTER( lang(?" + TYPE_LABEL + ") = \"en\"). ?" + ENTITY
            + " <" + RDFS.label.getURI() + "> ?" + ENTITY_LABEL + ". FILTER( lang(?" + ENTITY_LABEL + ") = \"en\"). ?"
            + ENTITY + " <http://dbpedia.org/ontology/abstract> ?" + ABSTRACT + ". FILTER( lang(?" + ABSTRACT
            + ") = \"en\"). }";

    private String endpoint;
    private QueryExecution qe = null;
    private ResultSet results;

    public CetusSPARQLDocumentSupplier(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public Document getNextDocument() {
        Document document = null;
        if (qe == null) {
            startQuery();
        }
        if (results != null) {
            if (results.hasNext()) {
                document = createDocument(results.next());
            } else {
                if (qe != null) {
                    qe.close();
                }
                results = null;
            }
        }
        return document;
    }

    private void startQuery() {
        try {
            long timeToLive = 360l * 24l * 60l * 60l * 1000l;
            CacheCoreEx cacheBackend;
            cacheBackend = CacheCoreH2.create("sparql", timeToLive, true);
            CacheEx cacheFrontend = new CacheExImpl(cacheBackend);
            QueryExecutionFactory qef = new QueryExecutionFactoryHttp(endpoint);
            qef = new QueryExecutionFactoryDelay(qef, 200);
            qef = new QueryExecutionFactoryCacheEx(qef, cacheFrontend);
            qef = new QueryExecutionFactoryPaginated(qef, 10000);
            qe = qef.createQueryExecution(QUERY);
            results = qe.execSelect();
        } catch (Exception e) {
            LOGGER.error("Couldn't query SPARQL endpoint. returning null.", e);
            if (qe != null) {
                qe.close();
            }
        }
    }

    private Document createDocument(QuerySolution solution) {
        Document document = new Document(getNextDocumentId());
        document.addProperty(new DocumentText(solution.getLiteral(ABSTRACT).getString()));
        document.addProperty(new DescribedEntity(solution.getLiteral(ENTITY_LABEL).getString(), solution.getResource(
                ENTITY).getURI()));
        document.addProperty(new DescribedEntityType(solution.getLiteral(TYPE_LABEL).getString(), solution.getResource(
                TYPE).getURI()));
        return document;
    }

}
