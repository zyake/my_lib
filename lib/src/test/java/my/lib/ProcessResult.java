package my.lib;

public class ProcessResult {

    private String stdout;

    private String stderr;

    private int exitCode;

    public ProcessResult(String stdout, String stderr, int exitCode) {
        this.stdout = stdout;
        this.stderr = stderr;
        this.exitCode = exitCode;
    }

    public String getStdout() {
        return stdout;
    }

    public String getStderr() {
        return stderr;
    }

    @Override
    public String toString() {
        return "{ exit code=" + exitCode + ", stdout=" + stdout + ",  stderr=" + stderr + " }";
    }

    public int getExitCode() {
        return exitCode;
    }
}
