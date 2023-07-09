package com.mine.java.internation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

/**
 * @author CaoY
 * @date 2023-07-09 15:11
 * @description 国际化的学习 - 日期和时间
 *
 * 下面代码给定了一个用户可以选择 locale（不通国家和语言）来展示不通日期和时间的格式的程序
 *
 */
public class InternationTest5 {

    public static void main(String[] args) {
        EventQueue.invokeLater(() ->
        {
            DateTimeFormatterFrame frame = new DateTimeFormatterFrame();
            frame.setTitle("DateFormatTest");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }

    private static class DateTimeFormatterFrame extends JFrame {
        private Locale[] locales;
        private LocalDate currentDate;
        private LocalTime currentTime;
        private ZonedDateTime currentDateTime;
        private DateTimeFormatter currentDateFormat;
        private DateTimeFormatter currentTimeFormat;
        private DateTimeFormatter currentDateTimeFormat;
        private JComboBox<String> localeCombo = new JComboBox<>();
        private JButton dateParseButton = new JButton("Parse");
        private JButton timeParseButton = new JButton("Parse");
        private JButton dateTimeParseButton = new JButton("Parse");
        private JTextField dateText = new JTextField(30);
        private JTextField timeText = new JTextField(30);
        private JTextField dateTimeText = new JTextField(30);
        private EnumCombo<FormatStyle> dateStyleCombo = new EnumCombo<>(FormatStyle.class,
                "Short", "Medium", "Long", "Full");
        private EnumCombo<FormatStyle> timeStyleCombo = new EnumCombo<>(FormatStyle.class,
                "Short", "Medium");
        private EnumCombo<FormatStyle> dateTimeStyleCombo = new EnumCombo<>(FormatStyle.class,
                "Short", "Medium", "Long", "Full");

        public DateTimeFormatterFrame()
        {
            setLayout(new GridBagLayout());
            add(new JLabel("Locale"), new GBC(0, 0).setAnchor(GBC.EAST));
            add(localeCombo, new GBC(1, 0, 2, 1).setAnchor(GBC.WEST));

            add(new JLabel("Date"), new GBC(0, 1).setAnchor(GBC.EAST));
            add(dateStyleCombo, new GBC(1, 1).setAnchor(GBC.WEST));
            add(dateText, new GBC(2, 1, 2, 1).setFill(GBC.HORIZONTAL));
            add(dateParseButton, new GBC(4, 1).setAnchor(GBC.WEST));

            add(new JLabel("Time"), new GBC(0, 2).setAnchor(GBC.EAST));
            add(timeStyleCombo, new GBC(1, 2).setAnchor(GBC.WEST));
            add(timeText, new GBC(2, 2, 2, 1).setFill(GBC.HORIZONTAL));
            add(timeParseButton, new GBC(4, 2).setAnchor(GBC.WEST));

            add(new JLabel("Date and time"), new GBC(0, 3).setAnchor(GBC.EAST));
            add(dateTimeStyleCombo, new GBC(1, 3).setAnchor(GBC.WEST));
            add(dateTimeText, new GBC(2, 3, 2, 1).setFill(GBC.HORIZONTAL));
            add(dateTimeParseButton, new GBC(4, 3).setAnchor(GBC.WEST));

            locales = (Locale[]) Locale.getAvailableLocales().clone();
            Arrays.sort(locales, Comparator.comparing(Locale::getDisplayName));
            for (Locale loc : locales)
                localeCombo.addItem(loc.getDisplayName());
            localeCombo.setSelectedItem(Locale.getDefault().getDisplayName());
            currentDate = LocalDate.now();
            currentTime = LocalTime.now();
            currentDateTime = ZonedDateTime.now();
            updateDisplay();

            ActionListener listener = event -> updateDisplay();
            localeCombo.addActionListener(listener);
            dateStyleCombo.addActionListener(listener);
            timeStyleCombo.addActionListener(listener);
            dateTimeStyleCombo.addActionListener(listener);

            addAction(dateParseButton, () ->
            {
                currentDate = LocalDate.parse(dateText.getText().trim(), currentDateFormat);
            });
            addAction(timeParseButton, () ->
            {
                currentTime = LocalTime.parse(timeText.getText().trim(), currentTimeFormat);
            });
            addAction(dateTimeParseButton, () ->
            {
                currentDateTime = ZonedDateTime.parse(
                        dateTimeText.getText().trim(), currentDateTimeFormat);
            });

            pack();
        }

        /**
         * 将给定的操作添加到按钮并在完成时更新显示。
         * @param button 要向其添加操作的按钮
         * @param action 单击按钮时要执行的操作
         */
        public void addAction(JButton button, Runnable action)
        {
            button.addActionListener(event ->
            {
                try
                {
                    action.run();
                    updateDisplay();
                }
                catch (Exception e)
                {
                    JOptionPane.showMessageDialog(null, e.getMessage());
                }
            });
        }

        /**
         * 根据用户设置更新显示并格式化日期
         */
        public void updateDisplay()
        {
            Locale currentLocale = locales[localeCombo.getSelectedIndex()];
            FormatStyle dateStyle = dateStyleCombo.getValue();
            currentDateFormat = DateTimeFormatter.ofLocalizedDate(
                    dateStyle).withLocale(currentLocale);
            dateText.setText(currentDateFormat.format(currentDate));
            FormatStyle timeStyle = timeStyleCombo.getValue();
            currentTimeFormat = DateTimeFormatter.ofLocalizedTime(
                    timeStyle).withLocale(currentLocale);
            timeText.setText(currentTimeFormat.format(currentTime));
            FormatStyle dateTimeStyle = dateTimeStyleCombo.getValue();
            currentDateTimeFormat = DateTimeFormatter.ofLocalizedDateTime(
                    dateTimeStyle).withLocale(currentLocale);
            dateTimeText.setText(currentDateTimeFormat.format(currentDateTime));
        }
    }

    private static class EnumCombo<T> extends JComboBox<String>
    {
        private Map<String, T> table = new TreeMap<>();

        /**
         * 构造一个产生T类型值的 EnumCombo
         * @param cl 一个类
         * @param labels 描述类型为T的cl的静态字段名的字符串数组
         */
        public EnumCombo(Class<?> cl, String... labels)
        {
            for (String label : labels)
            {
                String name = label.toUpperCase().replace(' ', '_');
                try
                {
                    java.lang.reflect.Field f = cl.getField(name);
                    @SuppressWarnings("unchecked") T value = (T) f.get(cl);
                    table.put(label, value);
                }
                catch (Exception e)
                {
                    label = "(" + label + ")";
                    table.put(label, null);
                }
                addItem(label);
            }
            setSelectedItem(labels[0]);
        }

        /**
         * Returns the value of the field that the user selected.
         * @return the static field value
         */
        public T getValue()
        {
            return table.get(getSelectedItem());
        }
    }

    private static class GBC extends GridBagConstraints
    {
        /**
         构造一个给定网格和网格位置的GBC，并将所有其他网格袋约束值设置为默认值。
         @param gridx X 坐标
         @param gridy Y 坐标
         */
        public GBC(int gridx, int gridy)
        {
            this.gridx = gridx;
            this.gridy = gridy;
        }

        /**
         构造一个GBC，将给定的gridx, gridy, gridwidth, gridheight和所有其他网格袋约束值设置为默认值。
         @param gridx X 坐标
         @param gridy Y坐标
         @param gridwidth 宽度（在X方向上的张量）
         @param gridheight 高度（在Y方向上的张量）
         */
        public GBC(int gridx, int gridy, int gridwidth, int gridheight)
        {
            this.gridx = gridx;
            this.gridy = gridy;
            this.gridwidth = gridwidth;
            this.gridheight = gridheight;
        }

        /**
         设置锚
         @param anchor 锚值
         @return 该对象可以进一步修改
         */
        public GBC setAnchor(int anchor)
        {
            this.anchor = anchor;
            return this;
        }

        /**
         设置填充方向。
         @param fill 填充方向
         @return 该对象可以进一步修改
         */
        public GBC setFill(int fill)
        {
            this.fill = fill;
            return this;
        }

        /**
         设置单元格权重。
         @param weightx 单元格在x方向上的权重
         @param weighty 单元格在y方向上的权重
         @return 该对象可以进一步修改
         */
        public GBC setWeight(double weightx, double weighty)
        {
            this.weightx = weightx;
            this.weighty = weighty;
            return this;
        }

        /**
         设置此单元格的插页。
         @param distance 在所有方向上使用的间距
         @return 该对象可以进一步修改
         */
        public GBC setInsets(int distance)
        {
            this.insets = new Insets(distance, distance, distance, distance);
            return this;
        }

        /**
         设置此单元格的插页。
         @param top 顶部的间距
         @param left 左边要使用的间距
         @param bottom 在底部使用的间距
         @param right 向右使用的间距
         @return 该对象可以进一步修改
         */
        public GBC setInsets(int top, int left, int bottom, int right)
        {
            this.insets = new Insets(top, left, bottom, right);
            return this;
        }

        /**
         设置内部填充
         @param ipadx x方向的内部填充
         @param ipady y方向的内部填充
         @return 该对象可以进一步修改
         */
        public GBC setIpad(int ipadx, int ipady)
        {
            this.ipadx = ipadx;
            this.ipady = ipady;
            return this;
        }
    }
}
