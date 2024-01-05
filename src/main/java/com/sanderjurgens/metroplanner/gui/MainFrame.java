package com.sanderjurgens.metroplanner.gui;

import com.sanderjurgens.metroplanner.model.Line;
import com.sanderjurgens.metroplanner.model.Network;
import com.sanderjurgens.metroplanner.model.Station;
import com.sanderjurgens.metroplanner.model.StationSet;
import com.sanderjurgens.metroplanner.planner.Planner;
import com.sanderjurgens.metroplanner.planner.MinStopsPlanner;
import com.sanderjurgens.metroplanner.planner.MinTransfersPlanner;
import com.sanderjurgens.metroplanner.planner.Route;
import com.sanderjurgens.metroplanner.planner.RouteSegment;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;

/**
 * The main GUI interface.
 *
 * @author sanderjurgens
 * @version 1.1
 */
public class MainFrame extends javax.swing.JFrame {

    /** The network that is currently loaded */
    private Network network;

    /** The a planner that minimizes the amount of stops */
    private Planner minStopsPlanner;
    /** The a planner that minimizes the amount of transfers */
    private Planner minTransfersPlanner;

    /** Static instance used by callbacks from individual components in the MapPanel */
    private static MainFrame instance;

    /**
     * Constructs a new MainFrame.
     */
    public MainFrame() {
        initComponents();

        // Automatically try to load the Paris network
        try {
            loadResource(getClass().getResource("paris.network"));
        } catch (IOException e) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, e);
        }

        // Fill the static instance and set it to fullscreen
        instance = this;
        instance.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    /**
     * Load a resource containing a network, completely discards the current loaded
     * network (if any).
     *
     * @param url the uniform locator to the resource containing the network
     * @throws IOException if an error occurs while reading the resource
     */
    private void loadResource(URL url) throws IOException {
        // Load the file into a new network
        network = new Network(url);

        // Create route planners based on the given network
        minTransfersPlanner = new MinTransfersPlanner(network);
        minStopsPlanner = new MinStopsPlanner(network);

        // Load all GUI information into the map panel
        mapPanel.loadResource(url, network);

        // Update GUI elements with the new network       
        fromCombobox.setModel(new DefaultComboBoxModel<>(network.getStationSet().toArray()));
        toCombobox.setModel(new DefaultComboBoxModel<>(network.getStationSet().toArray()));
        linesList.setModel(new DefaultComboBoxModel<>(network.getLineSet().toArray()));
        stationTable.setModel(new StationTableModel(network.getStationSet()));
    }

    /**
     * Helper class that provides a TableModel interface for a set of Stations.
     */
    private class StationTableModel extends AbstractTableModel {

        // The list of Stations
        private Station[] stations;

        /**
         * Class constructor. Make a new StationTableModel from a StationSet.
         *
         * @param stations the StationSet from which to create the TableModel
         */
        public StationTableModel(StationSet stations) {
            this.stations = stations.toArray(new Station[0]);
        }

        /**
         * Class constructor. Make a new StationTableModel from a Line.
         *
         * @param line the Line from which to create the TableModel
         */
        public StationTableModel(Line line) {
            this.stations = line.toArray(new Station[0]);
        }

        /**
         * Get the number of rows (Stations).
         *
         * @return the number of Stations
         */
        @Override
        public int getRowCount() {
            return stations.length;
        }

        /**
         * Get the number of columns (variables per Station).
         *
         * @return 2 (Code and Name, respectively)
         */
        @Override
        public int getColumnCount() {
            return 2;
        }

        /**
         * Get the name of the given column.
         *
         * @param column the column number for which to retrieve the variable
         * name
         * @return the name of the given column
         */
        @Override
        public String getColumnName(int column) {
            switch (column) {
                case 0:
                    return "Code";
                case 1:
                    return "Name";
                default:
                    return "";
            }
        }

        /**
         * Get the value of the given cell.
         *
         * @param row the row number
         * @param column the column number
         * @return the value of the given cell
         */
        @Override
        public Object getValueAt(int row, int column) {
            switch (column) {
                case 0:
                    return stations[row].getCode();
                case 1:
                    return stations[row].getName();
                default:
                    return "";
            }
        }
    }

    /**
     * Helper class that provides a TableModel interface for a Route consisting
     * of RouteSegments.
     */
    class RouteTableModel extends AbstractTableModel {

        // The list of RouteSegments
        private RouteSegment[] segments;

        /**
         * Class constructor. Make a new RouteTableModel from a Route.
         *
         * @param route the Route from which to create the TableModel
         */
        public RouteTableModel(Route route) {
            this.segments = route.toArray(new RouteSegment[0]);
        }

        /**
         * Get the number of rows (RouteSegments).
         *
         * @return the number of RouteSegments
         */
        @Override
        public int getRowCount() {
            return segments.length;
        }

        /**
         * Get the number of columns (variables per RouteSegment).
         *
         * @return 2 (From, To, Line and Direction, respectively)
         */
        @Override
        public int getColumnCount() {
            return 4;
        }

        /**
         * Get the name of the given column.
         *
         * @param column the column number for which to retrieve the variable
         * name
         * @return the name of the given column
         */
        @Override
        public String getColumnName(int column) {
            switch (column) {
                case 0:
                    return "From";
                case 1:
                    return "To";
                case 2:
                    return "Line";
                case 3:
                    return "Direction";
                default:
                    return "";
            }
        }

        /**
         * Get the value of the given cell.
         *
         * @param row the row number
         * @param column the column number
         * @return the value of the given cell
         */
        @Override
        public Object getValueAt(int row, int column) {
            switch (column) {
                case 0:
                    return segments[row].getFromStation();
                case 1:
                    return segments[row].getToStation();
                case 2:
                    return segments[row].getLine();
                case 3:
                    return segments[row].getDirection();
                default:
                    return "";
            }
        }
    }

    /**
     * Event handler for when a station is clicked on the map.
     *
     * This is called by the individual station components. It updates the flags
     * and ComboBoxes.
     *
     * @param station The station that was clicked
     * @param leftMouseButton true if the left mouse button was pressed, false
     * if it was the right mouse button
     */
    public static void stationClicked(Station station, boolean leftMouseButton) {

        // Left mouse button is for selecting start station
        if (leftMouseButton) {
            instance.fromCombobox.setSelectedItem(station);
            instance.mapPanel.markStartStation(station);
        } //Right mouse button is for selecting end station
        else {
            instance.toCombobox.setSelectedItem(station);
            instance.mapPanel.markEndStation(station);
        }

        instance.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        minimizeButtonGroup = new javax.swing.ButtonGroup();
        backgroundButtonGroup = new javax.swing.ButtonGroup();
        mainSplitpane = new javax.swing.JSplitPane();
        leftList = new javax.swing.JScrollPane();
        linesList = new javax.swing.JList<>();
        mainTabbedPanel = new javax.swing.JTabbedPane();
        textTab = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        lineTable = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        stationTable = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        routeTable = new javax.swing.JTable();
        mapTab = new javax.swing.JPanel();
        mapoptionPanel = new javax.swing.JPanel();
        mapbackgroundPanel = new javax.swing.JPanel();
        blankRadiobutton = new javax.swing.JRadioButton();
        mapRadiobutton = new javax.swing.JRadioButton();
        linesCheckbox = new javax.swing.JCheckBox();
        stationsCheckbox = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        mapPanel = new com.sanderjurgens.metroplanner.gui.MapPanel();
        topPanel = new javax.swing.JPanel();
        fromLabel = new javax.swing.JLabel();
        fromCombobox = new javax.swing.JComboBox<>();
        toLabel = new javax.swing.JLabel();
        toCombobox = new javax.swing.JComboBox<>();
        findButton = new javax.swing.JButton();
        minimizeLabel = new javax.swing.JLabel();
        stopsRadiobutton = new javax.swing.JRadioButton();
        transfersRadiobutton = new javax.swing.JRadioButton();
        menubar = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        menuOpen = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MetroPlanner");

        mainSplitpane.setResizeWeight(0.05);

        linesList.setModel(new javax.swing.AbstractListModel<Object>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        linesList.setMaximumSize(new java.awt.Dimension(100, 85));
        linesList.setMinimumSize(new java.awt.Dimension(85, 85));
        linesList.setPreferredSize(new java.awt.Dimension(60, 85));
        linesList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                linesListValueChanged(evt);
            }
        });
        leftList.setViewportView(linesList);

        mainSplitpane.setLeftComponent(leftList);

        textTab.setLayout(new java.awt.GridLayout(1, 3, 15, 0));

        lineTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Code", "Name"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane3.setViewportView(lineTable);

        textTab.add(jScrollPane3);

        stationTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Code", "Name"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(stationTable);

        textTab.add(jScrollPane2);

        routeTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "From", "To", "Line", "Direction"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane4.setViewportView(routeTable);

        textTab.add(jScrollPane4);

        mainTabbedPanel.addTab("Text", textTab);

        mapTab.setLayout(new java.awt.BorderLayout());

        mapoptionPanel.setPreferredSize(new java.awt.Dimension(110, 306));
        mapoptionPanel.setLayout(new javax.swing.BoxLayout(mapoptionPanel, javax.swing.BoxLayout.PAGE_AXIS));

        mapbackgroundPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Background"));
        mapbackgroundPanel.setPreferredSize(new java.awt.Dimension(100, 128));
        mapbackgroundPanel.setLayout(new javax.swing.BoxLayout(mapbackgroundPanel, javax.swing.BoxLayout.PAGE_AXIS));

        backgroundButtonGroup.add(blankRadiobutton);
        blankRadiobutton.setText("Blank");
        blankRadiobutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                blankRadiobuttonActionPerformed(evt);
            }
        });
        mapbackgroundPanel.add(blankRadiobutton);

        backgroundButtonGroup.add(mapRadiobutton);
        mapRadiobutton.setSelected(true);
        mapRadiobutton.setText("Map");
        mapRadiobutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mapRadiobuttonActionPerformed(evt);
            }
        });
        mapbackgroundPanel.add(mapRadiobutton);

        mapoptionPanel.add(mapbackgroundPanel);

        linesCheckbox.setSelected(true);
        linesCheckbox.setText("Lines");
        linesCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                linesCheckboxActionPerformed(evt);
            }
        });
        mapoptionPanel.add(linesCheckbox);

        stationsCheckbox.setSelected(true);
        stationsCheckbox.setText("Stations");
        stationsCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stationsCheckboxActionPerformed(evt);
            }
        });
        mapoptionPanel.add(stationsCheckbox);

        mapTab.add(mapoptionPanel, java.awt.BorderLayout.WEST);

        jScrollPane1.setAutoscrolls(true);
        jScrollPane1.getVerticalScrollBar().setUnitIncrement(16);
        jScrollPane1.setViewportView(mapPanel);

        mapTab.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        mainTabbedPanel.addTab("Map", mapTab);

        mainTabbedPanel.setSelectedIndex(1);

        mainSplitpane.setRightComponent(mainTabbedPanel);

        getContentPane().add(mainSplitpane, java.awt.BorderLayout.CENTER);

        topPanel.setPreferredSize(new java.awt.Dimension(400, 35));

        fromLabel.setText("From:");
        topPanel.add(fromLabel);

        fromCombobox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        fromCombobox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fromComboboxActionPerformed(evt);
            }
        });
        topPanel.add(fromCombobox);

        toLabel.setText("To:");
        topPanel.add(toLabel);

        toCombobox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        toCombobox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toComboboxActionPerformed(evt);
            }
        });
        topPanel.add(toCombobox);

        findButton.setText("Find!");
        findButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                findButtonActionPerformed(evt);
            }
        });
        topPanel.add(findButton);

        minimizeLabel.setText("Minimize:");
        topPanel.add(minimizeLabel);

        minimizeButtonGroup.add(stopsRadiobutton);
        stopsRadiobutton.setSelected(true);
        stopsRadiobutton.setText("Stops");
        topPanel.add(stopsRadiobutton);

        minimizeButtonGroup.add(transfersRadiobutton);
        transfersRadiobutton.setText("Transfers");
        topPanel.add(transfersRadiobutton);

        getContentPane().add(topPanel, java.awt.BorderLayout.NORTH);

        jMenu1.setText("File");

        menuOpen.setText("Open Network...");
        menuOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuOpenActionPerformed(evt);
            }
        });
        jMenu1.add(menuOpen);

        menubar.add(jMenu1);

        setJMenuBar(menubar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Open Network from file, through menu dialog.
     *
     * @param evt menu opened event
     */
	private void menuOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuOpenActionPerformed
            // Open a modal file dialog
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

            // Set file filter to only show accepted file types (and directories)
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            chooser.setFileFilter(new FileNameExtensionFilter("", "network"));
            int result = chooser.showOpenDialog(this);

            // Try to load the file into the program
            if (result == JFileChooser.APPROVE_OPTION && chooser.getSelectedFile() != null) {
                try {
                    loadResource(chooser.getSelectedFile().toURI().toURL());
                } catch (IOException e) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, e);
                }
            }
	}//GEN-LAST:event_menuOpenActionPerformed

    /**
     * Find the route between the two selected stations.
     *
     * @param evt find button pressed event
     */
	private void findButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findButtonActionPerformed
            // Get selected stations
            // Note that we can just get these from the comboboxes as the comboboxes
            // and the flag markers on the map are always in sync
            Station from = (Station) fromCombobox.getSelectedItem();
            Station to = (Station) toCombobox.getSelectedItem();

            // Get the appropriate planner
            Planner planner;
            if (stopsRadiobutton.isSelected()) {
                planner = minStopsPlanner;
            } else {
                planner = minTransfersPlanner;
            }

            // Calculate and show the route
            Route route = planner.findRoute(from, to);
            mapPanel.showRoute(route);
            routeTable.setModel(new RouteTableModel(route));
	}//GEN-LAST:event_findButtonActionPerformed

    /**
     * Make only the selected lines visible on the map.
     *
     * @param evt List item value changed event
     */
	private void linesListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_linesListValueChanged
            // Update the list of visible lines
            mapPanel.hideAllLines();
            for (Object value : linesList.getSelectedValuesList()) {
                Line line = (Line) value;
                mapPanel.showLine(line);
                lineTable.setModel(new StationTableModel(line));
            }
	}//GEN-LAST:event_linesListValueChanged

    /**
     * Set background to be visible.
     *
     * @param evt radio button selected event
     */
	private void mapRadiobuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mapRadiobuttonActionPerformed
            mapPanel.setBackgroundVisible(true);
	}//GEN-LAST:event_mapRadiobuttonActionPerformed

    /**
     * Set background to be blank/invisible.
     *
     * @param evt radio button selected event
     */
	private void blankRadiobuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_blankRadiobuttonActionPerformed
            mapPanel.setBackgroundVisible(false);
	}//GEN-LAST:event_blankRadiobuttonActionPerformed

    /**
     * Toggle visibility of the lines
     *
     * @param evt checkbox selected event
     */
	private void linesCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_linesCheckboxActionPerformed
            mapPanel.setLinesVisible(linesCheckbox.isSelected());
	}//GEN-LAST:event_linesCheckboxActionPerformed

    /**
     * Toggle visibility of the stations
     *
     * @param evt checkbox selected event
     */
	private void stationsCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stationsCheckboxActionPerformed
            mapPanel.setStationsVisible(stationsCheckbox.isSelected());
	}//GEN-LAST:event_stationsCheckboxActionPerformed

    /**
     * Event handler for when the ComboBox selects a new from station.
     *
     * Place the start marker flag on the newly selected station on the map.
     *
     * @param evt new ComboBox value selected event
     */
	private void fromComboboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fromComboboxActionPerformed
            Station fromStation = (Station) fromCombobox.getSelectedItem();
            mapPanel.markStartStation(fromStation);
            repaint();
	}//GEN-LAST:event_fromComboboxActionPerformed

    /**
     * Event handler for when the ComboBox selects a new to station.
     *
     * Place the end marker flag on the newly selected station on the map.
     *
     * @param evt new ComboBox value selected event
     */
	private void toComboboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toComboboxActionPerformed
            Station toStation = (Station) toCombobox.getSelectedItem();
            mapPanel.markEndStation(toStation);
            repaint();
	}//GEN-LAST:event_toComboboxActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    /** Group of map background type radio buttons */
    private javax.swing.ButtonGroup backgroundButtonGroup;
    /** Background type show blank */
    private javax.swing.JRadioButton blankRadiobutton;
    /** The button to find a route */
    private javax.swing.JButton findButton;
    /** Origin station chooser for route planner */
    private javax.swing.JComboBox<Object> fromCombobox;
    /** Origin label for route planner */
    private javax.swing.JLabel fromLabel;
    /** File menu in menu bar */
    private javax.swing.JMenu jMenu1;
    /** Scrolling pane that contains the map panel */
    private javax.swing.JScrollPane jScrollPane1;
    /** Scrolling pane that contains the stations table */
    private javax.swing.JScrollPane jScrollPane2;
    /** Scrolling pane that contains the lines table */
    private javax.swing.JScrollPane jScrollPane3;
    /** Scrolling pane that contains the route segment table */
    private javax.swing.JScrollPane jScrollPane4;
    /** Scrolling pane that contains the lines list */
    private javax.swing.JScrollPane leftList;
    /** The table of lines in the text tab */
    private javax.swing.JTable lineTable;
    /** Whether to show lines on the map */
    private javax.swing.JCheckBox linesCheckbox;
    /** List of all lines */
    private javax.swing.JList<Object> linesList;
    /** Main panel that splits between lines list and tabbed panel */
    private javax.swing.JSplitPane mainSplitpane;
    /** Main panel containing text and map tabs */
    private javax.swing.JTabbedPane mainTabbedPanel;
    /** Map panel on map tab*/
    private com.sanderjurgens.metroplanner.gui.MapPanel mapPanel;
    /** Background type show map */
    private javax.swing.JRadioButton mapRadiobutton;
    /** Central map tab */
    private javax.swing.JPanel mapTab;
    /** Small tab that contains background radio buttons */
    private javax.swing.JPanel mapbackgroundPanel;
    /** The panel containing visual options within the map tab */
    private javax.swing.JPanel mapoptionPanel;
    /** File menu item to open network */
    private javax.swing.JMenuItem menuOpen;
    /** Top menu bar */
    private javax.swing.JMenuBar menubar;
    /** Group of planner type radio buttons */
    private javax.swing.ButtonGroup minimizeButtonGroup;
    /** Planner type label */
    private javax.swing.JLabel minimizeLabel;
    /** The table of route segments in the text tab */
    private javax.swing.JTable routeTable;
    /** The table of stations in the text tab */
    private javax.swing.JTable stationTable;
    /** Whether to show stations on the map */
    private javax.swing.JCheckBox stationsCheckbox;
    /** Minimize stops planner type */
    private javax.swing.JRadioButton stopsRadiobutton;
    /** Central text tab */
    private javax.swing.JPanel textTab;
    /** Destination station chooser for route planner */
    private javax.swing.JComboBox<Object> toCombobox;
    /** Destination label for route planner */
    private javax.swing.JLabel toLabel;
    /** Route planner panel */
    private javax.swing.JPanel topPanel;
    /** Minimize transfers planner type */
    private javax.swing.JRadioButton transfersRadiobutton;
    // End of variables declaration//GEN-END:variables
}
