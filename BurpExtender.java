package burp;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;

public class BurpExtender implements IBurpExtender, IContextMenuFactory
{
	private IExtensionHelpers helpers;
	
    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks)
    {
        // your extension code here
    	callbacks.registerContextMenuFactory(this);
    	this.helpers = callbacks.getHelpers();
    }
    
    public List<JMenuItem> createMenuItems(final IContextMenuInvocation invocation)
    {
    	List<JMenuItem> menuList = new ArrayList<JMenuItem>();
    	JMenuItem menuItem = new JMenuItem("getParameters");
    	menuItem.addActionListener(new ActionListener()
    	{
    		public void actionPerformed(ActionEvent e)
    		{
    			IHttpRequestResponse[] requestResponseArray = invocation.getSelectedMessages();
    			for(IHttpRequestResponse requestResponse:requestResponseArray)
    			{
    				IRequestInfo requestInfo =  helpers.analyzeRequest(requestResponse);
    				String parameters = null;
    				
    				for(int i = 0; i < requestInfo.getParameters().size(); i++)
    				{
    					if(requestInfo.getParameters().get(i).getType() != 2)
    					{
    						if(parameters == null)
    						{
    							parameters = requestInfo.getParameters().get(i).getName() + "\t-\t" + requestInfo.getParameters().get(i).getValue();
    						}
    						else
    						{
    							parameters = parameters + "\n" +requestInfo.getParameters().get(i).getName() + "\t-\t" + requestInfo.getParameters().get(i).getValue();
    						}
    					}
    				}
    				Toolkit toolkit = Toolkit.getDefaultToolkit();
    				Clipboard clipboard = toolkit.getSystemClipboard();
    				StringSelection stringselection = new StringSelection(parameters);
    				clipboard.setContents(stringselection, null);
    			}
    		}
    	});
    	menuList.add(menuItem);
    	return menuList;
    }
}