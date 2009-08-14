/*
 * TheProperties.java
 * Copyright (C) 2005 by:
 *
 *----------------------------
 * cismet GmbH
 * Goebenstrasse 40
 * 66117 Saarbruecken
 * http://www.cismet.de
 *----------------------------
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *----------------------------
 * Author:
 * thorsten.hell@cismet.de
 *----------------------------
 *
 * Created on 8. September 2006, 12:30
 *
 */

package de.cismet.tools.gui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.JXTable;

/** 
 *
 * @author thorsten.hell@cismet.de x
 */
public class TheProperties
{
   public static void main(String[] args)
   {
       showPropertyFrame();
   }
 
   public static void showPropertyFrame() {
      final String SPLIT_STRING = "<=>";
 
      List defaults = getUIDefaults(SPLIT_STRING);
      Object[][] data = splitUIDefaults(defaults, SPLIT_STRING);
      showUIDefaultsGUI(data);
       
   }
   
   private static List getUIDefaults(String split)
   {
      List list = new ArrayList();
      UIDefaults uid = UIManager.getDefaults();
      Enumeration e = uid.keys();
      while (e.hasMoreElements())
      {
         Object key = e.nextElement();
         Object value = uid.get(key);
         list.add(key + split + value);
      }
      Collections.sort(list);
      return list;
   }
 
   private static Object[][] splitUIDefaults(List list, String split)
   {
      Object[][] data = new Object[list.size()][];
 
      for (int i = 0; i < list.size(); i++)
      {
         Object[] row = new Object[2];
         String unsplittedRow = (String) list.get(i);
         String[] values = unsplittedRow.split(split);
         row[0] = values[0];
         row[1] = values[1];
         data[i] = row;
      }
      return data;
   }
 
   public static void showUIDefaultsGUI(Object[][] modelData)
   {
      Object[] colNames = new Object[2];
      colNames[0] = "Key";
      colNames[1] = "Value";
 
      DefaultTableModel model = new DefaultTableModel(modelData, colNames)
      {
         public boolean isCellEditable(int row, int column)
         {
            return false;
         }
      };
      JXTable table= new JXTable(model);
      //JTable table = new JTable(model);
      JFrame frame = new JFrame("UIDefaults");
      frame.getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);
      frame.setSize(1050, 950);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
   }
}