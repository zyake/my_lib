package my.lib.data.jdbc;

public class CategoryConfig {

    private int id;

    private String configName;

    public CategoryConfig() {
    }

    public CategoryConfig(String configName, int id) {
        this.id = id;
        this.configName = configName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "{ id=" + id + ", config name=" + configName +" }";
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }
}
