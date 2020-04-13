import javax.swing.*;
import java.awt.*;
import java.util.List;

public class First extends JPanel {
    private JList<Country> list;
    private DefaultListModel<Country> listModel;
    private List<Country> countries;
    private JLabel label = new JLabel();

    public First(List<Country> countries) {
        this.countries = countries;
        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);
        //cell renderer
        list.setCellRenderer(new ListCellRenderer<Country>() {
            @Override
            public Component getListCellRendererComponent(
                    JList<? extends Country> list, Country value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                Icon iconFlag = value.getImage();
                String name = value.getName();
                JLabel label = new JLabel(name, iconFlag, JLabel.LEFT);
                label.setOpaque(true);
                if (isSelected) {
                    label.setBackground(Color.BLUE);
                    label.setForeground(Color.RED);
                }
                return label;
            }
        });
        JScrollPane scrollPane = new JScrollPane();
        list.setBounds(100, 100, 75, 75);
        list.addListSelectionListener(e -> {
            Country country = list.getSelectedValue();
            label.setIcon(country.getImage());
            label.setText(country.getName() + " " + country.getCapital());
                // JOptionPane.showMessageDialog(this, str);
        });

        scrollPane.setViewportView(list);
        scrollPane.setPreferredSize(new Dimension(250, 400));

        this.add(scrollPane);
        this.add(label);
        this.setSize(new Dimension(500, 500));
        //this.add(list);
    }

    public void update() {
        for (Country country : countries) {
            listModel.add(listModel.size(), country);
        }
    }
}
