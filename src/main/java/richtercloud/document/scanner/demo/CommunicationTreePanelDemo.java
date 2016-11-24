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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import richtercloud.document.scanner.components.tag.MemoryTagStorage;
import richtercloud.document.scanner.components.tag.TagStorage;
import richtercloud.document.scanner.gui.DocumentScanner;
import static richtercloud.document.scanner.gui.DocumentScanner.BIDIRECTIONAL_HELP_DIALOG_TITLE;
import static richtercloud.document.scanner.gui.DocumentScanner.INITIAL_QUERY_LIMIT_DEFAULT;
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
import richtercloud.message.handler.LoggerMessageHandler;
import richtercloud.message.handler.MessageHandler;
import richtercloud.reflection.form.builder.ReflectionFormBuilder;
import richtercloud.reflection.form.builder.ReflectionFormPanel;
import richtercloud.reflection.form.builder.components.money.AmountMoneyCurrencyStorage;
import richtercloud.reflection.form.builder.components.money.AmountMoneyExchangeRateRetriever;
import richtercloud.reflection.form.builder.components.money.AmountMoneyUsageStatisticsStorage;
import richtercloud.reflection.form.builder.components.money.FailsafeAmountMoneyExchangeRateRetriever;
import richtercloud.reflection.form.builder.components.money.FileAmountMoneyCurrencyStorage;
import richtercloud.reflection.form.builder.components.money.FileAmountMoneyUsageStatisticsStorage;
import richtercloud.reflection.form.builder.fieldhandler.FieldHandler;
import richtercloud.reflection.form.builder.fieldhandler.FieldHandlingException;
import richtercloud.reflection.form.builder.fieldhandler.MappingFieldHandler;
import richtercloud.reflection.form.builder.fieldhandler.factory.AmountMoneyMappingFieldHandlerFactory;
import richtercloud.reflection.form.builder.jpa.IdGenerator;
import richtercloud.reflection.form.builder.jpa.JPACachedFieldRetriever;
import richtercloud.reflection.form.builder.jpa.JPAReflectionFormBuilder;
import richtercloud.reflection.form.builder.jpa.SequentialIdGenerator;
import richtercloud.reflection.form.builder.jpa.WarningHandler;
import richtercloud.reflection.form.builder.jpa.fieldhandler.factory.JPAAmountMoneyMappingFieldHandlerFactory;
import richtercloud.reflection.form.builder.jpa.idapplier.GeneratedValueIdApplier;
import richtercloud.reflection.form.builder.jpa.idapplier.IdApplier;
import richtercloud.reflection.form.builder.jpa.typehandler.ElementCollectionTypeHandler;
import richtercloud.reflection.form.builder.jpa.typehandler.ToManyTypeHandler;
import richtercloud.reflection.form.builder.jpa.typehandler.ToOneTypeHandler;
import richtercloud.reflection.form.builder.jpa.typehandler.factory.JPAAmountMoneyMappingTypeHandlerFactory;
import richtercloud.reflection.form.builder.typehandler.TypeHandler;

/**
 *
 * @author richter
 */
public class CommunicationTreePanelDemo extends JFrame {

    private static final long serialVersionUID = 1L;
    private final static Logger LOGGER = LoggerFactory.getLogger(CommunicationTreePanelDemo.class);
    private final static String AMOUNT_MONEY_USAGE_STATISTICS_STORAGE_FILE_NAME = "money-usage-statistics-storage.xml";
    private final static String AMOUNT_MONEY_CURRENCY_STORAGE_FILE_NAME = "currency-storage.xml";
    private final ReflectionFormPanel reflectionFormPanel;
    private final IdGenerator idGenerator = SequentialIdGenerator.getInstance();
    private final MessageHandler messageHandler = new LoggerMessageHandler(LOGGER);
    private final ConfirmMessageHandler confirmMessageHandler = new DialogConfirmMessageHandler(this);
    private final ReflectionFormBuilder reflectionFormBuilder;
    private final JPACachedFieldRetriever fieldRetriever = new JPACachedFieldRetriever();
    private final IdApplier idApplier = new GeneratedValueIdApplier();
    private final Map<Class<?>, WarningHandler<?>> warningHandlers = new HashMap<>();
    private final TagStorage tagStorage = new MemoryTagStorage();

    public CommunicationTreePanelDemo() throws IllegalArgumentException, IllegalAccessException, IOException, InstantiationException, InvocationTargetException, NoSuchMethodException, FieldHandlingException {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("richtercloud_document-scanner-demo_jar_1.0-SNAPSHOTPU");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        this.reflectionFormBuilder = new JPAReflectionFormBuilder(entityManager,
                "fieldDescriptionDialogTitle",
                messageHandler,
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
        WorkflowItem root = new TelephoneCall(new Date(1000),
                new Date(1001),
                "root",
                new TelephoneNumber(49,
                        1,
                        2,
                        recipient,
                        TelephoneNumber.TYPE_LANDLINE),
                sender,
                recipient);
        WorkflowItem reply1 = new TelephoneCall(new Date(2000),
                new Date(2001),
                "reply1",
                new TelephoneNumber(49,
                        1,
                        2,
                        recipient,
                        TelephoneNumber.TYPE_LANDLINE),
                sender,
                recipient,
                new LinkedList<>(Arrays.asList(root)));
        root.getFollowingItems().add(reply1);
        WorkflowItem reply2 = new TelephoneCall(new Date(3000),
                new Date(3001),
                "reply2",
                new TelephoneNumber(49,
                        1,
                        2,
                        recipient,
                        TelephoneNumber.TYPE_LANDLINE),
                sender,
                recipient,
                new LinkedList<>(Arrays.asList(root)));
        root.getFollowingItems().add(reply2);
        WorkflowItem reply2reply1 = new TelephoneCall(new Date(4000),
                new Date(4001),
                "reply2reply1",
                new TelephoneNumber(49,
                        1,
                        2,
                        recipient,
                        TelephoneNumber.TYPE_LANDLINE),
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

        entityManager.getTransaction().begin();
        entityManager.persist(sender);
        entityManager.persist(recipient);
        entityManager.persist(reply2reply1);
        entityManager.persist(reply1);
        entityManager.persist(reply2);
        entityManager.persist(root);
        entityManager.getTransaction().commit();

        File amountMoneyUsageStatisticsStorageFile = new File( AMOUNT_MONEY_USAGE_STATISTICS_STORAGE_FILE_NAME);
        File amountMoneyCurrencyStorageFile = new File(AMOUNT_MONEY_CURRENCY_STORAGE_FILE_NAME);

        AmountMoneyUsageStatisticsStorage amountMoneyUsageStatisticsStorage;
        try {
            amountMoneyUsageStatisticsStorage = new FileAmountMoneyUsageStatisticsStorage(amountMoneyUsageStatisticsStorageFile);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        AmountMoneyCurrencyStorage amountMoneyCurrencyStorage = new FileAmountMoneyCurrencyStorage(amountMoneyCurrencyStorageFile);
        JPAAmountMoneyMappingTypeHandlerFactory fieldHandlerFactory = new JPAAmountMoneyMappingTypeHandlerFactory(entityManager,
                INITIAL_QUERY_LIMIT_DEFAULT,
                messageHandler,
                BIDIRECTIONAL_HELP_DIALOG_TITLE);
        Map<java.lang.reflect.Type, TypeHandler<?,?,?,?>> typeHandlerMapping = fieldHandlerFactory.generateTypeHandlerMapping();
        AmountMoneyExchangeRateRetriever amountMoneyExchangeRateRetriever = new FailsafeAmountMoneyExchangeRateRetriever();

        AmountMoneyMappingFieldHandlerFactory embeddableFieldHandlerFactory = new AmountMoneyMappingFieldHandlerFactory(amountMoneyUsageStatisticsStorage,
                amountMoneyCurrencyStorage,
                amountMoneyExchangeRateRetriever,
                messageHandler);
        FieldHandler embeddableFieldHandler = new MappingFieldHandler(embeddableFieldHandlerFactory.generateClassMapping(),
                embeddableFieldHandlerFactory.generatePrimitiveMapping());
        ElementCollectionTypeHandler elementCollectionTypeHandler = new ElementCollectionTypeHandler(typeHandlerMapping,
                typeHandlerMapping,
                messageHandler,
                embeddableFieldHandler);
        JPAAmountMoneyMappingFieldHandlerFactory jPAAmountMoneyMappingFieldHandlerFactory = JPAAmountMoneyMappingFieldHandlerFactory.create(entityManager,
                INITIAL_QUERY_LIMIT_DEFAULT,
                messageHandler,
                amountMoneyUsageStatisticsStorage,
                amountMoneyCurrencyStorage,
                amountMoneyExchangeRateRetriever,
                BIDIRECTIONAL_HELP_DIALOG_TITLE);
        ToManyTypeHandler toManyTypeHandler = new ToManyTypeHandler(entityManager,
                messageHandler,
                typeHandlerMapping,
                typeHandlerMapping,
                BIDIRECTIONAL_HELP_DIALOG_TITLE);
        ToOneTypeHandler toOneTypeHandler = new ToOneTypeHandler(entityManager,
                messageHandler,
                BIDIRECTIONAL_HELP_DIALOG_TITLE);
        DocumentScannerConf documentScannerConf = new DocumentScannerConf(entityManager,
                messageHandler,
                DocumentScanner.ENTITY_CLASSES,
                amountMoneyCurrencyStorageFile,
                amountMoneyCurrencyStorageFile);
        FieldHandler fieldHandler = new DocumentScannerFieldHandler(jPAAmountMoneyMappingFieldHandlerFactory.generateClassMapping(),
                embeddableFieldHandlerFactory.generateClassMapping(),
                embeddableFieldHandlerFactory.generatePrimitiveMapping(),
                elementCollectionTypeHandler,
                toManyTypeHandler,
                toOneTypeHandler,
                messageHandler,
                confirmMessageHandler,
                fieldRetriever,
                null, //oCRResultPanelFetcher
                null, //scanResultPanelFetcher
                documentScannerConf,
                this, //oCRProgressMonitorParent
                entityManager,
                DocumentScanner.ENTITY_CLASSES, //entityClasses
                DocumentScanner.PRIMARY_CLASS_SELECTION, //primaryClassSelection
                null, //mainPanel @TODO: figure out whether this is a good idea
                tagStorage,
                idApplier,
                warningHandlers
        );
        this.reflectionFormPanel = this.reflectionFormBuilder.transformEntityClass(TelephoneCall.class,
                root,
                fieldHandler);
        JScrollPane reflectionFormPanelScrollPane = new JScrollPane(this.reflectionFormPanel);
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
                try {
                    new CommunicationTreePanelDemo().setVisible(true);
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException | InstantiationException | InvocationTargetException | NoSuchMethodException | FieldHandlingException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
}
