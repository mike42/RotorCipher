import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JButton;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Graphical front-end for HebernRotorMachine class.
 * 
 * @author	Michael Billington <michael.billington@gmail.com>
 * @since	2012-09-10
 *
 */
public class HebernMachineGUI {

	private JFrame frmRotorCiphers;

	/**
	 * Create the application.
	 */
	public HebernMachineGUI() {
		initialise();
		frmRotorCiphers.setVisible(true);
	}

	/**
	 * Initialise the contents of the frame.
	 */
	private void initialise() {
		frmRotorCiphers = new JFrame();
		frmRotorCiphers.setTitle("Rotor Ciphers");
		frmRotorCiphers.setBounds(100, 100, 403, 317);
		frmRotorCiphers.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmRotorCiphers.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		frmRotorCiphers.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		JPanel machinePanel = new JPanel();
		tabbedPane.addTab("Machine", null, machinePanel, null);
		machinePanel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane machineScrollPane = new JScrollPane();
		machinePanel.add(machineScrollPane, BorderLayout.CENTER);
		
		final JTextArea txtMachineInput = new JTextArea();
		machineScrollPane.setViewportView(txtMachineInput);
		
		JPanel machinePanelFooter = new JPanel();
		machinePanel.add(machinePanelFooter, BorderLayout.SOUTH);
		machinePanelFooter.setLayout(new BorderLayout(0, 0));
		
		final JSpinner spnMachineRotor = new JSpinner();
		machinePanelFooter.add(spnMachineRotor, BorderLayout.CENTER);

		/* Initialise rotor spinner and machine class to be the same */
		String[] alphabetStr = HebernMachineSimulator.machine.alphabet.toStringArray();
		char c = HebernMachineSimulator.machine.alphabet.getCharFromIndex(0);
		HebernMachineSimulator.machine.setRotorsTo(new char[] {c});
		spnMachineRotor.setModel(new RolloverSpinnerListModel(alphabetStr));

		JButton btnMachineEncipher = new JButton("Encipher");
		btnMachineEncipher.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/* Set machine rotor to the setting in the interface */
				String key = spnMachineRotor.getValue().toString();
				HebernMachineSimulator.machine.setRotorsTo(key.toCharArray());

				char[] text = txtMachineInput.getText().toUpperCase().toCharArray();
				text = HebernMachineSimulator.machine.encipher(text);
				txtMachineInput.setText(new String(text));
			}
		});
		machinePanelFooter.add(btnMachineEncipher, BorderLayout.EAST);
		
		JLabel lblRotorPosition = new JLabel("Rotor position:");
		machinePanelFooter.add(lblRotorPosition, BorderLayout.WEST);
		
		JLabel lblMachineInput = new JLabel("Enter plain text or ciphertext:");
		machinePanel.add(lblMachineInput, BorderLayout.NORTH);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		machinePanel.add(horizontalStrut, BorderLayout.WEST);
		
		JPanel rotorsPanel = new JPanel();
		tabbedPane.addTab("Rotors", null, rotorsPanel, null);
		
		JPanel toolsPanel = new JPanel();
		tabbedPane.addTab("Tools", null, toolsPanel, null);
		
		JPanel formHeader = new JPanel();
		frmRotorCiphers.getContentPane().add(formHeader, BorderLayout.NORTH);
		
		JLabel lblHebernRotorMachine = new JLabel("Hebern Rotor Machine");
		lblHebernRotorMachine.setHorizontalAlignment(SwingConstants.CENTER);
		formHeader.add(lblHebernRotorMachine);
	}
}

