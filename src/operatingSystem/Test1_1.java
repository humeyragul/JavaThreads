package operatingSystem;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Test1_1 {

    public static void main(String[] args) {
    	//// Çalışma zamanında oluşturulan iş parçacıklarını depolamak için bir liste oluşturulur.
        List<Thread> threads = new ArrayList<>();
        Random random = new Random();
        
     // N ve M değerlerine bağlı olarak rastgele diziler oluşturan ve iş parçacıkları kullanarak alt kümeleri işleyen kod kümesi.
        for (int N = 10; N <= 100000; N *= 10) { //N degeri 10'dan 10 bine kadar 10000'er artar. 
            for (int M = 10; M <= 1000; M += 200) { //M degeri 10'dan 1000'e kadar 200'er artar.
                int[] array = generateRandomArray(N);
                for (int k = 0; k < M; k++) { //M tane iş parçacığı oluşturulur.
                    int index = k; //index değişkeni ile iş parçacığı numarası takip edilir.
                    int start = index * (N / M); // Her bir iş parçacığının çalışacağı alt kümenin başlangıç indisini belirlemek için kullanılır.
                    int end = (index + 1) * (N / M);//Bitiş dizinini hesaplar ve dizi sınırlarının dışına çıkmamasını sağlar.
                    if (index == M - 1) end = N; //Bu satır, son iş parçacığının dizinin sonuna kadar işlemesini sağlar.
                    //Yeni bir thread nesnesi oluşturur
                    Thread thread = new Thread(new SubsetProcessor(array, start, end, N, M)); /*Bu satır yeni bir Thread nesnesi oluşturur.
                    SubsetProcessor sınıfından bir nesne oluşturur ve bu nesneyi iş parçacığının görevi olarak atar.
                    Dizi, başlangıç dizini, bitiş dizini, orijinal dizinin boyutu (N) ve toplam iş parçacık sayısı (M ) gibi bilgileri iş parçacığına iletir.*/
                    threads.add(thread); //Bu satır, yeni oluşturulan iş parçacığını threads listesine ekler.
                    thread.start();
                }
            }
        }
        // Bu kod, threads listesindeki tüm iş parçacıklarının tamamlanmasını bekler.
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    // Bu fonksiyon, N boyutunda rastgele bir dizi oluşturur ve her bir öğesini 1 ile 10 arasında rastgele bir tamsayı ile doldurur.
    public static int[] generateRandomArray(int N) {
        Random random = new Random();
        int[] array = new int[N];
        for (int i = 0; i < N; i++) {
            array[i] = random.nextInt(10) + 1;
        }
        return array;
    }
       // Bu iç sınıf, bir dizinin alt kümesini işlemektedir.
      // SubsetProcessor sınıfı, Runnable arayüzünü uygular ve bu arayüzün run() yöntemini geçersiz kılar.
    static class SubsetProcessor implements Runnable { 
        private int[] array;
        private int start;
        private int end;
        private int finalN;
        private int finalM;

        //Constructor SubsetProcessor sınıfının nesnelerini oluşturur.
        public SubsetProcessor(int[] array, int start, int end, int finalN, int finalM) {
            this.array = array;
            this.start = start;
            this.end = end;
            this.finalN = finalN;
            this.finalM = finalM;
        }
        
        //@Override , SubsetProcessor sınıfındaki run() yönteminin, Runnable arayüzündeki sanal run() yöntemini geçersiz kılmak için kullanılmıştır.
        @Override 
        public void run() {
        	long startTime = System.nanoTime();
            int max = Integer.MIN_VALUE;
            int min = Integer.MAX_VALUE;
            int sum = 0;
            for (int j = start; j < end; j++) {
                max = Math.max(max, array[j]);
                min = Math.min(min, array[j]);
                sum += array[j];
            }
            long endTime = System.nanoTime();
            long executionTime = endTime - startTime;
            double executionTimeMicros = executionTime / 1000.0;
            double avg = (double) sum / (end - start);
            System.out.println("N = " + finalN + ", M = " + finalM + " - Max: " + max + " Min: " + min + " Toplam: " + sum + " Ortalama: " + avg);
            System.out.println("Çalışma Süresi (Mikrosaniye): " + executionTimeMicros);
        }
    }
}
