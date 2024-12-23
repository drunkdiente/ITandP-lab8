import java.util.Collections;
import java.util.List;

public class AggregationProcessor {
    @DataProcessor
    public List<String> aggregateData(List<String> data) {
        System.out.println("Агрегация данных...");
        return Collections.singletonList("Количество элементов: " + data.size());
    }
}
