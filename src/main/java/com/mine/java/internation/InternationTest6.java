package com.mine.java.internation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.Collator;
import java.util.*;
import java.util.List;

/**
 * @author CaoY
 * @date 2023-07-09 15:46
 * @description 国际化的学习 - 排序和规范化
 *
 * 可以通过 locale 来影响排序的行为，通过设置排序的强度和分解的程度来判断
 * 同一字符的在不同 locale 下是否相等。
 *
 * 下面是一个通过调节不同的 locale 以及强度和分解程度来判断两个字符串是否相等
 */
public class InternationTest6 {

    public static void main(String[] args)
    {
        EventQueue.invokeLater(() ->
        {
            CollationFrame frame = new CollationFrame();
            frame.setTitle("CollationTest");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }

    private static class CollationFrame extends JFrame
    {
        private Collator collator = Collator.getInstance(Locale.getDefault());
        private List<String> strings = new ArrayList<>();
        private Collator currentCollator;
        private Locale[] locales;
        private JComboBox<String> localeCombo = new JComboBox<>();
        private JTextField newWord = new JTextField(20);
        private JTextArea sortedWords = new JTextArea(20, 20);
        private JButton addButton = new JButton("Add");
        private EnumCombo<Integer> strengthCombo = new EnumCombo<>(Collator.class, "Primary",
                "Secondary", "Tertiary", "Identical");
        private EnumCombo<Integer> decompositionCombo = new EnumCombo<>(Collator.class,
                "Canonical Decomposition", "Full Decomposition", "No Decomposition");

        public CollationFrame()
        {
            setLayout(new GridBagLayout());
            add(new JLabel("Locale"), new GBC(0, 0).setAnchor(GBC.EAST));
            add(new JLabel("Strength"), new GBC(0, 1).setAnchor(GBC.EAST));
            add(new JLabel("Decomposition"), new GBC(0, 2).setAnchor(GBC.EAST));
            add(addButton, new GBC(0, 3).setAnchor(GBC.EAST));
            add(localeCombo, new GBC(1, 0).setAnchor(GBC.WEST));
            add(strengthCombo, new GBC(1, 1).setAnchor(GBC.WEST));
            add(decompositionCombo, new GBC(1, 2).setAnchor(GBC.WEST));
            add(newWord, new GBC(1, 3).setFill(GBC.HORIZONTAL));
            add(new JScrollPane(sortedWords), new GBC(0, 4, 2, 1).setFill(GBC.BOTH));

            locales = (Locale[]) Collator.getAvailableLocales().clone();
            Arrays.sort(locales,
                    (l1, l2) -> collator.compare(l1.getDisplayName(), l2.getDisplayName()));
            for (Locale loc : locales)
                localeCombo.addItem(loc.getDisplayName());
            localeCombo.setSelectedItem(Locale.getDefault().getDisplayName());

            strings.add("America");
            strings.add("able");
            strings.add("Zulu");
            strings.add("zebra");
            strings.add("\u00C5ngstr\u00F6m");
            strings.add("A\u030angstro\u0308m");
            strings.add("Angstrom");
            strings.add("Able");
            strings.add("office");
            strings.add("o\uFB03ce");
            strings.add("Java\u2122");
            strings.add("JavaTM");
            updateDisplay();

            addButton.addActionListener(event ->
            {
                strings.add(newWord.getText());
                updateDisplay();
            });

            ActionListener listener = event -> updateDisplay();

            localeCombo.addActionListener(listener);
            strengthCombo.addActionListener(listener);
            decompositionCombo.addActionListener(listener);
            pack();
        }

        /**
         * 根据用户设置更新显示并整理字符串。
         */
        public void updateDisplay()
        {
            Locale currentLocale = locales[localeCombo.getSelectedIndex()];
            localeCombo.setLocale(currentLocale);

            // 返回一个指定 locale 的排序器
            currentCollator = Collator.getInstance(currentLocale);
            // 设置排序器的强度，更强的排序器可以区分更多的单词。
            currentCollator.setStrength(strengthCombo.getValue());
            // 设置排序器的分解模式，分解越细，判断两个单词是否相等就越严格
            currentCollator.setDecomposition(decompositionCombo.getValue());

            strings.sort(currentCollator);

            sortedWords.setText("");
            for (int i = 0; i < strings.size(); i++)
            {
                String s = strings.get(i);
                if (i > 0 && currentCollator.compare(s, strings.get(i - 1)) == 0)
                    sortedWords.append("= ");
                sortedWords.append(s + "\n");
            }
            pack();
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
}
