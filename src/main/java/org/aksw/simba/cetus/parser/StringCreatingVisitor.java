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
package org.aksw.simba.cetus.parser;

import org.aksw.simba.cetus.parser.antlr4.CetusPatternsBaseVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

public class StringCreatingVisitor extends CetusPatternsBaseVisitor<String> {

    public String visitChildren(RuleNode node) {
	int n = node.getChildCount();
	if (n > 1) {
	    StringBuilder result = new StringBuilder();
	    ParseTree child;
	    String childResult;
	    for (int i = 0; i < n; i++) {
		child = node.getChild(i);
		childResult = child.accept(this);
		if (childResult != null) {
		    if (i > 0) {
			result.append(' ');
		    }
		    result.append(childResult);
		}
	    }
	    return result.toString();
	} else {
	    if (n == 1) {
		return node.getChild(0).accept(this);
	    } else {
		return node.getText();
	    }
	}
    }

    public String visitTerminal(TerminalNode node) {
	return node.getText();
    }
}
