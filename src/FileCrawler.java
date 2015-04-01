import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileCrawler implements Runnable {
            @SuppressWarnings("rawtypes")
			private final BlockingQueue fileQueue;
            @SuppressWarnings("rawtypes")
			private ConcurrentSkipListSet indexedFiles = new ConcurrentSkipListSet();
            private final FileFilter fileFilter;
            private final File root;
            private final ExecutorService exec = Executors.newCachedThreadPool();

            @SuppressWarnings("rawtypes")
			public FileCrawler(BlockingQueue fileQueue,
                               final FileFilter fileFilter,
                               File root) {
                this.fileQueue = fileQueue;
                this.root = root;
        
                this.fileFilter = new FileFilter() {
                    public boolean accept(File f) {
                        return f.isDirectory() || fileFilter.accept(f);
                    }
                };
            }

            public void run() {

                    submitCrawlTask(root);

            }

            private void submitCrawlTask(File f) {
                CrawlTask crawlTask = new CrawlTask(f);
                exec.execute(crawlTask);
            }

            private class CrawlTask implements Runnable {
                private final File file;

                CrawlTask(File file ) {

                    this.file= file;
                }

             @SuppressWarnings("unchecked")
			public void run() {        
                 if(Thread.currentThread().isInterrupted())
                return;

                    File[] entries = file.listFiles(fileFilter);

                    if (entries != null) {
                        for (File entry : entries)
                            if (entry.isDirectory())
                                submitCrawlTask(entry);
                            else if (entry !=null && !indexedFiles.contains(entry)){
                                indexedFiles.add(entry);
                                try {
                                    fileQueue.put(entry);
                                } catch (InterruptedException e) {
                                        Thread.currentThread().interrupt();
                                }
                            }
                    }
                }
        }
       }