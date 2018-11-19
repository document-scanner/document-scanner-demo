/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.richtercloud.document.scanner.gui;

import java.awt.HeadlessException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author richter
 */
public class ProgressButtonDemoTest {
    private final static Logger LOGGER = LoggerFactory.getLogger(ProgressButtonDemoTest.class);

    /**
     * Test of main method, of class ProgressButtonDemo.
     */
    @Test
    public void testInit() {
        try {
            new ProgressButtonDemo();
        } catch(HeadlessException ex) {
            LOGGER.warn("HeadlessException indicates that the test is run on a headless machine, e.g. a CI service",
                    ex);
        }
    }
}
