/**
 * GnucashAccountsTreeModel.java
 * Created on 15.05.2005
 * (c) 2005 by "Wolschon Softwaredesign und Beratung".
 *
 *
 * -----------------------------------------------------------
 * major Changes:
 *  15.05.2005 - initial version
 * ...
 *
 */
package biz.wolschon.finance.jgnucash.swingModels;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import biz.wolschon.fileformats.gnucash.GnucashAccount;
import biz.wolschon.fileformats.gnucash.GnucashFile;

/**
 * created: 15.05.2005 <br/>
 *
 * A TreeModel representing the accounts in a Gnucash-File
 * @author <a href="mailto:Marcus@Wolschon.biz">Marcus Wolschon</a>
 *
 */
public class GnucashAccountsTreeModel implements TreeModel {

    /**
     * @param file where we get our data from
     */
    public GnucashAccountsTreeModel(final GnucashFile file) {
        super();
        setFile(file);
    }

    private GnucashAccountTreeRootEntry rootEntry;

    public static class GnucashAccountTreeRootEntry extends GnucashAccountTreeEntry{
    	
    	/**
    	 * where we get our data from
    	 */
        private GnucashFile file;
        
        /**
         * @param file where we get our data from
         */
        public GnucashAccountTreeRootEntry(final GnucashFile file) {
            super(null);
            this.file = file;
        }
        public GnucashFile getFile() {
            return file;
        }

        public String toString() {
            return "";
        }

        public Collection getChildAccounts() {
            return file.getRootAccounts();
        }
    }

    public static class GnucashAccountTreeEntry {
    	
        private GnucashAccount account;
        /**
         * @param account
         */
        public GnucashAccountTreeEntry(final GnucashAccount account) {
            super();
            this.account = account;
        }
        public GnucashAccount getAccount() {
            return account;
        }
        public String toString() {
        	String hidden = getAccount().getUserDefinedAttribute("hidden");
        	if (hidden != null && hidden.equalsIgnoreCase("true"))
        		return "[hidden]" + getAccount().getName();
            return getAccount().getName();
        }

        private volatile List childTreeNodes = null;

        public List getChildTreeNodes() {

            if (childTreeNodes == null) {
                Collection c = getChildAccounts();
                childTreeNodes = new ArrayList(c.size());
                for (Iterator iter = c.iterator(); iter.hasNext();) {
                    GnucashAccount subaccount = (GnucashAccount) iter.next();
                    childTreeNodes.add(new GnucashAccountTreeEntry(subaccount));
                }
            }

            return childTreeNodes;
        }

        public Collection getChildAccounts() {
            return account.getChildren();
        }
    }

    /**
     *
     * @see javax.swing.tree.TreeModel#getRoot()
     */
    public Object getRoot() {
        return rootEntry;
    }

    /**
     *
     * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
     */
    public int getChildCount(Object parent) {
        return ((GnucashAccountTreeEntry)parent).getChildTreeNodes().size();
    }

    /**
     *
     * @see javax.swing.tree.TreeModel#isLeaf(java.lang.Object)
     */
    public boolean isLeaf(Object node) {
        return getChildCount(node)==0;
    }


    private Set listeners = new HashSet();

    /**
     *
     * @see javax.swing.tree.TreeModel#addTreeModelListener(javax.swing.event.TreeModelListener)
     */
    public void addTreeModelListener(TreeModelListener l) {
        listeners.add(l);

    }

    /**
     *
     * @see javax.swing.tree.TreeModel#removeTreeModelListener(javax.swing.event.TreeModelListener)
     */
    public void removeTreeModelListener(TreeModelListener l) {
        listeners.remove(l);

    }

    /**
     *
     * @see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)
     */
    public Object getChild(Object parent, int index) {
        return ((GnucashAccountTreeEntry)parent).getChildTreeNodes().get(index);
    }

    /**
     *
     * @see javax.swing.tree.TreeModel#getIndexOfChild(java.lang.Object, java.lang.Object)
     */
    public int getIndexOfChild(Object parent, Object child) {
        return ((GnucashAccountTreeEntry)parent).getChildTreeNodes().indexOf(child);
    }

    /**
     *
     * @see javax.swing.tree.TreeModel#valueForPathChanged(javax.swing.tree.TreePath, java.lang.Object)
     */
    public void valueForPathChanged(TreePath path, Object newValue) {
        // TODO unsupported

    }

    public GnucashFile getFile() {
        return rootEntry.getFile();
    }
    public void setFile(GnucashFile file) {
        if (file == null)
            throw new IllegalArgumentException(
                    "null not allowed for field this.file");
       rootEntry = new GnucashAccountTreeRootEntry(file);

        fireTreeStructureChanged(getPathToRoot());
    }


    protected TreePath getPathToRoot() {
     return new TreePath(getRoot());
    }

    protected void fireTreeStructureChanged(TreePath path) {
     TreeModelEvent evt = new TreeModelEvent(this, path);

     for (Iterator iter = listeners.iterator(); iter.hasNext();) {
        TreeModelListener listener = (TreeModelListener) iter.next();

        listener.treeStructureChanged(evt);

    }
    }

}
