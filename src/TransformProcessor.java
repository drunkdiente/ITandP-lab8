import java.util.List;
import java.util.stream.Collectors;

public class TransformProcessor {
    @DataProcessor
    public List<String> transformData(List<String> data) {
        System.out.println("Трансформация данных...");
        return data.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());
    }
}
