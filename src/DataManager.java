import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;

public class DataManager {
    private final List<Object> processors = new ArrayList<>();
    private List<String> rawData;
    private Map<String, List<String>> processedResults = new LinkedHashMap<>();

    public void registerDataProcessor(Object processor) {
        processors.add(processor);
    }

    public void loadData(String source) throws IOException {
        System.out.println("Загрузка данных из файла: " + source);
        rawData = Files.readAllLines(Paths.get(source));
    }

    public void processData() {
        if (rawData == null || rawData.isEmpty()) {
            System.out.println("Нет данных для обработки.");
            return;
        }

        System.out.println("Начало многопоточной обработки данных...");
        List<String> currentData = rawData;

        ExecutorService executorService = Executors.newFixedThreadPool(4);

        try {
            for (Object processor : processors) {
                for (var method : processor.getClass().getDeclaredMethods()) {
                    if (method.isAnnotationPresent(DataProcessor.class)) {
                        final List<String> inputData = currentData;

                        Callable<List<String>> task = () -> {
                            try {
                                return (List<String>) method.invoke(processor, inputData);
                            } catch (Exception e) {
                                System.err.println("Ошибка при выполнении метода: " + e.getMessage());
                                e.printStackTrace();
                                return Collections.emptyList();
                            }
                        };

                        Future<List<String>> future = executorService.submit(task);
                        currentData = future.get();

                        processedResults.put(processor.getClass().getSimpleName() + "::" + method.getName(),
                                new ArrayList<>(currentData));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка обработки данных: " + e.getMessage());
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }

        System.out.println("Обработка данных завершена.");
    }

    public void saveData(String destination) throws IOException {
        System.out.println("Сохранение данных в файл: " + destination);

        List<String> output = new ArrayList<>();
        for (var entry : processedResults.entrySet()) {
            output.add("Результаты: " + entry.getKey());
            output.addAll(entry.getValue());
            output.add("");
        }

        if (!output.isEmpty()) {
            Files.write(Paths.get(destination), output);
            System.out.println("Данные успешно сохранены.");
        } else {
            System.out.println("Нет обработанных данных для сохранения.");
        }
    }
}
