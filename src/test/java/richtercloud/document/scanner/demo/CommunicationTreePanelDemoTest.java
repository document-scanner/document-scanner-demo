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
package richtercloud.document.scanner.demo;

import java.awt.HeadlessException;
import java.io.IOException;
import java.sql.SQLException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import richtercloud.reflection.form.builder.ResetException;
import richtercloud.reflection.form.builder.TransformationException;
import richtercloud.reflection.form.builder.jpa.idapplier.IdApplicationException;
import richtercloud.reflection.form.builder.jpa.panels.QueryHistoryEntryStorageCreationException;
import richtercloud.reflection.form.builder.retriever.FieldOrderValidationException;
import richtercloud.reflection.form.builder.storage.StorageConfValidationException;
import richtercloud.reflection.form.builder.storage.StorageCreationException;
import richtercloud.reflection.form.builder.storage.StorageException;

/**
 *
 * @author richter
 */
public class CommunicationTreePanelDemoTest {
    private final static Logger LOGGER = LoggerFactory.getLogger(CommunicationTreePanelDemoTest.class);

    /**
     * Test of main method, of class CommunicationTreePanelDemo.
     */
    @Test
    public void testInit() throws IOException,
            StorageException,
            SQLException,
            NoSuchFieldException,
            StorageConfValidationException,
            StorageCreationException,
            QueryHistoryEntryStorageCreationException,
            IdApplicationException,
            TransformationException,
            ResetException,
            FieldOrderValidationException {
        CommunicationTreePanelDemo instance = null;
        try {
            instance = new CommunicationTreePanelDemo();
        } catch(HeadlessException ex) {
            LOGGER.warn("HeadlessException indicates that the test is run on a headless machine, e.g. a CI service",
                    ex);
        }finally {
            if(instance != null && instance.getStorage() != null) {
                instance.getStorage().shutdown();
            }
        }
    }
}
