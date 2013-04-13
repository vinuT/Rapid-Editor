//THE IMPORTED LIBRARIES
import javax.swing.*;
import javax.swing.undo.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.*;
import javax.swing.text.*;
import java.text.*;


public class Editor extends JFrame implements ActionListener
{
	public static Editor e;
	
	//DECLARATION OF ALL THE VARIABLES USED IN THIS APPLICATION
	
	JTextArea text = new JTextArea(0,0);
	JScrollPane scroll = new JScrollPane(text);

	JMenuBar mb=new JMenuBar();
	
	JMenu FILE = new JMenu("File");
	JMenu EDIT = new JMenu("Edit");
	JMenu SEARCH = new JMenu("Search");
	JMenu HELP = new JMenu("Help");

	JMenuItem NEWFILE = new JMenuItem("New");
	JMenuItem OPENFILE = new JMenuItem("Open...");
	JMenuItem SAVEFILE = new JMenuItem("Save");
	JMenuItem SAVEASFILE = new JMenuItem("Save As...");
	JMenuItem EXITFILE = new JMenuItem("Exit");


	JMenuItem CUTEDIT = new JMenuItem("Cut");	
	JMenuItem COPYEDIT = new JMenuItem("Copy");
	JMenuItem PASTEDIT = new JMenuItem("Paste");
	JMenuItem DELETEDIT = new JMenuItem("Delete");

	JMenuItem FINDSEARCH = new JMenuItem("Find");
	JMenuItem FINDNEXTSEARCH = new JMenuItem("Find Next");
	JMenuItem ABOUTHELP = new JMenuItem("About");

	JPopupMenu POPUP = new JPopupMenu();
	JMenuItem CUTPOPUP = new JMenuItem("Cut");
	JMenuItem COPYPOPUP = new JMenuItem("Copy");
	JMenuItem PASTEPOPUP = new JMenuItem("Paste");
	JMenuItem DELETEPOPUP = new JMenuItem("Delete");
	

	UndoManager undo = new UndoManager();
	UndoAction undoAction = new UndoAction();
	
	boolean opened = false;
	String wholeText,findString,filename = null;
	int ind = 0;
	
	//CLASS FOR HANDLING UNDO MENU OPTION OF EDIT AND POPUP MENU
	class UndoAction extends AbstractAction 
	{
		public UndoAction() 
		{
	    		super("Undo");
		}

		public void actionPerformed(ActionEvent e) 	
		{
	    		try 
	    		{
				undo.undo();
	    		} 
	    		catch (CannotUndoException ex) 
	    		{
				System.out.println("Unable to undo: " + ex);
				ex.printStackTrace();
	    		}
	    		update();
		}

		protected void update() 
		{
	    		if(undo.canUndo()) 
	    		{
				setEnabled(true);
				putValue("Undo", undo.getUndoPresentationName());
	    		}
	    		else 
	    		{
				setEnabled(false);
				putValue(Action.NAME, "Undo");
	    		}
		}
	}	
	
	//DEFAULT CONSTRUCTOR OF THE Editor CLASS		
	public Editor()
	{
		//SETING DEFAULT TITLE OF THE FRAME
		setTitle("Untitled");	
		
		//SETTING DEFAULT SIZE OF THE FRAME	
		setSize(600,400);
		
		//MAKING THE FRAME VISIBLE
		setVisible(true);						
		
		//SETTING THE LAYOUT OF THE FRAME
		getContentPane().setLayout(new BorderLayout());
		
		//ADDS THE SCROLLPANE CONTAINING THE TEXTAREA TO THE CONTAINER
		getContentPane().add(scroll, BorderLayout.CENTER);
		
		//ADDING THE MAIN MENUBAR TO THE FRAME
		setJMenuBar(mb);
		
		//ADDING MENUS TO THE MAIN MENUBAR 
		mb.add(FILE);
		mb.add(EDIT);
		mb.add(SEARCH);
		mb.add(HELP);	
		
		//ADDING MENUITEMS TO THE FILE MENU 
		FILE.add(NEWFILE);
		FILE.add(OPENFILE);
		FILE.add(SAVEFILE);
		FILE.add(SAVEASFILE);
		FILE.addSeparator();
		FILE.add(EXITFILE);
	
		//ADDING MENUITEMS TO THE EDIT MENU
		EDIT.add(undoAction);
		EDIT.add(CUTEDIT);
		EDIT.add(COPYEDIT);
		EDIT.add(PASTEDIT);
		EDIT.add(DELETEDIT);
		
		//ADDING MENUITEMS TO THE SEARCH MENU
		SEARCH.add(FINDSEARCH);
		SEARCH.add(FINDNEXTSEARCH);

		//ADDING MENUITEM TO THE HELP MENU
		HELP.add(ABOUTHELP);
				
		//ADDING MENUITEMS TO THE POPUPMENU
		POPUP.add(undoAction);
		POPUP.addSeparator();
		POPUP.add(CUTPOPUP);
		POPUP.add(COPYPOPUP);
		POPUP.add(PASTEPOPUP);
		POPUP.add(DELETEPOPUP);
		
		//SETTING SHORTCUT KEYS OF MENUS IN THE MAIN MENUBAR
		FILE.setMnemonic(KeyEvent.VK_F);
		EDIT.setMnemonic(KeyEvent.VK_E);
		HELP.setMnemonic(KeyEvent.VK_H);
	
		//SETTING SHORTCUT KEYS OF MENUITEMS IN THE FILE MENU
		NEWFILE.setMnemonic(KeyEvent.VK_N);
		OPENFILE.setMnemonic(KeyEvent.VK_O);
		SAVEFILE.setMnemonic(KeyEvent.VK_S);
		SAVEASFILE.setMnemonic(KeyEvent.VK_A);	
		EXITFILE.setMnemonic(KeyEvent.VK_X);	

		//SETTING SHORTCUT KEYS OF MENUITEMS IN THE EDIT MENU
		CUTEDIT.setMnemonic(KeyEvent.VK_T);
		COPYEDIT.setMnemonic(KeyEvent.VK_C);
		PASTEDIT.setMnemonic(KeyEvent.VK_P);
		DELETEDIT.setMnemonic(KeyEvent.VK_L);

		//SETTING SHORTCUT KEYS OF MENUITEMS IN THE SEARCH MENU
		FINDSEARCH.setMnemonic(KeyEvent.VK_F);
		FINDNEXTSEARCH.setMnemonic(KeyEvent.VK_N);

		//SETTING SHORTCUT KEYS OF MENUITEM IN THE HELP MENU
		ABOUTHELP.setMnemonic(KeyEvent.VK_A);
		
		//SETTING SHORTCUT KEYS OF MENUITEMS IN THE POPUPMENU
		CUTPOPUP.setMnemonic(KeyEvent.VK_T);
		COPYPOPUP.setMnemonic(KeyEvent.VK_C);
		PASTEPOPUP.setMnemonic(KeyEvent.VK_P);
		DELETEPOPUP.setMnemonic(KeyEvent.VK_D);

		//SETTING ACCELERATOR KEYS OF SOME MENUITEMS IN THE EDIT MENU
		CUTEDIT.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,ActionEvent.CTRL_MASK));
		COPYEDIT.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,ActionEvent.CTRL_MASK));
		PASTEDIT.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,ActionEvent.CTRL_MASK));

		//ADDING LISTENERS TO THE MENUITEMS IN FILE MENU 
		NEWFILE.addActionListener(this);
		OPENFILE.addActionListener(this);
		SAVEFILE.addActionListener(this);
		SAVEASFILE.addActionListener(this);
		EXITFILE.addActionListener(this);
	
		//ADDING LISTENERS TO THE MENUITEMS IN EDIT MENU
		text.getDocument().addUndoableEditListener(new UndoListener());
		CUTEDIT.addActionListener(this);
		COPYEDIT.addActionListener(this);
		PASTEDIT.addActionListener(this);
		DELETEDIT.addActionListener(this);

		//ADDING LISTENERS TO THE MENUITEMS IN SEARCH MENU
		FINDSEARCH.addActionListener(this);
		FINDNEXTSEARCH.addActionListener(this);

		//ADDING LISTENERS TO THE MENUITEM IN HELP MENU
		ABOUTHELP.addActionListener(this);
		
		//ADDING LISTENERS TO THE MENUITEMS IN POPUPMENU
		CUTPOPUP.addActionListener(this);
		COPYPOPUP.addActionListener(this);
		PASTEPOPUP.addActionListener(this);
		DELETEPOPUP.addActionListener(this);
		
		//ADDING MOUSELISTENER TO RIGHT CLICK FOR THE POPUPLISTENER
		text.addMouseListener(new MouseAdapter()
		{
   		   public void mousePressed(MouseEvent e) 
   		   {
	   		   if (e.isPopupTrigger()) 
	   		   {
            			POPUP.show(e.getComponent(),e.getX(), e.getY());
           	   	   }
    	    	
    		   }
		    public void mouseReleased(MouseEvent e) 
		    {
				if (e.isPopupTrigger()) 
				{
            				POPUP.show(e.getComponent(),e.getX(), e.getY());
        			}
    		    }
		});
			
		//ADDING WINDOWLISTENER TO HANDLE CLOSE WINDOW EVENT
		
	/*	addWindowListener( new WindowAdapter() 
		{ public void windowClosing(WindowEvent evt) 
		  { 
			  int response = JOptionPane.showConfirmDialog(null, "Do you really want to quit?"); 
			  System.out.println("Inside Window Listener");
			  switch (response)
			  {
				case 0:
					{
					dispose();
					break; }
				
			  	case 2: 
			  	{
				  	//Editor x = new Editor();
				  	System.out.println("Inside 2");
				  	e=new Editor();
				  	e.setVisible(true);
			  		break;} 			    
		 	   } 
			  System.out.println("response is :="+response);
		  } 
		} ); */

		
		
		
		addWindowListener(new WindowAdapter()
		{ 
			public void windowClosing(WindowEvent e)
			{
				exitApln();
			}
		});	
	}

	//HANDLING ALL EVENTS OF THE TEXT Editor
	public void actionPerformed(ActionEvent e)
	{
	
		//ACTION FOR NEW MENU OPTION OF FILE MENU
		if (e.getSource()==NEWFILE)
		{
			newfile();
		}

		//ACTION FOR OPEN MENU OPTION OF FILE MENU
		if (e.getSource()==OPENFILE)
		{
			open();
		}
	
		//ACTION FOR SAVE MENU OPTION OF FILE MENU
		if (e.getSource()==SAVEFILE)
		{
			save();
		}
		
		//ACTION FOR SAVEAS MENU OPTION OF FILE MENU
		if (e.getSource()==SAVEASFILE)
		{
			opened=false;
			save();		
		}
		
		//ACTION FOR EXIT MENU OPTION OF FILE MENU
		if (e.getSource()==EXITFILE)
		{
			exitApln();		
		}
		
		//ACTION FOR CUT MENU OPTION OF EDIT MENU AND POPUPMENU	
		if ((e.getSource()==CUTEDIT)||(e.getSource()==CUTPOPUP))
		{
			text.cut();
		}
	
		//ACTION FOR COPY MENU OPTION OF EDIT MENU AND POPUPMENU
		if ((e.getSource()==COPYEDIT)||(e.getSource()==COPYPOPUP))
		{
			text.copy();
		}
	
		//ACTION FOR PASTE MENU OPTION OF EDIT MENU AND POPUPMENU
		if ((e.getSource()==PASTEDIT)||(e.getSource()==PASTEPOPUP))
		{
			text.paste();
		}
		
		//ACTION FOR DELETE MENU OPTION OF EDIT MENU AND POPUPMENU
		if ((e.getSource()==DELETEDIT)||(e.getSource()==DELETEPOPUP))
		{
			text.replaceSelection(null);
		}
		
		//ACTION FOR FIND MENU OPTION OF SEARCH MENU
		if (e.getSource()==FINDSEARCH)
		{
			wholeText=text.getText();
			findString =JOptionPane.showInputDialog(null, "Find What", "Find",
JOptionPane.INFORMATION_MESSAGE);

			ind = wholeText.indexOf(findString,0);
			text.setCaretPosition(ind);
			text.setSelectionStart(ind);
			text.setSelectionEnd(ind+findString.length());
		}
		
		//ACTION FOR FIND NEXT MENU OPTION OF SEARCH MENU
		if (e.getSource()==FINDNEXTSEARCH)
		{
			wholeText= text.getText();
			findString = JOptionPane.showInputDialog(null, "Find What","Find Next",
JOptionPane.INFORMATION_MESSAGE);
			ind = wholeText.indexOf(findString, text.getCaretPosition());
			text.setCaretPosition(ind);
			text.setSelectionStart(ind);
			text.setSelectionEnd(ind+findString.length());
		}
	
		//ACTION FOR ABOUT MENU OPTION OF HELP MENU
		if (e.getSource()==ABOUTHELP)
		{
			JOptionPane.showMessageDialog(null, "This is a simple Text Editor application built using Java.",
			"About Editor",
			JOptionPane.INFORMATION_MESSAGE);	
	 	}
	}	

	//ACTION FOR NEW MENU OPTION OF FILE MENU
	public void newfile()
	{
		if(!text.getText().equals(""))
		{
			opened=false;
			int confirm = JOptionPane.showConfirmDialog(null,
			"Text in the Untitled file has changed. \n Do you want to save the changes?",
			"New File",
			JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.INFORMATION_MESSAGE);
	
			if( confirm == JOptionPane.YES_OPTION )
			{
				save();
				text.setText(null);
			}
			else if( confirm == JOptionPane.NO_OPTION )
			{
				text.setText(null);
			}
		}
	}
	
	//ACTION FOR OPEN MENU OPTION OF FILE MENU
	public void open()
	{
		text.setText(null);
		JFileChooser ch = new JFileChooser();
	    	ch.setCurrentDirectory(new File("."));
		ch.setFileFilter(new javax.swing.filechooser.FileFilter()
       		{
           		public boolean accept(File f)
        		{
              			return f.isDirectory()
               			|| f.getName().toLowerCase().endsWith(".java");
        		}
			public String getDescription()
        		{
     		           return "Java files";
           		}
         	});
		int result = ch.showOpenDialog(new JPanel());
		if(result == JFileChooser.APPROVE_OPTION) 
		{
			filename = String.valueOf(ch.getSelectedFile());
			setTitle(filename);
			opened=true;
			FileReader fr;
			BufferedReader br;
			
			try
			{
				fr=new FileReader (filename);
				br=new BufferedReader(fr);
				String s;
				while((s=br.readLine())!=null)
				{
					text.append(s);
					text.append("\n");
				}
				fr.close();
			}
			catch(FileNotFoundException ex)
                           {
				JOptionPane.showMessageDialog(this, "Requested file not found", "Error Dialog box", JOptionPane.ERROR_MESSAGE);}
                           
                        catch(Exception ex)
                           {System.out.println(ex);}
		}
	}

	//ACTION FOR SAVE MENU OPTION OF FILE MENU
	public void save()
	{
		if(opened==true)
		{
			try
			{
				FileWriter f1 = new FileWriter(filename);
				f1.write(text.getText());
				f1.close();
				opened = true;
			}
			catch(FileNotFoundException ex)
                           {
				JOptionPane.showMessageDialog(this, "Requested file not found", "Error Dialog box", JOptionPane.ERROR_MESSAGE);}
			catch(IOException ioe){ioe.printStackTrace();}
		}
		else
		{
			JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(new File("."));
			int result = fc.showSaveDialog(new JPanel());
					
			if(result == JFileChooser.APPROVE_OPTION) 
			{
				filename = String.valueOf(fc.getSelectedFile());
				setTitle(filename);
				try
				{
				FileWriter f1 = new FileWriter(filename);
				f1.write(text.getText());
				f1.close();
				opened = true;
				}
			catch(FileNotFoundException ex)
                           {
				JOptionPane.showMessageDialog(this, "Requested file not found", "Error Dialog box", JOptionPane.ERROR_MESSAGE);}

				catch(IOException ioe){ioe.printStackTrace();}
			}
			
		}
	}

	//ACTION FOR EXIT MENU OPTION OF FILE MENU AND CLOSE WINDOW BUTTON
	public void exitApln()
	{
		if(!text.getText().equals(""))
		{
			int confirm = JOptionPane.showConfirmDialog(null,
			"Text in the file has changed. \n Do you want to save the changes?",
			"Exit",
			JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.INFORMATION_MESSAGE);
	
			if( confirm == JOptionPane.YES_OPTION )
			{
				save();
				dispose();
				System.exit(0);
			}
			else if( confirm == JOptionPane.CANCEL_OPTION )
			{
				e=new Editor();
				String s= text.getText();
				e.setVisible(true);
				e.text.setText(s);
			}
			else if( confirm == JOptionPane.NO_OPTION )
			{
				dispose();
				System.exit(0);
			}
		}
		else
		{
			System.exit(0);
		}
    	}
	
    	//CLASS FOR UNDOLISTENER
	class UndoListener implements UndoableEditListener
	{
   		public void undoableEditHappened(UndoableEditEvent e) 
   		{
    		undo.addEdit(e.getEdit());
       		undoAction.update();
    		}	  
	}  
	
	//MAIN FUNCTION OF Editor CLASS
	public static void main(String args[])
	{
		e= new Editor();
	}
}//END OF Editor CLASS
