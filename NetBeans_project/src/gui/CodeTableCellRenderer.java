package gui;


import cpu.CPU;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Koki
 */
public class CodeTableCellRenderer extends JLabel
implements TableCellRenderer {

    CPU cpu;
    
    Color zuta = new Color(22, 55, 0);

    public CodeTableCellRenderer(CPU cpu) 
    {
        super();
        this.cpu = cpu;
        setOpaque(true);
        
    }

    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) 
    {
      setBackground(Color.white);
      if (row == cpu.RegisterFile.PC.get()) setBackground(Color.yellow);
      //setHorizontalAlignment(JLabel.CENTER);
      String str = (value == null) ? "" : value.toString();
      setText( " " + str );
      
      //setBackground(new Color(135,200,250));
      //if (isSelected) setBackground(new Color(255,255,255));
      return this;  
    }
    
}