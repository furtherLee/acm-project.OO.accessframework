/*
 * Copyright (C) 2007 ETH Zurich
 *
 * This file is part of Fosstrak (www.fosstrak.org).
 *
 * Fosstrak is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1, as published by the Free Software Foundation.
 *
 * Fosstrak is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Fosstrak; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA  02110-1301  USA
 */

package org.acm_project.acm09.OO.epcis.accessframework.demoTestQuery;

import org.acm_project.acm09.OO.epcis.accessframework.demoTestQuery.QueryBrokerHelper;
import org.acm_project.acm09.OO.epcis.accessframework.demoTestQuery.QueryBrokerHelper.ExampleQueries;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeFactory;

import org.fosstrak.epcis.gui.AuthenticationOptionsChangeEvent;
import org.fosstrak.epcis.gui.AuthenticationOptionsChangeListener;
import org.fosstrak.epcis.gui.AuthenticationOptionsPanel;
import org.fosstrak.epcis.model.AggregationEventType;
import org.fosstrak.epcis.model.ArrayOfString;
import org.fosstrak.epcis.model.BusinessTransactionType;
import org.fosstrak.epcis.model.EPC;
import org.fosstrak.epcis.model.EPCISEventType;
import org.fosstrak.epcis.model.ObjectEventType;
import org.fosstrak.epcis.model.Poll;
import org.fosstrak.epcis.model.QuantityEventType;
import org.fosstrak.epcis.model.QueryParam;
import org.fosstrak.epcis.model.QueryParams;
import org.fosstrak.epcis.model.QueryResults;
import org.fosstrak.epcis.model.QuerySchedule;
import org.fosstrak.epcis.model.Subscribe;
import org.fosstrak.epcis.model.SubscriptionControls;
import org.fosstrak.epcis.model.TransactionEventType;
import org.fosstrak.epcis.utils.TimeParser;

/**
 * Implements the GUI part of the EPCIS Query Interface client.
 * 
 * @author David Gubler
 */
public class QueryBrokerGui extends WindowAdapter implements ActionListener, AuthenticationOptionsChangeListener {

    /**
     * The enumeration of all possible query parameter types.
     */
    public enum ParameterType {
        ListOfString, Boolean, Int, Float, String, Time, noType
    };

    /**
     * The Map which holds all possible query parameters. Key is the user text.
     */
    private Map<String, QueryItem> queryParamsUserText;

    /**
     * The Map which holds all possible query parameters. Key is the query text.
     */
    private Map<String, QueryItem> queryParamsQueryText;

    /**
     * Contains the column names for the result table.
     */
    private final String[] columnNames = {
            "Event", "occurred", "recorded", "Parent ID", "Quantity", "EPCs", "Action", "Business step", "Disposition",
            "Readpoint ID", "Business location", "Business transaction" };

    /**
     * Contains the various choices for the query parameters in a human readable
     * form.
     */
    private String[] queryParameterUsertext;

    /**
     * Contains the data for the result table.
     */
    private Object[][] data = {};

    /**
     * The query client instance. Has methods to actually execute a query.
     */
    private QueryBroker client = null;

    private boolean configurationChanged;

    /**
     * Holds the query parameters.
     */
    private List<QueryParam> internalQueryParams = new ArrayList<QueryParam>();

    /* main window */
    private JFrame mainWindow;
    private JPanel mwMainPanel;
    private JPanel mwConfigPanel;
    private JPanel mwSubscribeManagementPanel;
    private JPanel mwEventTypeSelectPanel;
    private JPanel mwQueryPanel;
    private AuthenticationOptionsPanel mwAuthOptions;
    private JPanel mwSubscriptionPanel;
    private JPanel mwQueryArgsPanel;
    private JPanel mwQueryExamplesPanel;
    private JPanel mwButtonPanel;
    private JLabel mwServiceUrlLabel;

    private JTextField mwServiceUrlTextField = new JTextField("", 40);
    private JButton mwServiceInfoButton;
    private JLabel mwUnsubscribeQueryLabel;
    private JTextField mwUnsubscribeQueryTextField;
    private JButton mwUnsubscribeQueryButton;
    private JButton mwSubscriptionIdButton;
    private JCheckBox mwShowDebugWindowCheckBox;
    private JCheckBox mwObjectEventsCheckBox;
    private JCheckBox mwAggregationEventsCheckBox;
    private JCheckBox mwQuantityEventsCheckBox;
    private JCheckBox mwTransactionEventsCheckBox;

    /*
     * These lists hold the input fields for the query arguments. The lists are
     * modified by the user to allow for as many arguments as the user wants
     * Objects may be deleted from these lists by selecting "ignore" from the
     * drop-down box
     */
    private LinkedList<JComboBox> mwQuerySelectComboBoxes;
    private LinkedList<JTextFieldEnhanced> mwQueryArgumentTextFields;

    private int mwQueryArgumentTextFieldsExtraWidth = 550;

    private JButton mwRunQueryButton;
    private JButton mwFillInExampleButton;

    /* subscribe Query */
    private JCheckBox isSubscribed;
    private JTextField mwScheduleMinuteField;
    private JTextField mwScheduleSecField;
    private JTextField mwScheduleHourField;
    private JTextField mwScheduleWeekField;
    private JTextField mwScheduleMonthField;
    private JTextField mwScheduleDayField;
    private JTextField mwSubIdField;
    private JTextField mwInitRecTimeField;
    private JTextField mwDestUriTextField;
    private JCheckBox reportIf;
    private JCheckBox triggerIf;

    /* results window */
    private JFrame resultsWindow;
    private JPanel rwResultsPanel;
    private JTable rwResultsTable;
    private JScrollPane rwResultsScrollPane;

    /* example selection window */
    private JFrame exampleWindow;
    private JPanel ewMainPanel;
    private JPanel ewListPanel;
    private JPanel ewButtonPanel;
    private JList ewExampleList;
    private JScrollPane ewExampleScrollPane;
    private JButton ewOkButton;

    /* debug window */
    private JFrame debugWindow;
    private JTextArea dwOutputTextArea;
    private JScrollPane dwOutputScrollPane;
    private JPanel dwButtonPanel;
    private JButton dwClearButton;

    /**
     * The constructor. Starts a new thread which draws the main window.
     */
    public QueryBrokerGui() {
        try {
            initGui(null);
        } catch (MalformedURLException e) {
            // shouldn't happen because url is loaded from properties 
        }
    }

    /**
     * Constructs a new QueryClientGui which sends its queries to the given
     * endpoint address. If no such address is provided, the properties file is
     * checked; if there is an error with reading the properties, a default url
     * will be provided.
     * 
     * @param address
     *            The address to send the queries to.
     */
    public QueryBrokerGui(final String address) throws MalformedURLException {
        initGui(address);
    }
    
    private void initGui(String url) throws MalformedURLException {
        generateParamHashMap();
        drawDebugWindow();
        client = new QueryBroker(url);
        mwServiceUrlTextField.setText(client.getQueryUrl());
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                drawMainWindow(client.getQueryUrl());
            }
        });
    }

    /**
     * Initialized all the possible Query Parameters.
     */
    private void generateParamHashMap() {
        QueryItem newEntry = new QueryItem();
        queryParamsUserText = new LinkedHashMap<String, QueryItem>();
        queryParamsQueryText = new LinkedHashMap<String, QueryItem>();

        newEntry.setDescription("Choose a query parameter " + "from the drop-down menu");
        newEntry.setParamType(ParameterType.noType);
        newEntry.setQueryText("");
        newEntry.setRequired(false);
        newEntry.setUserText("ignore");
        queryParamsUserText.put(newEntry.getUserText(), newEntry);
        queryParamsQueryText.put(newEntry.getQueryText(), newEntry);

        newEntry = new QueryItem();
        newEntry.setDescription("Format is ISO 8601, i.e. YYYY-MM-DDThh:mm:ss.sssZ");
        newEntry.setParamType(ParameterType.Time);
        newEntry.setQueryText("GE_eventTime");
        newEntry.setRequired(false);
        newEntry.setUserText("event time >= ");
        queryParamsUserText.put(newEntry.getUserText(), newEntry);
        queryParamsQueryText.put(newEntry.getQueryText(), newEntry);

        newEntry = new QueryItem();
        newEntry.setDescription("Format is ISO 8601, i.e. YYYY-MM-DDThh:mm:ss.sssZ");
        newEntry.setParamType(ParameterType.Time);
        newEntry.setQueryText("LT_eventTime");
        newEntry.setRequired(false);
        newEntry.setUserText("event time < ");
        queryParamsUserText.put(newEntry.getUserText(), newEntry);
        queryParamsQueryText.put(newEntry.getQueryText(), newEntry);

        newEntry = new QueryItem();
        newEntry.setDescription("Format is ISO 8601, i.e. YYYY-MM-DDThh:mm:ss.sssZ");
        newEntry.setParamType(ParameterType.Time);
        newEntry.setQueryText("GE_recordTime");
        newEntry.setRequired(false);
        newEntry.setUserText("record time >= ");
        queryParamsUserText.put(newEntry.getUserText(), newEntry);
        queryParamsQueryText.put(newEntry.getQueryText(), newEntry);

        newEntry = new QueryItem();
        newEntry.setDescription("Format is ISO 8601, i.e. YYYY-MM-DDThh:mm:ss.sssZ");
        newEntry.setParamType(ParameterType.Time);
        newEntry.setQueryText("LT_recordTime");
        newEntry.setRequired(false);
        newEntry.setUserText("record time < ");
        queryParamsUserText.put(newEntry.getUserText(), newEntry);
        queryParamsQueryText.put(newEntry.getQueryText(), newEntry);

        newEntry = new QueryItem();
        newEntry.setDescription("Space-separated list of ADD, DELETE, OBSERVE");
        newEntry.setParamType(ParameterType.ListOfString);
        newEntry.setQueryText("EQ_action");
        newEntry.setRequired(false);
        newEntry.setUserText("action = ");
        queryParamsUserText.put(newEntry.getUserText(), newEntry);
        queryParamsQueryText.put(newEntry.getQueryText(), newEntry);

        newEntry = new QueryItem();
        newEntry.setDescription("Space-separated list " + "of URIs with OR semantics");
        newEntry.setParamType(ParameterType.ListOfString);
        newEntry.setQueryText("EQ_bizStep");
        newEntry.setRequired(false);
        newEntry.setUserText("business step = ");
        queryParamsUserText.put(newEntry.getUserText(), newEntry);
        queryParamsQueryText.put(newEntry.getQueryText(), newEntry);

        newEntry = new QueryItem();
        newEntry.setDescription("Space-separated list " + "of URIs with OR semantics");
        newEntry.setParamType(ParameterType.ListOfString);
        newEntry.setQueryText("EQ_disposition");
        newEntry.setRequired(false);
        newEntry.setUserText("disposition = ");
        queryParamsUserText.put(newEntry.getUserText(), newEntry);
        queryParamsQueryText.put(newEntry.getQueryText(), newEntry);

        newEntry = new QueryItem();
        newEntry.setDescription("Space-separated list " + "of URIs with OR semantics");
        newEntry.setParamType(ParameterType.ListOfString);
        newEntry.setQueryText("EQ_readPoint");
        newEntry.setRequired(false);
        newEntry.setUserText("readpoint = ");
        queryParamsUserText.put(newEntry.getUserText(), newEntry);
        queryParamsQueryText.put(newEntry.getQueryText(), newEntry);

        newEntry = new QueryItem();
        newEntry.setDescription("Space-separated list " + "of URIs with OR semantics");
        newEntry.setParamType(ParameterType.ListOfString);
        newEntry.setQueryText("WD_readPoint");
        newEntry.setRequired(false);
        newEntry.setUserText("readpoint descendant of ");
        queryParamsUserText.put(newEntry.getUserText(), newEntry);
        queryParamsQueryText.put(newEntry.getQueryText(), newEntry);

        newEntry = new QueryItem();
        newEntry.setDescription("Space-separated list " + "of URIs with OR semantics");
        newEntry.setParamType(ParameterType.ListOfString);
        newEntry.setQueryText("EQ_bizLocation");
        newEntry.setRequired(false);
        newEntry.setUserText("business location = ");
        queryParamsUserText.put(newEntry.getUserText(), newEntry);
        queryParamsQueryText.put(newEntry.getQueryText(), newEntry);

        newEntry = new QueryItem();
        newEntry.setDescription("Space-separated list " + "of URIs with OR semantics");
        newEntry.setParamType(ParameterType.ListOfString);
        newEntry.setQueryText("WD_bizLocation");
        newEntry.setRequired(false);
        newEntry.setUserText("business location descendant of ");
        queryParamsUserText.put(newEntry.getUserText(), newEntry);
        queryParamsQueryText.put(newEntry.getQueryText(), newEntry);

        // "EQ_bizTransaction_type", "business transaction type with ID's= ",
        // we do not support this in the GUI (List of String)

        newEntry = new QueryItem();
        newEntry.setDescription("Space-separated list " + "of URIs with OR semantics");
        newEntry.setParamType(ParameterType.ListOfString);
        newEntry.setQueryText("MATCH_epc");
        newEntry.setRequired(false);
        newEntry.setUserText("EPC = ");
        queryParamsUserText.put(newEntry.getUserText(), newEntry);
        queryParamsQueryText.put(newEntry.getQueryText(), newEntry);

        newEntry = new QueryItem();
        newEntry.setDescription("Space-separated list " + "of URIs with OR semantics");
        newEntry.setParamType(ParameterType.ListOfString);
        newEntry.setQueryText("MATCH_parentID");
        newEntry.setRequired(false);
        newEntry.setUserText("parent ID = ");
        queryParamsUserText.put(newEntry.getUserText(), newEntry);
        queryParamsQueryText.put(newEntry.getQueryText(), newEntry);

        newEntry = new QueryItem();
        newEntry.setDescription("Space-separated list " + "of URIs with OR semantics");
        newEntry.setParamType(ParameterType.ListOfString);
        newEntry.setQueryText("MATCH_epcClass");
        newEntry.setRequired(false);
        newEntry.setUserText("EPC class = ");
        queryParamsUserText.put(newEntry.getUserText(), newEntry);
        queryParamsQueryText.put(newEntry.getQueryText(), newEntry);

        newEntry = new QueryItem();
        newEntry.setDescription("Integer number");
        newEntry.setParamType(ParameterType.Int);
        newEntry.setQueryText("EQ_quantity");
        newEntry.setRequired(false);
        newEntry.setUserText("quantity = ");
        queryParamsUserText.put(newEntry.getUserText(), newEntry);
        queryParamsQueryText.put(newEntry.getQueryText(), newEntry);

        newEntry = new QueryItem();
        newEntry.setDescription("Integer number");
        newEntry.setParamType(ParameterType.Int);
        newEntry.setQueryText("GT_quantity");
        newEntry.setRequired(false);
        newEntry.setUserText("quantity > ");
        queryParamsUserText.put(newEntry.getUserText(), newEntry);
        queryParamsQueryText.put(newEntry.getQueryText(), newEntry);

        newEntry = new QueryItem();
        newEntry.setDescription("Integer number");
        newEntry.setParamType(ParameterType.Int);
        newEntry.setQueryText("GE_quantity");
        newEntry.setRequired(false);
        newEntry.setUserText("quantity >= ");
        queryParamsUserText.put(newEntry.getUserText(), newEntry);
        queryParamsQueryText.put(newEntry.getQueryText(), newEntry);

        newEntry = new QueryItem();
        newEntry.setDescription("Integer number");
        newEntry.setParamType(ParameterType.Int);
        newEntry.setQueryText("LT_quantity");
        newEntry.setRequired(false);
        newEntry.setUserText("quantity < ");
        queryParamsUserText.put(newEntry.getUserText(), newEntry);
        queryParamsQueryText.put(newEntry.getQueryText(), newEntry);

        newEntry = new QueryItem();
        newEntry.setDescription("Integer number");
        newEntry.setParamType(ParameterType.Int);
        newEntry.setQueryText("LE_quantity");
        newEntry.setRequired(false);
        newEntry.setUserText("quantity <= ");
        queryParamsUserText.put(newEntry.getUserText(), newEntry);
        queryParamsQueryText.put(newEntry.getQueryText(), newEntry);

        // "EQ_fieldname", "Fieldname with values = "
        // we do not support this in the GUI (List of String)
        // "EQ_fieldname", "Fieldname with values = "
        // we do not support this in the GUI (Int, Float, Time)
        // "GT_fieldname", "Fieldname with values > "
        // we do not support this in the GUI (Int, Float, Time)
        // "GE_fieldname", "Fieldname with values >= "
        // we do not support this in the GUI (Int, Float, Time)
        // "LT_fieldname", "Fieldname with values < "
        // we do not support this in the GUI (Int, Float, Time)
        // "LE_fieldname", "Fieldname with values <= "
        // we do not support this in the GUI (Int, Float, Time)
        // "EXISTS_fieldname", "exists field: "
        // we do not support this in the GUI (Void)
        // "HASATTR_fieldname", "Has fieldname attributes: "
        // we do not support this in the GUI (List of String)
        // "EQATTR_fieldname_attrname", "Equals fieldname attributname: "
        // we do not support this in the GUI (List of String)

        newEntry = new QueryItem();
        newEntry.setDescription("A single fieldname written in");
        newEntry.setParamType(ParameterType.String);
        newEntry.setQueryText("orderBy");
        newEntry.setRequired(false);
        newEntry.setUserText("Order by field: ");
        queryParamsUserText.put(newEntry.getUserText(), newEntry);
        queryParamsQueryText.put(newEntry.getQueryText(), newEntry);

        newEntry = new QueryItem();
        newEntry.setDescription("ASC or DESC. Default is DESC.");
        newEntry.setParamType(ParameterType.String);
        newEntry.setQueryText("orderDirection");
        newEntry.setRequired(false);
        newEntry.setUserText("direction of order: ");
        queryParamsUserText.put(newEntry.getUserText(), newEntry);
        queryParamsQueryText.put(newEntry.getQueryText(), newEntry);

        newEntry = new QueryItem();
        newEntry.setDescription("Integer number");
        newEntry.setParamType(ParameterType.Int);
        newEntry.setQueryText("eventCountLimit");
        newEntry.setRequired(false);
        newEntry.setUserText("only the first n: ");
        queryParamsUserText.put(newEntry.getUserText(), newEntry);
        queryParamsQueryText.put(newEntry.getQueryText(), newEntry);

        newEntry = new QueryItem();
        newEntry.setDescription("Integer number");
        newEntry.setParamType(ParameterType.Int);
        newEntry.setQueryText("maxEventCount");
        newEntry.setRequired(false);
        newEntry.setUserText("has not more then n Events ");
        queryParamsUserText.put(newEntry.getUserText(), newEntry);
        queryParamsQueryText.put(newEntry.getQueryText(), newEntry);

        queryParameterUsertext = new String[queryParamsUserText.size()];
        int i = 0;
        for (Iterator<String> it = queryParamsUserText.keySet().iterator(); it.hasNext();) {
            queryParameterUsertext[i] = (String) it.next();
            i++;
        }
    }

    /**
     * Sets up the main window. To be called only once per program run
     */
    private void drawMainWindow(String queryUrl) {
        JFrame.setDefaultLookAndFeelDecorated(true);

        mainWindow = new JFrame("EPCIS query interface client");
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setResizable(false);

        mwMainPanel = new JPanel();
        mwMainPanel.setLayout(new BoxLayout(mwMainPanel, BoxLayout.PAGE_AXIS));
        mwMainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        mwConfigPanel = new JPanel(new GridBagLayout());
        mwMainPanel.add(mwConfigPanel);

        mwSubscribeManagementPanel = new JPanel(new GridBagLayout());
        mwMainPanel.add(mwSubscribeManagementPanel);

        mwEventTypeSelectPanel = new JPanel();
        mwMainPanel.add(mwEventTypeSelectPanel);

        mwQueryPanel = new JPanel();
        mwSubscriptionPanel = new JPanel();
        mwQueryPanel.setLayout(new BoxLayout(mwQueryPanel, BoxLayout.PAGE_AXIS));
        mwSubscriptionPanel.setLayout(new GridBagLayout());
        mwMainPanel.add(mwQueryPanel);

        isSubscribed = new JCheckBox("Subscribe this query");
        mwMainPanel.add(isSubscribed);
        isSubscribed.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                if (mwSubscriptionPanel.isVisible()) {
                    mwSubscriptionPanel.setVisible(false);
                    mwRunQueryButton.setText("Run Query");
                    mainWindow.pack();
                } else {
                    mwSubscriptionPanel.setVisible(true);
                    mwRunQueryButton.setText("Subscribe Query");
                    mainWindow.pack();
                }
            }
        });
        mwSubscriptionPanel.setVisible(false);
        mwMainPanel.add(mwSubscriptionPanel);

        mwButtonPanel = new JPanel();
        mwMainPanel.add(mwButtonPanel);

        mwConfigPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Configuration"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        mwSubscribeManagementPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Subscribe Management"), BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        mwEventTypeSelectPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Events to be returned"), BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        mwQueryPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Query arguments"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        mwSubscriptionPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Subscription Arguments"), BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        mwServiceUrlLabel = new JLabel("Query interface URL: ");
        mwServiceInfoButton = new JButton("Info");
        mwServiceInfoButton.addActionListener(this);
        mwAuthOptions = new AuthenticationOptionsPanel(this);

        mwUnsubscribeQueryLabel = new JLabel("Unsubscribe ID: ");
        mwUnsubscribeQueryTextField = new JTextField("", 40);
        mwUnsubscribeQueryTextField.setToolTipText("Only one Subscription ID");
        mwUnsubscribeQueryButton = new JButton("Unsubscribe");
        mwUnsubscribeQueryButton.addActionListener(this);
        mwSubscriptionIdButton = new JButton("Show SubscriptionIDs");
        mwSubscriptionIdButton.addActionListener(this);

        mwShowDebugWindowCheckBox = new JCheckBox("Show debug window", false);
        mwShowDebugWindowCheckBox.addActionListener(this);

        mwServiceUrlTextField.getDocument().addDocumentListener(new DocumentListener() {

            public void changedUpdate(DocumentEvent e) {
                configurationChanged(new AuthenticationOptionsChangeEvent(this, isComplete()));
            }

            public void insertUpdate(DocumentEvent e) {
                configurationChanged(new AuthenticationOptionsChangeEvent(this, isComplete()));
            }

            public void removeUpdate(DocumentEvent e) {
                configurationChanged(new AuthenticationOptionsChangeEvent(this, isComplete()));
            }

            public boolean isComplete() {
                String url = mwServiceUrlTextField.getText();
                return url != null && url.length() > 0;
            }

        });

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 5, 5, 0);
        c.weightx = 0;
        c.gridx = 0;
        c.gridy = 0;
        mwConfigPanel.add(mwServiceUrlLabel, c);
        c.weightx = 1;
        c.gridx = 1;
        c.gridy = 0;
        mwConfigPanel.add(mwServiceUrlTextField, c);
        c.weightx = 0;
        c.gridx = 3;
        c.gridy = 0;
        mwConfigPanel.add(mwServiceInfoButton, c);
        c.weightx = 0;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        mwConfigPanel.add(mwAuthOptions, c);
        c.weightx = 0;
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        mwConfigPanel.add(mwShowDebugWindowCheckBox, c);

        c.weightx = 0;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        mwSubscribeManagementPanel.add(mwUnsubscribeQueryLabel, c);
        c.weightx = 1;
        c.gridx = 1;
        c.gridy = 0;
        mwSubscribeManagementPanel.add(mwUnsubscribeQueryTextField, c);
        c.weightx = 0;
        c.gridx = 2;
        c.gridy = 0;
        mwSubscribeManagementPanel.add(mwUnsubscribeQueryButton, c);
        c.weightx = 0;
        c.gridx = 3;
        c.gridy = 0;
        mwSubscribeManagementPanel.add(mwSubscriptionIdButton, c);

        mwObjectEventsCheckBox = new JCheckBox("ObjectEvents");
        mwEventTypeSelectPanel.add(mwObjectEventsCheckBox);
        mwAggregationEventsCheckBox = new JCheckBox("AggregationEvents");
        mwEventTypeSelectPanel.add(mwAggregationEventsCheckBox);
        mwQuantityEventsCheckBox = new JCheckBox("QuantityEvents");
        mwEventTypeSelectPanel.add(mwQuantityEventsCheckBox);
        mwTransactionEventsCheckBox = new JCheckBox("TransactionEvents");
        mwEventTypeSelectPanel.add(mwTransactionEventsCheckBox);

        mwQuerySelectComboBoxes = new LinkedList<JComboBox>();
        mwQueryArgumentTextFields = new LinkedList<JTextFieldEnhanced>();

        mwQuerySelectComboBoxes.add(new JComboBox(queryParameterUsertext));
        ((JComboBox) mwQuerySelectComboBoxes.getFirst()).addActionListener(this);
        queryParamsUserText.get("ignore");
        mwQueryArgumentTextFields.add(new JTextFieldEnhanced(15, queryParamsUserText.get("ignore")));

        mwQueryArgsPanel = new JPanel(new GridBagLayout());
        mwQueryExamplesPanel = new JPanel(new BorderLayout());
        mwQueryPanel.add(mwQueryArgsPanel);
        mwQueryPanel.add(mwQueryExamplesPanel);

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 5, 5, 0);
        c.weightx = 0;
        c.gridx = 0;
        c.gridy = 0;
        mwQueryArgsPanel.add((JComboBox) mwQuerySelectComboBoxes.getFirst(), c);
        c.weightx = 1;
        c.gridx = 1;
        c.ipadx = mwQueryArgumentTextFieldsExtraWidth;
        mwQueryArgsPanel.add((JTextField) mwQueryArgumentTextFields.getFirst(), c);

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 5, 5, 0);
        c.weightx = 0;
        c.gridx = 0;
        c.gridy = 0;
        reportIf = new JCheckBox("Report if empty?");
        reportIf.setSelected(true);
        mwSubscriptionPanel.add(reportIf);

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 5, 5, 0);
        c.weightx = 0;
        c.gridx = 1;
        c.gridy = 0;
        triggerIf = new JCheckBox("Use trigger instead of schedule?");
        triggerIf.setSelected(false);
        mwSubscriptionPanel.add(triggerIf);
        triggerIf.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                if (mwScheduleMonthField.isEnabled()) {
                    mwScheduleMonthField.setEnabled(false);
                    mwScheduleWeekField.setEnabled(false);
                    mwScheduleDayField.setEnabled(false);
                    mwScheduleHourField.setEnabled(false);
                    mwScheduleMinuteField.setEnabled(false);
                    mwScheduleSecField.setEnabled(false);
                } else {
                    mwScheduleMonthField.setEnabled(true);
                    mwScheduleWeekField.setEnabled(true);
                    mwScheduleDayField.setEnabled(true);
                    mwScheduleHourField.setEnabled(true);
                    mwScheduleMinuteField.setEnabled(true);
                    mwScheduleSecField.setEnabled(true);
                }
            }
        });

        c.weightx = 0;
        c.gridx = 0;
        c.gridy = 1;
        JLabel mwDestUri = new JLabel("Destination URI: ");
        mwSubscriptionPanel.add(mwDestUri, c);
        c.weightx = 1;
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 6;
        mwDestUriTextField = new JTextField("http://localhost:8888", 40);
        mwSubscriptionPanel.add(mwDestUriTextField, c);

        c.weightx = 0;
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        JLabel mwInitRecTime = new JLabel("Initial Record Time: ");
        mwSubscriptionPanel.add(mwInitRecTime, c);
        c.weightx = 1;
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 6;
        Calendar cal = Calendar.getInstance();
        mwInitRecTimeField = new JTextField(TimeParser.format(cal), 40);
        mwSubscriptionPanel.add(mwInitRecTimeField, c);

        c.weightx = 0;
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 1;
        JLabel mwSubId = new JLabel("Subscription ID: ");
        mwSubscriptionPanel.add(mwSubId, c);
        c.weightx = 1;
        c.gridx = 1;
        c.gridy = 3;
        c.gridwidth = 6;
        mwSubIdField = new JTextField("", 40);
        mwSubscriptionPanel.add(mwSubIdField, c);

        c.weightx = 0;
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 1;
        JLabel mwSchedule = new JLabel("Schedule: ");
        mwSubscriptionPanel.add(mwSchedule, c);

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 1;
        JLabel mwScheduleDay = new JLabel("Day of Month: ");
        mwSubscriptionPanel.add(mwScheduleDay, c);

        c.gridx = 2;
        c.insets = new Insets(10, 5, 5, 30);
        mwScheduleDayField = new JTextField("", 10);
        mwSubscriptionPanel.add(mwScheduleDayField, c);

        c.gridx = 3;
        c.insets = new Insets(10, 5, 5, 0);
        JLabel mwScheduleMonth = new JLabel("Month: ");
        mwSubscriptionPanel.add(mwScheduleMonth, c);

        c.gridx = 4;
        c.insets = new Insets(10, 5, 5, 30);
        mwScheduleMonthField = new JTextField("", 10);
        mwSubscriptionPanel.add(mwScheduleMonthField, c);

        c.gridx = 5;
        c.insets = new Insets(10, 5, 5, 0);
        JLabel mwScheduleWeek = new JLabel("Day of Week: ");
        mwSubscriptionPanel.add(mwScheduleWeek, c);

        c.gridx = 6;
        mwScheduleWeekField = new JTextField("", 10);
        mwSubscriptionPanel.add(mwScheduleWeekField, c);

        c.gridy = 5;
        c.gridx = 1;
        c.insets = new Insets(10, 5, 5, 0);
        JLabel mwScheduleHour = new JLabel("Hour: ");
        mwSubscriptionPanel.add(mwScheduleHour, c);

        c.gridx = 2;
        c.insets = new Insets(10, 5, 5, 30);
        mwScheduleHourField = new JTextField("", 10);
        mwSubscriptionPanel.add(mwScheduleHourField, c);

        c.gridx = 3;
        c.insets = new Insets(10, 5, 5, 0);
        JLabel mwScheduleMinute = new JLabel("Minute: ");
        mwSubscriptionPanel.add(mwScheduleMinute, c);

        c.gridx = 4;
        c.insets = new Insets(10, 5, 5, 30);
        mwScheduleMinuteField = new JTextField("", 10);
        mwSubscriptionPanel.add(mwScheduleMinuteField, c);

        c.gridx = 5;
        c.insets = new Insets(10, 5, 5, 0);
        JLabel mwScheduleSec = new JLabel("Sec: ");
        mwSubscriptionPanel.add(mwScheduleSec, c);

        c.gridx = 6;
        mwScheduleSecField = new JTextField("", 10);
        mwSubscriptionPanel.add(mwScheduleSecField, c);

        mwFillInExampleButton = new JButton("Fill in example");
        mwFillInExampleButton.addActionListener(this);
        mwQueryExamplesPanel.add(mwFillInExampleButton, BorderLayout.EAST);

        mwRunQueryButton = new JButton("Run query");
        mwRunQueryButton.addActionListener(this);
        mwButtonPanel.add(mwRunQueryButton);

        mainWindow.getContentPane().add(mwMainPanel);
        mainWindow.pack();
        mainWindow.setVisible(true);

        // /*
        // * Find out how much the window has to be scaled whenever new
        // components
        // * are added. This must be done after rendering the GUI, otherwise the
        // * sizes will be wrong!
        // */
        // if (((JComboBox) mwQuerySelectComboBoxes.getFirst()).getSize().height
        // > ((JTextField)
        // mwQueryArgumentTextFields.getFirst()).getSize().height) {
        // mwHeightDifference = ((JComboBox)
        // mwQuerySelectComboBoxes.getFirst()).getPreferredSize().height
        // + c.insets.top + c.insets.bottom;
        // } else {
        // mwHeightDifference = ((JTextField)
        // mwQueryArgumentTextFields.getFirst()).getPreferredSize().height
        // + c.insets.top + c.insets.bottom;
        // }
    }

    /**
     * Sets up the window used for results display. Does not destroy the old
     * window.
     */
    private void createResultsWindow() {
        resultsWindow = new JFrame("Query results");
        resultsWindow.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        rwResultsPanel = new JPanel();
        rwResultsPanel.setLayout(new BoxLayout(rwResultsPanel, BoxLayout.Y_AXIS));

        rwResultsTable = new JTable(data, columnNames);
        rwResultsScrollPane = new JScrollPane(rwResultsTable);
        rwResultsPanel.add(rwResultsTable.getTableHeader());
        rwResultsPanel.add(rwResultsScrollPane);

        resultsWindow.getContentPane().add(rwResultsPanel);
        resultsWindow.pack();
        resultsWindow.setVisible(true);
    }

    /**
     * Sets up the window used to show the list of examples. Can only be open
     * once.
     */
    private void drawExampleWindow() {
        if (exampleWindow != null) {
            exampleWindow.setVisible(true);
            return;
        }
        exampleWindow = new JFrame("Choose example");
        exampleWindow.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        ewMainPanel = new JPanel();
        ewMainPanel.setLayout(new BoxLayout(ewMainPanel, BoxLayout.PAGE_AXIS));
        exampleWindow.add(ewMainPanel);

        ewListPanel = new JPanel();
        ewListPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 5));
        ewListPanel.setLayout(new BoxLayout(ewListPanel, BoxLayout.PAGE_AXIS));

        ewButtonPanel = new JPanel();
        ewButtonPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        ewButtonPanel.setLayout(new BoxLayout(ewButtonPanel, BoxLayout.LINE_AXIS));

        ewMainPanel.add(ewListPanel);
        ewMainPanel.add(ewButtonPanel);

        ewExampleList = new JList();
        ewExampleScrollPane = new JScrollPane(ewExampleList);
        ewListPanel.add(ewExampleScrollPane);
        ewExampleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        String[] exampleList = new String[ExampleQueries.getExamples().size()];
        for (int i = 0; i < ExampleQueries.getExamples().size(); i++) {
            exampleList[i] = ((Query) ExampleQueries.getExamples().get(i)).getDescription();
        }
        ewExampleList.setListData(exampleList);

        ewOkButton = new JButton("Fill in");
        ewOkButton.addActionListener(this);
        ewButtonPanel.add(Box.createHorizontalGlue());
        ewButtonPanel.add(ewOkButton);
        ewButtonPanel.add(Box.createHorizontalGlue());

        exampleWindow.pack();
        exampleWindow.setVisible(true);
    }

    /**
     * Sets up the window used to show the debug output.
     */
    private void drawDebugWindow() {
        debugWindow = new JFrame("Debug output");
        debugWindow.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        debugWindow.addWindowListener(this);
        debugWindow.setLocation(500, 100);
        debugWindow.setSize(500, 300);

        dwOutputTextArea = new JTextArea();
        dwOutputScrollPane = new JScrollPane(dwOutputTextArea);
        debugWindow.add(dwOutputScrollPane);

        dwButtonPanel = new JPanel();
        debugWindow.add(dwButtonPanel, BorderLayout.AFTER_LAST_LINE);

        dwClearButton = new JButton("Clear");
        dwClearButton.addActionListener(this);
        dwButtonPanel.add(dwClearButton);
    }

    public final void actionPerformed(final ActionEvent e) {
        int i;
        if (e.getSource() == mwRunQueryButton) {
            mwQueryButtonPressed();
        } else if (e.getSource() == mwServiceInfoButton) {
            mwInfoButtonPressed();
        } else if (e.getSource() == mwSubscriptionIdButton) {
            mwSubscriptionIdButtonPressed();
        } else if (e.getSource() == mwFillInExampleButton) {
            drawExampleWindow();
        } else if (e.getSource() == dwClearButton) {
            dwOutputTextArea.setText("");
        } else if (e.getSource() == mwUnsubscribeQueryButton) {
            mwUnsubscribeQueryButtonPressed(mwUnsubscribeQueryTextField.getText());
        } else if (e.getSource() == ewOkButton) {
            examplesChanged();
        } else if (e.getSource() == mwShowDebugWindowCheckBox) {
            debugWindow.setVisible(mwShowDebugWindowCheckBox.isSelected());
        } else if ((i = mwQuerySelectComboBoxes.indexOf(e.getSource())) >= 0) {
            mwQuerySelectComboBoxesChanged(i);
        }
    }

    private void mwSubscriptionIdButtonPressed() {
        try {
            String title = "Service is responding";
            StringBuilder msg = new StringBuilder();
            List<String> subscriptionIDs = client.getSubscriptionIds("SimpleEventQuery");
            if (subscriptionIDs != null && !subscriptionIDs.isEmpty()) {
                msg.append("The following subscription IDs were found in the repository:\n");
                for (String s : subscriptionIDs) {
                    msg.append("- ").append(s).append("\n");
                }
            } else {
                msg.append("There are no subscribed queries.");
            }
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame, msg.toString(), title, JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            dwOutputTextArea.append("Could not fetch subscription IDs from repository.");
            StringWriter detailed = new StringWriter();
            PrintWriter pw = new PrintWriter(detailed);
            ex.printStackTrace(pw);
            dwOutputTextArea.append(detailed.toString());
            showErrorFrame();
        }
    }

    private void mwUnsubscribeQueryButtonPressed(String subscriptionID) {
        try {
            JFrame frame = new JFrame();
            if (subscriptionID.equals("")) {
                JOptionPane.showMessageDialog(frame, "Please specify a SubscriptionID", "Service is responding",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            client.unsubscribe(subscriptionID);
            JOptionPane.showMessageDialog(frame, "Successfully unsubscribed.", "Service is responding",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            dwOutputTextArea.append("Could not unsubscribe query.");
            StringWriter detailed = new StringWriter();
            PrintWriter pw = new PrintWriter(detailed);
            ex.printStackTrace(pw);
            dwOutputTextArea.append(detailed.toString());
            showErrorFrame();
        }
    }

    /**
     * Handler for pressed "Run query" button.
     */
    private void mwQueryButtonPressed() {
        dwOutputTextArea.setText("");
        if (resultsWindow != null) {
            resultsWindow.dispose();
        }
        try {
            // checkQueryUrl(mwServiceUrlTextField.getText());
            clearParameters();

            /* get event type selection from GUI */
            ArrayOfString events = new ArrayOfString();
            if (mwObjectEventsCheckBox.isSelected()) {
                events.getString().add("ObjectEvent");
            }
            if (mwAggregationEventsCheckBox.isSelected()) {
                events.getString().add("AggregationEvent");
            }
            if (mwQuantityEventsCheckBox.isSelected()) {
                events.getString().add("QuantityEvent");
            }
            if (mwTransactionEventsCheckBox.isSelected()) {
                events.getString().add("TransactionEvent");
            }
            if (!events.getString().isEmpty()) {
                QueryParam queryParam = new QueryParam();
                queryParam.setName("eventType");
                queryParam.setValue(events);
                addParameter(queryParam);
            }

            String name;
            for (int i = 0; i < mwQueryArgumentTextFields.size() - 1; i++) {
                name = ((JTextFieldEnhanced) mwQueryArgumentTextFields.get(i)).queryItem.getQueryText();
                QueryParam param = new QueryParam();
                param.setName(name);
                switch (((JTextFieldEnhanced) mwQueryArgumentTextFields.get(i)).queryItem.getParamType()) {
                case ListOfString:
                    ArrayOfString valueArray = QueryBrokerHelper.stringListToArray(((JTextField) mwQueryArgumentTextFields.get(i)).getText());
                    param.setValue(valueArray);
                    addParameter(param);
                    break;
                case Int:
                    Integer valueInteger = Integer.decode(((JTextField) mwQueryArgumentTextFields.get(i)).getText());
                    param.setValue(valueInteger);
                    addParameter(param);
                    break;
                case Time:
                    // parse given ISO8601 date string into a calendar
                    String dateStr = ((JTextField) mwQueryArgumentTextFields.get(i)).getText();
                    Calendar cal = TimeParser.parseAsCalendar(dateStr);
                    param.setValue(cal);
                    addParameter(param);
                    break;
                default:
                    String value = ((JTextField) mwQueryArgumentTextFields.get(i)).getText();
                    param.setValue(value);
                    addParameter(param);
                    break;
                }
            }

            configureServiceIfNecessary();
            if (isSubscribed.isSelected()) {
                if (mwSubIdField.getText().equals("")) {
                    JFrame frame = new JFrame();
                    JOptionPane.showMessageDialog(frame, "Please specify a SubscriptionID", "Missing input",
                            JOptionPane.PLAIN_MESSAGE);
                    return;
                }
                Subscribe subcr = new Subscribe();
                subcr.setDest(mwDestUriTextField.getText());
                subcr.setQueryName("SimpleEventQuery");
                subcr.setSubscriptionID(mwSubIdField.getText());
                SubscriptionControls controls = new SubscriptionControls();
                String dateStr = mwInitRecTimeField.getText();
                DatatypeFactory factory = DatatypeFactory.newInstance();
                controls.setInitialRecordTime(factory.newXMLGregorianCalendar(dateStr));
                controls.setReportIfEmpty(reportIf.isSelected());
                QuerySchedule sched = new QuerySchedule();

                if (!triggerIf.isSelected()) {
                    sched.setSecond(mwScheduleSecField.getText());
                    sched.setMinute(mwScheduleMinuteField.getText());
                    sched.setHour(mwScheduleHourField.getText());
                    sched.setDayOfMonth(mwScheduleDayField.getText());
                    sched.setMonth(mwScheduleMonthField.getText());
                    sched.setDayOfWeek(mwScheduleWeekField.getText());
                    controls.setSchedule(sched);
                } else {
                    controls.setTrigger(mwDestUriTextField.getText());
                }
                subcr.setControls(controls);
                QueryParams queryParams = new QueryParams();
                queryParams.getParam().addAll(internalQueryParams);
                debug("Number of query parameters: " + queryParams.getParam().size() + "\n");
                for (QueryParam queryParam : internalQueryParams) {
                    debug(queryParam.getName() + " " + queryParam.getValue() + "\n");
                }
                subcr.setParams(queryParams);
                client.subscribe(subcr);
                JFrame frame = new JFrame();
                JOptionPane.showMessageDialog(frame, "Query subscription successful.", "Service invocation successful",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                data = runQuery();
                createResultsWindow();
            }
        } catch (ParseException e) {
            String msg = "Unable to parse a Time value.";
            dwOutputTextArea.append("\n" + msg + "\n");
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            dwOutputTextArea.append(sw.toString());
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame, msg + "\n" + e.getMessage(), "Service invocation error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            String msg = "Unexpected error while invoking EPCIS Query Interface.";
            dwOutputTextArea.append("\n" + msg + "\n");
            dwOutputTextArea.append(e.toString());
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            dwOutputTextArea.append(sw.toString());
            showErrorFrame();
        }
    }

    /**
     * Shows a new JFrame with a specific invocation error message.
     */
    private void showErrorFrame() {
        JFrame frame = new JFrame();
        String msg = "Unexpected error while invoking EPCIS Query Interface.\n"
                + "See the Debug output window for more details.";
        JOptionPane.showMessageDialog(frame, msg, "Service invocation failed", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Handles the event of a changed JComboBox in the query arguments section
     * Will add or remove JComboBoxes as necessary and resize the window.
     * 
     * @param i
     *            The index of the combo box.
     */
    private void mwQuerySelectComboBoxesChanged(final int i) {
        JComboBox cb = (JComboBox) mwQuerySelectComboBoxes.get(i);

        if ((cb.getSelectedIndex() == 0) && (cb != mwQuerySelectComboBoxes.getLast())) {
            /*
             * the user selected "ignore" and this is not the last row, so
             * remove it
             */
            removeArgumentRow(i);
        } else if ((cb.getSelectedIndex() != 0) && (cb == mwQuerySelectComboBoxes.getLast())) {
            /* the user changed the value of the last row, so add a new row */
            addArgumentRow(i);
        } else {
            /* the user changed an existing row, so just update description */
            ((JTextFieldEnhanced) mwQueryArgumentTextFields.get(i)).setQueryItem(queryParamsUserText.get(queryParameterUsertext[cb.getSelectedIndex()]));
        }
    }

    /**
     * Handles the event of a pressed "info" button. Queries the server for
     * information about it's version and the implemented standard. If this
     * succeeds, one can assume that the connection to the service works fine;
     * if this fails, an error message will be shown to the user with the cause
     * and a stack trace will be printed to the console
     */
    private void mwInfoButtonPressed() {
        dwOutputTextArea.setText("");
        try {
            configureServiceIfNecessary();
            String standardVersion = client.getStandardVersion();
            String vendorVersion = client.getVendorVersion();
            List<String> queryNames = client.getQueryNames();
            String text = "EPCIS Query Service responding:\n" + "Standard version: " + standardVersion + "\n"
                    + "Service version: " + vendorVersion + "\n" + "Supported query names: ";
            for (String elem : queryNames) {
                text += elem + "\n";
            }
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame, text, "Service invocation successful", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            dwOutputTextArea.append("Could not execute query:");
            StringWriter detailed = new StringWriter();
            PrintWriter pw = new PrintWriter(detailed);
            e.printStackTrace(pw);
            dwOutputTextArea.append(detailed.toString());
            showErrorFrame();
        }
    }

    /**
     * Handler for the case that the user chooses an example. Updates the GUI
     * with the example
     */
    private void examplesChanged() {
        int selected = ewExampleList.getSelectedIndex();
        if (selected >= 0) {
            Query ex = (Query) ExampleQueries.getExamples().get(selected);
            mwObjectEventsCheckBox.setSelected(ex.getReturnObjectEvents());
            mwAggregationEventsCheckBox.setSelected(ex.getReturnAggregationEvents());
            mwQuantityEventsCheckBox.setSelected(ex.getReturnQuantityEvents());
            mwTransactionEventsCheckBox.setSelected(ex.getReturnTransactionEvents());

            QueryItem toAddItem = null;
            int i = 0;
            for (QueryParam item : ex.getQueryParameters()) {
                toAddItem = null;
                toAddItem = queryParamsQueryText.get(item.getName());
                if (toAddItem == null) {
                    dwOutputTextArea.append("bugbug: Query example " + "uses unknown queryParam");
                } else {
                    ((JComboBox) mwQuerySelectComboBoxes.get(i)).setSelectedItem(toAddItem.getUserText());
                    ((JTextFieldEnhanced) mwQueryArgumentTextFields.get(i)).setText((String) item.getValue());
                }
                i++;
            }

            /* set the not necessary rows to "ignore" which will delete them */
            int tobedeleted = mwQuerySelectComboBoxes.size() - 1 - i;
            for (int j = 0; j < tobedeleted; j++) {
                ((JComboBox) mwQuerySelectComboBoxes.get(i)).setSelectedIndex(0);
            }

            exampleWindow.setVisible(false);
        }
    }

    /**
     * Removes the row ith row of the query parameters list and updates
     * constraints of the others. Only used by queryselect_changed()
     * 
     * @param i
     *            The index of the row to be removed.
     */
    private void removeArgumentRow(final int i) {
        mwQueryArgsPanel.remove((JComboBox) mwQuerySelectComboBoxes.get(i));
        mwQueryArgsPanel.remove((JTextField) mwQueryArgumentTextFields.get(i));

        mwQuerySelectComboBoxes.remove(i);
        mwQueryArgumentTextFields.remove(i);

        /* Update constraints */
        GridBagLayout layout = (GridBagLayout) mwQueryArgsPanel.getLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 5, 5, 0);
        for (int j = i; j < mwQuerySelectComboBoxes.size(); j++) {
            c.weightx = 0;
            c.gridx = 0;
            c.gridy = j;
            c.ipadx = 0;
            layout.setConstraints((JComboBox) mwQuerySelectComboBoxes.get(j), c);
            c.weightx = 1;
            c.gridx = 1;
            c.ipadx = mwQueryArgumentTextFieldsExtraWidth;
            layout.setConstraints((JTextField) mwQueryArgumentTextFields.get(j), c);
        }
        /* update graphics */
        mainWindow.pack();
    }

    /**
     * Adds another row at the end of the query parameters list. Only used by
     * queryselect_changed()
     * 
     * @param i
     *            The index of the row to be added.
     */
    private void addArgumentRow(final int i) {

        mwQuerySelectComboBoxes.add(new JComboBox(queryParameterUsertext));
        ((JComboBox) mwQuerySelectComboBoxes.getLast()).addActionListener(this);
        mwQueryArgumentTextFields.add(new JTextFieldEnhanced(15, queryParamsUserText.get("ignore")));

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 5, 5, 0);
        c.weightx = 0;
        c.gridx = 0;
        c.gridy = i + 1;
        mwQueryArgsPanel.add((JComboBox) mwQuerySelectComboBoxes.getLast(), c);
        c.weightx = 1;
        c.gridx = 1;
        c.ipadx = mwQueryArgumentTextFieldsExtraWidth;
        mwQueryArgsPanel.add((JTextFieldEnhanced) mwQueryArgumentTextFields.getLast(), c);

        /* update tooltip of TextField */
        JComboBox cb = (JComboBox) mwQuerySelectComboBoxes.get(i);
        ((JTextFieldEnhanced) mwQueryArgumentTextFields.get(i)).setQueryItem(queryParamsUserText.get(queryParameterUsertext[cb.getSelectedIndex()]));

        /* update graphics */
        mainWindow.pack();
    }

    @Override
    public void windowClosing(final WindowEvent e) {
        if (e.getSource() == debugWindow) {
            mwShowDebugWindowCheckBox.setSelected(false);
            return;
        }
    }

    /**
     * A extended JTextField which allows us to store the corresponding
     * QueryItem.
     */
    public class JTextFieldEnhanced extends JTextField {

        private static final long serialVersionUID = -8874871130001273285L;

        /**
         * The stored QueryItem.
         */
        private QueryItem queryItem;

        /**
         * Constructor which assigns a QueryItem.
         * 
         * @param columns
         *            for the length of the JTextField
         * @param item
         *            which should be stored
         */
        public JTextFieldEnhanced(final int columns, final QueryItem item) {
            super(columns);
            setQueryItem(item);
        }

        /**
         * Default Constructor.
         * 
         * @param columns
         *            for the length of the JTextField
         */
        public JTextFieldEnhanced(final int columns) {
            super(columns);
        }

        /**
         * Default Constructor.
         */
        public JTextFieldEnhanced() {
            super();
        }

        /**
         * Sets another QueryItem an does the update of the tool-tip.
         * 
         * @param item
         *            the new QueryItem
         */
        public void setQueryItem(final QueryItem item) {
            this.queryItem = item;
            this.setToolTipText(queryItem.getDescription());
        }
    }

    /**
     * A new class for a QueryItem which can store all its specific features.
     */
    public class QueryItem {

        private boolean required;

        private String userText;

        private String queryText;

        private String description;

        private ParameterType paramType;

        /**
         * @return the description
         */
        public String getDescription() {
            return description;
        }

        /**
         * @param description
         *            the description to set
         */
        public void setDescription(final String description) {
            this.description = description;
        }

        /**
         * @return the paramType
         */
        public ParameterType getParamType() {
            return paramType;
        }

        /**
         * @param paramType
         *            the paramType to set
         */
        public void setParamType(final ParameterType paramType) {
            this.paramType = paramType;
        }

        /**
         * @return the queryText
         */
        public String getQueryText() {
            return queryText;
        }

        /**
         * @param queryText
         *            the queryText to set
         */
        public void setQueryText(final String queryText) {
            this.queryText = queryText;
        }

        /**
         * @return the required
         */
        public boolean getRequired() {
            return required;
        }

        /**
         * @param required
         *            the required to set
         */
        public void setRequired(final boolean required) {
            this.required = required;
        }

        /**
         * @return the userText
         */
        public String getUserText() {
            return userText;
        }

        /**
         * @param userText
         *            the userText to set
         */
        public void setUserText(final String userText) {
            this.userText = userText;
        }
    }

    void debug(String debugMessage) {
        if (!debugMessage.endsWith("\n")) {
            debugMessage += "\n";
        }
        dwOutputTextArea.append(debugMessage);
    }

    String getAddress() {
        return mwServiceUrlTextField.getText();
    }

    Object[] getAuthenticationOptions() {
        return mwAuthOptions.getAuthenticationOptions();
    }

    void setButtonsEnabled(boolean buttonsEnabled) {
        JButton[] buttons = { mwRunQueryButton, mwServiceInfoButton, mwUnsubscribeQueryButton, mwSubscriptionIdButton };
        for (JButton button : buttons) {
            button.setEnabled(buttonsEnabled);
        }
    }

    public void configurationChanged(AuthenticationOptionsChangeEvent ace) {
        this.configurationChanged = true;
        setButtonsEnabled(ace.isComplete());
    }

    /**
     * Prints the results from a query invocation to the debug window and
     * returns a two-dimensional array in a format suitable for a JTable object.
     * 
     * @param eventList
     *            The result list containing the matching events.
     * @return A two-dimensional array containing the matching events in a
     *         format suitable for displaying in a JTable object.
     */
    private Object[][] processEvents(final List<Object> eventList) {
        int nofEvents = eventList.size();
        Object[][] table = new Object[nofEvents][12];
        int row = 0;

        debug("\n\n" + nofEvents + " events returned by the server:\n\n");
        for (Object o : eventList) {
            if (o instanceof JAXBElement<?>) {
                o = ((JAXBElement<?>) o).getValue();
            }
            EPCISEventType event = (EPCISEventType) o;
            debug("[ EPCISEvent ]\n");
            String eventTime = QueryBrokerHelper.printCalendar(event.getEventTime().toGregorianCalendar());
            debug("eventTime:\t" + eventTime + "\n");
            table[row][1] = eventTime;
            String recordTime = QueryBrokerHelper.printCalendar(event.getRecordTime().toGregorianCalendar());
            debug("recordTime:\t" + recordTime + "\n");
            table[row][2] = recordTime;
            debug("timeZoneOffset:\t" + event.getEventTimeZoneOffset() + "\n");

            if (event instanceof ObjectEventType) {
                debug("[ ObjectEvent ]\n");
                ObjectEventType e = (ObjectEventType) event;
                table[row][0] = "Object";
                debug("epcList:\t");
                table[row][5] = "";
                for (EPC epc : e.getEpcList().getEpc()) {
                    debug(" '" + epc.getValue() + "'");
                    table[row][5] = table[row][5] + "'" + epc.getValue() + "' ";
                }
                debug("\n");
                debug("action:\t\t" + e.getAction().toString() + "\n");
                table[row][6] = e.getAction().toString();
                debug("bizStep:\t" + e.getBizStep() + "\n");
                table[row][7] = e.getBizStep();
                debug("disposition:\t" + e.getDisposition() + "\n");
                table[row][8] = e.getDisposition();
                if (e.getReadPoint() != null) {
                    debug("readPoint:\t" + e.getReadPoint().getId() + "\n");
                    table[row][9] = e.getReadPoint().getId();
                } else {
                    debug("readPoint:\tnull\n");
                }
                if (e.getBizLocation() != null) {
                    debug("bizLocation:\t" + e.getBizLocation().getId() + "\n");
                    table[row][10] = e.getBizLocation().getId();
                } else {
                    debug("bizLocation:\tnull\n");
                }
                if (e.getBizTransactionList() != null) {
                    debug("bizTrans:\tType, ID\n");
                    table[row][11] = "";
                    for (BusinessTransactionType bizTrans : e.getBizTransactionList().getBizTransaction()) {
                        debug("\t'" + bizTrans.getType() + "', '" + bizTrans.getValue() + "'\n");
                        table[row][11] = table[row][11] + "'" + bizTrans.getType() + ", " + bizTrans.getValue()
                                + "' ; ";
                    }
                    if (!"".equals(table[row][11])) {
                        // remove last "; "
                        table[row][11] = ((String) table[row][11]).substring(0, ((String) table[row][11]).length() - 2);
                    }
                } else {
                    debug("bizTrans:\tnull\n");
                }
                debug("\n");

            } else if (event instanceof TransactionEventType) {
                debug("[ TransactionEvent ]\n");
                TransactionEventType e = (TransactionEventType) event;
                table[row][0] = "Transaction";
                debug("parentID:\t" + e.getParentID() + "\n");
                table[row][3] = e.getParentID();
                debug("epcList:\t");
                table[row][5] = "";
                for (EPC epc : e.getEpcList().getEpc()) {
                    debug(" '" + epc.getValue() + "'");
                    table[row][5] = table[row][5] + "'" + epc.getValue() + "' ";
                }
                debug("\n");
                debug("action:\t\t" + e.getAction().toString() + "\n");
                table[row][6] = e.getAction().toString();
                debug("bizStep:\t" + e.getBizStep() + "\n");
                table[row][7] = e.getBizStep();
                debug("disposition:\t" + e.getDisposition() + "\n");
                table[row][8] = e.getDisposition();
                if (e.getReadPoint() != null) {
                    debug("readPoint:\t" + e.getReadPoint().getId() + "\n");
                    table[row][9] = e.getReadPoint().getId();
                } else {
                    debug("readPoint:\tnull\n");
                }
                if (e.getBizLocation() != null) {
                    debug("bizLocation:\t" + e.getBizLocation().getId() + "\n");
                    table[row][10] = e.getBizLocation().getId();
                } else {
                    debug("bizLocation:\tnull\n");
                }
                if (e.getBizTransactionList() != null) {
                    debug("bizTrans:\tType, ID\n");
                    table[row][11] = "";
                    for (BusinessTransactionType bizTrans : e.getBizTransactionList().getBizTransaction()) {
                        debug("\t'" + bizTrans.getType() + "', '" + bizTrans.getValue() + "'\n");
                        table[row][11] = table[row][11] + "'" + bizTrans.getType() + ", " + bizTrans.getValue()
                                + "' ; ";
                    }
                    if (!"".equals(table[row][11])) {
                        // remove last "; "
                        table[row][11] = ((String) table[row][11]).substring(0, ((String) table[row][11]).length() - 2);
                    }
                } else {
                    debug("bizTrans:\tnull\n");
                }
                debug("\n");

            } else if (event instanceof AggregationEventType) {
                debug("[ AggregationEvent ]\n");
                AggregationEventType e = (AggregationEventType) event;
                table[row][0] = "Aggregation";
                debug("parentID:\t" + e.getParentID() + "\n");
                table[row][3] = e.getParentID();
                debug("childEPCs:\t");
                table[row][5] = "";
                for (EPC epc : e.getChildEPCs().getEpc()) {
                    debug(" '" + epc.getValue() + "'");
                    table[row][5] = table[row][5] + "'" + epc.getValue() + "' ";
                }
                debug("\n");
                debug("action:\t\t" + e.getAction().toString() + "\n");
                table[row][6] = e.getAction().toString();
                debug("bizStep:\t" + e.getBizStep() + "\n");
                table[row][7] = e.getBizStep();
                debug("disposition:\t" + e.getDisposition() + "\n");
                table[row][8] = e.getDisposition();
                if (e.getReadPoint() != null) {
                    debug("readPoint:\t" + e.getReadPoint().getId() + "\n");
                    table[row][9] = e.getReadPoint().getId();
                } else {
                    debug("readPoint:\tnull\n");
                }
                if (e.getBizLocation() != null) {
                    debug("bizLocation:\t" + e.getBizLocation().getId() + "\n");
                    table[row][10] = e.getBizLocation().getId();
                } else {
                    debug("bizLocation:\tnull\n");
                }
                if (e.getBizTransactionList() != null) {
                    debug("bizTrans:\tType, ID\n");
                    table[row][11] = "";
                    for (BusinessTransactionType bizTrans : e.getBizTransactionList().getBizTransaction()) {
                        debug("\t'" + bizTrans.getType() + "', '" + bizTrans.getValue() + "'\n");
                        table[row][11] = table[row][11] + "'" + bizTrans.getType() + ", " + bizTrans.getValue()
                                + "' ; ";
                    }
                    if (!"".equals(table[row][11])) {
                        // remove last "; "
                        table[row][11] = ((String) table[row][11]).substring(0, ((String) table[row][11]).length() - 2);
                    }
                } else {
                    debug("bizTrans:\tnull\n");
                }
                debug("\n");

            } else if (event instanceof QuantityEventType) {
                debug("[ QuantityEvent ]\n");
                QuantityEventType e = (QuantityEventType) event;
                table[row][0] = "Quantity";
                debug("quantity:\t" + e.getQuantity() + "\n");
                table[row][4] = Integer.valueOf(e.getQuantity());
                debug("ecpClass:\t" + e.getEpcClass() + "\n");
                table[row][5] = e.getEpcClass();
                debug("bizStep:\t" + e.getBizStep() + "\n");
                table[row][7] = e.getBizStep();
                debug("disposition:\t" + e.getDisposition() + "\n");
                table[row][8] = e.getDisposition();
                if (e.getReadPoint() != null) {
                    debug("readPoint:\t" + e.getReadPoint().getId() + "\n");
                    table[row][9] = e.getReadPoint().getId();
                } else {
                    debug("readPoint:\tnull\n");
                }
                if (e.getBizLocation() != null) {
                    debug("bizLocation:\t" + e.getBizLocation().getId() + "\n");
                    table[row][10] = e.getBizLocation().getId();
                } else {
                    debug("bizLocation:\tnull\n");
                }
                if (e.getBizTransactionList() != null) {
                    debug("bizTrans:\tType, ID\n");
                    table[row][11] = "";
                    for (BusinessTransactionType bizTrans : e.getBizTransactionList().getBizTransaction()) {
                        debug("\t'" + bizTrans.getType() + "', '" + bizTrans.getValue() + "'\n");
                        table[row][11] = table[row][11] + "'" + bizTrans.getType() + ", " + bizTrans.getValue()
                                + "' ; ";
                    }
                    if (!"".equals(table[row][11])) {
                        // remove last "; "
                        table[row][11] = ((String) table[row][11]).substring(0, ((String) table[row][11]).length() - 2);
                    }
                } else {
                    debug("bizTrans:\tnull\n");
                }
                debug("\n");
            }
            row++;
        }
        return table;
    }

    /**
     * Reset the query arguments.
     */
    public void clearParameters() {
        internalQueryParams.clear();
    }

    /**
     * Add a new query parameter.
     * 
     * @param param
     *            The query parameter to add.
     */
    public void addParameter(final QueryParam param) {
        internalQueryParams.add(param);
    }

    /**
     * Forces the client to reconfigure the service if the any of its parameters
     * (authentication, endpoint address) have been changed.
     * 
     * @throws Exception
     */
    private void configureServiceIfNecessary() throws Exception {
        if (!client.isServiceConfigured() || configurationChanged) {
            // validate the URL and get auth options from the main window
            client.configureService(new URL(getAddress()), getAuthenticationOptions());
            configurationChanged = false;
        }
    }

    /**
     * Run the query with the currently set query arguments Returns the results
     * in a format that is suitable for JTable.
     * 
     * @return The pretty-printed query results.
     * @throws Exception
     *             If any Exception occurred while invoking the query service.
     */
    private Object[][] runQuery() throws Exception {
        QueryParams queryParams = new QueryParams();
        queryParams.getParam().addAll(internalQueryParams);
        debug("Number of query parameters: " + queryParams.getParam().size() + "\n");
        for (QueryParam queryParam : internalQueryParams) {
            if (queryParam.getValue() instanceof ArrayOfString) {
                debug(queryParam.getName() + " " + ((ArrayOfString) queryParam.getValue()).getString() + "\n");
            } else {
                debug(queryParam.getName() + " " + queryParam.getValue() + "\n");
            }
        }

        Poll poll = new Poll();
        poll.setQueryName("SimpleEventQuery");
        poll.setParams(queryParams);

        debug("running query...\n");
        QueryResults results = client.poll(poll);
        debug("done\n");

        // print to debug window and return result
        if (results != null && results.getResultsBody() != null && results.getResultsBody().getEventList() != null) {
            return processEvents(results.getResultsBody().getEventList().getObjectEventOrAggregationEventOrQuantityEvent());
        } else {
            return null;
        }
    }

    /**
     * Instantiates a new QueryClientGui and sets its look-and-feel to the one
     * matching the current operating system.
     * 
     * @param args
     *            The address to which the QueryClient should send the queries
     *            to. If omitted, a default address will be provided.
     */
    public static void main(final String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (args != null && args.length > 0) {
            try {
                new QueryBrokerGui(args[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else {
            new QueryBrokerGui();
        }
    }
}
