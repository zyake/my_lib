package my.lib;

import java.io.*;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestResourceUtil {

    private static final Charset DEFAULT_CHARSET = Charset.forName(System.getProperty("file.encoding"));

    public static ProcessResult executeProcess(Process process) {
        return executeProcess(process, DEFAULT_CHARSET);
    }

    public static ProcessResult executeProcess(Process process, Charset charset) {
        StreamReadingTask stdoutTask = new StreamReadingTask(process.getInputStream(), charset);
        StreamReadingTask stderrTask = new StreamReadingTask(process.getErrorStream(), charset);

        ExecutorService service = Executors.newFixedThreadPool(2);
        service.execute(stdoutTask);
        service.execute(stderrTask);
        service.shutdown();

        try {
            process.waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        ProcessResult processResult = new ProcessResult(
                stdoutTask.streamResult, stderrTask.streamResult, process.exitValue());

        return processResult;
    }

    public static String loadStringFromClassPath(String classPath) {
        InputStream inputStream = loadStream(classPath);

        return readString(inputStream);
    }

    public static String loadStringFromFilePath(String filePath) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return readString(inputStream);
    }

    private static String readString(InputStream inputStream) {
        InputStreamReader reader = new InputStreamReader(inputStream);
        StringBuilder stringBuilder = new StringBuilder();
        int ch;
        try {
            while ( (ch = reader.read()) != -1 ) {
                stringBuilder.append(ch);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return stringBuilder.toString();
    }

    public static InputStream loadStream(String classPath) {
        InputStream resource = null;
        try {
            resource = new FileInputStream("src/test/java/" + classPath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return resource;
    }

    public static String getPackagePath(Class clazz) {
        return clazz.getPackage().getName().replace(".", "/") + "/";
    }

    public static String createRelativeFilePath(String classPath) {
        return "test/" + classPath;
    }

    private static class StreamReadingTask implements Runnable {

        private InputStream inputStream;

        private Charset encoding;

        private String streamResult;

        private StreamReadingTask(InputStream inputStream, Charset encoding) {
            this.inputStream = inputStream;
            this.encoding = encoding;
        }

        @Override
        public void run() {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            int ch;
            try {
                while ( ( ch = inputStream.read()) != -1 ) {
                    outputStream.write(ch);
                    streamResult = outputStream.toString(encoding.name());
                }
            } catch (IOException e) {
                streamResult = "ERROR!";
                e.printStackTrace();
            }
        }

        public String getStreamResult() {
            return streamResult;
        }
    }
}
