import java.io.File;
import java.io.FileFilter;

  public class Filefilter implements FileFilter
    {
      private final String[] okFileExtensions = 
        new String[] {"txt", "jpg", "png"};

      public boolean accept(File file)
      {
        for (String extension : okFileExtensions)
        {
          if (file.getName().toLowerCase().endsWith(extension))
          {
            return true;
          }
        }
        return false;
      }
    }