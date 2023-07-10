package com.mine.java.internation;

import com.mine.java.internation.assist.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * @author CaoY
 * @date 2023-07-10 16:22
 * @description 国际化的学习 - 一个完整的国际化的例子（退休金小程序），可以选择 中文、英文、德文三种显示
 *
 * 注意：使用 assist 包下的类以及 resources/retire 下的 properties 文件辅助实现。
 *
 * 实现选择三种语言功能的流程：
 *  1）locales数组：该数组包含了三种语言的Locale对象，分别是美国英语（Locale.US）、中文（Locale.CHINA）和
 *  德语（Locale.GERMANY）。
 *
 *  2）localeCombo组件：这是一个下拉列表框（JComboBox），用于选择当前的语言。
 *  它使用了自定义的LocaleCombo类，该类继承自JComboBox，并在构造函数中将locales数组作为下拉列表框的选项。
 *
 *  3）setCurrentLocale()方法：该方法用于设置当前的语言环境和资源束。
 *  它接受一个Locale对象作为参数，并将其设置为当前语言环境。然后，
 *  它使用ResourceBundle.Control来加载相应语言环境的资源束文件，
 *  根据当前语言环境获取对应的资源束对象（res和resStrings），并使用NumberFormat来设置货币、数字和百分比的格式。
 *
 *  4）updateDisplay()方法：该方法用于更新窗口中的标签显示文本。
 *  它通过resStrings对象获取对应语言环境的资源字符串，然后将这些字符串设置为各个标签的文本。
 *
 *  5）updateInfo()方法：该方法用于更新文本字段中的信息。类似于updateDisplay()方法，
 *  它使用resStrings对象获取对应语言环境的资源字符串，并将这些字符串设置为各个文本字段的文本。
 *
 *  6）updateData()方法：该方法用于更新文本区域中的数据显示。
 *  它使用resStrings对象获取对应语言环境的资源字符串，然后根据这些字符串构建MessageFormat对象，
 *  并通过格式化操作将数据插入到字符串中，最后将格式化后的字符串追加到文本区域中。
 */
public class InternationTest8 {

    public static void main(String[] args)
    {
        EventQueue.invokeLater(() ->
        {
            RetireFrame frame = new RetireFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }

    public static class RetireFrame extends JFrame
    {
        private JTextField savingsField = new JTextField(10);
        private JTextField contribField = new JTextField(10);
        private JTextField incomeField = new JTextField(10);
        private JTextField currentAgeField = new JTextField(4);
        private JTextField retireAgeField = new JTextField(4);
        private JTextField deathAgeField = new JTextField(4);
        private JTextField inflationPercentField = new JTextField(6);
        private JTextField investPercentField = new JTextField(6);
        private JTextArea retireText = new JTextArea(10, 25);
        private RetireComponent retireCanvas = new RetireComponent();
        private JButton computeButton = new JButton();
        private JLabel languageLabel = new JLabel();
        private JLabel savingsLabel = new JLabel();
        private JLabel contribLabel = new JLabel();
        private JLabel incomeLabel = new JLabel();
        private JLabel currentAgeLabel = new JLabel();
        private JLabel retireAgeLabel = new JLabel();
        private JLabel deathAgeLabel = new JLabel();
        private JLabel inflationPercentLabel = new JLabel();
        private JLabel investPercentLabel = new JLabel();
        private RetireInfo info = new RetireInfo();
        private Locale[] locales = { Locale.US, Locale.CHINA, Locale.GERMANY };// 包含英文、中文、德文
        private Locale currentLocale;
        private JComboBox<Locale> localeCombo = new LocaleCombo(locales);
        private ResourceBundle res;
        private ResourceBundle resStrings;
        private NumberFormat currencyFmt;
        private NumberFormat numberFmt;
        private NumberFormat percentFmt;

        public RetireFrame()
        {
            setLayout(new GridBagLayout());
            add(languageLabel, new GBC(0, 0).setAnchor(GBC.EAST));
            add(savingsLabel, new GBC(0, 1).setAnchor(GBC.EAST));
            add(contribLabel, new GBC(2, 1).setAnchor(GBC.EAST));
            add(incomeLabel, new GBC(4, 1).setAnchor(GBC.EAST));
            add(currentAgeLabel, new GBC(0, 2).setAnchor(GBC.EAST));
            add(retireAgeLabel, new GBC(2, 2).setAnchor(GBC.EAST));
            add(deathAgeLabel, new GBC(4, 2).setAnchor(GBC.EAST));
            add(inflationPercentLabel, new GBC(0, 3).setAnchor(GBC.EAST));
            add(investPercentLabel, new GBC(2, 3).setAnchor(GBC.EAST));
            add(localeCombo, new GBC(1, 0, 3, 1));
            add(savingsField, new GBC(1, 1).setWeight(100, 0).setFill(GBC.HORIZONTAL));
            add(contribField, new GBC(3, 1).setWeight(100, 0).setFill(GBC.HORIZONTAL));
            add(incomeField, new GBC(5, 1).setWeight(100, 0).setFill(GBC.HORIZONTAL));
            add(currentAgeField, new GBC(1, 2).setWeight(100, 0).setFill(GBC.HORIZONTAL));
            add(retireAgeField, new GBC(3, 2).setWeight(100, 0).setFill(GBC.HORIZONTAL));
            add(deathAgeField, new GBC(5, 2).setWeight(100, 0).setFill(GBC.HORIZONTAL));
            add(inflationPercentField, new GBC(1, 3).setWeight(100, 0).setFill(GBC.HORIZONTAL));
            add(investPercentField, new GBC(3, 3).setWeight(100, 0).setFill(GBC.HORIZONTAL));
            add(retireCanvas, new GBC(0, 4, 4, 1).setWeight(100, 100).setFill(GBC.BOTH));
            add(new JScrollPane(retireText),
                    new GBC(4, 4, 2, 1).setWeight(0, 100).setFill(GBC.BOTH));

            computeButton.setName("computeButton");
            computeButton.addActionListener(event ->
            {
                getInfo();
                updateData();
                updateGraph();
            });
            add(computeButton, new GBC(5, 3));

            retireText.setEditable(false);
            retireText.setFont(new Font("Monospaced", Font.PLAIN, 10));

            info.setSavings(0);
            info.setContrib(9000);
            info.setIncome(60000);
            info.setCurrentAge(35);
            info.setRetireAge(65);
            info.setDeathAge(85);
            info.setInvestPercent(0.1);
            info.setInflationPercent(0.05);

            int localeIndex = 0; // US locale is default selection
            for (int i = 0; i < locales.length; i++)
                // if current locale one of the choices, select it
                if (getLocale().equals(locales[i])) localeIndex = i;
            setCurrentLocale(locales[localeIndex]);

            localeCombo.addActionListener(event ->
            {
                setCurrentLocale((Locale) localeCombo.getSelectedItem());
                validate();
            });
            pack();
        }

        /**
         * 设置当前 locale。
         * @param locale the desired locale
         */
        public void setCurrentLocale(Locale locale)
        {
            currentLocale = locale;
            localeCombo.setLocale(currentLocale);
            localeCombo.setSelectedItem(currentLocale);

            // 用于解决读入的中文乱码问题，通过自定义 Control 来指定读入资源采用的编码格式为 UTF-8
            ResourceBundle.Control control = new ResourceBundle.Control() {
                @Override
                public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) throws IllegalAccessException, InstantiationException, IOException {
                    String bundleName = toBundleName(baseName, locale);
                    String resourceName = toResourceName(bundleName, "properties");
                    try (InputStream stream = loader.getResourceAsStream(resourceName)) {
                        return new PropertyResourceBundle(new InputStreamReader(stream, StandardCharsets.UTF_8));
                    }
                }
            };

            // 读取下列类和 properties 资源时，需要注意路径和所用的类加载器，可以通过观察
            // java.util.ResourceBundle.getBundleImpl 中 loader 加载的路径来判断是否能读取到资源文件
            res = ResourceBundle.getBundle("com.mine.java.internation.assist.RetireResources", currentLocale);
            // 需要设置相应的类加载器才能加载到位于 src/main/resources/retire/RetireStrings.properties
            resStrings = ResourceBundle.getBundle("retire.RetireStrings", currentLocale, getClass().getClassLoader(), control);
            currencyFmt = NumberFormat.getCurrencyInstance(currentLocale);
            numberFmt = NumberFormat.getNumberInstance(currentLocale);
            percentFmt = NumberFormat.getPercentInstance(currentLocale);

            updateDisplay();
            updateInfo();
            updateData();
            updateGraph();
        }

        /**
         * Updates all labels in the display.
         */
        public void updateDisplay()
        {
            languageLabel.setText(resStrings.getString("language"));
            savingsLabel.setText(resStrings.getString("savings"));
            contribLabel.setText(resStrings.getString("contrib"));
            incomeLabel.setText(resStrings.getString("income"));
            currentAgeLabel.setText(resStrings.getString("currentAge"));
            retireAgeLabel.setText(resStrings.getString("retireAge"));
            deathAgeLabel.setText(resStrings.getString("deathAge"));
            inflationPercentLabel.setText(resStrings.getString("inflationPercent"));
            investPercentLabel.setText(resStrings.getString("investPercent"));
            computeButton.setText(resStrings.getString("computeButton"));
        }

        /**
         * Updates the information in the text fields.
         */
        public void updateInfo()
        {
            savingsField.setText(currencyFmt.format(info.getSavings()));
            contribField.setText(currencyFmt.format(info.getContrib()));
            incomeField.setText(currencyFmt.format(info.getIncome()));
            currentAgeField.setText(numberFmt.format(info.getCurrentAge()));
            retireAgeField.setText(numberFmt.format(info.getRetireAge()));
            deathAgeField.setText(numberFmt.format(info.getDeathAge()));
            investPercentField.setText(percentFmt.format(info.getInvestPercent()));
            inflationPercentField.setText(percentFmt.format(info.getInflationPercent()));
        }

        /**
         * Updates the data displayed in the text area.
         */
        public void updateData()
        {
            retireText.setText("");
            MessageFormat retireMsg = new MessageFormat("");
            retireMsg.setLocale(currentLocale);
            retireMsg.applyPattern(resStrings.getString("retire"));

            for (int i = info.getCurrentAge(); i <= info.getDeathAge(); i++)
            {
                Object[] args = { i, info.getBalance(i) };
                retireText.append(retireMsg.format(args) + "\n");
            }
        }

        /**
         * Updates the graph.
         */
        public void updateGraph()
        {
            retireCanvas.setColorPre((Color) res.getObject("colorPre"));
            retireCanvas.setColorGain((Color) res.getObject("colorGain"));
            retireCanvas.setColorLoss((Color) res.getObject("colorLoss"));
            retireCanvas.setInfo(info);
            repaint();
        }

        /**
         * Reads the user input from the text fields.
         */
        public void getInfo()
        {
            try
            {
                info.setSavings(currencyFmt.parse(savingsField.getText()).doubleValue());
                info.setContrib(currencyFmt.parse(contribField.getText()).doubleValue());
                info.setIncome(currencyFmt.parse(incomeField.getText()).doubleValue());
                info.setCurrentAge(numberFmt.parse(currentAgeField.getText()).intValue());
                info.setRetireAge(numberFmt.parse(retireAgeField.getText()).intValue());
                info.setDeathAge(numberFmt.parse(deathAgeField.getText()).intValue());
                info.setInvestPercent(percentFmt.parse(investPercentField.getText()).doubleValue());
                info.setInflationPercent(
                        percentFmt.parse(inflationPercentField.getText()).doubleValue());
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * The information required to compute retirement income data.
     */
    public static class RetireInfo
    {
        private double savings;
        private double contrib;
        private double income;
        private int currentAge;
        private int retireAge;
        private int deathAge;
        private double inflationPercent;
        private double investPercent;
        private int age;
        private double balance;

        /**
         * Gets the available balance for a given year.
         * @param year the year for which to compute the balance
         * @return the amount of money available (or required) in that year
         */
        public double getBalance(int year)
        {
            if (year < currentAge) return 0;
            else if (year == currentAge)
            {
                age = year;
                balance = savings;
                return balance;
            }
            else if (year == age) return balance;
            if (year != age + 1) getBalance(year - 1);
            age = year;
            if (age < retireAge) balance += contrib;
            else balance -= income;
            balance = balance * (1 + (investPercent - inflationPercent));
            return balance;
        }

        /**
         * Gets the amount of prior savings.
         * @return the savings amount
         */
        public double getSavings()
        {
            return savings;
        }

        /**
         * Sets the amount of prior savings.
         * @param newValue the savings amount
         */
        public void setSavings(double newValue)
        {
            savings = newValue;
        }

        /**
         * Gets the annual contribution to the retirement account.
         * @return the contribution amount
         */
        public double getContrib()
        {
            return contrib;
        }

        /**
         * Sets the annual contribution to the retirement account.
         * @param newValue the contribution amount
         */
        public void setContrib(double newValue)
        {
            contrib = newValue;
        }

        /**
         * Gets the annual income.
         * @return the income amount
         */
        public double getIncome()
        {
            return income;
        }

        /**
         * Sets the annual income.
         * @param newValue the income amount
         */
        public void setIncome(double newValue)
        {
            income = newValue;
        }

        /**
         * Gets the current age.
         * @return the age
         */
        public int getCurrentAge()
        {
            return currentAge;
        }

        /**
         * Sets the current age.
         * @param newValue the age
         */
        public void setCurrentAge(int newValue)
        {
            currentAge = newValue;
        }

        /**
         * Gets the desired retirement age.
         * @return the age
         */
        public int getRetireAge()
        {
            return retireAge;
        }

        /**
         * Sets the desired retirement age.
         * @param newValue the age
         */
        public void setRetireAge(int newValue)
        {
            retireAge = newValue;
        }

        /**
         * Gets the expected age of death.
         * @return the age
         */
        public int getDeathAge()
        {
            return deathAge;
        }

        /**
         * Sets the expected age of death.
         * @param newValue the age
         */
        public void setDeathAge(int newValue)
        {
            deathAge = newValue;
        }

        /**
         * Gets the estimated percentage of inflation.
         * @return the percentage
         */
        public double getInflationPercent()
        {
            return inflationPercent;
        }

        /**
         * Sets the estimated percentage of inflation.
         * @param newValue the percentage
         */
        public void setInflationPercent(double newValue)
        {
            inflationPercent = newValue;
        }

        /**
         * Gets the estimated yield of the investment.
         * @return the percentage
         */
        public double getInvestPercent()
        {
            return investPercent;
        }

        /**
         * Sets the estimated yield of the investment.
         * @param newValue the percentage
         */
        public void setInvestPercent(double newValue)
        {
            investPercent = newValue;
        }
    }

    /**
     * This component draws a graph of the investment result.
     */
    public static class RetireComponent extends JComponent
    {
        private static final int PANEL_WIDTH = 400;
        private static final int PANEL_HEIGHT = 200;
        private static final Dimension PREFERRED_SIZE = new Dimension(800, 600);
        private RetireInfo info = null;
        private Color colorPre;
        private Color colorGain;
        private Color colorLoss;

        public RetireComponent()
        {
            setSize(PANEL_WIDTH, PANEL_HEIGHT);
        }

        /**
         * Sets the retirement information to be plotted.
         * @param newInfo the new retirement info.
         */
        public void setInfo(RetireInfo newInfo)
        {
            info = newInfo;
            repaint();
        }

        public void paintComponent(Graphics g)
        {
            Graphics2D g2 = (Graphics2D) g;
            if (info == null) return;

            double minValue = 0;
            double maxValue = 0;
            int i;
            for (i = info.getCurrentAge(); i <= info.getDeathAge(); i++)
            {
                double v = info.getBalance(i);
                if (minValue > v) minValue = v;
                if (maxValue < v) maxValue = v;
            }
            if (maxValue == minValue) return;

            int barWidth = getWidth() / (info.getDeathAge() - info.getCurrentAge() + 1);
            double scale = getHeight() / (maxValue - minValue);

            for (i = info.getCurrentAge(); i <= info.getDeathAge(); i++)
            {
                int x1 = (i - info.getCurrentAge()) * barWidth + 1;
                int y1;
                double v = info.getBalance(i);
                int height;
                int yOrigin = (int) (maxValue * scale);

                if (v >= 0)
                {
                    y1 = (int) ((maxValue - v) * scale);
                    height = yOrigin - y1;
                }
                else
                {
                    y1 = yOrigin;
                    height = (int) (-v * scale);
                }

                if (i < info.getRetireAge()) g2.setPaint(colorPre);
                else if (v >= 0) g2.setPaint(colorGain);
                else g2.setPaint(colorLoss);
                Rectangle2D.Double bar = new Rectangle2D.Double(x1, y1, barWidth - 2, height);
                g2.fill(bar);
                g2.setPaint(Color.black);
                g2.draw(bar);
            }
        }

        /**
         * Sets the color to be used before retirement.
         * @param color the desired color
         */
        public void setColorPre(Color color)
        {
            colorPre = color;
            repaint();
        }

        /**
         * Sets the color to be used after retirement while the account balance is positive.
         * @param color the desired color
         */
        public void setColorGain(Color color)
        {
            colorGain = color;
            repaint();
        }

        /**
         * Sets the color to be used after retirement when the account balance is negative.
         * @param color the desired color
         */
        public void setColorLoss(Color color)
        {
            colorLoss = color;
            repaint();
        }

        public Dimension getPreferredSize() { return PREFERRED_SIZE; }
    }
}
