import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Client {

    private static Socket clientSocket; //сокет для общения
    private static BufferedInputStream inS; // поток чтения из сокета
    private static BufferedOutputStream outS; // поток записи в сокет
    private static BufferedReader inR; // поток чтения из сокета
    private static BufferedWriter outW; // поток записи в сокет

    private String errorMessage;
    private List<FileData> files = new ArrayList<>();
    private List<String> users = new ArrayList<>();

    public String getErrorMessage() {
        return errorMessage;
    }

    public List<FileData> getFiles() {
        return files;
    }

    public List<String> getUsers() { return users; }

    public int auth(String username, String password) {
        System.out.println("auth: name " + username + " pass " + password);
        int res = connectToServer();
        if (res != 0) {
            closeConnectionWithServer();
            return res;
        }

        res = sendToServer(String.format("Authorization %s %s", username, password));
        if (res != 0) {
            closeConnectionWithServer();
            return res;
        }
        res = getServerAnswer();
        if (res != 0) {
            closeConnectionWithServer();
        }

        return res;
    }

    public int register(String username, String email, String password) {
        System.out.println("register: name " + username + " pass " + password + " mail " + email);
        int res = connectToServer();
        if (res != 0)
            return res;
        res = sendToServer(String.format("Registration %s %s %s", username, email, password));
        if (res != 0)
            return res;
        res = getServerAnswer();
        return res;
    }

    public int closeConnection() {
        int res;
        res = sendToServer("Stop");
        if (res != 0)
            return res;
        res = getServerAnswer();
        if (res != 0)
            return res;
        return closeConnectionWithServer();
    }

    public int sendFile(String receiver, File file) {
        if (receiver.isEmpty()) {
            errorMessage = "Receiver is empty";
            return -1;
        }
        if (file == null) {
            errorMessage = "File is empty";
            return -1;
        }

        System.out.println("send file: recv " + receiver + " file " + file.getAbsolutePath());
        return 0;
    }

    public int updateFiles() {
        sendToServer("UpdateFilesList");
        return getUpdatedListOfFiles();
    }

    public int updateUsers() {
        sendToServer("UpdateUsersList");
        return getUpdatedListOfUsers();
    }

    public int downloadFile(String dir, List<String> fileNames) {
        System.out.println("Download file: dir " + dir + " filename " + fileNames.get(0));
        return 0;
    }

    private int connectToServer() {
        try {
            clientSocket = new Socket("localhost", 8000);
            outS = new BufferedOutputStream(new DataOutputStream(clientSocket.getOutputStream()));
            inS = new BufferedInputStream(new DataInputStream(clientSocket.getInputStream()));
            inR = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outW = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            errorMessage = "Не удалось подключиться к серверу";
            return -1;
        }
        return 0;
    }

    private int sendToServer(String data) {
        try {
            outW.write(data + "\n");
            outW.flush();
        } catch (IOException e) {
            e.printStackTrace();
            errorMessage = "Не удалось отправить данные на сервер";
            return -1;
        }
        return 0;
    }

    private int getUpdatedListOfFiles() {
        String serverWord; // ждём, что скажет сервер
        try {
            serverWord = inR.readLine();
            parseListOfFiles(serverWord);
        } catch (IOException e) {
            e.printStackTrace();
            errorMessage = "Не удалось получить ответ от сервера";
            return -1;
        }
        return 0;
    }

    private int getUpdatedListOfUsers() {
        String serverWord; // ждём, что скажет сервер
        try {
            serverWord = inR.readLine();
            parseListOfUsers(serverWord);
        } catch (IOException e) {
            e.printStackTrace();
            errorMessage = "Не удалось получить ответ от сервера";
            return -1;
        }
        return 0;
    }

    private int parseListOfFiles(String data) {
        String[] filesData = data.split(" ");
        List<FileData> newFileList = new ArrayList<>();
        for (int i = 0; i < filesData.length-3; i++) {
            FileData fdata = new FileData(UUID.fromString(filesData[i]), filesData[i+1], filesData[i+2]);
            newFileList.add(fdata);
        }
        files = newFileList;
        return 0;
    }

    private int parseListOfUsers(String data) {
        String[] usersData = data.split(" ");
        List<String> newUsersList = new ArrayList<>(Arrays.asList(usersData));
        users = newUsersList;
        return 0;
    }

    private int getServerAnswer() {
        String serverWord; // ждём, что скажет сервер
        try {
            serverWord = inR.readLine();
            if (serverWord.equals("Ok"))
                return 0;
            else {
                errorMessage = serverWord;
                return -1;
            }
        } catch (IOException e) {
            e.printStackTrace();
            errorMessage = "Не удалось получить ответ от сервера";
            return -1;
        }
    }

    private int closeConnectionWithServer() {
        System.out.println("Клиент был закрыт...");
        try {
            clientSocket.close();
            inS.close();
            outS.close();
            inR.close();
            outW.close();
        } catch (IOException e) {
            e.printStackTrace();
            errorMessage = "Не удалось закрыть соединение";
            return -1;
        }
        return 0;
    }
}
