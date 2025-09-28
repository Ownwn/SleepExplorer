import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class App extends JFrame {
    private static final int width = 1400;
    private static final int height = 1000;

    private App(Consumer<Graphics> drawOverride) {
        setSize(width, height);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                drawOverride.accept(g);



            }
        };
        add(panel);
    }

    public static void createVisualisation(List<Main.Day> days) {

        double maxEff = days.stream().mapToDouble(Main.Day::getEfficiency).max().getAsDouble();
        double minEff = days.stream().mapToDouble(Main.Day::getEfficiency).min().getAsDouble();

       Consumer<Graphics> toDraw = drawer(maxEff, minEff, days.stream().map(Main.Day::getEfficiency).toList());



        new App(toDraw).setVisible(true);
    }

    private static Consumer<Graphics> drawer(double max, double min, List<Double> items) {
        int itemWidth = (width - 40) / items.size();

        return g -> {
            for (int i = 0; i < items.size(); i++) {
                double y = getYScale(max, min, items.get(i));
                int x = 10 + i*itemWidth;

                g.drawLine(x, height, x, (int) y);
            }
        };

    }

    private static double getYScale(double max, double min, double value) {
        double range = max - min;
        return 10 + ((max - value) / range) * (height - (2 * 10));
    }
}
