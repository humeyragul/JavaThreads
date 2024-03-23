package operatingSystem;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FirstQuestion {

    private static final int DIZI_BOYUTU = 1000;
    private static final int IS_PARCACIGI_SAYISI = 200;

    public static void main(String[] args) {

        // Rastgele bir dizi oluştur
        int[] dizi = new int[DIZI_BOYUTU];
        Random rastgele = new Random();
        for (int i = 0; i < DIZI_BOYUTU; i++) {
            dizi[i] = rastgele.nextInt(100);
        }

        // İş parçacığı havuzunu oluştur
        ExecutorService executorService = Executors.newFixedThreadPool(IS_PARCACIGI_SAYISI);

        // Her alt küme için bir iş parçacığı oluştur
        for (int i = 0; i < DIZI_BOYUTU / IS_PARCACIGI_SAYISI; i++) {
            int baslangicIndeks = i * IS_PARCACIGI_SAYISI;
            int bitisIndeks = Math.min((i + 1) * IS_PARCACIGI_SAYISI, DIZI_BOYUTU);
            executorService.submit(() -> {
                int max = Integer.MIN_VALUE;
                int min = Integer.MAX_VALUE;
                int toplam = 0;
                for (int j = baslangicIndeks; j < bitisIndeks; j++) {
                    max = Math.max(max, dizi[j]);
                    min = Math.min(min, dizi[j]);
                    toplam += dizi[j];
                }
                int ortalama = toplam / (bitisIndeks - baslangicIndeks);
                long calismaSuresi = System.nanoTime();
                // ... alt kümeyle ilgili işlemler ...
                calismaSuresi = System.nanoTime() - calismaSuresi;
                System.out.println("N = " + DIZI_BOYUTU + ", M = " + IS_PARCACIGI_SAYISI + ", Max = " + max + ", Min = " + min + ", Toplam = " + toplam + ", Ortalama = " + ortalama + ", Çalışma Süresi (Mikrosaniye) = " + TimeUnit.NANOSECONDS.toMicros(calismaSuresi));
            });
        }

        // İş parçacıklarının tamamlanmasını bekle
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}