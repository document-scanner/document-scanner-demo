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

import java.awt.HeadlessException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
import richtercloud.document.scanner.components.OCRResultPanelFetcher;
import richtercloud.document.scanner.components.OCRResultPanelFetcherProgressListener;
import richtercloud.document.scanner.components.ScanResultPanelFetcher;
import richtercloud.document.scanner.components.ValueDetectionReflectionFormBuilder;
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
import richtercloud.message.handler.ConfirmMessageHandler;
import richtercloud.message.handler.DialogConfirmMessageHandler;
import richtercloud.message.handler.DialogIssueHandler;
import richtercloud.message.handler.IssueHandler;
import richtercloud.reflection.form.builder.ReflectionFormPanel;
import richtercloud.reflection.form.builder.TransformationException;
import richtercloud.reflection.form.builder.components.money.AmountMoneyCurrencyStorage;
import richtercloud.reflection.form.builder.components.money.AmountMoneyExchangeRateRetriever;
import richtercloud.reflection.form.builder.components.money.AmountMoneyUsageStatisticsStorage;
import richtercloud.reflection.form.builder.components.money.MemoryAmountMoneyCurrencyStorage;
import richtercloud.reflection.form.builder.components.money.MemoryAmountMoneyUsageStatisticsStorage;
import richtercloud.reflection.form.builder.components.money.StaticAmountMoneyExchangeRateRetriever;
import richtercloud.reflection.form.builder.jpa.JPACachedFieldRetriever;
import richtercloud.reflection.form.builder.jpa.JPAFieldRetriever;
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
import richtercloud.validation.tools.FieldRetrievalException;

/**
 * Demo for resizability of class components.
 *
 * @author richter
 */
public class ValueDetectionReflectionFormBuilderDemo extends JFrame {
    private static final long serialVersionUID = 1L;
    private PersistenceStorage<Long> storage;

    /**
     * Test of getComboBoxModelMap method, of class
     * ValueDetectionReflectionFormBuilder.
     *
     * @throws HeadlessException allows to skip the constructor test on a remote
     * CI service
     */
    public ValueDetectionReflectionFormBuilderDemo() throws IOException,
            QueryHistoryEntryStorageCreationException,
            InstantiationException,
            IllegalAccessException,
            TransformationException,
            StorageConfValidationException,
            StorageCreationException,
            FieldRetrievalException,
            HeadlessException {
        //There's no mocking in integration tests, but for the GUI test it's
        //fine.
        Set<Class<?>> entityClasses = new HashSet<>(Arrays.asList(DocumentScannerExtensionsTestClass.class,
                DatePickerTestClass.class,
                AmountMoneyPanelTestClass.class,
                ListTestClass.class,
                QueryPanelTestClass.class,
                PrimitivesTestClass.class,
                EntityB.class));
        File databaseDir = Files.createTempDirectory(ValueDetectionReflectionFormBuilderDemo.class.getSimpleName()).toFile();
        FileUtils.forceDelete(databaseDir);
            //databaseDir mustn't exist for Apache Derby
        String databaseName = databaseDir.getAbsolutePath();
        File schemeChecksumFile = File.createTempFile(ValueDetectionReflectionFormBuilderDemo.class.getSimpleName(), null);
        DerbyEmbeddedPersistenceStorageConf storageConf = new DerbyEmbeddedPersistenceStorageConf(entityClasses,
                databaseName,
                schemeChecksumFile);
        String persistenceUnitName = "richtercloud_document-scanner-demo_jar_1.0-SNAPSHOTPU";
        int parallelQueryCount = 20;
        JPAFieldRetriever fieldRetriever = new JPACachedFieldRetriever();
        storage = new DerbyEmbeddedPersistenceStorage(storageConf,
                persistenceUnitName,
                parallelQueryCount,
                fieldRetriever);
        storage.start();
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE
            //must not be EXIT_ON_CLOSE because that terminates the
            //application and closes all JFrames
        );
        GroupLayout frameLayout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(frameLayout);
        GroupLayout.Group frameLayoutHorizontalGroup = frameLayout.createSequentialGroup();
        GroupLayout.Group frameLayoutVerticalGroup = frameLayout.createSequentialGroup();
        for(Class<?> testClass : entityClasses) {
            IssueHandler issueHandler = new DialogIssueHandler(this, "http://example.com");
            ConfirmMessageHandler confirmMessageHandler = new DialogConfirmMessageHandler(this);
            IdApplier idApplier = new GeneratedValueIdApplier();
            DocumentScannerConf documentScannerConf = new DocumentScannerConf();

            ValueDetectionReflectionFormBuilder instance = new ValueDetectionReflectionFormBuilder(storage,
                    "fieldDescriptionDialogTitle",
                    issueHandler,
                    confirmMessageHandler,
                    fieldRetriever,
                    idApplier,
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
                    throw new UnsupportedOperationException();
                }

                @Override
                public void removeProgressListener(OCRResultPanelFetcherProgressListener progressListener) {
                    throw new UnsupportedOperationException();
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
                    throw new UnsupportedOperationException();
                }

                @Override
                public void removeProgressListener(OCREngineProgressListener progressListener) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public OCREngineConf getoCREngineConf() {
                    throw new UnsupportedOperationException();
                }
            };
            TagStorage tagStorage = new MemoryTagStorage();
            Map<Class<?>, WarningHandler<?>> warningHandlers = new HashMap<>();
            FieldInitializer fieldInitializer = new ReflectionFieldInitializer(fieldRetriever);
            File entryStorageFile = File.createTempFile(ValueDetectionReflectionFormBuilderDemo.class.getSimpleName(),
                    null);
            QueryHistoryEntryStorageFactory<?> entryStorageFactory = new XMLFileQueryHistoryEntryStorageFactory(entryStorageFile,
                    entityClasses,
                    false,
                    issueHandler);
            QueryHistoryEntryStorage initialQueryTextGenerator = entryStorageFactory.create();
            MainPanel mainPanel = new DefaultMainPanel(entityClasses,
                    primaryClassSelection,
                    storage,
                    amountMoneyCurrencyStorage,
                    amountMoneyExchangeRateRetriever,
                    issueHandler,
                    confirmMessageHandler,
                    this, //dockingControlFrame,
                    oCREngine,
                    typeHandlerMapping,
                    documentScannerConf,
                    tagStorage,
                    idApplier,
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
                    issueHandler,
                    confirmMessageHandler,
                    typeHandlerMapping,
                    storage,
                    fieldRetriever,
                    oCRResultPanelFetcher,
                    scanResultPanelFetcher,
                    documentScannerConf,
                    entityClasses,
                    primaryClassSelection,
                    mainPanel,
                    tagStorage,
                    idApplier,
                    initialQueryLimit,
                    bidirectionalHelpDialogTitle,
                    fieldInitializer,
                    initialQueryTextGenerator);

            Object entityToUpdate = testClass.newInstance();
            ReflectionFormPanel<?> testPanel = instance.transformEntityClass(testClass,
                    entityToUpdate,
                    fieldHandler);
            frameLayoutHorizontalGroup.addComponent(testPanel);
            frameLayoutVerticalGroup.addComponent(testPanel);
        }
        frameLayout.setHorizontalGroup(frameLayoutHorizontalGroup);
        frameLayout.setVerticalGroup(frameLayoutVerticalGroup);
        this.setBounds(10, 10, 800, 600);
        this.pack();
    }

    public PersistenceStorage<Long> getStorage() {
        return storage;
    }

    /**
     * Needs to be in {@code main} method because otherwise {@code JFrame}s
     * are killed by test runner.
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new ValueDetectionReflectionFormBuilderDemo().setVisible(true);
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | IOException | QueryHistoryEntryStorageCreationException | TransformationException | StorageConfValidationException | StorageCreationException | FieldRetrievalException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
}
