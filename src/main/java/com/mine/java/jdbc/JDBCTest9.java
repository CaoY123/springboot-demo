package com.mine.java.jdbc;

import javax.sql.RowSet;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author CaoY
 * @date 2023-07-04 21:48
 * @description 数据库编程 - 元数据
 *
 * 1. 在 SQL 中，描述数据库或其组成部分的数据称为元数据（区别于那些存在数据库中的实际数据）
 * 我们可以获得三类元数据：关于数据库的元数据、关于结果集的元数据、关于预备语句参数的元数据
 *
 * 2. 下面是一个简单的数据库工具，由《Java核心技术卷2提供》，演示了通过使用元数据来浏览数据库所有表，
 * 还演示了如何使用缓存的行集。虽然有一些BUG，但是基本能起到演示的效果。
 *
 * 前提：
 *  1）需要确保已经导入 MySQL 驱动程序
 *
 * 注意：确保配置正确自己的数据库基本信息，包括：url、驱动类名、用户名和密码。
 */
public class JDBCTest9 {

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            ViewDBFrame frame = new ViewDBFrame();
            frame.setTitle("ViewDB");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }

    // 保存数据面板和导航按钮的框架
    private static class ViewDBFrame extends JFrame {
        private JButton previousButton;
        private JButton nextButton;
        private JButton deleteButton;
        private JButton saveButton;
        private DataPanel dataPanel;
        private Component scrollPane;
        private JComboBox<String> tableNames;
        private Properties props;
        private CachedRowSet crs;
        private Connection conn;

        public ViewDBFrame() {
            tableNames = new JComboBox<String>();

            try {
                readDatabaseProperties();
                conn = getConnection();
                DatabaseMetaData meta = conn.getMetaData();
                // 下面的元数据表信息获取的时候需要指定具体的数据库名，否则就会将所有数据库都获取到
                try (ResultSet mrs = meta.getTables("corejava", null, null, new String[]{"TABLE"})) {
                    while (mrs.next()) {
                        tableNames.addItem(mrs.getString("TABLE_NAME"));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                for (Throwable t : e) {
                    t.printStackTrace();
                }
            }

            tableNames.addActionListener(event -> showTable((String) tableNames.getSelectedItem(), conn));
            add(tableNames, BorderLayout.NORTH);
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent event) {
                    try {
                        if (conn != null) {
                            conn.close();
                        }
                    } catch (SQLException e) {
                        for (Throwable t : e) {
                            t.printStackTrace();
                        }
                    }
                }
            });

            JPanel buttonPanel = new JPanel();
            add(buttonPanel, BorderLayout.SOUTH);

            previousButton = new JButton("Previous");
            previousButton.addActionListener(event -> showPreviousRow());
            buttonPanel.add(previousButton);

            nextButton = new JButton("Next");
            nextButton.addActionListener(event -> showNextRow());
            buttonPanel.add(nextButton);

            deleteButton = new JButton("Delete");
            deleteButton.addActionListener(event -> deleteRow());
            buttonPanel.add(deleteButton);

            saveButton = new JButton("Save");
            saveButton.addActionListener(event -> saveChanges());
            buttonPanel.add(saveButton);
            if (tableNames.getItemCount() > 0) {
                showTable(tableNames.getItemAt(0), conn);
            }
        }

        // 为显示新表准备文本字段，并显示第一行
        public void showTable(String tableName, Connection conn) {
            try (Statement stat = conn.createStatement();
            ResultSet result = stat.executeQuery("SELECT * FROM " + tableName)) {

                // 拷贝到缓存行集
                RowSetFactory factory = RowSetProvider.newFactory();
                crs = factory.createCachedRowSet();
                crs.setTableName(tableName);
                crs.populate(result);

                if (scrollPane != null) {
                    remove(scrollPane);
                }
                dataPanel = new DataPanel(crs);
                scrollPane = new JScrollPane(dataPanel);
                add(scrollPane, BorderLayout.CENTER);
                pack();
                showNextRow();
            } catch (SQLException e) {
                for (Throwable t : e) {
                    t.printStackTrace();
                }
            }
        }

        // 移动到上一个表行
        public void showPreviousRow() {
            try {
                if (crs == null || crs.isFirst()) {
                    return;
                }
                crs.previous();
                dataPanel.showRow(crs);
            } catch (SQLException e) {
                for (Throwable t : e) {
                    t.printStackTrace();
                }
            }
        }

        // 移动到下一个表行
        public void showNextRow() {
            try {
                if (crs == null || crs.isLast()) {
                    return;
                }
                crs.next();
                dataPanel.showRow(crs);
            } catch (SQLException e) {
                for (Throwable t : e) {
                    t.printStackTrace();
                }
            }
        }

        // 删除当前表的行
        public void deleteRow() {
            if (crs == null) {
                return;
            }
            new SwingWorker<Void, Void>() {

                @Override
                protected Void doInBackground() throws Exception {
                    crs.deleteRow();
                    crs.acceptChanges(conn);
                    if (crs.isAfterLast()) {
                        if (!crs.last()) {
                            crs = null;
                        }
                    }
                    return null;
                }

                @Override
                public void done() {
                    dataPanel.showRow(crs);
                }
            }.execute();
        }

        // 保存所有的修改
        public void saveChanges() {
            if (crs == null) {
                return;
            }
            new SwingWorker<Void, Void>() {

                @Override
                protected Void doInBackground() throws Exception {
                    dataPanel.setRow(crs);
                    crs.updateRow();
                    crs.acceptChanges(conn);
                    return null;
                }
            }.execute();
        }

        // 读取数据库连接配置信息，此处要保证需要的数据库驱动已经导入
        private void readDatabaseProperties() throws IOException {
            props = new Properties();
            Path propsPath = Paths.get("src", "main", "resources",
                    "jdbc", "database1.properties");
            try (InputStream in = Files.newInputStream(propsPath)) {
                props.load(in);
            }
        }

        private Connection getConnection() throws SQLException {
            String url = props.getProperty("jdbc.url");
            String username = props.getProperty("jdbc.username");
            String password = props.getProperty("jdbc.password");

            return DriverManager.getConnection(url, username, password);
        }
    }

    // 该面板显示结果集的内容
    private static class DataPanel extends JPanel {
        private List<JTextField> fields;

        // 构造数据面板
        public DataPanel(RowSet rs) throws SQLException {
            fields = new ArrayList<>();
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = 1;
            gbc.gridheight = 1;

            ResultSetMetaData rsmd = rs.getMetaData();
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                gbc.gridy = i - 1;

                String columnName = rsmd.getColumnLabel(i);
                gbc.gridx = 0;
                gbc.anchor = GridBagConstraints.EAST;
                add(new JLabel(columnName), gbc);

                int columnWidth = rsmd.getColumnDisplaySize(i);
                JTextField tb = new JTextField(columnWidth);
                if (!"java.lang.String".equals(rsmd.getColumnClassName(i))) {
                    tb.setEditable(false);
                }

                fields.add(tb);

                gbc.gridx = 1;
                gbc.anchor = GridBagConstraints.WEST;
                add(tb, gbc);
            }
        }

        // 通过用列值填充所有文本字段来显示数据库行
        public void showRow(ResultSet rs) {
            try {
                if (rs == null) {
                    return;
                }
                for (int i = 1; i <= fields.size(); i++) {
                    String field = rs == null ? "" : rs.getString(i);
                    JTextField tb = fields.get(i - 1);
                    tb.setText(field);
                }
            } catch (SQLException e) {
                for (Throwable t : e) {
                    t.printStackTrace();
                }
            }
        }

        // 将更改的数据更新到行集的当前行
        public void setRow(RowSet rs) throws SQLException {
            for (int i = 1; i <= fields.size(); i++) {
                String field = rs.getString(i);
                JTextField tb = fields.get(i - 1);
                if (!field.equals(tb.getText())) {
                    rs.updateString(i, tb.getText());
                }
            }
            rs.updateRow();
        }
    }
}
