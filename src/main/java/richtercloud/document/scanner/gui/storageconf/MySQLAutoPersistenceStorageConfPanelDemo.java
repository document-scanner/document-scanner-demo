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

import java.awt.EventQueue;
import java.awt.HeadlessException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Set;
import javax.swing.JFrame;
import org.apache.commons.io.FileUtils;
import richtercloud.document.scanner.gui.Constants;
import richtercloud.jhbuild.java.wrapper.DownloadCombi;
import richtercloud.jhbuild.java.wrapper.ExtractionException;
import richtercloud.jhbuild.java.wrapper.ExtractionMode;
import richtercloud.message.handler.ConfirmMessageHandler;
import richtercloud.message.handler.DialogBugHandler;
import richtercloud.message.handler.DialogConfirmMessageHandler;
import richtercloud.message.handler.DialogIssueHandler;
import richtercloud.message.handler.DialogMessageHandler;
import richtercloud.message.handler.IssueHandler;
import richtercloud.reflection.form.builder.jpa.storage.MySQLAutoPersistenceStorageConf;

/**
 *
 * @author richter
 */
public class MySQLAutoPersistenceStorageConfPanelDemo extends JFrame {
    private static final long serialVersionUID = 1L;

    public MySQLAutoPersistenceStorageConfPanelDemo() throws HeadlessException, IOException, ExtractionException {
        //Linux 32-bit
        final String downloadURLLinux32 = MySQLAutoPersistenceStorageConfPanel.MYSQL_DOWNLOAD_URL_LINUX_32;
        final String downloadTargetLinux32 = MySQLAutoPersistenceStorageConfPanel.MYSQL_DOWNLOAD_TARGET_LINUX_32;
        final ExtractionMode extractionModeLinux32 = MySQLAutoPersistenceStorageConfPanel.MYSQL_EXTRACTION_MODE_LINUX_32;
        final String extractionDirLinux32 = MySQLAutoPersistenceStorageConfPanel.MYSQL_EXTRACTION_LOCATION_LINUX_32;
        final String md5SumLinux32 = MySQLAutoPersistenceStorageConfPanel.MD5_SUM_LINUX_32;
        Set<Class<?>> entityClasses = Constants.ENTITY_CLASSES;
        String username = "documentScanner";
        File databaseDir = Files.createTempDirectory(MySQLAutoPersistenceStorageConfPanelDemo.class.getSimpleName()).toFile();
        FileUtils.forceDelete(databaseDir);
            //databaseDir mustn't exist for MySQL
        File schemeChecksumFile = File.createTempFile(MySQLAutoPersistenceStorageConfPanelDemo.class.getSimpleName(), null);
        MySQLAutoPersistenceStorageConf storageConf = new MySQLAutoPersistenceStorageConf(entityClasses,
                "localhost", //hostname
                username,
                databaseDir.getAbsolutePath(),
                schemeChecksumFile);
        IssueHandler issueHandler = new DialogIssueHandler(new DialogMessageHandler(null //parent
                ),
                new DialogBugHandler(null, //parent
                        "[bug reporting URL]"));
        ConfirmMessageHandler confirmMessageHandler = new DialogConfirmMessageHandler(null //parent
        );
        MySQLAutoPersistenceStorageConfPanel instance = new MySQLAutoPersistenceStorageConfPanel(
                storageConf,
                issueHandler,
                confirmMessageHandler,
                false) {
            @Override
            protected DownloadCombi getDownloadCombi() {
                return new DownloadCombi(downloadURLLinux32,
                        downloadTargetLinux32,
                        extractionModeLinux32,
                        extractionDirLinux32,
                        md5SumLinux32);
            }
        };
        this.getContentPane().add(instance);
        //Linux 64-bit
        final String downloadURLLinux64 = MySQLAutoPersistenceStorageConfPanel.MYSQL_DOWNLOAD_URL_LINUX_64;
        final String downloadTargetLinux64 = MySQLAutoPersistenceStorageConfPanel.MYSQL_DOWNLOAD_TARGET_LINUX_64;
        final ExtractionMode extractionModeLinux64 = MySQLAutoPersistenceStorageConfPanel.MYSQL_EXTRACTION_MODE_LINUX_64;
        final String extractionDirLinux64 = MySQLAutoPersistenceStorageConfPanel.MYSQL_EXTRACTION_LOCATION_LINUX_64;
        final String md5SumLinux64 = MySQLAutoPersistenceStorageConfPanel.MD5_SUM_LINUX_64;
        instance = new MySQLAutoPersistenceStorageConfPanel(
                storageConf,
                issueHandler,
                confirmMessageHandler,
                false) {
            @Override
            protected DownloadCombi getDownloadCombi() {
                return new DownloadCombi(downloadURLLinux64,
                        downloadTargetLinux64,
                        extractionModeLinux64,
                        extractionDirLinux64,
                        md5SumLinux64);
            }
        };
        this.getContentPane().add(instance);
        //Windows 32-bit
        final String downloadURLWindows32 = MySQLAutoPersistenceStorageConfPanel.MYSQL_DOWNLOAD_URL_WINDOWS_32;
        final String downloadTargetWindows32 = MySQLAutoPersistenceStorageConfPanel.MYSQL_DOWNLOAD_TARGET_WINDOWS_32;
        final ExtractionMode extractionModeWindows32 = MySQLAutoPersistenceStorageConfPanel.MYSQL_EXTRACTION_MODE_WINDOWS_32;
        final String extractionDirWindows32 = MySQLAutoPersistenceStorageConfPanel.MYSQL_EXTRACTION_LOCATION_WINDOWS_32;
        final String md5SumWindows32 = MySQLAutoPersistenceStorageConfPanel.MD5_SUM_WINDOWS_32;
        instance = new MySQLAutoPersistenceStorageConfPanel(
                storageConf,
                issueHandler,
                confirmMessageHandler,
                false) {
            @Override
            protected DownloadCombi getDownloadCombi() {
                return new DownloadCombi(downloadURLWindows32,
                        downloadTargetWindows32,
                        extractionModeWindows32,
                        extractionDirWindows32,
                        md5SumWindows32);
            }
        };
        this.getContentPane().add(instance);
        //Windows 64-bit
        final String downloadURLWindows64 = MySQLAutoPersistenceStorageConfPanel.MYSQL_DOWNLOAD_URL_WINDOWS_64;
        final String downloadTargetWindows64 = MySQLAutoPersistenceStorageConfPanel.MYSQL_DOWNLOAD_TARGET_WINDOWS_64;
        final ExtractionMode extractionModeWindows64 = MySQLAutoPersistenceStorageConfPanel.MYSQL_EXTRACTION_MODE_WINDOWS_64;
        final String extractionDirWindows64 = MySQLAutoPersistenceStorageConfPanel.MYSQL_EXTRACTION_LOCATION_WINDOWS_64;
        final String md5SumWindows64 = MySQLAutoPersistenceStorageConfPanel.MD5_SUM_WINDOWS_64;
        instance = new MySQLAutoPersistenceStorageConfPanel(
                storageConf,
                issueHandler,
                confirmMessageHandler,
                false) {
            @Override
            protected DownloadCombi getDownloadCombi() {
                return new DownloadCombi(downloadURLWindows64,
                        downloadTargetWindows64,
                        extractionModeWindows64,
                        extractionDirWindows64,
                        md5SumWindows64);
            }
        };
        this.getContentPane().add(instance);
        //Mac OSX 64-bit
        final String downloadURLMacOSX64 = MySQLAutoPersistenceStorageConfPanel.MYSQL_DOWNLOAD_URL_MAC_OSX_64;
        final String downloadTargetMacOSX64 = MySQLAutoPersistenceStorageConfPanel.MYSQL_DOWNLOAD_TARGET_MAC_OSX_64;
        final ExtractionMode extractionModeMacOSX64 = MySQLAutoPersistenceStorageConfPanel.MYSQL_EXTRACTION_MODE_MAC_OSX_64;
        final String extractionDirMacOSX64 = MySQLAutoPersistenceStorageConfPanel.MYSQL_EXTRACTION_LOCATION_MAC_OSX_64;
        final String md5SumMacOSX64 = MySQLAutoPersistenceStorageConfPanel.MD5_SUM_MAC_OSX_64;
        instance = new MySQLAutoPersistenceStorageConfPanel(
                storageConf,
                issueHandler,
                confirmMessageHandler,
                false) {
            @Override
            protected DownloadCombi getDownloadCombi() {
                return new DownloadCombi(downloadURLMacOSX64,
                        downloadTargetMacOSX64,
                        extractionModeMacOSX64,
                        extractionDirMacOSX64,
                        md5SumMacOSX64);
            }
        };
        this.getContentPane().add(instance);
        setSize(800, 600);
        pack();
    }

    public static void main(String[] args) throws IOException, ExtractionException {
        EventQueue.invokeLater(() -> {
            MySQLAutoPersistenceStorageConfPanelDemo instance = null;
            try {
                instance = new MySQLAutoPersistenceStorageConfPanelDemo();
                instance.setVisible(true);
            } catch (IOException | ExtractionException | HeadlessException ex) {
                if(instance != null) {
                    instance.setVisible(false);
                    instance.dispose();
                }
                throw new RuntimeException(ex);
            }
        });
    }
}
