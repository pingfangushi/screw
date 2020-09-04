package main.java.cn.smallbun.screw.idea.ui;

import cn.smallbun.screw.idea.model.ConfigStatement;
import cn.smallbun.screw.idea.model.DataSourceConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.event.*;

public class DatasourcePanel extends JDialog {

    private JPanel contentPane;
    private JComboBox driver;
    private JTextField url;
    private JTextField username;
    private JComboBox pattern;
    private JTextField password;
    private JTextField filename;
    private JCheckBox isOpen;
    private JTextArea ignoreTable;
    private JTextArea ignorePrefix;
    private JTextArea ignoreSuffix;
    private JTextField desc;


    public DatasourcePanel() {
        setContentPane(contentPane);
        setModal(true);
        isOpen.setSelected(true);

        DataSourceConfig config = ConfigStatement.getDataSourceConfig();
        if (config != null) {
            driver.setSelectedItem(config.getDriver());
            url.setText(config.getUrl());
            username.setText(config.getUserName());
            password.setText(config.getPassword());
            filename.setText(config.getFileName());
            isOpen.setSelected(config.getOpen());
            desc.setText(config.getDesc());
            if (CollectionUtils.isNotEmpty(config.getIgnoreTable())) {
                ignoreTable.setText(StringUtils.join(config.getIgnoreTable(),"\n"));
            }
            if (CollectionUtils.isNotEmpty(config.getIgnorePrefix())) {
                ignorePrefix.setText(StringUtils.join(config.getIgnorePrefix(),"\n"));
            }
            if (CollectionUtils.isNotEmpty(config.getIgnoreSuffix())) {
                ignoreSuffix.setText(StringUtils.join(config.getIgnoreSuffix(),"\n"));
            }
        }

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        System.out.println("success");
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        DatasourcePanel dialog = new DatasourcePanel();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    public JPanel getCenter() {
        return contentPane;
    }

    public JComboBox getDriver() {
        return driver;
    }

    public void setDriver(JComboBox driver) {
        this.driver = driver;
    }

    public JTextField getUrl() {
        return url;
    }

    public void setUrl(JTextField url) {
        this.url = url;
    }

    public JTextField getUsername() {
        return username;
    }

    public void setUsername(JTextField username) {
        this.username = username;
    }

    public JComboBox getPattern() {
        return pattern;
    }

    public void setPattern(JComboBox pattern) {
        this.pattern = pattern;
    }

    public JTextField getPassword() {
        return password;
    }

    public void setPassword(JTextField password) {
        this.password = password;
    }

    public JTextField getFilename() {
        return filename;
    }

    public void setFilename(JTextField filename) {
        this.filename = filename;
    }

    public JCheckBox getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(JCheckBox isOpen) {
        this.isOpen = isOpen;
    }

    public JTextArea getIgnoreTable() {
        return ignoreTable;
    }

    public void setIgnoreTable(JTextArea ignoreTable) {
        this.ignoreTable = ignoreTable;
    }

    public JTextArea getIgnorePrefix() {
        return ignorePrefix;
    }

    public void setIgnorePrefix(JTextArea ignorePrefix) {
        this.ignorePrefix = ignorePrefix;
    }

    public JTextArea getIgnoreSuffix() {
        return ignoreSuffix;
    }

    public void setIgnoreSuffix(JTextArea ignoreSuffix) {
        this.ignoreSuffix = ignoreSuffix;
    }

    public JTextField getDesc() {
        return desc;
    }

    public void setDesc(JTextField desc) {
        this.desc = desc;
    }
}
