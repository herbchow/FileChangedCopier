/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import Util.FileModifiedMonitor;
import Util.ILogMessages;
import Util.INotifyFileChanged;
import java.io.File;
import javax.swing.SwingWorker;

/**
 *
 * @author Herbert.Chow
 */
public class MonitorEntryWorker extends SwingWorker {

    private FileModifiedMonitor watcher;
    private File source;
    private File destination;
    private final ILogMessages log;
    private final INotifyFileChanged notifier;
    private final String sourceFileName;

    public MonitorEntryWorker(File source, String sourceFileName, File destination, ILogMessages log, INotifyFileChanged notifier) {
        watcher = new FileModifiedMonitor();
        this.sourceFileName = sourceFileName;
        this.source = source;
        this.destination = destination;
        this.log = log;
        this.notifier = notifier;
    }

    @Override
    protected Object doInBackground() throws Exception {
        watcher.Monitor(source, sourceFileName, destination, notifier, log);
        return true;
    }
}
