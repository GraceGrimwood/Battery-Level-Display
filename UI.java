package battLvl.ui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;

import battLvl.util.BattLvl;
import battLvl.util.Kernel32;

@SuppressWarnings("unused")
public class UI {
	private static Kernel32.POWER_STATUS p = new Kernel32.POWER_STATUS();
	private BattLvl battLvl = new BattLvl(p);
//	private String fullAh = "5225mAh";
//	private String fullVolts = "11.1V";
//	private String fullWh = "62Wh";
//	private String maxTemp = "60C";
//	private String batteryType = "Lithium-ion";
//	private String mfgDate = "07/2014";
//	private String partNum = "HP SPARE 710417-001";
	private String level;
	private boolean charging;
	private Duration currentDur;
	private Duration fullDur;
	private String state;
	private int count;

	private JFrame window;
	private Box headerPanel;
	private Box statusPanel;
	private Box imagePanel;
	private JPanel hidden;
	private JLabel headerLabel;
	private JLabel levelLabel;
	private JLabel imageLabel;
	private JLabel subLabel1;
	private JLabel subLabel2;
	private Font font;

	public UI() {
		font = new Font("Caviar Dreams Bold", Font.TRUETYPE_FONT, 16);
		window = new JFrame("Battery Indicator");
		window.setSize(210, 400);
		window.setAlwaysOnTop(true);
		window.setLayout(new GridLayout(1, 1));
		window.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});

		Icon i = new ImageIcon("battery_animation.gif");
		
		hidden = new JPanel();
		hidden.setBackground(Color.BLACK);
		hidden.setLayout(new BoxLayout(hidden, BoxLayout.Y_AXIS));
		
		headerPanel = new Box(BoxLayout.Y_AXIS);
		headerPanel.setAlignmentY(Box.TOP_ALIGNMENT);
		headerPanel.setBackground(Color.BLACK);
		headerPanel.setSize(new Dimension(210, 100));

		imagePanel = new Box(BoxLayout.Y_AXIS);
		imagePanel.setAlignmentY(Box.CENTER_ALIGNMENT);
		imagePanel.setSize(new Dimension(210, 150));
		imagePanel.setBounds(new Rectangle(210, 150));
		imagePanel.setBackground(Color.BLACK);
		
		statusPanel = new Box(BoxLayout.Y_AXIS);
		statusPanel.setAlignmentY(Box.BOTTOM_ALIGNMENT);
		statusPanel.setBackground(Color.BLACK);
		statusPanel.setSize(new Dimension(210, 75));
		
		headerLabel = new JLabel("", JLabel.CENTER);
		headerLabel.setHorizontalAlignment(JLabel.CENTER);
		headerLabel.setVerticalAlignment(JLabel.TOP);
		headerLabel.setSize(new Dimension(210, 50));
		headerLabel.setFont(font);
		headerLabel.setForeground(Color.WHITE);

		levelLabel = new JLabel("", JLabel.CENTER);
		levelLabel.setHorizontalAlignment(JLabel.CENTER);
		levelLabel.setVerticalAlignment(JLabel.BOTTOM);
		levelLabel.setSize(new Dimension(210, 50));
		levelLabel.setFont(font);
		levelLabel.setForeground(Color.WHITE);

		imageLabel = new JLabel(i);
		imageLabel.setBounds(imagePanel.getBounds());
		imageLabel.setSize(210, 150);

		subLabel1 = new JLabel("", JLabel.CENTER);
		subLabel1.setHorizontalAlignment(JLabel.CENTER);
		subLabel1.setVerticalAlignment(JLabel.TOP);
		subLabel1.setFont(font.deriveFont(12f));
		subLabel1.setForeground(Color.WHITE);

		subLabel2 = new JLabel("", JLabel.CENTER);
		subLabel2.setHorizontalAlignment(JLabel.CENTER);
		subLabel2.setVerticalAlignment(JLabel.BOTTOM);
		subLabel2.setFont(font.deriveFont(12f));
		subLabel2.setForeground(Color.WHITE);

		headerPanel.add(headerLabel);
		headerPanel.add(levelLabel);
		imagePanel.add(imageLabel);
		statusPanel.add(subLabel1);
		statusPanel.add(subLabel2);
		hidden.add(headerLabel);
		hidden.add(imagePanel);
		hidden.add(statusPanel);
		window.add(hidden);
		window.setVisible(true);
	}

	public void updateDisplay() {
		while (true) {
			battLvl.refresh();
			level = battLvl.getLevel();
			charging = battLvl.isCharging();
			currentDur = battLvl.getCurrentDur();
			fullDur = battLvl.getFullDur();
			state = battLvl.getState();
			subLabel1.setText("Battery State:");
			if (!charging) {
				headerLabel.setText("Battery Discharging " + "(" + level + ")");
				headerLabel.setFont(font.deriveFont(14f));
				if (!state.equals("Unknown")) {
					subLabel2.setText(state);
				}
			} else {
				headerLabel.setText("Battery Charging " + "(" + level + ")");
				headerLabel.setFont(font);
				if (!state.equals("Unknown") || !state.equals("Charging")) {
					subLabel2.setText(state);
				}
			}
			levelLabel.setText("(" + level + ")");
			if (currentDur != Duration.ZERO) {
			}
			if (fullDur != Duration.ZERO) {
			}
			try {
				// for testing
				Thread.sleep(100);

				// for actual use
				// Thread.sleep(60000);
			} catch (Throwable t) {
				t.printStackTrace();
			}
			headerLabel.repaint();
			subLabel1.repaint();
			subLabel2.repaint();
			headerLabel.repaint();
			statusPanel.repaint();
		}
	}

	public static void main(String[] args) {
		UI ui = new UI();
		ui.updateDisplay();

	}
}
