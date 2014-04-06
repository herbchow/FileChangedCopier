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
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;

/**
 *
 * @author Herbert.Chow
 */
public class MonitorEntryWorker extends SwingWorker {

    private FileModifiedMonitor watcher;
    private File sourceFolder;
    private File destination;
    private final ILogMessages log;
    private final INotifyFileChanged notifier;
    private final String sourceFileName;

    public MonitorEntryWorker(File sourceFolder, String sourceFileName, File destination, ILogMessages log, INotifyFileChanged notifier) {
        watcher = new FileModifiedMonitor();
        this.sourceFileName = sourceFileName;
        this.sourceFolder = sourceFolder;
        this.destination = destination;
        this.log = log;
        this.notifier = notifier;
    }

    
    //public void Monitor(File folder, String sourceFileName, File destinationFolder, INotifyFileChanged fileChanged, ILogMessages messageLog)
    @Override
    protected Object doInBackground() throws Exception {
        //watcher.Monitor(sourceFolder, sourceFileName, destination, notifier, log);

        final Path path = FileSystems.getDefault().getPath(sourceFolder.getAbsolutePath());
        System.out.println(path);
        final WatchService watchService;
        try {
            watchService = FileSystems.getDefault().newWatchService();
            final WatchKey watchKey = path.register(watchService, ENTRY_MODIFY);

            while (!isCancelled()) {
                final WatchKey wk;
                try {
                    wk = watchService.take();
                    for (WatchEvent<?> event : wk.pollEvents()) {
                        //we only register "ENTRY_MODIFY" so the context is always a Path.
                        final Path changed = (Path) event.context();
                        File modifiedFile = new File(sourceFolder.getAbsolutePath(),changed.getFileName().toString());
                        
                        log.Log(modifiedFile + " modify detected ");
                        if(modifiedFile.toString().endsWith(sourceFileName))
                        {
                            log.Log("Copying " + modifiedFile + " to destination " + destination);
                            notifier.onFileChanged(modifiedFile, destination);
                        }
                    }
                    // reset the key
                    boolean valid = wk.reset();
                    if (!valid) {
                        log.Log("Key has been unregistered\n");
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(FileModifiedMonitor.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        } catch (IOException ex) {
            Logger.getLogger(FileModifiedMonitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        log.Log("Stopping thread");
        return null;
    }
}
