import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class AppWindow extends JFrame {
    public AppWindow() {
        SwingUtilities.invokeLater(() -> {
            setVisible(true);
            setSize(700, 500);
            this.setLocationRelativeTo(null);
            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentHidden(ComponentEvent e) {
                    System.exit(0);
                }
            });
        });
    }

    private List<Country> countries;
    private List<Path> paths;
    private JFileChooser chooser = new JFileChooser();
    private Second second;
    private First first;
    private Map<String, String> stringStringMap;

    @Override
    public JRootPane createRootPane() {
//        JRootPane pane = new JRootPane();
//        JPanel panel = new JPanel();
//        panel.setLayout(new BorderLayout());
//
//        countries = new ArrayList<>();
//        paths = new ArrayList<>();
//        JTabbedPane tabbedPane = createTabbedPane();
//        JMenuBar menuBar = createMenuBar();
//        initialMap();
//        panel.add(menuBar, BorderLayout.NORTH);
//        panel.add(tabbedPane, BorderLayout.CENTER);
//        pane.setContentPane(panel);
        JRootPane pane = new JRootPane();
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        countries = new ArrayList<>();
        paths = new ArrayList<>();
        JTabbedPane tabbedPane = new JTabbedPane();
        first = new First(countries);
        second = new Second(countries);
        tabbedPane.addTab("First", first);
        tabbedPane.addTab("Second", second);
        JMenuBar menuBar = new JMenuBar();
        JMenu submenu = new JMenu("File");

        JMenuItem open = new JMenuItem("Open");
        open.addActionListener(e -> {
            try (PrintWriter pr = new PrintWriter(new File("output.txt"))) {
                chooser = new JFileChooser();
                chooser.setDialogTitle("Октрытие файла");
                chooser.setCurrentDirectory(new File("."));
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setAcceptAllFileFilterUsed(false);
                if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    getPathList();
                    addCountries();
                    for (Country country : countries) {
                        pr.println(country.getName());
                    }
                    first.update();
                    second.update();
                }
            } catch (IOException er) {
                er.printStackTrace();
            }
        });
        menuBar.add(submenu);
        submenu.add(open);

        initialMap();
        panel.add(menuBar, BorderLayout.NORTH);
        panel.add(tabbedPane, BorderLayout.CENTER);
        pane.setContentPane(panel);

        return pane;
    }

//    private JMenuBar createMenuBar() {
//        JMenuBar menuBar = new JMenuBar();
//        JMenu submenu = new JMenu("File");
//        JMenuItem open = createOpenButton();
//        menuBar.add(submenu);
//        submenu.add(open);
//        return menuBar;
//    }
//
//    private JTabbedPane createTabbedPane() {
//        JTabbedPane tabbedPane = new JTabbedPane();
//        first = new First(countries);
//        second = new Second(countries);
//        tabbedPane.addTab("FirstTab", first);
//        tabbedPane.addTab("SecondTab", second);
//        return tabbedPane;
//    }
//
//    private JMenuItem createOpenButton() {
//        JMenuItem open = new JMenuItem("Open");
//        open.addActionListener(e -> {
//            try (PrintWriter pr = new PrintWriter(new File("output.txt"))) {
//                chooser = new JFileChooser();
//                chooser.setDialogTitle("Октрытие файла");
//                chooser.setCurrentDirectory(new File("."));
//                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//                chooser.setAcceptAllFileFilterUsed(false);
//                if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
//                    fillPaths();
//                    fillCountries();
//                    for (Country country : countries) {
//                        pr.println(country.getName());
//                    }
//                    second.update();
//                    first.update();
//                }
//            } catch (IOException er) {
//                er.printStackTrace();
//            }
//        });
//        return open;
//    }

    private void getPathList() throws IOException {
        paths.addAll(Files.walk(Paths.get(""))
                .filter(Files::isRegularFile)
                .collect(Collectors.toList()).stream().filter(f -> f.toString().endsWith("png")).
                        collect(Collectors.toList()));
    }



    private void initialMap() {
        stringStringMap = new TreeMap<>();
        try (Scanner sc = new Scanner(new File("input.txt"))) {
            while (sc.hasNext()) {
                stringStringMap.put(sc.next(), sc.next());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void addCountries() {
        for (Path path : paths) {
            Country country = new Country(path);
            if (stringStringMap.get(country.getName()) != null) {
                country.setCapital(stringStringMap.get(country.getName()));
            }
            countries.add(country);
        }
    }
}
