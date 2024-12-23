import java.util.List;
import java.util.stream.Collectors;

public class FilterProcessor {
    @DataProcessor
    public List<String> filterData(List<String> data) {
        System.out.println("Фильтрация данных...");
        return data.stream()
                .filter(d -> d.contains("1") || d.contains("3"))
                .collect(Collectors.toList());
    }
}
