package homework1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * A JDailog GUI for choosing a GeoSegemnt and adding it to the route shown
 * by RoutDirectionGUI.
 * <p>
 * A figure showing this GUI can be found in homework assignment #1.
 */
public class GeoSegmentsDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	// the RouteDirectionsGUI that this JDialog was opened from
	private RouteFormatterGUI parent;
	
	// a control contained in this 
	private JList<GeoSegment> lstSegments;
	
	/**
	 * Creates a new GeoSegmentsDialog JDialog.
	 * @effects Creates a new GeoSegmentsDialog JDialog with owner-frame
	 * 			owner and parent pnlParent
	 */
	public GeoSegmentsDialog(Frame owner, RouteFormatterGUI pnlParent) {
		// create a modal JDialog with the an owner Frame (a modal window
		// in one that doesn't allow other windows to be active at the
		// same time).
		super(owner, "Please choose a GeoSegment", true);
		
		this.parent = pnlParent;

		// Layout
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;

		// Label
		JLabel lbl = new JLabel("Select a GeoSegment:");
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.insets = new Insets(10, 10, 5, 10);
		this.add(lbl, c);

		// List of segments
		DefaultListModel<GeoSegment> model = new DefaultListModel<>();
		for (GeoSegment seg: ExampleGeoSegments.segments) {
			model.addElement(seg);
		}

		lstSegments = new JList<>(model);
		JScrollPane scrollPane = new JScrollPane(lstSegments);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.insets = new Insets(0, 10, 10, 10);
		this.add(scrollPane, c);

		// Add button
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GeoSegment selected = lstSegments.getSelectedValue();
				if (selected != null) {
					try { 
						parent.addSegment(selected);
					} catch(IllegalArgumentException ex) {
						// new segment doesnt start when the last one ends
					}
				}
				setVisible(false);
			}
		});

		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		c.weightx = 0.5;
		c.weighty = 0.0;
		c.insets = new Insets(5, 10, 10, 5);
		this.add(btnAdd, c);

		// Cancel button
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(e -> setVisible(false));

		c.gridx = 1;
		c.gridy = 2;
		c.insets = new Insets(5, 5, 10, 10);
		this.add(btnCancel, c);

		// Final setup
		this.setPreferredSize(new Dimension(400, 250));
		this.pack();
	}
}
