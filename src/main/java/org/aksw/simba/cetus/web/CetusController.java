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
/**
 * This file is part of Cetus.
 *
 * Cetus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Cetus is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Cetus.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.aksw.simba.cetus.web;

import javax.annotation.PostConstruct;

import org.aksw.gerbil.transfer.nif.Document;
import org.aksw.gerbil.transfer.nif.TurtleNIFDocumentCreator;
import org.aksw.gerbil.transfer.nif.TurtleNIFDocumentParser;
import org.aksw.simba.cetus.annotator.CetusAnnotator;
import org.aksw.simba.cetus.annotator.CetusSurfaceFormExtractor;
import org.aksw.simba.cetus.fox.FoxBasedTypeSearcher;
import org.aksw.simba.cetus.yago.YagoBasedTypeSearcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CetusController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CetusController.class);

    @Autowired
    private CetusSurfaceFormExtractor extractor;

    @Autowired
    private YagoBasedTypeSearcher yagoBasedSearcher;

    @Autowired
    private FoxBasedTypeSearcher foxBasedTypeSearcher;

    private CetusAnnotator yagoBasedAnnotator;
    private CetusAnnotator foxBasedAnnotator;

    private TurtleNIFDocumentParser parser = new TurtleNIFDocumentParser();
    private TurtleNIFDocumentCreator creator = new TurtleNIFDocumentCreator();

    @PostConstruct
    public void init() {
        yagoBasedAnnotator = new CetusAnnotator(extractor, yagoBasedSearcher);
        foxBasedAnnotator = new CetusAnnotator(extractor, foxBasedTypeSearcher);
    }

    @RequestMapping(value = "/yago", produces = "application/x-turtle;charset=utf-8")
    public ResponseEntity<String> yago(@RequestBody String data) {
        Document document = null;
        try {
            document = parser.getDocumentFromNIFString(data);
        } catch (Exception e) {
            LOGGER.error("Exception while parsing NIF string.", e);
            throw new IllegalArgumentException("Couldn't parse the given NIF document.");
        }
        LOGGER.info("Request: " + document.toString());
        document = yagoBasedAnnotator.performTypeExtraction(document);
        LOGGER.info("Response: " + document.toString());
        // Add a header that clearly says, that we produce an utf-8 String.
        // Otherwise Spring might encode the response in the wrong way...
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/x-turtle;charset=utf-8");
        return new ResponseEntity<String>(creator.getDocumentAsNIFString(document), responseHeaders, HttpStatus.OK);
    }

    @RequestMapping("/fox")
    public ResponseEntity<String> fox(@RequestBody String data) {
        Document document = null;
        try {
            document = parser.getDocumentFromNIFString(data);
        } catch (Exception e) {
            LOGGER.error("Exception while parsing NIF string.", e);
            throw new IllegalArgumentException("Couldn't parse the given NIF document.");
        }
        document = foxBasedAnnotator.performTypeExtraction(document);
        // Add a header that clearly says, that we produce an utf-8 String.
        // Otherwise Spring might encode the response in the wrong way...
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/x-turtle;charset=utf-8");
        return new ResponseEntity<String>(creator.getDocumentAsNIFString(document), responseHeaders, HttpStatus.OK);
    }

}
