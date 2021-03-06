package gui;


import cpu.CpuExternalInterface;
import javax.swing.table.AbstractTableModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Koki
 */
public class RamTableModel extends AbstractTableModel 
{
    CpuExternalInterface cpu;

    public RamTableModel(CpuExternalInterface cpu) 
    {
        super();
        this.cpu = cpu;
    }


    @Override
    public int getRowCount() 
    {
        return 16;
    }

    @Override
    public int getColumnCount() 
    {
        return 9;
    }
    
    @Override
    public String getColumnName(int i) 
    {
        if (i == 0) return "Adr.";
        else return "  +" + (i-1);
    }
    
    @Override
    public Object getValueAt(int i, int i1) 
    {
        if (i1 == 0)
        {
            int val = i*8;
            if ( cpu.getStatus().getRP0() )
            {
                val += 0x80;
            }
            return Integer.toHexString(val);
        }

        int value = cpu.getRam( (byte)(i*8 + i1-1) ).get();
        
        return String.format("%02X", value);
    }  
}
