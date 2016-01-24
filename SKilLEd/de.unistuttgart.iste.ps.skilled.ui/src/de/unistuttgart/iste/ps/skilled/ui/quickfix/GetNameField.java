package de.unistuttgart.iste.ps.skilled.ui.quickfix;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class GetNameField implements Runnable{
	private String newName= "";
	private String oldName = "";
	private setName name;
	
	public void run(){
		open();
	}
	public void open(){
		System.out.println("running");
		newName = "";
		Display d = Display.getDefault();
		System.out.println("display...");
		final Shell shell = new Shell(d);
		shell.setText("Extract Type or Interface");
        shell.setLayout(new GridLayout(2, true));	
        final Text text1 = new Text(shell, SWT.NONE);
        text1.setText(oldName);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.CENTER;
		gridData.horizontalSpan = 2;System.out.println("1");		
		text1.setLayoutData(gridData);
		GridData gridDataButton = new GridData();
    	gridDataButton.horizontalAlignment = SWT.CENTER;
   		gridDataButton.horizontalSpan = 2;
   		Button continueButton = new Button(shell, SWT.NONE);		
    	continueButton.setText("OK");
    	continueButton.setLayoutData(gridDataButton);
    	continueButton.addListener(SWT.Selection, new Listener() {
          public void handleEvent(Event e) {
            switch (e.type) {
            case SWT.Selection:
             	newName = text1.getText();		
             	if(!newName.equals("")){
             		name.setName(newName);
                 	shell.close();             		
             	}
            }
          }
        });
		Point newSize = shell.computeSize(150, 150, true);
		shell.setSize(newSize);
		shell.open();
	}
	public void setOldName(String s){
		oldName = s;
	}
	
	public String getName(){
		return newName;
	}
	
	public GetNameField(setName name){
		this.name = name;
	}
	
}
