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
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import richtercloud.document.scanner.components.tag.MemoryTagStorage;
import richtercloud.document.scanner.components.tag.TagStorage;
import richtercloud.document.scanner.gui.Constants;
import richtercloud.document.scanner.gui.DocumentScannerFieldHandler;
import richtercloud.document.scanner.gui.conf.DocumentScannerConf;
import richtercloud.document.scanner.model.Address;
import richtercloud.document.scanner.model.EmailAddress;
import richtercloud.document.scanner.model.Person;
import richtercloud.document.scanner.model.TelephoneCall;
import richtercloud.document.scanner.model.TelephoneNumber;
import richtercloud.document.scanner.model.WorkflowItem;
import richtercloud.message.handler.ConfirmMessageHandler;
import richtercloud.message.handler.DialogConfirmMessageHandler;
import richtercloud.message.handler.IssueHandler;
import richtercloud.message.handler.LoggerIssueHandler;
import richtercloud.reflection.form.builder.ReflectionFormBuilder;
import richtercloud.reflection.form.builder.ReflectionFormPanel;
import richtercloud.reflection.form.builder.TransformationException;
import richtercloud.reflection.form.builder.components.money.AmountMoneyCurrencyStorage;
import richtercloud.reflection.form.builder.components.money.AmountMoneyExchangeRateRetriever;
import richtercloud.reflection.form.builder.components.money.FailsafeAmountMoneyExchangeRateRetriever;
import richtercloud.reflection.form.builder.components.money.FileAmountMoneyCurrencyStorage;
import richtercloud.reflection.form.builder.fieldhandler.FieldHandler;
import richtercloud.reflection.form.builder.fieldhandler.MappingFieldHandler;
import richtercloud.reflection.form.builder.fieldhandler.factory.AmountMoneyMappingFieldHandlerFactory;
import richtercloud.reflection.form.builder.jpa.JPACachedFieldRetriever;
import richtercloud.reflection.form.builder.jpa.JPAFieldRetriever;
import richtercloud.reflection.form.builder.jpa.JPAReflectionFormBuilder;
import richtercloud.reflection.form.builder.jpa.MemorySequentialIdGenerator;
import richtercloud.reflection.form.builder.jpa.WarningHandler;
import richtercloud.reflection.form.builder.jpa.fieldhandler.factory.JPAAmountMoneyMappingFieldHandlerFactory;
import richtercloud.reflection.form.builder.jpa.idapplier.IdApplicationException;
import richtercloud.reflection.form.builder.jpa.idapplier.IdApplier;
import richtercloud.reflection.form.builder.jpa.idapplier.LongIdPanelIdApplier;
import richtercloud.reflection.form.builder.jpa.panels.LongIdPanel;
import richtercloud.reflection.form.builder.jpa.panels.QueryHistoryEntryStorage;
import richtercloud.reflection.form.builder.jpa.panels.QueryHistoryEntryStorageCreationException;
import richtercloud.reflection.form.builder.jpa.panels.QueryHistoryEntryStorageFactory;
import richtercloud.reflection.form.builder.jpa.panels.XMLFileQueryHistoryEntryStorageFactory;
import richtercloud.reflection.form.builder.jpa.storage.DerbyEmbeddedPersistenceStorage;
import richtercloud.reflection.form.builder.jpa.storage.DerbyEmbeddedPersistenceStorageConf;
import richtercloud.reflection.form.builder.jpa.storage.FieldInitializer;
import richtercloud.reflection.form.builder.jpa.storage.PersistenceStorage;
import richtercloud.reflection.form.builder.jpa.storage.ReflectionFieldInitializer;
import richtercloud.reflection.form.builder.jpa.typehandler.ElementCollectionTypeHandler;
import richtercloud.reflection.form.builder.jpa.typehandler.ToManyTypeHandler;
import richtercloud.reflection.form.builder.jpa.typehandler.ToOneTypeHandler;
import richtercloud.reflection.form.builder.jpa.typehandler.factory.JPAAmountMoneyMappingTypeHandlerFactory;
import richtercloud.reflection.form.builder.storage.StorageConfValidationException;
import richtercloud.reflection.form.builder.storage.StorageCreationException;
import richtercloud.reflection.form.builder.storage.StorageException;
import richtercloud.reflection.form.builder.typehandler.TypeHandler;
import richtercloud.validation.tools.FieldRetrievalException;

/**
 *
 * @author richter
 */
public class CommunicationTreePanelDemo extends JFrame {
    private static final long serialVersionUID = 1L;
    private final static Logger LOGGER = LoggerFactory.getLogger(CommunicationTreePanelDemo.class);
    private final static String AMOUNT_MONEY_CURRENCY_STORAGE_FILE_NAME = "currency-storage.xml";
    private final ReflectionFormPanel<?> reflectionFormPanel;
    private final IssueHandler issueHandler = new LoggerIssueHandler(LOGGER);
    private final ConfirmMessageHandler confirmMessageHandler = new DialogConfirmMessageHandler(this);
    private final ReflectionFormBuilder<JPAFieldRetriever> reflectionFormBuilder;
    private final JPACachedFieldRetriever fieldRetriever = new JPACachedFieldRetriever();
    private final IdApplier<LongIdPanel> idApplier = new LongIdPanelIdApplier(MemorySequentialIdGenerator.getInstance());
    private final Map<Class<?>, WarningHandler<?>> warningHandlers = new HashMap<>();
    private final TagStorage tagStorage = new MemoryTagStorage();
    private final boolean deleteDatabase = true;
    private final QueryHistoryEntryStorage entryStorage;
    private final File cacheFileDir;
    private PersistenceStorage<Long> storage;

    /**
     * Creates a new {@code CommunicationTreePanelDemo}.
     *
     * @throws IOException
     * @throws StorageException
     * @throws SQLException
     * @throws NoSuchFieldException
     * @throws StorageConfValidationException
     * @throws StorageCreationException
     * @throws QueryHistoryEntryStorageCreationException
     * @throws IdApplicationException
     * @throws TransformationException
     * @throws FieldRetrievalException
     * @throws HeadlessException allows to skip the constructor test on a remote
     * CI service
     */
    public CommunicationTreePanelDemo() throws IOException,
            StorageException,
            SQLException,
            NoSuchFieldException,
            StorageConfValidationException,
            StorageCreationException,
            QueryHistoryEntryStorageCreationException,
            IdApplicationException,
            TransformationException,
            FieldRetrievalException,
            HeadlessException {
        File entryStorageFile = File.createTempFile(CommunicationTreePanelDemo.class.getSimpleName(), null);
        QueryHistoryEntryStorageFactory entryStorageFactory = new XMLFileQueryHistoryEntryStorageFactory(entryStorageFile,
                Constants.ENTITY_CLASSES,
                false,
                issueHandler);
        this.entryStorage = entryStorageFactory.create();
        File databaseDir = new File("/tmp/communication-tree-panel-demo");
        File schemeChecksumFile = new File("/tmp/communcation-tree-panel-demo-checksum-file");
        DerbyEmbeddedPersistenceStorageConf storageConf = new DerbyEmbeddedPersistenceStorageConf(Constants.ENTITY_CLASSES,
                databaseDir.getAbsolutePath(),
                schemeChecksumFile);
        DriverManager.getConnection(storageConf.getConnectionURL()+";create=true");
        storage = new DerbyEmbeddedPersistenceStorage(storageConf,
                "richtercloud_document-scanner-demo_jar_1.0-SNAPSHOTPU",
                1, //parallelQueryCount
                this.fieldRetriever
        );
        storage.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("Shutting down storage");
            storage.shutdown();
            LOGGER.info("Storage shut down");
            if(deleteDatabase) {
                try {
                    FileUtils.deleteDirectory(databaseDir);
                    LOGGER.info(String.format("Removed database directory '%s'",
                            databaseDir.getAbsolutePath()));
                } catch (IOException ex) {
                    LOGGER.info(String.format("Removing database directory '%s' failed, see nested exception for details",
                            databaseDir.getAbsolutePath()),
                            ex);
                }
            }
        },
                String.format("%s shutdown hook", CommunicationTreePanelDemo.class.getSimpleName())
        ));
        FieldInitializer fieldInitializer = new ReflectionFieldInitializer(fieldRetriever);
        this.reflectionFormBuilder = new JPAReflectionFormBuilder(storage,
                "fieldDescriptionDialogTitle",
                issueHandler,
                confirmMessageHandler,
                fieldRetriever,
                idApplier,
                warningHandlers);
        Person sender = new Person(new LinkedList<>(Arrays.asList("Alice")),
                new LinkedList<>(Arrays.asList("A")),
                "Alice A",
                new LinkedList<>(Arrays.asList("Alice", "A")),
                new LinkedList<Address>(),
                new LinkedList<EmailAddress>(),
                new LinkedList<TelephoneNumber>());
        Person recipient = new Person(new LinkedList<>(Arrays.asList("Bob")),
                new LinkedList<>(Arrays.asList("B")),
                "Bob B",
                new LinkedList<>(Arrays.asList("Bob", "B")),
                new LinkedList<Address>(),
                new LinkedList<EmailAddress>(),
                new LinkedList<TelephoneNumber>());
        TelephoneNumber rootTelephoneNumber = new TelephoneNumber(49,
                1,
                2,
                recipient,
                TelephoneNumber.TYPE_LANDLINE);
        WorkflowItem root = new TelephoneCall(new Date(1000),
                new Date(1001),
                "root",
                rootTelephoneNumber,
                sender,
                recipient);
        TelephoneNumber reply1TelephoneNumber = new TelephoneNumber(49,
                1,
                2,
                recipient,
                TelephoneNumber.TYPE_LANDLINE);
        WorkflowItem reply1 = new TelephoneCall(new Date(2000),
                new Date(2001),
                "reply1",
                reply1TelephoneNumber,
                sender,
                recipient,
                new LinkedList<>(Arrays.asList(root)));
        root.getFollowingItems().add(reply1);
        TelephoneNumber reply2TelephoneNumber = new TelephoneNumber(49,
                1,
                2,
                recipient,
                TelephoneNumber.TYPE_LANDLINE);
        WorkflowItem reply2 = new TelephoneCall(new Date(3000),
                new Date(3001),
                "reply2",
                reply2TelephoneNumber,
                sender,
                recipient,
                new LinkedList<>(Arrays.asList(root)));
        root.getFollowingItems().add(reply2);
        TelephoneNumber reply2reply1TelephoneNumber = new TelephoneNumber(49,
                1,
                2,
                recipient,
                TelephoneNumber.TYPE_LANDLINE);
        WorkflowItem reply2reply1 = new TelephoneCall(new Date(4000),
                new Date(4001),
                "reply2reply1",
                reply2reply1TelephoneNumber,
                sender,
                recipient,
                new LinkedList<>(Arrays.asList(reply2)));
        reply2.getFollowingItems().add(reply2reply1);

        //validate manually since EclipseLink is so smart to fails with
        //absolutely no feedback which constraint is violated when calling
        //EntityManager.persist
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        for(Object entity : Arrays.asList(sender, recipient, reply2reply1, reply1, reply2, root)) {
            Set<ConstraintViolation<Object>> constraintViolations = validator.validate(entity);
            if(constraintViolations.size() > 0){
                Iterator<ConstraintViolation<Object>> iterator = constraintViolations.iterator();
                while(iterator.hasNext()){
                    ConstraintViolation<Object> cv = iterator.next();
                    LOGGER.error(cv.getRootBeanClass().getName()+"."+cv.getPropertyPath() + " " +cv.getMessage());
                }
            }
        }

        //apply IDs without the IDApplier because we don't know the components
        //mapped to ID fields here
        sender.setId(MemorySequentialIdGenerator.getInstance().getNextId(sender));
        recipient.setId(MemorySequentialIdGenerator.getInstance().getNextId(recipient));
        reply2reply1.setId(MemorySequentialIdGenerator.getInstance().getNextId(reply2reply1));
        reply1.setId(MemorySequentialIdGenerator.getInstance().getNextId(reply1));
        reply2.setId(MemorySequentialIdGenerator.getInstance().getNextId(reply2));
        root.setId(MemorySequentialIdGenerator.getInstance().getNextId(root));
        assert sender.getId() != null;
        assert recipient.getId() != null;
        assert reply2reply1.getId() != null;
        assert reply1.getId() != null;
        assert reply2.getId() != null;
        assert root.getId() != null;
        rootTelephoneNumber.setId(MemorySequentialIdGenerator.getInstance().getNextId(rootTelephoneNumber));
        reply1TelephoneNumber.setId(MemorySequentialIdGenerator.getInstance().getNextId(reply1TelephoneNumber));
        reply2TelephoneNumber.setId(MemorySequentialIdGenerator.getInstance().getNextId(reply2TelephoneNumber));
        reply2reply1TelephoneNumber.setId(MemorySequentialIdGenerator.getInstance().getNextId(reply2reply1TelephoneNumber));
        assert rootTelephoneNumber.getId() != null;
        assert reply1TelephoneNumber.getId() != null;
        assert reply2TelephoneNumber.getId() != null;
        assert reply2reply1TelephoneNumber.getId() != null;
        storage.store(sender);
        storage.store(recipient);
        storage.store(rootTelephoneNumber);
        storage.store(reply1TelephoneNumber);
        storage.store(reply2TelephoneNumber);
        storage.store(reply2reply1TelephoneNumber);
        storage.store(reply2reply1);
            //reply1, reply2 and root should be stored through cascading
            //persistence

        File amountMoneyCurrencyStorageFile = new File(AMOUNT_MONEY_CURRENCY_STORAGE_FILE_NAME);
        AmountMoneyCurrencyStorage amountMoneyCurrencyStorage = new FileAmountMoneyCurrencyStorage(amountMoneyCurrencyStorageFile);
        JPAAmountMoneyMappingTypeHandlerFactory fieldHandlerFactory = new JPAAmountMoneyMappingTypeHandlerFactory(storage,
                Constants.INITIAL_QUERY_LIMIT_DEFAULT,
                issueHandler,
                fieldRetriever);
        Map<java.lang.reflect.Type, TypeHandler<?,?,?,?>> typeHandlerMapping = fieldHandlerFactory.generateTypeHandlerMapping();
        cacheFileDir = Files.createTempDirectory(CommunicationTreePanelDemo.class.getSimpleName()).toFile();
        AmountMoneyExchangeRateRetriever amountMoneyExchangeRateRetriever = new FailsafeAmountMoneyExchangeRateRetriever(cacheFileDir);

        AmountMoneyMappingFieldHandlerFactory embeddableFieldHandlerFactory = new AmountMoneyMappingFieldHandlerFactory(amountMoneyCurrencyStorage,
                amountMoneyExchangeRateRetriever,
                issueHandler);
        FieldHandler embeddableFieldHandler = new MappingFieldHandler(embeddableFieldHandlerFactory.generateClassMapping(),
                embeddableFieldHandlerFactory.generatePrimitiveMapping());
        ElementCollectionTypeHandler elementCollectionTypeHandler = new ElementCollectionTypeHandler(typeHandlerMapping,
                typeHandlerMapping,
                issueHandler,
                embeddableFieldHandler,
                fieldRetriever);
        JPAAmountMoneyMappingFieldHandlerFactory jPAAmountMoneyMappingFieldHandlerFactory = JPAAmountMoneyMappingFieldHandlerFactory.create(storage,
                Constants.INITIAL_QUERY_LIMIT_DEFAULT,
                issueHandler,
                amountMoneyCurrencyStorage,
                amountMoneyExchangeRateRetriever,
                fieldRetriever);
        ToManyTypeHandler toManyTypeHandler = new ToManyTypeHandler(storage,
                issueHandler,
                typeHandlerMapping,
                typeHandlerMapping,
                Constants.BIDIRECTIONAL_HELP_DIALOG_TITLE,
                fieldInitializer,
                entryStorage,
                fieldRetriever);
        ToOneTypeHandler toOneTypeHandler = new ToOneTypeHandler(storage,
                issueHandler,
                Constants.BIDIRECTIONAL_HELP_DIALOG_TITLE,
                fieldInitializer,
                entryStorage,
                fieldRetriever);
        DocumentScannerConf documentScannerConf = new DocumentScannerConf();
        FieldHandler fieldHandler = new DocumentScannerFieldHandler(jPAAmountMoneyMappingFieldHandlerFactory.generateClassMapping(),
                embeddableFieldHandlerFactory.generateClassMapping(),
                embeddableFieldHandlerFactory.generatePrimitiveMapping(),
                elementCollectionTypeHandler,
                toManyTypeHandler,
                toOneTypeHandler,
                issueHandler,
                confirmMessageHandler,
                null, //oCRResultPanelFetcher
                null, //scanResultPanelFetcher
                documentScannerConf,
                storage,
                Constants.ENTITY_CLASSES, //entityClasses
                Constants.PRIMARY_CLASS_SELECTION, //primaryClassSelection
                null, //mainPanel @TODO: figure out whether this is a good idea
                tagStorage,
                idApplier,
                fieldInitializer,
                entryStorage,
                fieldRetriever
        );
        this.reflectionFormPanel = this.reflectionFormBuilder.transformEntityClass(TelephoneCall.class,
                root,
                fieldHandler);
        JScrollPane reflectionFormPanelScrollPane = new JScrollPane(this.reflectionFormPanel);
        reflectionFormPanelScrollPane.getVerticalScrollBar().setUnitIncrement(richtercloud.document.scanner.ifaces.Constants.DEFAULT_SCROLL_INTERVAL);
        reflectionFormPanelScrollPane.getVerticalScrollBar().setUnitIncrement(richtercloud.document.scanner.ifaces.Constants.DEFAULT_SCROLL_INTERVAL);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        GroupLayout groupLayout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(groupLayout);
        GroupLayout.SequentialGroup horizontalGroup = groupLayout.createSequentialGroup();
        GroupLayout.SequentialGroup verticalGroup = groupLayout.createSequentialGroup();
        horizontalGroup.addComponent(reflectionFormPanelScrollPane, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
        verticalGroup.addComponent(reflectionFormPanelScrollPane, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
        groupLayout.setHorizontalGroup(horizontalGroup);
        groupLayout.setVerticalGroup(verticalGroup);
        pack();
    }

    public PersistenceStorage<Long> getStorage() {
        return storage;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /* Set the Nimbus look and feel */
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            throw new RuntimeException(ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                CommunicationTreePanelDemo communicationTreePanelDemo = null;
                try {
                    communicationTreePanelDemo = new CommunicationTreePanelDemo();
                    communicationTreePanelDemo.setVisible(true);
                } catch (IOException | StorageException | SQLException | NoSuchFieldException | StorageCreationException | StorageConfValidationException | QueryHistoryEntryStorageCreationException | TransformationException | IdApplicationException | FieldRetrievalException ex) {
                    if(communicationTreePanelDemo != null) {
                        communicationTreePanelDemo.setVisible(false);
                        communicationTreePanelDemo.dispose();
                    }
                    throw new RuntimeException(ex);
                }
            }
        });
    }
}
