public class Main {
    public static void main(String[] args) throws Exception {
        DataManager dataManager = new DataManager();

        dataManager.registerDataProcessor(new FilterProcessor());
        dataManager.registerDataProcessor(new TransformProcessor());
        dataManager.registerDataProcessor(new AggregationProcessor());

        dataManager.loadData("src/data_source.txt");
        dataManager.processData();
        dataManager.saveData("src/result_output.txt");
    }
}
