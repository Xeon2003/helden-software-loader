package de.mb.heldensoftware.customentries;

import de.mb.heldensoftware.customentries.CustomEntryLoader;
import helden.plugin.HeldenDatenPlugin;
import helden.plugin.HeldenPlugin;
import helden.plugin.datenplugin.DatenPluginHeldenWerkzeug;
import helden.plugin.werteplugin2.PluginHeld2;

import javax.swing.*;

/**
 * Created by Markus on 19.03.2017.
 */
public class CustomEntryLoaderPlugin implements HeldenDatenPlugin {

	private static final ImageIcon PLUGINICON = new ImageIcon();
	private static final String    VERSION    = "1.0";

	public CustomEntryLoaderPlugin(){
		EntryCreator.getInstance();
	}

	@Override
	public int compareVersion(String s) {
		return String.CASE_INSENSITIVE_ORDER.compare(getVersion(), s);
	}

	@Override
	public void doWork(JFrame jFrame) {
		System.out.println("doWork");
	}

	@Override
	public ImageIcon getIcon() {
		return PLUGINICON;
	}

	@Override
	public String getMenuName() {
		return "Eigenes Talent hinzufügen...";
	}

	@Override
	public String getToolTipText() {
		return "Ein neues, inoffizielles Talent erstellen und dem aktuellen Helden hinzufügen";
	}

	@Override
	public String getType() {
		return HELDENDATEN;
	}

	@Override
	public String getVersion() {
		return VERSION;
	}

	@Override
	public void doWork(JFrame jFrame, PluginHeld2[] pluginHeld2s, DatenPluginHeldenWerkzeug datenPluginHeldenWerkzeug) {
		final Object held = datenPluginHeldenWerkzeug.getSelectesHeld().getHeldObject();

		NewTalentDialog dialog = new NewTalentDialog(jFrame);
		dialog.setNewTalentCallback(new NewTalentDialog.TalentCallback() {
			@Override
			public void talentCreated(Object talent, int value) {
				EntryCreator.getInstance().addTalentToHeld(held, talent, value);
			}
		});
		dialog.pack();
		dialog.setVisible(true);
	}

	@Override
	public void initTab(DatenPluginHeldenWerkzeug datenPluginHeldenWerkzeug) {

	}
}
