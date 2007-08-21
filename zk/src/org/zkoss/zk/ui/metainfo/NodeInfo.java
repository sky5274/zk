/* NodeInfo.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Mon Mar 26 17:33:45     2007, Created by tomyeh
}}IS_NOTE

Copyright (C) 2007 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zk.ui.metainfo;

import java.util.List;
import java.util.LinkedList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 * Represents a node of the ZUML tree.
 * It is an abstract class while the concrete classes include
 * {@link PageDefinition} and {@link ComponentInfo}.
 * The root must be an instance of {@link PageDefinition}
 * and the other nodes must be instances of {@link ComponentInfo},
 * {@link ZScript}, {@link VariablesInfo}, or {@link AttributesInfo}.
 *
 * <p>Note:it is not thread-safe.
 * 
 * @author tomyeh
 */
abstract public class NodeInfo implements java.io.Serializable {
	/** A list of {@link ComponentInfo} and {@link ZScript}. */
	private final List _children = new LinkedList();

	public NodeInfo() {
	}

	/** Returns the page definition.
	 */
	abstract public PageDefinition getPageDefinition();
	/** Returns the parent.
	 */
	abstract public NodeInfo getParent();

	/** Adds a zscript child.
	 */
	public void appendChild(ZScript zscript) {
		appendChild0((Object)zscript);
	}
	/** Adds a variables child.
	 */
	public void appendChild(VariablesInfo variables) {
		appendChild0((Object)variables);
	}
	/** Adds a custom-attributes child.
	 */
	public void appendChild(AttributesInfo custAttrs) {
		appendChild0((Object)custAttrs);
	}
	/** Adds a {@link ComponentInfo} child.
	 * @since 2.4.0
	 */
	public void appendChild(ComponentInfo compInfo) {
		compInfo.setParent(this); //it will call back appendChild0
	}

	/** Removes a zscript child.
	 * @return whether the child is removed successfully.
	 */
	public boolean removeChild(ZScript zscript) {
		return removeChild0((Object)zscript);
	}
	/** Removes a variables child.
	 * @return whether the child is removed successfully.
	 */
	public boolean removeChild(VariablesInfo variables) {
		return removeChild0((Object)variables); 
	}
	/** Removes a custom-attributes child.
	 * @return whether the child is removed successfully.
	 */
	public boolean removeChild(AttributesInfo custAttrs) {
		return removeChild0((Object)custAttrs); 
	}
	/** Removes a {@link ComponentInfo} child.
	 *
	 * <p>Call {@link ComponentInfo#setParent} instead.
	 * @return whether the child is removed successfully.
	 * @since 2.4.0
	 */
	public boolean removeChild(ComponentInfo compInfo) {
		if (compInfo == null || compInfo.getParent() == null)
			return false;
//		synchronized (_children) {
			if (!_children.contains(compInfo))
				return false;
//		}

		compInfo.setParent(null);
		return true;
	}
	/*package*/ boolean removeChild0(ComponentInfo compInfo) {
		return removeChild0((Object)compInfo);
	}

	/** Adds a child.
	 */
	/*pacakge*/ void appendChild0(Object child) {
		if (child == null)
			throw new IllegalArgumentException("child required");
//		synchronized (_children) {
			_children.add(child);
//		}
	}
	/** Removes a child.
	 */
	/*package*/ boolean removeChild0(Object child) {
//		synchronized (_children) {
			return _children.remove(child);
//		}
	}

	/** Returns a list of children.
	 * Children include instances of {@link ComponentInfo}, {@link ZScript}
	 * {@link VariablesInfo}, or {@link AttributesInfo}.
	 *
	 * <p>Note: the returned list is modifiable but it is not a good idea,
	 * because, unlike {@link org.zkoss.zk.ui.Component}, it doesn't maintain
	 * {@link NodeInfo#getParent}. Thus, it is better to invoke
	 * {@link #appendChild(ComponentInfo)} and {@link #removeChild(ComponentInfo)}
	 * instead.
	 */
	public List getChildren() {
		return _children;
	}

	//Serializable//
	private synchronized void readObject(java.io.ObjectInputStream s)
	throws java.io.IOException, ClassNotFoundException {
		s.defaultReadObject();

		for (Iterator it = _children.iterator(); it.hasNext();) {
			final Object o = it.next();
			if (o instanceof ComponentInfo)
				((ComponentInfo)o).setParentDirectly(this);
		}
	}
}
