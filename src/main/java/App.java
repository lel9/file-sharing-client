import javax.swing.*;
import java.io.File;
import java.util.List;

public class App {
    private JPanel mainPanel;
    private JTabbedPane tabbedPane1;
    private JButton authButton;
    private JTextField authLogin;
    private JPasswordField authPass;
    private JPasswordField regPass;
    private JTextField regEmail;
    private JTextField regLogin;
    private JButton regButton;

    private JFrame frame;

    private Client client = new Client();
    private ClientMain clientMain;
    private File fileToSend;

    public App() {
        frame = new JFrame("Приложение для обмена файлами");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(this.mainPanel);
        frame.pack();
        frame.setVisible(true);

        clientMain = new ClientMain();
    }

    public void run() {
        authButton.addActionListener(e -> {
            if (client.auth(authLogin.getText(), new String(authPass.getPassword())) == 0) {
                runMain();
            } else {
                showMessage();
            }
        });

        regButton.addActionListener(e -> {
            if (client.register(regLogin.getText(), regEmail.getText(), new String(regPass.getPassword())) == 0) {
                runMain();
            } else {
                showMessage();
            }
        });
    }

    private void showMessage() {
        String errorMessage = client.getErrorMessage();
        JOptionPane.showMessageDialog(frame, errorMessage);
    }

    private void runMain() {
        frame.setContentPane(clientMain.getMainPanel());
        frame.pack();

        setSelectListener();
        setDownloadListener();
        setCloseListener();
        setUpdateListener();
        setUpdateUsersListener();
        setSendFileListener();
    }

    private void setSelectListener() {
        clientMain.setSelectButtonListener(e -> {
            final JFileChooser fc = new JFileChooser();
            int returnVal = fc.showOpenDialog(clientMain);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                clientMain.setFilenames(file.getName());
                fileToSend = file;
            }
        });
    }

    private void setCloseListener() {
        clientMain.setCloseButtonListener(e -> {
            frame.setContentPane(mainPanel);
            frame.pack();
            client.closeConnection();
        });
    }

    private void setUpdateListener() {
        clientMain.setUpdateButtonListener(e -> {
            if (client.updateFiles() == 0) {
                clientMain.fillTable(client.getFiles());
            }
        });
    }

    private void setUpdateUsersListener() {
        clientMain.setUpdateUsersButtonListener(e -> {
            if (client.updateUsers() == 0) {
                clientMain.fillUsersList(client.getUsers());
            }
        });
    }

    private void setDownloadListener() {
        clientMain.setDownloadButtonListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Выберите директорию для сохранения");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);
            if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                client.downloadFile(chooser.getSelectedFile().getAbsolutePath(), clientMain.getSelectedFiles());
            }
        });
    }

    private void setSendFileListener() {
        clientMain.setSendButtonListener(e -> {
            if (client.sendFile(clientMain.getReceiver(), fileToSend) != 0) {
                showMessage();
            }
        });
    }
}
