import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.RowSpec;
import java.awt.FlowLayout;

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
		frmRotorCiphers.setBounds(100, 100, 505, 317);
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

		/* Initialise rotor spinner and machine class to be the same */
		String[] alphabetStr = HebernMachineSimulator.machine.alphabet.toStringArray();
		char c = HebernMachineSimulator.machine.alphabet.getCharFromIndex(0);
		HebernMachineSimulator.machine.setRotorsTo(new char[] {c});
		
		JPanel machineButtonPanel = new JPanel();
		machinePanelFooter.add(machineButtonPanel, BorderLayout.EAST);
				machineButtonPanel.setLayout(new FormLayout(new ColumnSpec[] {
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						ColumnSpec.decode("95px"),},
					new RowSpec[] {
						FormFactory.LINE_GAP_ROWSPEC,
						RowSpec.decode("25px"),}));
				
				JButton btnDecipher = new JButton("Decipher");
				machineButtonPanel.add(btnDecipher, "2, 2");
		
				JButton btnMachineEncipher = new JButton("Encipher");
				machineButtonPanel.add(btnMachineEncipher, "4, 2, left, top");
				
				JPanel panel = new JPanel();
				machinePanelFooter.add(panel, BorderLayout.WEST);
				
				JLabel lblRotorPosition = new JLabel("Rotor position:");
				panel.add(lblRotorPosition);
				
				final JSpinner spnMachineRotor = new JSpinner();
				panel.add(spnMachineRotor);
				spnMachineRotor.setModel(new RolloverSpinnerListModel(alphabetStr));
				
				JPanel machinePanelHeader = new JPanel();
				FlowLayout flowLayout = (FlowLayout) machinePanelHeader.getLayout();
				flowLayout.setAlignment(FlowLayout.LEFT);
				machinePanel.add(machinePanelHeader, BorderLayout.NORTH);
				
				JLabel lblPlainOrCiphertext = new JLabel("Enter plaintext or ciphertext:");
				machinePanelHeader.add(lblPlainOrCiphertext);
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
				
				btnDecipher.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						String key = spnMachineRotor.getValue().toString();
						HebernMachineSimulator.machine.setRotorsTo(key.toCharArray());

						char[] text = txtMachineInput.getText().toUpperCase().toCharArray();
						text = HebernMachineSimulator.machine.decipher(text);
						txtMachineInput.setText(new String(text).toLowerCase());
					}
				});
				
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

