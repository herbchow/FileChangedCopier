/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.FileSystems;
import static java.nio.file.StandardWatchEventKinds.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Herbert.Chow
 */
public class FileModifiedMonitor {

    public void Monitor(File folder, String sourceFileName, File destinationFolder, INotifyFileChanged fileChanged, ILogMessages messageLog) {
        final Path path = FileSystems.getDefault().getPath(folder.getAbsolutePath());
        System.out.println(path);
        final WatchService watchService;
        try {
            watchService = FileSystems.getDefault().newWatchService();
            final WatchKey watchKey = path.register(watchService, ENTRY_MODIFY);

            while (true) {
                final WatchKey wk;
                try {
                    wk = watchService.take();
                    for (WatchEvent<?> event : wk.pollEvents()) {
                        //we only register "ENTRY_MODIFY" so the context is always a Path.
                        final Path changed = (Path) event.context();
                        File modifiedFile = new File(folder.getAbsolutePath(),changed.getFileName().toString());
                        
                        messageLog.Log(modifiedFile + " modify detected ");
                        if(modifiedFile.toString().endsWith(sourceFileName))
                        {
                            messageLog.Log("Copying " + modifiedFile + " to destination " + destinationFolder);
                            fileChanged.onFileChanged(modifiedFile, destinationFolder);
                        }
                    }
                    // reset the key
                    boolean valid = wk.reset();
                    if (!valid) {
                        messageLog.Log("Key has been unregistered\n");
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(FileModifiedMonitor.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        } catch (IOException ex) {
            Logger.getLogger(FileModifiedMonitor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
