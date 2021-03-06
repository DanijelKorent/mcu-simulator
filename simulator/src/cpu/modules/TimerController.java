/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpu.modules;

/**
 *
 * @author dkorent
 */
public class TimerController 
{
    private int counter;
    private int prescalerCnt;
    private int prescalerSetting;
    private boolean prescalerActive;
    
    private InterruptController interruptController;
    
    public TimerController(InterruptController intController )
    {
        counter = 0;
        prescalerCnt = 0;
        prescalerActive = false;
        
        interruptController = intController;
    }

    public void onTick()
    {
        if( prescalerActive )
        {
            int oldValue = prescalerCnt;
            prescalerCnt++;
            prescalerCnt &= 0xFF; 
            
            if( ((oldValue & prescalerSetting) == 0) && ((prescalerCnt & prescalerSetting) != 0) )
            //if ( ((prescalerCnt & prescalerSetting) == 0) && ((prescalerCnt ^ (prescalerCnt+1)) & prescalerSetting) != 0 )
            {
                counter++;
            }
        }
        else
        {
            counter++;
        }
        
        if ( counter > 0xFF )
        {
            counter = 0;
            interruptController.setInterruptFlag( InterruptController.FLAG_TIMER , true);
        }
    }
    
    public void setPrescalerActive( boolean active)
    {
        prescalerActive = active;
    }
    
    public void setPrescalerSetting( int setting)
    {
        
        prescalerSetting = 1 << setting;
    }
    
    
    public int get()
    {
        return counter;
    }
    
    public void set( int val)
    {
        if (counter < 0 || counter > 255) throw new IllegalArgumentException("Nedopustena vrijednost argumenta r: " + val);
        
        counter = val;
        prescalerCnt = 0; // datasheet, str 21.: Writing to TMR0 when the prescaler is assigned to Timer0 will clear the prescaler count
    }
}
