/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 */
package com.totsp.mavenplugin.gwt.support.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Consolidate file and IO related utils from other classes.
 * @author ccollins
 * @author Marek Romanowski
 */
public class FileIOUtils {

  
  
    public static final int DEFAULT_BUFFER_SIZE = 1024;

    
    
    private FileIOUtils() {
    }
    
    
    
    /**
     * Copy all files from <code>from</code> directory to </code>to</code>
     * directory. If you pass additional parameter - <code>filter</code> then
     * it will copy only files matching that filter.
     */
    public static void copyRecursive(File from, File to, FileFilter filter
        ) throws IOException {
      if (!from.exists()) {
        return;
      }
      
      to.mkdirs();
      
      // for every file in "from" directory
      for (File fileToCopy : from.listFiles()) {
        File destinationFile = new File(to, fileToCopy.getName());
        // if it is directory, copy files recursively
        if (fileToCopy.isDirectory()) {
          copyRecursive(fileToCopy, destinationFile, filter);
        } else if ((filter == null) || (filter.accept(fileToCopy.getAbsoluteFile()))) {
          // do not overwriter if not needed
          if (destinationFile.exists() 
              && (destinationFile.lastModified() > fileToCopy.lastModified())) {
            continue;
          }
          
          FileIOUtils.copyFile(fileToCopy, destinationFile);
        }
      }
    }
    
    

    /**
     * Copies the data from an InputStream object to an OutputStream object.
     * 
     * @param sourceStream
     *            The input stream to be read.
     * @param destinationStream
     *            The output stream to be written to.
     * @return int value of the number of bytes copied.
     * @exception IOException
     *                from java.io calls.
     */
    public static int copyStream(InputStream sourceStream, 
        OutputStream destinationStream) throws IOException {
        int bytesRead = 0;
        int totalBytes = 0;
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];

        while (bytesRead >= 0) {
            bytesRead = sourceStream.read(buffer, 0, buffer.length);

            if (bytesRead > 0) {
                destinationStream.write(buffer, 0, bytesRead);
            }

            totalBytes += bytesRead;
        }
        destinationStream.flush();
        destinationStream.close();
        return totalBytes;
    }

    
    
    /**
     * Copy a file from source to destination.
     */
    public static void copyFile(
        File source, File destination) throws IOException {
        FileOutputStream fos = new FileOutputStream(destination);
        FileInputStream fis = new FileInputStream(source);
        copyStream(fis, fos);
        fis.close();
    }
}


