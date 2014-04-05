/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import Util.ILogMessages;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JTextArea;

/**
 *
 * @author Herbert.Chow
 */
public class LoggerUI implements ILogMessages {

    private JTextArea textArea;
    private DateFormat dateFormat;
    private Calendar calendar;

    public LoggerUI(JTextArea area) {
        textArea = area;
        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        calendar = Calendar.getInstance();
    }

    @Override
    public void Log(String message) {
        textArea.append(dateFormat.format(calendar.getTime()).toString() + ": " + message + "\n");
        System.out.println(message);
    }

}
