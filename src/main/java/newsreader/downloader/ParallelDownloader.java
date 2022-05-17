package newsreader.downloader;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

public class ParallelDownloader extends Downloader {

    int count = 0;

    @Override
    public int process(List<String> urls) throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newCachedThreadPool();

        Set<Callable<String>> callables = new HashSet<>();

        for (String url : urls) {
            callables.add(() -> saveUrl2File(url));
        }

        java.util.List<Future<String>> futures = executorService.invokeAll(callables);

        for(Future<String> future : futures){
            if(future.get() != null)
                count++;
        }

        executorService.shutdown();


        return count;
    }

}
