//Thomas Vermeersch
//Java Swing Text Viewer

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.text.*;

public class JTextViewer extends JFrame{
	private static Boolean cmdline = false;
	public static File cmdfile;
	private JTextArea area = new JTextArea(100, 100);
	JTextViewer frame;

	public JTextViewer(){

		//Default font style
		area.setFont(new Font("Courier", Font.PLAIN, 14));
		//Font chooser
		//Scroll pane for our text
		JScrollPane textscroll = new JScrollPane(area, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		add(textscroll, BorderLayout.CENTER);

		/* Menu Bar */
		//initialize menu bar
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		//menu bar menus and their items
		/*file menu*/
		JMenu file = new JMenu("File");
		JMenuItem open = new JMenuItem("Open");
		open.setAccelerator(KeyStroke.getKeyStroke('O', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent event) {
            	System.exit(0);
        	}
        });
		/*format menu*/
		JMenu format = new JMenu("Format");
		JMenu colorSch = new JMenu("Color Scheme");
		JMenuItem fontMenu = new JMenuItem("Font");

		JMenuItem bwitem = new JMenuItem("Black/White");
		bwitem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent event) {
            	setColor("bw");
        	}
        });
		JMenuItem wbitem = new JMenuItem("White/Blue");
		wbitem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent event) {
            	setColor("wb");
        	}
        });

        //Help Menu
		JMenu help = new JMenu("Help");
		JMenuItem about = new JMenuItem("About JTextViewer");
		//add items to menus
		/*file items*/
		file.add(open);
		file.add(exit);
		/*format items*/
		//color scheme items
		colorSch.add(bwitem);
		colorSch.add(wbitem);
		format.add(colorSch);
		format.add(fontMenu);
		//
		help.add(about);
		//add menus to menu bar
		menuBar.add(file);
		menuBar.add(format);
		menuBar.add(help);

		//help actionlistener
		if(cmdline == true){
			System.out.println("CMM: " + cmdfile);
			populateArea(cmdfile);
		}
		

		//file reader action listener
		open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae){
				File readFile = getFileName();
				System.out.println(readFile);
				populateArea(readFile);
			}
		});

		//file reader action listener
		fontMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae){
				String fontName = JFontChooser.showDialog(frame);
                area.setFont(new Font(fontName, Font.PLAIN, 14));
			}
		});

		//about action listener

		about.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae){
				JOptionPane.showMessageDialog(null, "JTextViewer\nVersion 0.1\n(c) Thomas Vermeersch 2017");
			}
		});
		
	}

	//Color of text area
	public void setColor(String co){
		String textColor = "";
		String bgColor = "";
		switch (co) {
			case "bw":
				area.setBackground(Color.WHITE);
				area.setForeground(Color.BLACK);
				break;
			case "wb":
				area.setBackground(Color.BLUE);
				area.setForeground(Color.WHITE);
				break;
		}

	}
	//populate text area

	public void populateArea(File readFile){
		System.out.println(readFile);

		area.setText("");
		try{
			BufferedReader in = new BufferedReader(new FileReader(readFile));
			String line = in.readLine();
			while(line != null){
				area.append(line + "\n");
				line = in.readLine();
			}
		} catch(Exception e) {
			System.out.println("File not found");
		}
	}

	public File getFileName(){
		JFileChooser chooser = new JFileChooser();
		int returnVal = chooser.showOpenDialog(null);
		File readFile = null;
		if(returnVal == JFileChooser.APPROVE_OPTION){
			readFile = chooser.getSelectedFile();
		}
		return readFile;
	}

	private static void createAndShowGUI(){
	
		JTextViewer frame = new JTextViewer();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Display Frame
		frame.pack();
		frame.setVisible(true);

	}

	public static void main(String[] args){
		if(args.length != 0){
			cmdline = true;
			try{
				cmdfile = new File(args[0]);
				System.out.println("CMD:" + cmdfile);


			} catch (Exception e){
				System.out.println("Not a file");
			}

		}
		javax.swing.SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				createAndShowGUI();
			}
		});
	}
}

class JFontChooser {
    
    static String fontSel;
    
    static String showDialog(JFrame parent) {
        // Create a simple modal dialog. 
        JDialog jdlg = new JDialog(parent, "Font", true);
        jdlg.setSize(250, 240);
        jdlg.setLayout(new FlowLayout());

        // Create buttons used by the dialog. 
        JList fontList = new JList<String>(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        fontList.setSelectedIndex(0);
        JButton ok = new JButton("Ok");
        JButton cancel = new JButton("Cancel");

        JScrollPane scroll = new JScrollPane(fontList);
        scroll.setPreferredSize(new Dimension(180, 120));
        jdlg.add(scroll);
        jdlg.add(ok);
        
        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent le) {
                fontSel = (String)fontList.getSelectedValue();
                jdlg.dispose();
            }
        });
        
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent le) {
                jdlg.dispose();
            }
        });
        
        jdlg.add(cancel);
        jdlg.setLocationRelativeTo(parent);
        
        fontSel = null;
        
        jdlg.setVisible(true);
        
        return fontSel;
    }
}



