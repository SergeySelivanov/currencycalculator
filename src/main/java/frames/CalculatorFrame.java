package frames;

import org.apache.commons.lang.StringUtils;
import org.jdesktop.swingx.JXDatePicker;
import org.json.simple.parser.ParseException;
import services.ExchangeRatesService;
import services.ProfitCalculateService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;

/**
 * Контейнер для окна с расчтом прибыли/убытков
 */
public class CalculatorFrame extends JFrame {
    private JButton button = new JButton("Расчитать");
    private JFormattedTextField amountField = new JFormattedTextField(NumberFormat.getInstance());
    private JTextField profitField = new JTextField();
    private JXDatePicker datePicker = new JXDatePicker();
    private JTextArea errorMassageLabel = new JTextArea();

    public CalculatorFrame() {
        super("Title");
        this.setBounds(300, 300, 350, 250);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel amountLabel = new JLabel("Сумма купленных EUR:");
        amountLabel.setBounds(10, 10, 170, 30);
        amountField.setBounds(180, 10, 150, 30);
        panel.add(amountLabel);
        panel.add(amountField);

        JLabel datePickerLabel = new JLabel("Дата покупки EUR:");
        datePickerLabel.setBounds(10, 50, 170, 30);
        datePicker.setBounds(180, 50, 150, 30);
        panel.add(datePickerLabel);
        panel.add(datePicker);

        JLabel profitLabel = new JLabel("Прибыль после продажи:");
        profitLabel.setBounds(10, 90, 170, 30);
        profitField.setBounds(180, 90, 150, 30);
        profitField.setEditable(false);
        panel.add(profitLabel);
        panel.add(profitField);

        button.setBounds(10, 130, 150, 30);
        button.addActionListener(new CalculatorButtonListener());
        panel.add(button);

        errorMassageLabel.setBounds(10, 170, 320, 40);
        errorMassageLabel.setLineWrap(true);
        errorMassageLabel.setEditable(false);
        errorMassageLabel.setVisible(false);
        panel.add(errorMassageLabel);

        this.getContentPane().add(panel);
    }

    /**
     * Класс слушатель, для обработки нажати кнопки "расчитать"
     */
    private class CalculatorButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (StringUtils.isBlank(amountField.getText())) {
                errorMassageLabel.setVisible(true);
                errorMassageLabel.setText("Необходимо заполнить поле \"Сумма\"");
            } else if (datePicker.getDate() == null || datePicker.getDate().after(new Date())) {
                errorMassageLabel.setVisible(true);
                errorMassageLabel.setText("Необходимо корректно заполнить поле \"Дата покупки\"");
            } else {
                errorMassageLabel.setVisible(false);
                errorMassageLabel.setText(StringUtils.EMPTY);
                try {
                    BigDecimal startPrice = ExchangeRatesService.getExchangeRate(datePicker.getDate());
                    BigDecimal endPrice = ExchangeRatesService.getExchangeRate(new Date());
                    BigDecimal amount = new BigDecimal((Long) amountField.getValue());
                    BigDecimal profit = ProfitCalculateService.calculateProfit(startPrice, endPrice, amount);
                    profitField.setText(profit.toString());
                } catch (IOException e1) {
                    errorMassageLabel.setVisible(true);
                    errorMassageLabel.setText("Произошла ошибка при загрузке курса валют");
                } catch (ParseException e1) {
                    errorMassageLabel.setVisible(true);
                    errorMassageLabel.setText("Произошла ошибка при обработке полученных курсов валют");
                }
            }
        }
    }
}
