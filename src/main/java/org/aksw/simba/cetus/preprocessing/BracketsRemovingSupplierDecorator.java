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
package org.aksw.simba.cetus.preprocessing;

import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.AbstractPropertyEditingDocumentSupplierDecorator;
import org.aksw.simba.topicmodeling.utils.doc.DocumentText;

public class BracketsRemovingSupplierDecorator extends AbstractPropertyEditingDocumentSupplierDecorator<DocumentText> {

    private BracketsRemovingAutomaton automaton = new BracketsRemovingAutomaton();

    public BracketsRemovingSupplierDecorator(DocumentSupplier documentSource) {
        super(documentSource, DocumentText.class);
    }

    @Override
    protected void editDocumentProperty(DocumentText text) {
        text.setText(automaton.removeBrackets(text.getText()));
    }

}
