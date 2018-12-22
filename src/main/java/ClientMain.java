import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ClientMain extends JFrame  {
    private JList userList;
    private JButton sendButton;
    private JButton selectButton;
    private JButton closeButton;
    private JLabel filenames;
    private JPanel mainPanel;
    private JTabbedPane tabbedPane1;
    private JTable table1;

    public JPanel getMainPanel() {
        return mainPanel;
    }
    private JButton downloadButton;
    private JButton updateButton;
    private JButton updateUsersButton;

    private String[] columnNames = {"Имя файла", "Отправитель"};


    public ClientMain() {
        this.setContentPane(this.mainPanel);
        this.pack();
    }

    public String getReceiver() {
        if (userList.getSelectedValue() != null) {
            return userList.getSelectedValue().toString();
        } else {
            return "";
        }
    }

    public void setSendButtonListener(ActionListener l) {
        sendButton.addActionListener(l);
    }

    public void setSelectButtonListener(ActionListener l) {
        selectButton.addActionListener(l);
    }

    public void setCloseButtonListener(ActionListener l) {
        closeButton.addActionListener(l);
    }

    public void setFilenames(String text) {
        filenames.setText(text);
    }

    public String getFilenames() {
        return filenames.getText();
    }

    public void setDownloadButtonListener(ActionListener l) {
        downloadButton.addActionListener(l);
    }

    public void setUpdateButtonListener(ActionListener l) {
        updateButton.addActionListener(l);
    }

    public void setUpdateUsersButtonListener(ActionListener l) {
        updateUsersButton.addActionListener(l);
    }

    public void fillTable(List<FileData> files) {
        String[][] data = new String[files.size()][2];
        int i = 0;
        for (FileData file : files) {
            data[i][0] = file.getName();
            data[i][1] = file.getSender();
            i++;
        }
        TableModel model = new DefaultTableModel(data, columnNames);
        table1.setModel(model);
    }

    public void fillUsersList(List<String> users) {
        DefaultListModel model = new DefaultListModel();
        users.forEach(model::addElement);
        userList.setModel(model);
    }

    public List<String> getSelectedFiles() {
        List<String> res = new ArrayList<>();
        int[] selectedRows = table1.getSelectedRows();
        for (int i = 0; i < selectedRows.length; i++) {
            res.add(table1.getModel().getValueAt(selectedRows[i], 0).toString());
        }
        return res;
    }
}
