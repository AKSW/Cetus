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
package org.aksw.simba.cetus.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TypeExtractorTest {

    @Parameters
    public static Collection<Object[]> data() {
        List<Object[]> testConfigs = new ArrayList<Object[]>();

        // testConfigs.add(new Object[] { "$ENTITY$ device_device_n1 ._._.", new
        // String[] { "device_device_n1" } });
        // testConfigs.add(new Object[] {
        // "$ENTITY$ was_be_vbd a_a_dt German_german_jp philosopher_philosopher_n1 ._._.",
        // new String[] { "German_german_jp philosopher_philosopher_n1" } });
        // testConfigs
        // .add(new Object[] {
        // "$ENTITY$ was_be_vbd an_an_dt American_american_jp novelist_novelist_n1 ,_,_, philosopher_philosopher_n1 ,_,_, playwright_playwright_n1 ,_,_, and_and_cc screen_screen_n1 writer_writer_n1 ._._.",
        // new String[] { "American_american_jp novelist_novelist_n1",
        // "philosopher_philosopher_n1",
        // "playwright_playwright_n1", "screen_screen_n1 writer_writer_n1" } });
        // testConfigs.add(new Object[] {
        // "$ENTITY$ is_be_vbz the_the_dt tenth-largest_tenth-large_js country_country_n1 ._._.",
        // new String[] { "tenth-largest_tenth-large_js country_country_n1" }
        // });
        // testConfigs
        // .add(new Object[] {
        // "$ENTITY$ ,_,_, officially_official_av-jn The_the_dt People's_people_ng1 Democratic_democratic_j Republic_Republic_n1 of_of_pp-f Algeria_Algeria_np1 ,_,_, is_be_vbz a_a_dt country_country_n1 ._._.",
        // new String[] { "country_country_n1" } });
        // testConfigs
        // .add(new Object[] {
        // "$ENTITY$ is_be_vbz an_an_dt extinct_extinct_j genus_genus_n1 of_of_pp-f sauropterygian_sauropterygian_n1 type_type_n",
        // new String[] { "extinct_extinct_j genus_genus_n1",
        // "sauropterygian_sauropterygian_n1 type_type_n" } });
        // testConfigs.add(new Object[] {
        // "$ENTITY$ is_be_vbz a_a_dt sub_sub_fw-la type_type_n",
        // new String[] { "sub_sub_fw-la type_type_n" } });
        // testConfigs.add(new Object[] {
        // "$ENTITY$ is_be_vbz a_a_dt quarterly_quarter_av-j peer-reviewed_peer-review_vvn type_type_n",
        // new String[] {
        // "quarterly_quarter_av-j peer-reviewed_peer-review_vvn type_type_n" }
        // });
        testConfigs.add(new Object[] { "$ENTITY$ device_device_NN ._._.", new String[] { "device_device_NN" } });
        testConfigs.add(new Object[] { "$ENTITY$ was_be_VBD a_a_DT German_german_JJ philosopher_philosopher_NN ._._.",
                new String[] { "German_german_JJ philosopher_philosopher_NN" } });
        testConfigs
                .add(new Object[] {
                        "$ENTITY$ was_be_VBD an_an_DT American_american_JJ novelist_novelist_NN ,_,_, philosopher_philosopher_NN ,_,_, playwright_playwright_NN ,_,_, and_and_CC screen_screen_NN writer_writer_NN ._._.",
                        new String[] { "American_american_JJ novelist_novelist_NN", "philosopher_philosopher_NN",
                                "playwright_playwright_NN", "screen_screen_NN writer_writer_NN" } });
        testConfigs.add(new Object[] {
                "$ENTITY$ is_be_VBZ the_the_DT tenth-largest_tenth-large_JJ country_country_NN ._._.",
                new String[] { "tenth-largest_tenth-large_JJ country_country_NN" } });
        testConfigs
                .add(new Object[] {
                        "$ENTITY$ ,_,_, officially_official_RB The_the_DT People's_people_ng1 Democratic_democratic_JJ Republic_Republic_NN of_of_IN Algeria_Algeria_NN ,_,_, is_be_VBZ a_a_DT country_country_NN ._._.",
                        new String[] { "country_country_NN" } });
        testConfigs
                .add(new Object[] {
                        "$ENTITY$ is_be_VBZ an_an_DT extinct_extinct_JJ genus_genus_NN of_of_IN sauropterygian_sauropterygian_JJ type_type_NN",
                        new String[] { "extinct_extinct_JJ genus_genus_NN",
                                "sauropterygian_sauropterygian_JJ type_type_NN" } });
        testConfigs.add(new Object[] { "$ENTITY$ is_be_VBZ a_a_DT sub_sub_FW type_type_NN",
                new String[] { "sub_sub_FW type_type_NN" } });
        testConfigs.add(new Object[] {
                "$ENTITY$ is_be_VBZ a_a_DT quarterly_quarter_RB peer-reviewed_peer-review_VBN type_type_NN",
                new String[] { "quarterly_quarter_RB peer-reviewed_peer-review_VBN type_type_NN" } });
        return testConfigs;
    }

    private String text;
    private Set<String> expectedTypes;

    public TypeExtractorTest(String text, String expectedTypes[]) {
        this.text = text;
        this.expectedTypes = new HashSet<String>(expectedTypes.length);
        for (int i = 0; i < expectedTypes.length; ++i) {
            this.expectedTypes.add(expectedTypes[i]);
        }
    }

    @Test
    public void test() {
        TypeExtractor extractor = new TypeExtractor();
        List<String> types = extractor.extractTypeStrings(text);

        if (expectedTypes == null) {
            Assert.assertNull(types);
            return;
        } else {
            Assert.assertNotNull(types);
        }

        Assert.assertEquals(expectedTypes.size(), types.size());
        for (String type : types) {
            Assert.assertTrue(
                    "Couldn't find \"" + type + "\" in the list of expected types (" + expectedTypes.toString() + ").",
                    expectedTypes.contains(type));
        }
    }
}
