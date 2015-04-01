import java.io.File;
import java.util.concurrent.BlockingQueue;

public class FileIndexer implements Runnable {
        @SuppressWarnings("rawtypes")
		private final BlockingQueue queue;

        @SuppressWarnings("rawtypes")
		public FileIndexer(BlockingQueue queue) {
            this.queue = queue;
            
        }

        public void run() { 
        	try {
                while (true) {
                    indexFile((File) queue.take());
                }
            } catch (InterruptedException e) {
                System.out.println("Indexer Interrupted");
                Thread.currentThread().interrupt();
            }
        }

        public void indexFile(File file) {
             
            System.out.println("Indexing File : " + file.getAbsolutePath() + " " + file.getName());
        };
    }