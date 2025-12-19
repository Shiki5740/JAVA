import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UserData {
    private static final String FILE_NAME = "users.txt";
    private Map<String, String> users;
    
    public UserData() {
        users = new HashMap<>();
        loadUsers();
    }
    
    private void loadUsers() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    users.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (FileNotFoundException e) {
            // 文件不存在，创建新文件
            try {
                new File(FILE_NAME).createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void saveUsers() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Map.Entry<String, String> entry : users.entrySet()) {
                bw.write(entry.getKey() + "," + entry.getValue());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public boolean registerUser(String username, String password) {
        if (users.containsKey(username)) {
            return false; // 用户名已存在
        }
        
        users.put(username, password);
        saveUsers();
        return true;
    }
    
    public boolean loginUser(String username, String password) {
        if (!users.containsKey(username)) {
            return false; // 用户名不存在
        }
        
        return users.get(username).equals(password);
    }
    
    public boolean userExists(String username) {
        return users.containsKey(username);
    }
}