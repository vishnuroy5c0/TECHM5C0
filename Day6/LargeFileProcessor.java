package Day7;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LargeFileProcessor {
    private static final String INPUT_FILE = "data.txt";
    private static final String OUTPUT_FILE = "output.txt";
    private static final int CHUNK_SIZE = 10000; // Number of lines per thread
    private static final int THREAD_COUNT = 5; // Number of threads in the pool

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        
        try (BufferedReader reader = new BufferedReader(new FileReader(INPUT_FILE))) {
            List<String> lines = new ArrayList<>();
            String line;
            int lineCount = 0;
            int chunkIndex = 0;
            
            while ((line = reader.readLine()) != null) {
                lines.add(processLine(line)); // Simulate processing
                lineCount++;
                
                if (lineCount >= CHUNK_SIZE) {
                    executor.execute(new FileWriterTask(new ArrayList<>(lines), chunkIndex++));
                    lines.clear();
                    lineCount = 0;
                }
            }
            
            // Process remaining lines
            if (!lines.isEmpty()) {
                executor.execute(new FileWriterTask(new ArrayList<>(lines), chunkIndex));
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        } finally {
            executor.shutdown();
        }
    }

    private static String processLine(String line) {
        return line.toUpperCase(); // Example processing: Convert text to uppercase
    }
}

class FileWriterTask implements Runnable {
    private final List<String> lines;
    private final int chunkIndex;

    public FileWriterTask(List<String> lines, int chunkIndex) {
        this.lines = lines;
        this.chunkIndex = chunkIndex;
    }

    @Override
    public void run() {
        synchronized (FileWriterTask.class) { // Ensure thread-safe file writing
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt", true))) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
                System.out.println("Processed chunk: " + chunkIndex);
            } catch (IOException e) {
                System.err.println("Error writing to the file: " + e.getMessage());
            }
        }
    }
}
