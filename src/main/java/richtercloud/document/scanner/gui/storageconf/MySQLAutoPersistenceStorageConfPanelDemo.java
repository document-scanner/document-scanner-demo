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
package richtercloud.document.scanner.gui.storageconf;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import javax.swing.JOptionPane;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import richtercloud.document.scanner.gui.Constants;
import richtercloud.message.handler.ConfirmMessageHandler;
import richtercloud.message.handler.DialogConfirmMessageHandler;
import richtercloud.message.handler.DialogMessageHandler;
import richtercloud.message.handler.MessageHandler;
import richtercloud.reflection.form.builder.jpa.storage.MySQLAutoPersistenceStorageConf;

/**
 *
 * @author richter
 */
public class MySQLAutoPersistenceStorageConfPanelDemo {
    private final static Logger LOGGER = LoggerFactory.getLogger(MySQLAutoPersistenceStorageConfPanelDemo.class);
    private final static String DOWNLOAD_FAILED = "Download failed (result is false)!";
    private final static String FAILURE = "Failure";

    public static void main(String[] args) throws IOException {
        testMySQLDownload();
    }

    /**
     * Test of mySQLDownload method, of class MySQLAutoPersistenceStorageConfPanel.
     */
    public static void testMySQLDownload() throws IOException {
        LOGGER.debug("Running download test for Linux 32-bit");
        //Linux 32-bit
        String downloadURL = MySQLAutoPersistenceStorageConfPanel.DOWNLOAD_URL_LINUX_32;
        String downloadTarget = MySQLAutoPersistenceStorageConfPanel.MYSQL_DOWNLOAD_TARGET_LINUX_32;
        int extractionMode = MySQLAutoPersistenceStorageConfPanel.EXTRACTION_MODE_TAR_GZ;
        String extractionDir = MySQLAutoPersistenceStorageConfPanel.MYSQL_EXTRACTION_TARGET_LINUX_32;
        String md5Sum = MySQLAutoPersistenceStorageConfPanel.MD5_SUM_LINUX_32;
        Set<Class<?>> entityClasses = Constants.ENTITY_CLASSES;
        String username = "documentScanner";
        File databaseDir = File.createTempFile(MySQLAutoPersistenceStorageConfPanelDemo.class.getSimpleName(), null);
        FileUtils.forceDelete(databaseDir);
        File schemeChecksumFile = File.createTempFile(MySQLAutoPersistenceStorageConfPanelDemo.class.getSimpleName(), null);
        MySQLAutoPersistenceStorageConf storageConf = new MySQLAutoPersistenceStorageConf(entityClasses,
                username,
                databaseDir.getAbsolutePath(),
                schemeChecksumFile);
        MessageHandler messageHandler = new DialogMessageHandler(null //parent
        );
        ConfirmMessageHandler confirmMessageHandler = new DialogConfirmMessageHandler(null //parent
        );
        MySQLAutoPersistenceStorageConfPanel instance = new MySQLAutoPersistenceStorageConfPanel(
                storageConf,
                messageHandler,
                confirmMessageHandler,
                false);
        boolean result = instance.mySQLDownload(downloadURL,
                downloadTarget,
                extractionMode,
                extractionDir,
                md5Sum);
        if(!result) {
            JOptionPane.showConfirmDialog(instance,
                    DOWNLOAD_FAILED,
                    FAILURE,
                    JOptionPane.OK_OPTION,
                    JOptionPane.ERROR_MESSAGE);
        }
        LOGGER.debug("Running download test for Linux 64-bit");
        //Linux 64-bit
        downloadURL = MySQLAutoPersistenceStorageConfPanel.DOWNLOAD_URL_LINUX_64;
        downloadTarget = MySQLAutoPersistenceStorageConfPanel.MYSQL_DOWNLOAD_TARGET_LINUX_64;
        extractionMode = MySQLAutoPersistenceStorageConfPanel.EXTRACTION_MODE_TAR_GZ;
        extractionDir = MySQLAutoPersistenceStorageConfPanel.MYSQL_EXTRACTION_TARGET_LINUX_64;
        md5Sum = MySQLAutoPersistenceStorageConfPanel.MD5_SUM_LINUX_64;
        instance = new MySQLAutoPersistenceStorageConfPanel(
                storageConf,
                messageHandler,
                confirmMessageHandler,
                false);
        result = instance.mySQLDownload(downloadURL,
                downloadTarget,
                extractionMode,
                extractionDir,
                md5Sum);
        if(!result) {
            JOptionPane.showConfirmDialog(instance,
                    DOWNLOAD_FAILED,
                    FAILURE,
                    JOptionPane.OK_OPTION,
                    JOptionPane.ERROR_MESSAGE);
        }
        LOGGER.debug("Running download test for Windows 32-bit");
        //Windows 32-bit
        downloadURL = MySQLAutoPersistenceStorageConfPanel.DOWNLOAD_URL_WINDOWS_32;
        downloadTarget = MySQLAutoPersistenceStorageConfPanel.MYSQL_DOWNLOAD_TARGET_WINDOWS_32;
        extractionMode = MySQLAutoPersistenceStorageConfPanel.EXTRACTION_MODE_ZIP;
        extractionDir = MySQLAutoPersistenceStorageConfPanel.MYSQL_EXTRACTION_TARGET_WINDOWS_32;
        md5Sum = MySQLAutoPersistenceStorageConfPanel.MD5_SUM_WINDOWS_32;
        instance = new MySQLAutoPersistenceStorageConfPanel(
                storageConf,
                messageHandler,
                confirmMessageHandler,
                false);
        result = instance.mySQLDownload(downloadURL,
                downloadTarget,
                extractionMode,
                extractionDir,
                md5Sum);
        if(!result) {
            JOptionPane.showConfirmDialog(instance,
                    DOWNLOAD_FAILED,
                    FAILURE,
                    JOptionPane.OK_OPTION,
                    JOptionPane.ERROR_MESSAGE);
        }
        LOGGER.debug("Running download test for Windows 64-bit");
        //Windows 64-bit
        downloadURL = MySQLAutoPersistenceStorageConfPanel.DOWNLOAD_URL_WINDOWS_64;
        downloadTarget = MySQLAutoPersistenceStorageConfPanel.MYSQL_DOWNLOAD_TARGET_WINDOWS_64;
        extractionMode = MySQLAutoPersistenceStorageConfPanel.EXTRACTION_MODE_ZIP;
        extractionDir = MySQLAutoPersistenceStorageConfPanel.MYSQL_EXTRACTION_TARGET_WINDOWS_64;
        md5Sum = MySQLAutoPersistenceStorageConfPanel.MD5_SUM_WINDOWS_64;
        instance = new MySQLAutoPersistenceStorageConfPanel(
                storageConf,
                messageHandler,
                confirmMessageHandler,
                false);
        result = instance.mySQLDownload(downloadURL,
                downloadTarget,
                extractionMode,
                extractionDir,
                md5Sum);
        if(!result) {
            JOptionPane.showConfirmDialog(instance,
                    DOWNLOAD_FAILED,
                    FAILURE,
                    JOptionPane.OK_OPTION,
                    JOptionPane.ERROR_MESSAGE);
        }
        LOGGER.debug("Running download test for Mac OSX 64-bit");
        //Mac OSX 64-bit
        downloadURL = MySQLAutoPersistenceStorageConfPanel.DOWNLOAD_URL_MAC_OSX_64;
        downloadTarget = MySQLAutoPersistenceStorageConfPanel.MYSQL_DOWNLOAD_TARGET_MAC_OSX_64;
        extractionMode = MySQLAutoPersistenceStorageConfPanel.EXTRACTION_MODE_TAR_GZ;
        extractionDir = MySQLAutoPersistenceStorageConfPanel.MYSQL_EXTRACTION_TARGET_MAC_OSX_64;
        md5Sum = MySQLAutoPersistenceStorageConfPanel.MD5_SUM_MAC_OSX_64;
        instance = new MySQLAutoPersistenceStorageConfPanel(
                storageConf,
                messageHandler,
                confirmMessageHandler,
                false);
        result = instance.mySQLDownload(downloadURL,
                downloadTarget,
                extractionMode,
                extractionDir,
                md5Sum);
        if(!result) {
            JOptionPane.showConfirmDialog(instance,
                    DOWNLOAD_FAILED,
                    FAILURE,
                    JOptionPane.OK_OPTION,
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
