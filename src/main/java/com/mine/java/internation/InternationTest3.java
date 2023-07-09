package com.mine.java.internation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

/**
 * @author CaoY
 * @date 2023-07-09 1:17
 * @description 国际化的学习 - 数字格式
 *
 * 下面的练习输入一个数字，可以给出不同国家该数字的货币表示以及百分数表示。
 */
public class InternationTest3 {

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            NumberFormatFrame frame = new NumberFormatFrame();
            frame.setTitle("NumberFormatTest");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }

    // 该框架包含用于选择数字格式的按钮、用于选择区域设置的组合框、
    // 用于显示格式化数字的文本字段，以及用于解析文本字段内容的按钮。
    private static class NumberFormatFrame extends JFrame {
        private Locale[] locales;
        private double currentNumber;
        private JComboBox<String> localeCombo = new JComboBox<>();
        private JButton parseButton = new JButton("Parse");
        private JTextField numberText = new JTextField(30);
        private JRadioButton numberRatioButton = new JRadioButton("Number");
        private JRadioButton currencyRatioButton = new JRadioButton("Currency");
        private JRadioButton percentRatioButton = new JRadioButton("Percent");
        private ButtonGroup rbGroup = new ButtonGroup();
        private NumberFormat currentNumberFormat;

        public NumberFormatFrame() {
            setLayout(new GridBagLayout());

            ActionListener listener = event -> updateDisplay();

            JPanel p = new JPanel();
            addRatioButton(p, numberRatioButton, rbGroup, listener);
            addRatioButton(p, currencyRatioButton, rbGroup, listener);
            addRatioButton(p, percentRatioButton, rbGroup, listener);

            add(new JLabel("Locale"), new GBC(0, 0).setAnchor(GBC.EAST));
            add(p, new GBC(1, 1));
            add(parseButton, new GBC(0, 2).setInsets(2));
            add(localeCombo, new GBC(1, 0).setAnchor(GBC.WEST));
            add(numberText, new GBC(1, 2).setFill(GBC.HORIZONTAL));
            locales = (Locale[]) NumberFormat.getAvailableLocales().clone();
            Arrays.sort(locales, Comparator.comparing(Locale::getDisplayName));
            for (Locale loc : locales) {
                localeCombo.addItem(loc.getDisplayName());
            }
            localeCombo.setSelectedItem(Locale.getDefault().getDisplayName());
            currentNumber = 123456.78;
            updateDisplay();

            localeCombo.addActionListener(listener);

            parseButton.addActionListener(event -> {
                String s = numberText.getText().trim();
                try {
                    Number n = currentNumberFormat.parse(s);
                    currentNumber = n.doubleValue();
                    updateDisplay();
                } catch (ParseException e) {
                    numberText.setText(e.getMessage());
                }
            });
            pack();
        }

        /**
         * 向容器添加一个 Ratio Button
         * @param p         放置按钮的容器
         * @param b         按钮
         * @param g         按钮组
         * @param listener  按钮监听器
         */
        public void addRatioButton(Container p, JRadioButton b, ButtonGroup g, ActionListener listener) {
            b.setSelected(g.getButtonCount() == 0);
            b.addActionListener(listener);
            g.add(b);
            p.add(b);
        }

        // 根据用户设置更新显示并格式化数字
        public void updateDisplay() {
            Locale currentLocale = locales[localeCombo.getSelectedIndex()];
            currentNumberFormat = null;
            if (numberRatioButton.isSelected()) {
                currentNumberFormat = NumberFormat.getNumberInstance(currentLocale);
            } else if (currencyRatioButton.isSelected()) {
                currentNumberFormat = NumberFormat.getCurrencyInstance(currentLocale);
            } else if (percentRatioButton.isSelected()) {
                currentNumberFormat = NumberFormat.getPercentInstance(currentLocale);
            }
            String formatted = currentNumberFormat.format(currentNumber);
            numberText.setText(formatted);
        }
    }

    // GBC 是一个自定义的辅助类，用于简化 GridBagLayout 的使用
    private static class GBC extends GridBagConstraints
    {
        /**
         Constructs a GBC with a given gridx and gridy position and
         all other grid bag constraint values set to the default.
         @param gridx the gridx position
         @param gridy the gridy position
         */
        public GBC(int gridx, int gridy)
        {
            this.gridx = gridx;
            this.gridy = gridy;
        }

        /**
         Constructs a GBC with given gridx, gridy, gridwidth, gridheight
         and all other grid bag constraint values set to the default.
         @param gridx the gridx position
         @param gridy the gridy position
         @param gridwidth the cell span in x-direction
         @param gridheight the cell span in y-direction
         */
        public GBC(int gridx, int gridy, int gridwidth, int gridheight)
        {
            this.gridx = gridx;
            this.gridy = gridy;
            this.gridwidth = gridwidth;
            this.gridheight = gridheight;
        }

        /**
         Sets the anchor.
         @param anchor the anchor value
         @return this object for further modification
         */
        public GBC setAnchor(int anchor)
        {
            this.anchor = anchor;
            return this;
        }

        /**
         Sets the fill direction.
         @param fill the fill direction
         @return this object for further modification
         */
        public GBC setFill(int fill)
        {
            this.fill = fill;
            return this;
        }

        /**
         Sets the cell weights.
         @param weightx the cell weight in x-direction
         @param weighty the cell weight in y-direction
         @return this object for further modification
         */
        public GBC setWeight(double weightx, double weighty)
        {
            this.weightx = weightx;
            this.weighty = weighty;
            return this;
        }

        /**
         Sets the insets of this cell.
         @param distance the spacing to use in all directions
         @return this object for further modification
         */
        public GBC setInsets(int distance)
        {
            this.insets = new Insets(distance, distance, distance, distance);
            return this;
        }

        /**
         Sets the insets of this cell.
         @param top the spacing to use on top
         @param left the spacing to use to the left
         @param bottom the spacing to use on the bottom
         @param right the spacing to use to the right
         @return this object for further modification
         */
        public GBC setInsets(int top, int left, int bottom, int right)
        {
            this.insets = new Insets(top, left, bottom, right);
            return this;
        }

        /**
         Sets the internal padding
         @param ipadx the internal padding in x-direction
         @param ipady the internal padding in y-direction
         @return this object for further modification
         */
        public GBC setIpad(int ipadx, int ipady)
        {
            this.ipadx = ipadx;
            this.ipady = ipady;
            return this;
        }
    }
}
