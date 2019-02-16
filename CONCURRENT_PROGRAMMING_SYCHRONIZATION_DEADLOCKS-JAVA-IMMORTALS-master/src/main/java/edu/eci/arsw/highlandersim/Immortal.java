package edu.eci.arsw.highlandersim;

import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Immortal extends Thread {

    private ImmortalUpdateReportCallback updateCallback = null;

    private int health;

    private int defaultDamageValue;

    private final List<Immortal> immortalsPopulation;

    private final String name;

    private final Random r = new Random(System.currentTimeMillis());
    private boolean pause;
    private boolean stop;
    private int sums;

    public Immortal(String name, List<Immortal> immortalsPopulation, int health, int defaultDamageValue,
            ImmortalUpdateReportCallback ucb) {
        super(name);
        this.updateCallback = ucb;
        this.name = name;
        this.immortalsPopulation = immortalsPopulation;
        this.health = health;
        this.defaultDamageValue = defaultDamageValue;
        pause = false;
        stop = false;
        sums = 0;
    }

    public void run() {

        while (!stop) {
            if (health <= 0) {
                stop = true;
            }
            if (pause) {
                synchronized (this) {
                    try {
                        this.wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Immortal.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            Immortal im;

            int myIndex = immortalsPopulation.indexOf(this);

            int nextFighterIndex = r.nextInt(immortalsPopulation.size());

            //avoid self-fight
            if (nextFighterIndex == myIndex) {
                nextFighterIndex = ((nextFighterIndex + 1) % immortalsPopulation.size());
            }

            im = immortalsPopulation.get(nextFighterIndex);

            this.fight(im);

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void fight(Immortal i2) {
        if (i2.getHealth() > 0) {
            // Se intenta darle un orden a los bloqueos, para evitar el deadlock.
            Immortal bloqueado1;
            Immortal bloqueado2;
            // Extraemos los índices de los inmortales que van a pelear.
            int myIndex = immortalsPopulation.indexOf(this);
            int hisIndex = immortalsPopulation.indexOf(i2);
            if (myIndex < hisIndex) {// Si el índice de este inmortal es menor al del índice del inmortal con el que va a pelear, entonces este inmortal bloquea al otro.
                bloqueado1 = this;
                bloqueado2 = i2;
            } else {
                bloqueado1 = i2;
                bloqueado2 = this;
            }
            synchronized (bloqueado1) {
                synchronized (bloqueado2) {
                    // Esta es nuestra región crítica, pues se hace el proceso de agregar y quitar vida al mismo tiempo.
                    i2.changeHealth(i2.getHealth() - defaultDamageValue);
                    this.health += defaultDamageValue;
                }
            }
            updateCallback.processReport("Fight: " + this + " vs " + i2 + "\n");
        } else {
            if (i2.getSums() < 1) {
                updateCallback.processReport(this + " says:" + i2 + " is already dead!\n");
            }
            i2.setSums(i2.getSums() + 1);
        }
    }

    public void changeHealth(int v) {
        health = v;
    }

    public int getHealth() {
        return health;
    }

    @Override
    public String toString() {

        return name + "[" + health + "]";
    }

    public void resumeFight() {
        pause = false;
        synchronized (this) {
            this.notifyAll();
        }
    }

    public ImmortalUpdateReportCallback getUpdateCallback() {
        return updateCallback;
    }

    public void setUpdateCallback(ImmortalUpdateReportCallback updateCallback) {
        this.updateCallback = updateCallback;
    }

    public int getDefaultDamageValue() {
        return defaultDamageValue;
    }

    public void setDefaultDamageValue(int defaultDamageValue) {
        this.defaultDamageValue = defaultDamageValue;
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public int getSums() {
        return sums;
    }

    public void setSums(int sums) {
        this.sums = sums;
    }

}
