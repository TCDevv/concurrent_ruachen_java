import java.util.concurrent.Semaphore;

class Kitchen {
    private Semaphore cleanDish = new Semaphore(1);
    private Semaphore foodReady = new Semaphore(0);

    public void washDish() {
        try {
            cleanDish.acquire();
            System.out.println("Rửa chén...");
            Thread.sleep(2000);
            System.out.println("Chén đã được rửa.");
            cleanDish.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pourFood() {
        try {
            cleanDish.acquire();
            System.out.println("Đổ đồ ăn vào chén...");
            Thread.sleep(1000);
            System.out.println("Đồ ăn đã đuợc đổ vào chén");
            foodReady.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void eatFood(){
        try{
            foodReady.acquire();
            System.out.println("Ăn đồ ăn...");
            Thread.sleep(2000);
            System.out.println("Ăn xong");
            cleanDish.release();
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}

class Dishwasher extends Thread {
    private Kitchen kitchen;

    public Dishwasher(Kitchen kitchen) {
        this.kitchen = kitchen;
    }

    public void run() {
        while(true) {
            kitchen.washDish();
        }
    }
}

class FoodPourer extends Thread {
    private Kitchen kitchen;

    public FoodPourer(Kitchen kitchen) {
        this.kitchen = kitchen;
    }

    public void run() {
        while(true) {
            kitchen.pourFood();
        }
    }
}

class FoodConsumer extends Thread {

    private Kitchen kitchen;

    public FoodConsumer(Kitchen kitchen){
        this.kitchen = kitchen;
    }

    public void run(){
        while (true){
            kitchen.eatFood();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Kitchen kitchen = new Kitchen();
        Dishwasher dishwasher = new Dishwasher(kitchen);
        FoodPourer foodPourer = new FoodPourer(kitchen);
        FoodConsumer foodConsumer = new FoodConsumer(kitchen);

        dishwasher.start();
        foodPourer.start();
        foodConsumer.start();
    }
}
