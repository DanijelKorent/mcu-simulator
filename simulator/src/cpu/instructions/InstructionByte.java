package cpu.instructions;

import cpu.registers.Flags;
import cpu.registers.Register8b_Base;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Danijel Korent
 */
public class InstructionByte extends Instruction
{
    private final int type;
    private final int registerAddress;
    private final boolean destinationIsW;
    
    public InstructionByte(int op) 
    {
        super(op);
        type = opcode & MASKA_INSTR_6;
        registerAddress = opcode & MASKA_FILE_7;
        destinationIsW = ( 0 == (opcode & MASKA_INSTR_DEST) );
    }
    
    @Override
    public void execute()
    {
        Register8b_Base registarOperand;
        Register8b_Base registarDestination; 
        
        super.execute(); // uvecava PC

        // .getRAM mora bit pozvan kod svakog izvrsavanja zbog bankirane memorije
        if (destinationIsW )
        {
            registarOperand = cpu.getRam( registerAddress );
            registarDestination = cpu.getW();
        }
        else
        {
            registarDestination = cpu.getRam( registerAddress );
            registarOperand = cpu.getW();
        }
        
        
        if (type == (OPCODE_MOVWF & MASKA_INSTR_6))
        {
            registarDestination.set( cpu.getW() );
        }
        else if (type == OPCODE_ADDWF)
        {        
            Flags zastavice = registarDestination.add( registarOperand );

            cpu.getStatus().setCpuFlags( zastavice ); 
        }
        else if (type == OPCODE_SUBWF)
        {        
            Flags zastavice = registarDestination.sub( registarOperand );

            cpu.getStatus().setCpuFlags( zastavice ); 
        }
        else if ( type == OPCODE_MOVF )
        {
            registarDestination.set( cpu.getRam(registerAddress) );
            
            if (registarDestination.get() == 0)
            {
                cpu.getStatus().setZ();
            }
            else 
            {
                cpu.getStatus().clearZ();
            }
        }
        else  if (type == OPCODE_ANDWF)
        {
            boolean newZ = registarDestination.logAnd( registarOperand );
            
            if ( newZ )
            {
                cpu.getStatus().setZ();
            }
            else 
            {
                cpu.getStatus().clearZ();
            }
        }
        else if (type == OPCODE_IORWF)
        {
            boolean newZ = registarDestination.logOr( registarOperand );
            
            if ( newZ )
            {
                cpu.getStatus().setZ();
            }
            else 
            {
                cpu.getStatus().clearZ();
            }
        }
        else if (type == OPCODE_XORWF)
        {
            boolean newZ = registarDestination.logXor( registarOperand );
            
            if ( newZ )
            {
                cpu.getStatus().setZ();
            }
            else 
            {
                cpu.getStatus().clearZ();
            }
        }
        else
        {
            if (destinationIsW )
            {
                registarDestination.set(registarOperand);
            }

            if ( type ==  OPCODE_RLF)
            {
                boolean newC = registarDestination.rotateLeft( cpu.getStatus().getC() );

                if ( newC )
                {
                    cpu.getStatus().setC();
                }
                else
                {
                    cpu.getStatus().clearC();
                }
            } 
            else if ( type ==  OPCODE_RRF)
            {
                boolean newC = registarDestination.retateRight( cpu.getStatus().getC() );

                if ( newC )
                {
                    cpu.getStatus().setC();
                }
                else
                {
                    cpu.getStatus().clearC();
                }
            }
            else if ( type ==  OPCODE_COMF)
            {
                boolean newZ = registarDestination.complement();

                if ( newZ )
                 {
                     cpu.getStatus().setZ();
                 }
                 else 
                 {
                     cpu.getStatus().clearZ();
                 }
            }
            else if ( type ==  OPCODE_SWAPF)
            {
                registarDestination.swapNibbles();
            }
            else if ( type ==  OPCODE_DECF)
            {
                registarDestination.sub(1);

                if ( 0 == registarDestination.get() )
                {
                    cpu.getStatus().setZ();
                }
                else 
                {
                    cpu.getStatus().clearZ();
                }
            }
            else if ( type ==  OPCODE_INCF)
            {
                registarDestination.add(1);

                if ( 0 == registarDestination.get() )
                {
                    cpu.getStatus().setZ();
                }
                else 
                {
                    cpu.getStatus().clearZ();
                }
            }
            else if ( type ==  OPCODE_DECFSZ)
            {
                registarDestination.sub(1);

                if ( 0 == registarDestination.get() )
                {
                    cpu.getPc().inc();
                }
            }
            else if ( type ==  OPCODE_INCFSZ)
            {
                registarDestination.add(1);

                if ( 0 == registarDestination.get() )
                {
                    cpu.getPc().inc();
                }
            }  
            else if ( type ==  OPCODE_CLR)
            {
                registarDestination.set(0);
                cpu.getStatus().clearZ();
            }  
        }
    }
    
    @Override
    public String getAsmCode()
    {
        String destination = ", ";
        
        if( destinationIsW )
        {
            destination += "w";
        }
        else
        {
            destination += "f";
        }
        
        // ToDo: ispisati i destinaciju operacije
        if (type == (OPCODE_MOVWF & MASKA_INSTR_6))  return "MOVWF  " + registerAddress;
        else if (type == OPCODE_MOVF)   return "MOVF   " + registerAddress + destination;
        else if (type == OPCODE_ADDWF)  return "ADDWF  " + registerAddress + destination;
        else if (type == OPCODE_ANDWF ) return "ANDWF  " + registerAddress + destination;
        else if (type == OPCODE_IORWF ) return "IORWF  " + registerAddress + destination;
        else if (type == OPCODE_RLF )   return "RLF    " + registerAddress + destination;
        else if (type == OPCODE_RRF )   return "RRF    " + registerAddress + destination;
        else if (type == OPCODE_CLR )   return "CLR    " + registerAddress + destination;
        else if (type == OPCODE_COMF )  return "COMF   " + registerAddress + destination;
        else if (type == OPCODE_SWAPF ) return "SWAPF  " + registerAddress + destination;

        return "Nepoznata istrukcija!! PC = " + ( cpu.getPc().get()-1) + " : Inst: " + opcode;
    }
    
}
