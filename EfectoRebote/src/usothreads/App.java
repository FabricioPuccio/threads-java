package usothreads;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class App {

    public static void ejecutar() {
        JFrame marco = new MarcoRebote();

        marco.setVisible(true);
        marco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}

class Pelota {

    private static final int TAMX = 15;
    private static final int TAMY = 15;
    private double x = 0;
    private double y = 0;
    private double dx = 1;
    private double dy = 1;

    /**
     * Mueve la pelota invirtiendo posición si choca con límites
     *
     * @param limites
     */
    public void muevePelota(Rectangle2D limites) {

        x += dx;
        y += dy;

        if (x < limites.getMinX()) {
            x = limites.getMinX();
            dx = -dx;
        }

        if (x + TAMX >= limites.getMaxX()) {
            x = limites.getMaxX() - TAMX;
            dx = -dx;

        }

        if (y < limites.getMinY()) {
            y = limites.getMinY();
            dy = -dy;
        }

        if (y + TAMY >= limites.getMaxY()) {
            y = limites.getMaxY() - TAMY;
            dy = -dy;
        }

    }

    /**
     * Forma de la pelota y su posición
     *
     * @return Ellipse2D
     */
    public Ellipse2D getShape() {
        return new Ellipse2D.Double(x, y, TAMX, TAMY);
    }

}

class PelotaHilos implements Runnable {

    private Pelota pelota;
    private Component componente;

    public PelotaHilos(Pelota pelota, Component componente) {
        this.pelota = pelota;
        this.componente = componente;
    }

    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            pelota.muevePelota(componente.getBounds());
            componente.paint(componente.getGraphics());
        }

    }
}

class LaminaPelota extends JPanel {

    private ArrayList<Pelota> pelotas;

    public LaminaPelota() {
        pelotas = new ArrayList<Pelota>();
    }

    /**
     * Añadimos pelota a la lamina
     *
     * @param pelota
     */
    public void addPelota(Pelota pelota) {
        pelotas.add(pelota);
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        for (Pelota pelo : pelotas) {
            g2.fill(pelo.getShape());
        }

    }
}

class MarcoRebote extends JFrame {

    private LaminaPelota lamina;
    private JPanel laminaBotones;
    private Thread hilo1, hilo2, hilo3;
    private JButton arranca1, arranca2, arranca3, detener1, detener2, detener3;

    public MarcoRebote() {

        setTitle("Efecto rebote");
        setBounds(750, 50, 600, 300);

        lamina = new LaminaPelota();
        add(lamina, BorderLayout.CENTER);

        laminaBotones = new JPanel();

//-------------------------------------------------------------------------------------------------
        arranca1 = new JButton("Bola 1");

        arranca1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evento) {
                comienzaElJuego(evento);
            }
        });

        laminaBotones.add(arranca1);

        arranca2 = new JButton("Bola 2");

        arranca2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evento) {
                comienzaElJuego(evento);
            }
        });

        laminaBotones.add(arranca2);

        arranca3 = new JButton("Bola 3");

        arranca3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evento) {
                comienzaElJuego(evento);
            }
        });

        laminaBotones.add(arranca3);

//-------------------------------------------------------------------------------------------------
        detener1 = new JButton("det-bola 1");

        detener1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evento) {
                detener(evento);
            }
        });

        laminaBotones.add(detener1);

        detener2 = new JButton("det-bola 2");

        detener2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evento) {
                detener(evento);
            }
        });

        laminaBotones.add(detener2);

        detener3 = new JButton("det-bola 3");

        detener3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evento) {
                detener(evento);
            }
        });

        laminaBotones.add(detener3);

        add(laminaBotones, BorderLayout.SOUTH);

    }

    /**
     * Añade pelota y la hace rebotar
     *
     * @param e
     */
    public void comienzaElJuego(ActionEvent e) {

        Pelota pelota = new Pelota();
        lamina.addPelota(pelota);
        Runnable r = new PelotaHilos(pelota, lamina);

        if (e.getSource().equals(arranca1)) {

            hilo1 = new Thread(r);

            hilo1.start();

        } else if (e.getSource().equals(arranca2)) {

            hilo2 = new Thread(r);

            hilo2.start();
        } else if (e.getSource().equals(arranca3)) {

            hilo3 = new Thread(r);

            hilo3.start();
        }

    }

    public void detener(ActionEvent e) {
        // hilo.stop(); -> el método stop() está deprecado

        // Solicitud de interrupción del hilo

        if (e.getSource().equals(detener1)) {

            hilo1.interrupt();

        } else if (e.getSource().equals(detener2)) {

            hilo2.interrupt();

        } else if (e.getSource().equals(detener3)) {

            hilo3.interrupt();

        }
    }

}
