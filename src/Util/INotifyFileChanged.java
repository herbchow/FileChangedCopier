/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Util;

import java.io.File;

/**
 *
 * @author Herbert.Chow
 */
public interface INotifyFileChanged {
    public void onFileChanged(File source,File destination);
}
