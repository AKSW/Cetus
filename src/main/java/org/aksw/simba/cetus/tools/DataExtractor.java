/**
 * Cetus - Cetus performs Entity Typing Using patternS
 * Copyright © 2015 Data Science Group (DICE) (michael.roeder@uni-paderborn.de)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.aksw.simba.cetus.tools;

import java.io.FileWriter;
import java.io.IOException;

import org.aksw.jena_sparql_api.cache.core.QueryExecutionFactoryCacheEx;
import org.aksw.jena_sparql_api.cache.extra.CacheCoreEx;
import org.aksw.jena_sparql_api.cache.extra.CacheCoreH2;
import org.aksw.jena_sparql_api.cache.extra.CacheEx;
import org.aksw.jena_sparql_api.cache.extra.CacheExImpl;
import org.aksw.jena_sparql_api.core.QueryExecutionFactory;
import org.aksw.jena_sparql_api.delay.core.QueryExecutionFactoryDelay;
import org.aksw.jena_sparql_api.http.QueryExecutionFactoryHttp;
import org.aksw.jena_sparql_api.pagination.core.QueryExecutionFactoryPaginated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVWriter;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * This class is the first step of the preprocessing. It gathers the needed data
 * from a SPARQL endpoint containing the DBpedia.
 * 
 * @author Michael R&ouml;der <roeder@informatik.uni-leipzig.de>
 * 
 */
public class DataExtractor {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataExtractor.class);

    public static final String SPARQL_ENDPOINT = "http://139.18.2.164:3030/ds/sparql";
    public static final String OUTPUT_FILE = "raw_data.csv";

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

    public static void main(String[] args) {
        DataExtractor extractor = new DataExtractor();
        extractor.extractDataToFile(SPARQL_ENDPOINT, OUTPUT_FILE);
    }

    public void extractDataToFile(String endpoint, String fileName) {
        FileWriter fWriter = null;
        CSVWriter csvWriter = null;
        long writtenLines = 0;
        QueryExecution qe = null;
        try {
            long timeToLive = 360l * 24l * 60l * 60l * 1000l;
            CacheCoreEx cacheBackend = CacheCoreH2.create("sparql", timeToLive, true);
            CacheEx cacheFrontend = new CacheExImpl(cacheBackend);
            QueryExecutionFactory qef = new QueryExecutionFactoryHttp(endpoint);
            qef = new QueryExecutionFactoryDelay(qef, 200);
            qef = new QueryExecutionFactoryCacheEx(qef, cacheFrontend);
            qef = new QueryExecutionFactoryPaginated(qef, 10000);

            fWriter = new FileWriter(fileName);
            csvWriter = new CSVWriter(fWriter);

            qe = qef.createQueryExecution(QUERY);
            ResultSet results;
            String line[] = new String[5];
            QuerySolution solution;
            if (qe != null) {
                results = qe.execSelect();
                while (results.hasNext()) {
                    solution = results.next();
                    line[0] = solution.getResource(ENTITY).getURI();
                    line[1] = solution.getLiteral(ENTITY_LABEL).getString();
                    line[2] = solution.getResource(TYPE).getURI();
                    line[3] = solution.getLiteral(TYPE_LABEL).getString();
                    line[4] = solution.getLiteral(ABSTRACT).getString();
                    csvWriter.writeNext(line);
                    ++writtenLines;
                    if ((writtenLines % 100000) == 0) {
                        LOGGER.info("Wrote the " + writtenLines + "th line.");
                    }
                }
            } else {
                LOGGER.error("Could not create SPARQL query. Aborting");
                return;
            }

        } catch (Exception e) {
            LOGGER.error("Exception while acquiring data.", e);
        } finally {
            if (qe != null) {
                qe.close();
            }
            if (csvWriter != null) {
                try {
                    csvWriter.close();
                } catch (IOException e) {
                }
            }
            if (fWriter != null) {
                try {
                    fWriter.close();
                } catch (IOException e) {
                }
            }
        }
        LOGGER.info("Finished. Wrote " + writtenLines + " lines.");
    }
}
