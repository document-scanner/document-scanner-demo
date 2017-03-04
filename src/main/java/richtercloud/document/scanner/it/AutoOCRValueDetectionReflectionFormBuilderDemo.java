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
package richtercloud.document.scanner.it;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.GroupLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import org.apache.commons.collections4.OrderedMap;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import richtercloud.document.scanner.components.AutoOCRValueDetectionReflectionFormBuilder;
import richtercloud.document.scanner.components.OCRResultPanelFetcher;
import richtercloud.document.scanner.components.OCRResultPanelFetcherProgressListener;
import richtercloud.document.scanner.components.ScanResultPanelFetcher;
import richtercloud.document.scanner.components.tag.MemoryTagStorage;
import richtercloud.document.scanner.components.tag.TagStorage;
import richtercloud.document.scanner.gui.DefaultMainPanel;
import richtercloud.document.scanner.gui.DocumentScannerFieldHandler;
import richtercloud.document.scanner.gui.conf.DocumentScannerConf;
import richtercloud.document.scanner.ifaces.ImageWrapper;
import richtercloud.document.scanner.ifaces.MainPanel;
import richtercloud.document.scanner.ifaces.OCREngine;
import richtercloud.document.scanner.ifaces.OCREngineConf;
import richtercloud.document.scanner.ifaces.OCREngineProgressListener;
import richtercloud.document.scanner.ifaces.OCREngineRecognitionException;
import richtercloud.document.scanner.it.entities.AmountMoneyPanelTestClass;
import richtercloud.document.scanner.it.entities.DatePickerTestClass;
import richtercloud.document.scanner.it.entities.DocumentScannerExtensionsTestClass;
import richtercloud.document.scanner.it.entities.EntityB;
import richtercloud.document.scanner.it.entities.ListTestClass;
import richtercloud.document.scanner.it.entities.PrimitivesTestClass;
import richtercloud.document.scanner.it.entities.QueryPanelTestClass;
import richtercloud.document.scanner.setter.ValueSetter;
import richtercloud.message.handler.BugHandler;
import richtercloud.message.handler.ConfirmMessageHandler;
import richtercloud.message.handler.DefaultIssueHandler;
import richtercloud.message.handler.DialogBugHandler;
import richtercloud.message.handler.DialogConfirmMessageHandler;
import richtercloud.message.handler.DialogMessageHandler;
import richtercloud.message.handler.IssueHandler;
import richtercloud.message.handler.MessageHandler;
import richtercloud.reflection.form.builder.ReflectionFormPanel;
import richtercloud.reflection.form.builder.TransformationException;
import richtercloud.reflection.form.builder.components.money.AmountMoneyCurrencyStorage;
import richtercloud.reflection.form.builder.components.money.AmountMoneyExchangeRateRetriever;
import richtercloud.reflection.form.builder.components.money.AmountMoneyUsageStatisticsStorage;
import richtercloud.reflection.form.builder.components.money.MemoryAmountMoneyCurrencyStorage;
import richtercloud.reflection.form.builder.components.money.MemoryAmountMoneyUsageStatisticsStorage;
import richtercloud.reflection.form.builder.components.money.StaticAmountMoneyExchangeRateRetriever;
import richtercloud.reflection.form.builder.jpa.IdGenerator;
import richtercloud.reflection.form.builder.jpa.JPACachedFieldRetriever;
import richtercloud.reflection.form.builder.jpa.JPAFieldRetriever;
import richtercloud.reflection.form.builder.jpa.MemorySequentialIdGenerator;
import richtercloud.reflection.form.builder.jpa.WarningHandler;
import richtercloud.reflection.form.builder.jpa.idapplier.GeneratedValueIdApplier;
import richtercloud.reflection.form.builder.jpa.idapplier.IdApplier;
import richtercloud.reflection.form.builder.jpa.panels.QueryHistoryEntryStorage;
import richtercloud.reflection.form.builder.jpa.panels.QueryHistoryEntryStorageCreationException;
import richtercloud.reflection.form.builder.jpa.panels.QueryHistoryEntryStorageFactory;
import richtercloud.reflection.form.builder.jpa.panels.XMLFileQueryHistoryEntryStorageFactory;
import richtercloud.reflection.form.builder.jpa.storage.DerbyEmbeddedPersistenceStorage;
import richtercloud.reflection.form.builder.jpa.storage.DerbyEmbeddedPersistenceStorageConf;
import richtercloud.reflection.form.builder.jpa.storage.FieldInitializer;
import richtercloud.reflection.form.builder.jpa.storage.PersistenceStorage;
import richtercloud.reflection.form.builder.jpa.storage.ReflectionFieldInitializer;
import richtercloud.reflection.form.builder.storage.StorageConfValidationException;
import richtercloud.reflection.form.builder.storage.StorageCreationException;
import richtercloud.reflection.form.builder.typehandler.TypeHandler;

/**
 * Demo for resizability of class components.
 *
 * @author richter
 */
public class AutoOCRValueDetectionReflectionFormBuilderDemo extends JFrame {
    private static final long serialVersionUID = 1L;
    private final static Logger LOGGER = LoggerFactory.getLogger(AutoOCRValueDetectionReflectionFormBuilderDemo.class);

    /**
     * Test of getComboBoxModelMap method, of class AutoOCRValueDetectionReflectionFormBuilder.
     */
    public AutoOCRValueDetectionReflectionFormBuilderDemo() throws IOException, QueryHistoryEntryStorageCreationException, InstantiationException, IllegalAccessException, TransformationException, StorageConfValidationException, StorageCreationException {
        //There's no mocking in integration tests, but for the GUI test it's
        //fine.
        Set<Class<?>> entityClasses = new HashSet<>(Arrays.asList(DocumentScannerExtensionsTestClass.class,
                DatePickerTestClass.class,
                AmountMoneyPanelTestClass.class,
                ListTestClass.class,
                QueryPanelTestClass.class,
                PrimitivesTestClass.class,
                EntityB.class));
        File databaseDir = File.createTempFile(AutoOCRValueDetectionReflectionFormBuilderDemo.class.getSimpleName(), null);
        FileUtils.forceDelete(databaseDir);
        String databaseName = databaseDir.getAbsolutePath();
        File schemeChecksumFile = File.createTempFile(AutoOCRValueDetectionReflectionFormBuilderDemo.class.getSimpleName(), null);
        DerbyEmbeddedPersistenceStorageConf storageConf = new DerbyEmbeddedPersistenceStorageConf(entityClasses,
                databaseName,
                schemeChecksumFile);
        String persistenceUnitName = "richtercloud_document-scanner-demo_jar_1.0-SNAPSHOTPU";
        int parallelQueryCount = 20;
        JPAFieldRetriever fieldRetriever = new JPACachedFieldRetriever();
        PersistenceStorage storage = new DerbyEmbeddedPersistenceStorage(storageConf,
                persistenceUnitName,
                parallelQueryCount,
                fieldRetriever);
        storage.start();
        for(Class<?> testClass : entityClasses) {
            MessageHandler messageHandler = new DialogMessageHandler(this);
            BugHandler bugHandler = new DialogBugHandler(this, "http://example.com", "", "");
            IssueHandler issueHandler = new DefaultIssueHandler(messageHandler, bugHandler);
            ConfirmMessageHandler confirmMessageHandler = new DialogConfirmMessageHandler(this);
            IdApplier idApplier = new GeneratedValueIdApplier();
            IdGenerator idGenerator = MemorySequentialIdGenerator.getInstance();
            DocumentScannerConf documentScannerConf = new DocumentScannerConf();

            AutoOCRValueDetectionReflectionFormBuilder instance = new AutoOCRValueDetectionReflectionFormBuilder(storage,
                    "fieldDescriptionDialogTitle",
                    messageHandler,
                    confirmMessageHandler,
                    fieldRetriever,
                    idApplier,
                    idGenerator,
                    new HashMap<Class<?>, WarningHandler<?>>(), //warningHandlers
                    new HashMap<Class<? extends JComponent>, ValueSetter<?,?>>(), //valueSetterMapping
                    documentScannerConf
            );
            AmountMoneyUsageStatisticsStorage amountMoneyUsageStatisticsStorage = new MemoryAmountMoneyUsageStatisticsStorage();
            AmountMoneyCurrencyStorage amountMoneyCurrencyStorage = new MemoryAmountMoneyCurrencyStorage();
            AmountMoneyExchangeRateRetriever amountMoneyExchangeRateRetriever = new StaticAmountMoneyExchangeRateRetriever();
            Map<java.lang.reflect.Type, TypeHandler<?, ?,?, ?>> typeHandlerMapping = new HashMap<>();
            OCRResultPanelFetcher oCRResultPanelFetcher = new OCRResultPanelFetcher() {
                @Override
                public String fetch() throws OCREngineRecognitionException {
                    return "OCR result";
                }

                @Override
                public void cancelFetch() throws UnsupportedOperationException {
                    //do nothing
                }

                @Override
                public void addProgressListener(OCRResultPanelFetcherProgressListener progressListener) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public void removeProgressListener(OCRResultPanelFetcherProgressListener progressListener) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            };
            ScanResultPanelFetcher scanResultPanelFetcher = new ScanResultPanelFetcher() {
                @Override
                public List<ImageWrapper> fetch() {
                    return new LinkedList<>();
                }

                @Override
                public void cancelFetch() {
                    //do nothing
                }
            };
            Class<?> primaryClassSelection = DocumentScannerExtensionsTestClass.class;
            OCREngine oCREngine = new OCREngine() {
                @Override
                public String recognizeImages(List images) throws OCREngineRecognitionException {
                    return "OCR result";
                }

                @Override
                public String recognizeImageStreams(OrderedMap imageStreams) throws OCREngineRecognitionException {
                    return "OCR result";
                }

                @Override
                public void cancelRecognizeImages() {
                    //do nothing
                }

                @Override
                public void addProgressListener(OCREngineProgressListener progressListener) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public void removeProgressListener(OCREngineProgressListener progressListener) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public OCREngineConf getoCREngineConf() {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            };
            TagStorage tagStorage = new MemoryTagStorage();
            Map<Class<?>, WarningHandler<?>> warningHandlers = new HashMap<>();
            FieldInitializer fieldInitializer = new ReflectionFieldInitializer(fieldRetriever);
            File entryStorageFile = File.createTempFile(AutoOCRValueDetectionReflectionFormBuilderDemo.class.getSimpleName(),
                    null);
            QueryHistoryEntryStorageFactory entryStorageFactory = new XMLFileQueryHistoryEntryStorageFactory(entryStorageFile,
                    entityClasses,
                    false,
                    messageHandler);
            QueryHistoryEntryStorage initialQueryTextGenerator = entryStorageFactory.create();
            MainPanel mainPanel = new DefaultMainPanel(entityClasses,
                    primaryClassSelection,
                    storage,
                    amountMoneyUsageStatisticsStorage,
                    amountMoneyCurrencyStorage,
                    amountMoneyExchangeRateRetriever,
                    issueHandler,
                    confirmMessageHandler,
                    this, //dockingControlFrame,
                    oCREngine,
                    typeHandlerMapping,
                    documentScannerConf,
                    this, //oCRProgressMonitorParent
                    tagStorage,
                    idApplier,
                    idGenerator,
                    warningHandlers,
                    fieldInitializer,
                    initialQueryTextGenerator,
                    fieldRetriever,
                    fieldRetriever);
            int initialQueryLimit = 20;
            String bidirectionalHelpDialogTitle = "Title";
            DocumentScannerFieldHandler fieldHandler = DocumentScannerFieldHandler.create(amountMoneyUsageStatisticsStorage,
                    amountMoneyCurrencyStorage,
                    amountMoneyExchangeRateRetriever,
                    messageHandler,
                    confirmMessageHandler,
                    typeHandlerMapping,
                    storage,
                    fieldRetriever,
                    fieldRetriever,
                    oCRResultPanelFetcher,
                    scanResultPanelFetcher,
                    documentScannerConf,
                    this, //oCRProgressMonitorParent
                    entityClasses,
                    primaryClassSelection,
                    mainPanel,
                    tagStorage,
                    idApplier,
                    warningHandlers,
                    initialQueryLimit,
                    bidirectionalHelpDialogTitle,
                    fieldInitializer,
                    initialQueryTextGenerator);

            Object entityToUpdate = testClass.newInstance();
            ReflectionFormPanel testPanel = instance.transformEntityClass(testClass,
                    entityToUpdate,
                    fieldHandler);
            this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE
                //must not be EXIT_ON_CLOSE because that terminates the
                //application and closes all JFrames
            );
            GroupLayout frameLayout = new GroupLayout(this.getContentPane());
            this.getContentPane().setLayout(frameLayout);
            frameLayout.setHorizontalGroup(frameLayout.createSequentialGroup().addComponent(testPanel));
            frameLayout.setVerticalGroup(frameLayout.createSequentialGroup().addComponent(testPanel));
            this.setBounds(10, 10, 800, 600);
            this.pack();
        }
    }

    /**
     * Needs to be in {@code main} method because otherwise {@code JFrame}s
     * are killed by test runner.
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new AutoOCRValueDetectionReflectionFormBuilderDemo().setVisible(true);
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | IOException | QueryHistoryEntryStorageCreationException | TransformationException | StorageConfValidationException | StorageCreationException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
}
