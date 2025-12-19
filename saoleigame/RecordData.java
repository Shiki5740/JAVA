import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class RecordData {
    private static final String FILE_NAME = "records.txt";
    private Map<String, List<Record>> records;
    
    public static final String BEGINNER = "beginner";
    public static final String INTERMEDIATE = "intermediate";
    public static final String EXPERT = "expert";
    private static final int MAX_RECORDS_PER_DIFFICULTY = 10;
    
    public static class Record {
        private String username;
        private int time;
        
        public Record(String username, int time) {
            this.username = username;
            this.time = time;
        }
        
        public String getUsername() {
            return username;
        }
        
        public int getTime() {
            return time;
        }
    }
    
    public RecordData() {
        records = new HashMap<>();
        // 初始化每个难度的空记录列表
        records.put(BEGINNER, new ArrayList<>());
        records.put(INTERMEDIATE, new ArrayList<>());
        records.put(EXPERT, new ArrayList<>());
        loadRecords();
    }
    
    private void loadRecords() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String difficulty = parts[0].trim();
                    String username = parts[1].trim();
                    int time = Integer.parseInt(parts[2].trim());
                    
                    // 确保难度存在于记录列表中
                    records.computeIfAbsent(difficulty, k -> new ArrayList<>())
                           .add(new Record(username, time));
                }
            }
            
            // 对每个难度的记录按时间排序，并截取前10名
            for (Map.Entry<String, List<Record>> entry : records.entrySet()) {
                List<Record> sortedRecords = entry.getValue().stream()
                        .sorted(Comparator.comparingInt(Record::getTime))
                        .limit(MAX_RECORDS_PER_DIFFICULTY)
                        .collect(Collectors.toList());
                entry.setValue(sortedRecords);
            }
        } catch (FileNotFoundException e) {
            // 文件不存在，创建新文件
            try {
                new File(FILE_NAME).createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }
    
    private void saveRecords() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Map.Entry<String, List<Record>> entry : records.entrySet()) {
                String difficulty = entry.getKey();
                List<Record> recordList = entry.getValue();
                for (Record record : recordList) {
                    bw.write(difficulty + "," + record.getUsername() + "," + record.getTime());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public List<Record> getRecords(String difficulty) {
        return records.getOrDefault(difficulty, new ArrayList<>());
    }
    
    public boolean isNewRecord(String difficulty, int time) {
        List<Record> recordList = records.get(difficulty);
        if (recordList == null) {
            return true; // 难度不存在，直接返回true
        }
        // 如果记录数少于10，或者时间比最后一个记录的时间短，则是新记录
        return recordList.size() < MAX_RECORDS_PER_DIFFICULTY || time < recordList.get(recordList.size() - 1).getTime();
    }
    
    public void addRecord(String difficulty, String username, int time) {
        List<Record> recordList = records.get(difficulty);
        if (recordList == null) {
            recordList = new ArrayList<>();
            records.put(difficulty, recordList);
        }
        
        // 添加新记录
        recordList.add(new Record(username, time));
        
        // 按时间排序，并截取前10名
        List<Record> sortedRecords = recordList.stream()
                .sorted(Comparator.comparingInt(Record::getTime))
                .limit(MAX_RECORDS_PER_DIFFICULTY)
                .collect(Collectors.toList());
        
        records.put(difficulty, sortedRecords);
        saveRecords();
    }
    
    public String getRecordString(String difficulty, String difficultyName) {
        List<Record> recordList = records.get(difficulty);
        if (recordList == null || recordList.isEmpty()) {
            return difficultyName + "最快记录：暂无";
        } else {
            Record bestRecord = recordList.get(0);
            return difficultyName + "最快记录：" + bestRecord.getUsername() + " - " + bestRecord.getTime() + "秒";
        }
    }
}