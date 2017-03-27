package de.mb.heldensoftware.customentries;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;

public class NewTalentDialog extends JDialog {
	private JPanel contentPane;
	private JButton buttonOK;
	private JButton buttonCancel;
	private JTextField textTalentName;
	private JTextField textTalentAbkuerzung;
	private JComboBox comboProbe1;
	private JComboBox comboProbe2;
	private JComboBox comboProbe3;
	private JComboBox comboKategorie;
	private JComboBox comboArt;
	private JComboBox comboSprachFamilie;
	private JSpinner spinnerSprachKomplex;
	private JTextField textBehinderung;
	private JSpinner spinnerBehinderung;
	private JCheckBox paradeMoeglichCheckBox;
	private JLabel lblProbe;
	private JLabel lblBehinderungStr;
	private JLabel lblBehinderungInt;
	private JLabel lblSprachFamilie;
	private JLabel lblSprachKomplex;
	private JPanel paneFields;
	private JLabel lblTalentname;

	public NewTalentDialog() {
		setContentPane(contentPane);
		setModal(true);
		getRootPane().setDefaultButton(buttonOK);

		buttonOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onOK();
			}
		});

		buttonCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onCancel();
			}
		});

		// call onCancel() when cross is clicked
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				onCancel();
			}
		});

		// call onCancel() on ESCAPE
		contentPane.registerKeyboardAction(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onCancel();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

		initComponents();

		try {
			initModels();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		onTalentArtChanged();
	}

	private void initComponents(){
		spinnerSprachKomplex.setModel(new SpinnerNumberModel(18, 1, 99, 1));
		spinnerBehinderung.setModel(new SpinnerNumberModel(2, 0, 99, 1));
		comboArt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onTalentArtChanged();
			}
		});

		// Force initial size to fit all components
		int col1Width = lblSprachKomplex.getPreferredSize().width + 5;
		lblTalentname.setPreferredSize(new Dimension(col1Width, lblTalentname.getPreferredSize().height));
	}

	private void initModels() throws IllegalAccessException, InvocationTargetException {
		EntryCreator ec = EntryCreator.getInstance();
		// Probe / Eigenschaft
		for (Object o: ec.getAllStaticInstances(ec.eigenschaftType)){
			comboProbe1.addItem(o);
			comboProbe2.addItem(o);
			comboProbe3.addItem(o);
		}
		// Kategorien A-H
		for (Object o: ec.alleKategorien.values()){
			comboKategorie.addItem(o);
		}
		// Arten
		for (Object o: ec.getAllStaticInstances(ec.TalentArtType)){
			boolean b = ((Boolean) ec.talentArtIsPrimitive.invoke(o)).booleanValue();
			if (b) comboArt.addItem(o);
		}
		// Sprachfamilien
		for (String s: ec.getAllStringConstants(ec.SprachFamilieType)){
			comboSprachFamilie.addItem(s);
		}
	}

	private void onTalentArtChanged(){
		// Get information about talent
		String art = comboArt.getSelectedItem().toString();
		boolean isKampf = art.equals("Kampf") || art.equals("Nahkampf") || art.equals("Fernkampf");
		boolean isKoerper = art.equals("Körperlich");
		boolean isSprache = art.equals("Sprachen");
		boolean isSpracheSchrift = isSprache || art.equals("Schriften");

		// Probe
		comboProbe1.setEnabled(!isKampf && !isSpracheSchrift);
		comboProbe2.setEnabled(!isKampf && !isSpracheSchrift);
		comboProbe3.setEnabled(!isKampf && !isSpracheSchrift);
		lblProbe.setEnabled(!isKampf && !isSpracheSchrift);
		if (isKampf){
			comboProbe1.setSelectedItem(EntryCreator.getInstance().alleEigenschaften.get("GE"));
			comboProbe2.setSelectedItem(EntryCreator.getInstance().alleEigenschaften.get(art.equals("Fernkampf") ? "FF" : "GE"));
			comboProbe3.setSelectedItem(EntryCreator.getInstance().alleEigenschaften.get("KK"));
		}else if (isSprache){
			comboProbe1.setSelectedItem(EntryCreator.getInstance().alleEigenschaften.get("KL"));
			comboProbe2.setSelectedItem(EntryCreator.getInstance().alleEigenschaften.get("IN"));
			comboProbe3.setSelectedItem(EntryCreator.getInstance().alleEigenschaften.get("CH"));
		}else if (isSpracheSchrift){
			comboProbe1.setSelectedItem(EntryCreator.getInstance().alleEigenschaften.get("KL"));
			comboProbe2.setSelectedItem(EntryCreator.getInstance().alleEigenschaften.get("KL"));
			comboProbe3.setSelectedItem(EntryCreator.getInstance().alleEigenschaften.get("FF"));
		}

		// Sprachen
		lblSprachFamilie.setVisible(isSprache);
		comboSprachFamilie.setVisible(isSprache);
		lblSprachKomplex.setVisible(isSpracheSchrift);
		spinnerSprachKomplex.setVisible(isSpracheSchrift);

		// Behinderung
		lblBehinderungStr.setVisible(isKoerper);
		textBehinderung.setVisible(isKoerper);
		lblBehinderungInt.setVisible(isKampf);
		spinnerBehinderung.setVisible(isKampf);

		// Parade
		paradeMoeglichCheckBox.setVisible(isKampf);
	}



	private void onOK() {
		// add your code here
		dispose();
	}

	private void onCancel() {
		// add your code here if necessary
		dispose();
	}

	public static void main(String[] args) {
		NewTalentDialog dialog = new NewTalentDialog();
		dialog.pack();
		dialog.setVisible(true);
		System.exit(0);
	}
}
