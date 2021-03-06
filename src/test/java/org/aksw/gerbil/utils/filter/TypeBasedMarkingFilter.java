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
package org.aksw.gerbil.utils.filter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.aksw.gerbil.transfer.nif.TypedMarking;

public class TypeBasedMarkingFilter<T extends TypedMarking> extends AbstractMarkingFilter<T> {

    private Set<String> types = new HashSet<String>();
    private boolean acceptTypes;

    public TypeBasedMarkingFilter(boolean acceptTypes, String... types) {
        this.types.addAll(Arrays.asList(types));
        this.acceptTypes = acceptTypes;
    }

    @Override
    public boolean isMarkingGood(T marking) {
        Set<String> types = marking.getTypes();
        if (acceptTypes) {
            for (String acceptedType : this.types) {
                if (types.contains(acceptedType)) {
                    return true;
                }
            }
            return false;
        } else {
            for (String notAcceptedType : this.types) {
                if (types.contains(notAcceptedType)) {
                    return false;
                }
            }
            return true;
        }
    }

}
